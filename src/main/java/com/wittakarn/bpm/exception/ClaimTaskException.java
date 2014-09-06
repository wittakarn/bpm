package com.wittakarn.bpm.exception;

import java.io.Serializable;


public class ClaimTaskException extends BPMException implements Serializable {

	private static final long serialVersionUID = 1L;

	public ClaimTaskException() {
		super();
	}
	
	public ClaimTaskException(String message) {
		super(message);
	}
	
	public ClaimTaskException(Throwable throwable) {
		super(throwable);
	}
	
	public ClaimTaskException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
