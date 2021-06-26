package com.tuba.mall.common;
/*自定义一个异常类，继承运行时异常，可以不捕获直接抛给jvm处理*/
public class TuBaMallException extends RuntimeException {

    public TuBaMallException() {
    }

    public TuBaMallException(String message) {
        super(message);
    }

    /**
     * 丢出一个异常
     */
    public static void fail(String message) {
        throw new TuBaMallException(message);
    }

}
