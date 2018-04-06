package com.rxr.store.web.common;

import com.google.common.collect.ImmutableMap;
import com.rxr.store.web.common.exception.WithTypeException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.web.servlet.NoHandlerFoundException;


public class Exception2CodeRepo {
    private static final ImmutableMap<Object, RestCode> MAP = ImmutableMap.<Object, RestCode>builder()
            .put(IllegalStateException.class,RestCode.UNKNOWN_ERROR)
            .put(NoHandlerFoundException.class,RestCode.NOT_FOUND).build();

    private static Object getType(Throwable throwable){
        try {
            return FieldUtils.readDeclaredField(throwable, "type", true);
        } catch (Exception e) {
            return null;
        }
    }


    public static RestCode getCode(Throwable throwable) {
        if (throwable == null) {
            return RestCode.UNKNOWN_ERROR;
        }
        Object target = throwable;
        if (throwable instanceof WithTypeException) {
            Object type = getType(throwable);
            if (type != null ) {
                target = type;
            }
        }
        RestCode restCode =  MAP.get(target);
        if (restCode != null) {
            return restCode;
        }
        Throwable rootCause = ExceptionUtils.getRootCause(throwable);
        if (rootCause != null) {
            return getCode(rootCause);
        }
        return RestCode.UNKNOWN_ERROR;
    }

}
