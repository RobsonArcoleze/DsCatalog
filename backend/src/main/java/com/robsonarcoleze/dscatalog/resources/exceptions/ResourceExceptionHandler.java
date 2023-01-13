package com.robsonarcoleze.dscatalog.resources.exceptions;

import java.time.Instant;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.robsonarcoleze.dscatalog.services.exceptions.DataBaseException;
import com.robsonarcoleze.dscatalog.services.exceptions.ResourceNotFoundException;

@ControllerAdvice
public class ResourceExceptionHandler {
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<StandardError> entityNotFound(ResourceNotFoundException e, HttpServletRequest request){
		
		HttpStatus status = HttpStatus.NOT_FOUND;
		StandardError erro = new StandardError();
		erro.setTimestamp(Instant.now());
		erro.setStatus(status.value());
		erro.setError("Resource not found");
		erro.setMessage(e.getMessage());
		erro.setPath(request.getRequestURI());
		return ResponseEntity.status(status).body(erro);
	}
	
	@ExceptionHandler(DataBaseException.class)
	public ResponseEntity<StandardError> dataBase(DataBaseException e, HttpServletRequest request){
		
		HttpStatus status = HttpStatus.BAD_REQUEST;
		StandardError erro = new StandardError();
		erro.setTimestamp(Instant.now());
		erro.setStatus(status.value());
		erro.setError("Data Base exception");
		erro.setMessage(e.getMessage());
		erro.setPath(request.getRequestURI());
		return ResponseEntity.status(status).body(erro);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ValidationError> validation(MethodArgumentNotValidException e, HttpServletRequest request){
		
		HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
		ValidationError erro = new ValidationError();
		erro.setTimestamp(Instant.now());
		erro.setStatus(status.value());
		erro.setError("Validation exception");
		erro.setMessage(e.getMessage());
		erro.setPath(request.getRequestURI());
		
		for (FieldError f : e.getBindingResult().getFieldErrors()) {
			erro.addError(f.getField(), f.getDefaultMessage()); //GET FIELD PEGA O CAMPO DO ERRO
		}
		
		return ResponseEntity.status(status).body(erro);
	}
}
