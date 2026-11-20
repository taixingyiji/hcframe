package com.taixingyiji.base.module.log;

import com.alibaba.fastjson2.modules.ObjectWriterModule;
import com.alibaba.fastjson2.writer.ObjectWriter;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Part;
import org.springframework.core.io.InputStreamSource;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;

/**
 * Log-only serializers: never inspect upload contents or request-bound streams.
 * Runtime type checks also cover files nested in arrays, collections, maps and beans.
 */
final class RequestLogSerializeConfig implements ObjectWriterModule {
    private static final ObjectWriter<Object> MULTIPART_FILE = (writer, object, name, type, features) ->
            writer.writeString(((MultipartFile) object).getOriginalFilename());
    private static final ObjectWriter<Object> PART = (writer, object, name, type, features) ->
            writer.writeString(((Part) object).getSubmittedFileName());
    private static final ObjectWriter<Object> OMITTED = (writer, object, name, type, features) ->
            writer.writeString("[omitted: " + object.getClass().getSimpleName() + "]");

    @Override
    public ObjectWriter<?> getObjectWriter(Type type, Class clazz) {
        if (MultipartFile.class.isAssignableFrom(clazz)) {
            return MULTIPART_FILE;
        }
        if (Part.class.isAssignableFrom(clazz)) {
            return PART;
        }
        if (ServletRequest.class.isAssignableFrom(clazz)
                || ServletResponse.class.isAssignableFrom(clazz)
                || InputStreamSource.class.isAssignableFrom(clazz)
                || InputStream.class.isAssignableFrom(clazz)
                || OutputStream.class.isAssignableFrom(clazz)
                || Reader.class.isAssignableFrom(clazz)
                || Writer.class.isAssignableFrom(clazz)) {
            return OMITTED;
        }
        return null;
    }
}
