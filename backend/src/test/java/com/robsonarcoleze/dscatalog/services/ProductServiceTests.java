package com.robsonarcoleze.dscatalog.services;

import java.util.List;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.robsonarcoleze.dscatalog.dto.ProductDTO;
import com.robsonarcoleze.dscatalog.entities.Category;
import com.robsonarcoleze.dscatalog.entities.Product;
import com.robsonarcoleze.dscatalog.repositories.CategoryRepository;
import com.robsonarcoleze.dscatalog.repositories.ProductRepository;
import com.robsonarcoleze.dscatalog.services.exceptions.DataBaseException;
import com.robsonarcoleze.dscatalog.services.exceptions.ResourceNotFoundException;
import com.robsonarcoleze.dscatalog.tests.Factory;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

	@InjectMocks
	private ProductService service;
	
	@Mock
	private ProductRepository repository;
	
	@Mock
	private CategoryRepository categoryRepository;
	
	
	private long existingId;
	private long nonExistingId;
	private long dependtId;
	private PageImpl<Product> page;
	private Product product;
	private Category category;
	private ProductDTO productDTO;
	
	
	@BeforeEach
	void setUp() throws Exception{
		existingId = 1L;
		nonExistingId = 1000L;
		dependtId = 3L;
		product = Factory.createProduct();
		page = new PageImpl<>(List.of(product));
		productDTO = Factory.createProductDTO();
	
		Mockito.when(repository.findAll((Pageable)any())).thenReturn(page);
		
		Mockito.when(repository.save(any())).thenReturn(product);
		
		Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(product));
		Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
		Mockito.when(repository.find(any(), any(), any())).thenReturn(page);

		Mockito.when(repository.getReferenceById(existingId)).thenReturn(product);
		Mockito.when(repository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);
		
		Mockito.when(categoryRepository.getReferenceById(existingId)).thenReturn(category);
		Mockito.when(categoryRepository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);
		
		Mockito.doNothing().when(repository).deleteById(existingId);
		Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(nonExistingId);
		Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependtId);		
			}
	
	@Test
	public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.update(nonExistingId, productDTO);
		});
	}
	
	@Test
	public void updateShouldReturnProductDtoWhenIdExists() {
		
		ProductDTO result = service.update(existingId, productDTO);
		
		Assertions.assertNotNull(result);
	}
	
	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.findById(nonExistingId);
		});
	}
	
	@Test
	public void findByIdShouldReturnProductDtoWhenIdExists() {
		
		ProductDTO result = service.findById(existingId); 
		
		Assertions.assertNotNull(result);
	}
	
	@Test
	public void findAllPagedShoulReturnPageable() {
		
		Pageable pageable = PageRequest.of(0, 10);
		
		Page<ProductDTO> result = service.findAllPaged(0L, "", pageable);
		
		Assertions.assertNotNull(result);
	}
	
	@Test
	public void deleteShouldThrowDataBaseExceptionWhenDependetId() {
		Assertions.assertThrows(DataBaseException.class, ()->{
			service.delete(dependtId);
		});
		
		Mockito.verify(repository, Mockito.times(1)).deleteById(dependtId);
	}

	
	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		Assertions.assertThrows(ResourceNotFoundException.class, ()->{
			service.delete(nonExistingId);
		});
	}
	
	@Test
	public void deleteShouldDoNothingWhenIdExists() {
		Assertions.assertDoesNotThrow(() -> {
			service.delete(existingId);
		});
		
		Mockito.verify(repository, Mockito.times(1)).deleteById(existingId);
	}
	
	
}
