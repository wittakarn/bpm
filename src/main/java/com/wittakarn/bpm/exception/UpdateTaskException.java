package com.wittakarn.bpm.exception;

import java.io.Serializable;


public class UpdateTaskException extends WorkflowException implements Serializable {

	private static final long serialVersionUID = 1L;

	public UpdateTaskException() {
		super();
	}
	
	public UpdateTaskException(String message) {
		super(message);
	}
	
	public UpdateTaskException(Throwable throwable) {
		super(throwable);
	}
	
	public UpdateTaskException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
