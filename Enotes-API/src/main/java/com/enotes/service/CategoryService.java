package com.enotes.service;

import java.util.List;

import com.enotes.entity.Category;

public interface CategoryService {

	public Boolean savecategory(Category category);
	public List<Category> getAllCategory();
	
}
