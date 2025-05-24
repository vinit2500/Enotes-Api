package com.enotes.service.impl;

import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.enotes.dto.CategoryDto;
import com.enotes.dto.CategoryResponse;
import com.enotes.entity.Category;
import com.enotes.repository.CategoryRepository;
import com.enotes.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private CategoryRepository categoryRepository;

	@Override    
	public Boolean savecategory(CategoryDto categoryDto) {
		
		categoryDto.setIsDeleted(false);
		categoryDto.setCreatedBy(1);
		categoryDto.setCreatedOn(new Date());
		// attributes ke name exactly same hone chaiye DTO aur Entity mai 
		Category category = modelMapper.map(categoryDto, Category.class);
		 Category savedCategory = categoryRepository.save(category);
	   	 return ObjectUtils.isEmpty(savedCategory) ? false : true;
	}

	@Override
	public List<CategoryDto> getAllCategory() {
		List<Category> categories = categoryRepository.findAll();
		//java 8 stream api concept 
		List<CategoryDto> categoryDtoList = categories.stream().map(cat-> modelMapper.map(cat, CategoryDto.class)).toList();
		return categoryDtoList;
	}

	@Override
	public List<CategoryResponse> getActiveCategory() {
		List<Category> categories = categoryRepository.findByIsActiveTrue();
		//only matching field map kr dega(id,name,description) baki nhi krega 
		List<CategoryResponse> categoryList = categories.stream().map(cat -> modelMapper.map(cat, CategoryResponse.class)).toList();
		return categoryList;
	}
}
