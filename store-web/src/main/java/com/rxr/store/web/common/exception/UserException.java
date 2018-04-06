package com.rxr.store.web.common.exception;

public class UserException extends RuntimeException implements WithTypeException {
    private Type type;

    public UserException(String message) {
        super(message);
        this.type = Type.LACK_PARAMTER;
    }

    public UserException(Type type, String message) {
        super(message);
        this.type = type;
    }

    public  Type type(){
        return type;
    }


    public enum Type{
        WRONG_PAGE_NUM,LACK_PARAMTER,USER_NOT_LOGIN,USER_NOT_FOUND,USER_AUTH_FAIL;
    }
}
