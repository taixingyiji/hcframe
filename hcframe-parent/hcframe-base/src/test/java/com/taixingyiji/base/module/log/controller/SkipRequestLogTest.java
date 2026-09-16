package com.taixingyiji.base.module.log.controller;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.taixingyiji.base.common.config.FrameConfig;
import com.taixingyiji.base.module.log.RequestLogAspect;
import com.taixingyiji.base.module.log.annotation.SkipRequestLog;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.LoggerFactory;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.junit.jupiter.api.Assertions.*;

class SkipRequestLogTest {
    private final Logger logger = (Logger) LoggerFactory.getLogger(RequestLogAspect.class);
    private final ListAppender<ILoggingEvent> appender = new ListAppender<>();

    @BeforeEach
    void setUp() {
        appender.start();
        logger.addAppender(appender);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(new MockHttpServletRequest()));
    }

    @AfterEach
    void tearDown() {
        RequestContextHolder.resetRequestAttributes();
        logger.detachAppender(appender);
        appender.stop();
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    void methodAnnotationSkipsSuccessAndFailureForBothProxyTypes(boolean classProxy) {
        assertSkipsLogs(new MethodController(), classProxy);
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    void classAnnotationSkipsSuccessAndFailureForBothProxyTypes(boolean classProxy) {
        assertSkipsLogs(new ClassController(), classProxy);
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    void inheritedClassAnnotationSkipsLogs(boolean classProxy) {
        assertSkipsLogs(new ChildController(), classProxy);
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    void interfaceMethodAnnotationSkipsLogs(boolean classProxy) {
        assertSkipsLogs(new InterfaceController(), classProxy);
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    void unannotatedMethodStillLogs(boolean classProxy) {
        Endpoint proxy = proxy(new MethodController(), classProxy);

        assertEquals("ok", proxy.normal());

        assertEquals(1, appender.list.size());
        assertTrue(appender.list.getFirst().getFormattedMessage().startsWith("Request Info"));
    }

    private void assertSkipsLogs(Endpoint target, boolean classProxy) {
        Endpoint proxy = proxy(target, classProxy);
        Payload payload = new Payload();
        RuntimeException failure = new IllegalStateException("business failure");

        assertSame(payload, proxy.upload(payload, null));
        assertSame(failure, assertThrows(IllegalStateException.class, () -> proxy.upload(payload, failure)));
        assertEquals(0, payload.reads);
        assertTrue(appender.list.isEmpty(), "Both normal and error request logging must be bypassed");
    }

    private Endpoint proxy(Endpoint target, boolean classProxy) {
        AspectJProxyFactory factory = new AspectJProxyFactory(target);
        factory.setProxyTargetClass(classProxy);
        factory.addAspect(new RequestLogAspect(new FrameConfig()));
        return factory.getProxy();
    }

    public interface Endpoint {
        Payload upload(Payload payload, RuntimeException failure);

        String normal();
    }

    public static class BaseController implements Endpoint {
        @Override
        public Payload upload(Payload payload, RuntimeException failure) {
            if (failure != null) {
                throw failure;
            }
            return payload;
        }

        @Override
        public String normal() {
            return "ok";
        }
    }

    public static class MethodController extends BaseController {
        @Override
        @SkipRequestLog
        public Payload upload(Payload payload, RuntimeException failure) {
            return super.upload(payload, failure);
        }
    }

    @SkipRequestLog
    public static class ClassController extends BaseController {
    }

    public static class ChildController extends ClassController {
    }

    public interface AnnotatedEndpoint extends Endpoint {
        @Override
        @SkipRequestLog
        Payload upload(Payload payload, RuntimeException failure);
    }

    public static class InterfaceController extends BaseController implements AnnotatedEndpoint {
        @Override
        public Payload upload(Payload payload, RuntimeException failure) {
            return super.upload(payload, failure);
        }
    }

    public static class Payload {
        private int reads;

        public String getValue() {
            reads++;
            throw new IllegalStateException("Request log must not parse this payload");
        }
    }
}
