package com.robsonarcoleze.dscatalog.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.robsonarcoleze.dscatalog.repositories.ProductRepository;
import com.robsonarcoleze.dscatalog.services.exceptions.ResourceNotFoundException;

@SpringBootTest
public class ProductSerceIt {
	
	@Autowired
	private ProductService service;
	
	@Autowired
	private ProductRepository repository;
	
	private long existingId;
	private long nonExistingId;
	private Long countTotalProducts;

	@BeforeEach
	void setUp() throws Exception{
		
		existingId = 1L;
		nonExistingId = 1000L;
		countTotalProducts = 25L;
	}
	
	@Test
	public void deleteShouldDleleteResourceWhenIdExists() {
		service.delete(existingId);
		
		Assertions.assertEquals(countTotalProducts - 1, repository.count());
	}
	
	@Test
	public void deleteShouldThrowNotFoundExceptionIdDoesNotExist() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, ()-> {
			service.delete(nonExistingId);
		});
	}
}
