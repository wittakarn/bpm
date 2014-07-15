package com.wittakarn.bpm.exception;

import java.io.Serializable;


public class InitialTaskException extends WorkflowException implements Serializable {

	private static final long serialVersionUID = 1L;

	public InitialTaskException() {
		super();
	}
	
	public InitialTaskException(String message) {
		super(message);
	}
	
	public InitialTaskException(Throwable throwable) {
		super(throwable);
	}
	
	public InitialTaskException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
