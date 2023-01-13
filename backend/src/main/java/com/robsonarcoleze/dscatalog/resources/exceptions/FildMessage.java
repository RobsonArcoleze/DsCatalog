package com.robsonarcoleze.dscatalog.resources.exceptions;

import java.io.Serializable;

public class FildMessage implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String fildName;
	private String message;
	
	public FildMessage() {
	}

	public FildMessage(String fildName, String message) {
		this.fildName = fildName;
		this.message = message;
	}

	public String getFildName() {
		return fildName;
	}

	public void setFildName(String fildName) {
		this.fildName = fildName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
