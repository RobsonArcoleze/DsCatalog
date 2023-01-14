package com.robsonarcoleze.dscatalog.services.validation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.robsonarcoleze.dscatalog.dto.UserDTO;
import com.robsonarcoleze.dscatalog.dto.UserInsertDTO;
import com.robsonarcoleze.dscatalog.entities.User;
import com.robsonarcoleze.dscatalog.repositories.UserRepository;
import com.robsonarcoleze.dscatalog.resources.exceptions.FieldMessage;



public class UserUpdateValidator implements ConstraintValidator<UserUpdateValid, UserDTO> {
	
	@Autowired
	private UserRepository repository;
	
	@Override
	public void initialize(UserInsertValid ann) {
	}

	@Override
	public boolean isValid(UserInsertDTO dto, ConstraintValidatorContext context) {
		
		List<FieldMessage> list = new ArrayList<>();
		User user = repository.findByEmail(dto.getEmail());
		
		if(user != null) {
			list.add(new FieldMessage("email", "Email já existe"));
		}
		
		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}
		return list.isEmpty();
	}
}
