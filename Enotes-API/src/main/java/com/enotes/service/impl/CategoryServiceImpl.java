package com.enotes.service.impl;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.enotes.dto.CategoryDto;
import com.enotes.dto.CategoryResponse;
import com.enotes.entity.Category;
import com.enotes.exception.ResourceNotFoundException;
import com.enotes.repository.CategoryRepository;
import com.enotes.service.CategoryService;
import com.enotes.util.Validation;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private Validation validation;

	@Autowired
	private CategoryRepository categoryRepository;

	@Override
	public Boolean savecategory(CategoryDto categoryDto) {

		//validation checking 
		validation.categoryValidation(categoryDto);
		// attributes ke name exactly same hone chaiye DTO aur Entity mai
		Category category = modelMapper.map(categoryDto, Category.class);

		if (ObjectUtils.isEmpty(category.getId())) {
			// creating first time
			category.setIsDeleted(false);
//			category.setCreatedby(1);
//			category.setCreatedOn(new Date());
		} else {
			updatecategory(category);
		}

		Category savedCategory = categoryRepository.save(category);
		return ObjectUtils.isEmpty(savedCategory) ? false : true;
	}

	@Override
	public List<CategoryDto> getAllCategory() {
		List<Category> categories = categoryRepository.findByIsDeletedFalse();
		// java 8 stream api concept
		List<CategoryDto> categoryDtoList = categories.stream().map(cat -> modelMapper.map(cat, CategoryDto.class))
				.toList();
		return categoryDtoList;
	}

	@Override
	public List<CategoryResponse> getActiveCategory() {
		List<Category> categories = categoryRepository.findByIsActiveTrueAndIsDeletedFalse();
		// only matching field map kr dega(id,name,description) baki nhi krega
		List<CategoryResponse> categoryList = categories.stream()
				.map(cat -> modelMapper.map(cat, CategoryResponse.class)).toList();
		return categoryList;
	}

	@Override
	public CategoryDto getCategoryById(Integer id) throws ResourceNotFoundException {
		Category category = categoryRepository.findByIdAndIsDeletedFalse(id).orElseThrow(()->
		new ResourceNotFoundException("Category not found"));
		if (!ObjectUtils.isEmpty(category)) {
//			if(category.getName() == null ) {
//				throw new IllegalArgumentException("name is null");
//			}
			category.getName().toLowerCase();
			return modelMapper.map(category, CategoryDto.class);
		}
		return null;
	}

	@Override
	public Boolean deleteCategory(Integer id) {
		Optional<Category> findByCategory = categoryRepository.findById(id);

		if (findByCategory.isPresent()) {
			Category category = findByCategory.get();
			category.setIsDeleted(true);
			categoryRepository.save(category);
			return true;
		}
		return false;
	}

	private void updatecategory(Category category) {
		Optional<Category> findById = categoryRepository.findById(category.getId());

		if (findById.isPresent()) {
			Category existingCategory = findById.get();
			category.setCreatedby(existingCategory.getCreatedby());
			category.setCreatedOn(existingCategory.getCreatedOn());
			category.setIsDeleted(existingCategory.getIsDeleted());
//			category.setUpdatedBy(1);
//			category.setUpdatedOn(new Date());
		}
	}

}
