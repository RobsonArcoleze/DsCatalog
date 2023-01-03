package com.robsonarcoleze.dscatalog.services.exceptions;

public class ResourceNotFounException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public ResourceNotFounException(String msg) {
		super(msg);
	}

}
