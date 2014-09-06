package com.wittakarn.bpm.exception;

import java.io.Serializable;


public class CancelClaimTaskException extends BPMException implements Serializable {

	private static final long serialVersionUID = 1L;

	public CancelClaimTaskException() {
		super();
	}
	
	public CancelClaimTaskException(String message) {
		super(message);
	}
	
	public CancelClaimTaskException(Throwable throwable) {
		super(throwable);
	}
	
	public CancelClaimTaskException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
