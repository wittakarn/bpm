package com.wittakarn.bpm.exception;

import java.io.Serializable;


public class WorkflowException extends RuntimeException implements Serializable {

	private static final long serialVersionUID = 1L;

	public WorkflowException() {
		super();
	}
	
	public WorkflowException(String message) {
		super(message);
	}
	
	public WorkflowException(Throwable throwable) {
		super(throwable);
	}
	
	public WorkflowException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
