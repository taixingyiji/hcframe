package com.taixingyiji.base.module.log;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.taixingyiji.base.common.config.FrameConfig;
import jakarta.servlet.http.Part;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RequestLogAspectTest {
    private final FrameConfig config = new FrameConfig();
    private final RequestLogAspect aspect = new RequestLogAspect(config);
    private final Logger logger = (Logger) LoggerFactory.getLogger(RequestLogAspect.class);
    private final ListAppender<ILoggingEvent> appender = new ListAppender<>();
    private ProceedingJoinPoint joinPoint;
    private MethodSignature signature;

    @BeforeEach
    void setUp() throws NoSuchMethodException {
        appender.start();
        logger.addAppender(appender);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(new MockHttpServletRequest()));
        joinPoint = mock(ProceedingJoinPoint.class);
        signature = mock(MethodSignature.class);
        when(joinPoint.getSignature()).thenReturn(signature);
        when(signature.getMethod()).thenReturn(UploadForm.class.getMethod("getTitle"));
        when(signature.getDeclaringTypeName()).thenReturn("UploadController");
        when(signature.getName()).thenReturn("upload");
        when(signature.getParameterNames()).thenReturn(new String[]{"upload"});
    }

    @AfterEach
    void tearDown() {
        RequestContextHolder.resetRequestAttributes();
        logger.detachAppender(appender);
        appender.stop();
    }

    @Test
    void logsSingleArrayListMapAndDtoUploadsWithoutReadingContents() throws Throwable {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("report.csv");
        Object[] arguments = {file, new MultipartFile[]{file}, List.of(file), Map.of("file", file), new UploadForm(file)};
        when(signature.getParameterNames()).thenReturn(new String[]{"single", "array", "list", "map", "form"});
        when(joinPoint.getArgs()).thenReturn(arguments);
        when(joinPoint.proceed()).thenReturn("ok");
        config.setControllerLogMaxValueLength(4096);

        assertEquals("ok", aspect.doAround(joinPoint));

        String message = appender.list.getFirst().getFormattedMessage();
        assertTrue(message.contains("\"single\":\"report.csv\""), message);
        assertTrue(message.contains("\"array\":[\"report.csv\"]"), message);
        assertTrue(message.contains("\"list\":[\"report.csv\"]"), message);
        assertTrue(message.contains("\"map\":{\"file\":\"report.csv\"}"), message);
        assertTrue(message.contains("\"form\":{\"file\":\"report.csv\",\"title\":\"upload\"}"), message);
        verify(file, times(5)).getOriginalFilename();
        verifyNoMoreInteractions(file);
    }

    @Test
    void omitsNestedRequestStreamsAndDownloadResourceContents() throws Throwable {
        Part part = mock(Part.class);
        when(part.getSubmittedFileName()).thenReturn("part.csv");
        InputStream stream = mock(InputStream.class);
        when(joinPoint.getArgs()).thenReturn(new Object[]{Map.of("part", part, "stream", stream,
                "request", new MockHttpServletRequest())});
        InputStreamResource result = new InputStreamResource(stream);
        when(joinPoint.proceed()).thenReturn(result);

        assertSame(result, aspect.doAround(joinPoint));

        String message = appender.list.getFirst().getFormattedMessage();
        assertTrue(message.contains("part.csv"), message);
        assertTrue(message.contains("[omitted: InputStreamResource]"), message);
        assertTrue(message.contains("[omitted: MockHttpServletRequest]"), message);
        verify(part).getSubmittedFileName();
        verifyNoMoreInteractions(part);
        verifyNoInteractions(stream);
    }

    @Test
    void logsUploadOnErrorWithoutReadingContents() {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("failed.csv");
        when(joinPoint.getArgs()).thenReturn(new Object[]{List.of(file)});

        assertDoesNotThrow(() -> aspect.doAfterThrow(joinPoint, new IllegalStateException("upload failed")));

        String message = appender.list.getFirst().getFormattedMessage();
        assertTrue(message.contains("failed.csv"), message);
        assertTrue(message.contains("upload failed"), message);
        verify(file).getOriginalFilename();
        verifyNoMoreInteractions(file);
    }

    @Test
    void serializationFailureDoesNotReplaceSuccessfulResult() throws Throwable {
        when(joinPoint.getArgs()).thenReturn(new Object[]{new BrokenForm()});
        Object result = Map.of("status", "ok");
        when(joinPoint.proceed()).thenReturn(result);

        assertSame(result, aspect.doAround(joinPoint));
        assertTrue(appender.list.getFirst().getFormattedMessage().contains("Failed to write"));
    }

    @Test
    void serializationFailureInErrorAdvicePreservesOriginalException() {
        when(joinPoint.getArgs()).thenReturn(new Object[]{new BrokenForm()});
        RuntimeException failure = new IllegalArgumentException("original failure");

        assertDoesNotThrow(() -> aspect.doAfterThrow(joinPoint, failure));

        ILoggingEvent event = appender.list.getLast();
        assertEquals("Controller request failed", event.getFormattedMessage());
        assertEquals("original failure", event.getThrowableProxy().getMessage());
    }

    @Test
    void handlesMissingParameterNames() throws Throwable {
        when(signature.getParameterNames()).thenReturn(null);
        when(joinPoint.getArgs()).thenReturn(new Object[]{"value"});

        aspect.doAround(joinPoint);

        assertTrue(appender.list.getFirst().getFormattedMessage().contains("\"arg0\":\"value\""));
    }

    @Test
    void summaryDoesNotInspectArguments() throws Throwable {
        config.setControllerLogDetail(false);
        config.setControllerLogSlowMillis(-1L);
        config.setControllerLogSampleRate(0D);

        aspect.doAround(joinPoint);

        verify(joinPoint, never()).getArgs();
        assertTrue(appender.list.getFirst().getFormattedMessage().contains("Request Summary"));
    }

    public static class UploadForm {
        private final MultipartFile file;

        UploadForm(MultipartFile file) {
            this.file = file;
        }

        public MultipartFile getFile() {
            return file;
        }

        public String getTitle() {
            return "upload";
        }

        @Override
        public String toString() {
            throw new IllegalStateException("Logging must not invoke DTO toString");
        }
    }

    public static class BrokenForm {
        public String getValue() {
            throw new IllegalStateException("unavailable request data");
        }
    }
}
