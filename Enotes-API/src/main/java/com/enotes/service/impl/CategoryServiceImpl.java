package com.enotes.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.enotes.entity.Category;
import com.enotes.repository.CategoryRepository;
import com.enotes.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	@Override    
	public Boolean savecategory(Category category) {
	     category.setIsDeleted(false);
	     category.setCreatedby(1);
	     category.setCreatedOn(new Date());
		 Category savedCategory = categoryRepository.save(category);
	   	 return ObjectUtils.isEmpty(savedCategory) ? false : true;
	}

	@Override
	public List<Category> getAllCategory() {
		List<Category> categories = categoryRepository.findAll();
		return categories;
	}
}
