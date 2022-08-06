package com.devsuperior.bds02.controllers.exceptions;

public class DependentComponentException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public DependentComponentException(String msg) {
		super(msg);
	}
}
