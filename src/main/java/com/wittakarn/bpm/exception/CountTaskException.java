package com.wittakarn.bpm.exception;

import java.io.Serializable;


public class CountTaskException extends WorkflowException implements Serializable {

	private static final long serialVersionUID = 1L;

	public CountTaskException() {
		super();
	}
	
	public CountTaskException(String message) {
		super(message);
	}
	
	public CountTaskException(Throwable throwable) {
		super(throwable);
	}
	
	public CountTaskException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
