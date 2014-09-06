package com.wittakarn.bpm.exception;

import java.io.Serializable;

public class BPMException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 1L;
    private String code;

    public BPMException() {
        super();
    }

    public BPMException(String message) {
        super(message);
    }

    public BPMException(Throwable throwable) {
        super(throwable);
    }

    public BPMException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
