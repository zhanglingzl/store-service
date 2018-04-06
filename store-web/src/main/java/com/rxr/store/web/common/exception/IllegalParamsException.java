package com.rxr.store.web.common.exception;

public class IllegalParamsException extends RuntimeException implements WithTypeException {
    private Type type;
    public IllegalParamsException(){

    }
    public IllegalParamsException(Type type,String msg){
        super(msg);
        this.type = type;
    }

    public Type type(){
        return type;
    }

    public enum Type{
        WRONG_PAGE_NUM,WRONG_TYPE
    }
}
