package com.taixingyiji.base.module.log;

import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Part;
import org.springframework.core.io.InputStreamSource;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

/**
 * Log-only serializers: never inspect upload contents or request-bound streams.
 * Runtime type checks also cover files nested in arrays, collections, maps and beans.
 */
final class RequestLogSerializeConfig extends SerializeConfig {
    private static final ObjectSerializer MULTIPART_FILE = (serializer, object, name, type, features) ->
            serializer.write(((MultipartFile) object).getOriginalFilename());
    private static final ObjectSerializer PART = (serializer, object, name, type, features) ->
            serializer.write(((Part) object).getSubmittedFileName());
    private static final ObjectSerializer OMITTED = (serializer, object, name, type, features) ->
            serializer.write("[omitted: " + object.getClass().getSimpleName() + "]");

    RequestLogSerializeConfig() {
        // Keep nested bean fields on the same runtime serializer lookup path.
        setAsmEnable(false);
    }

    @Override
    public ObjectSerializer getObjectWriter(Class<?> clazz) {
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
        return super.getObjectWriter(clazz);
    }
}
