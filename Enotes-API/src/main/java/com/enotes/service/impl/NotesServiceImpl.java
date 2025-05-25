package com.enotes.service.impl;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.enotes.dto.NotesDto;
import com.enotes.dto.NotesDto.CategoryDto;
import com.enotes.entity.Category;
import com.enotes.entity.Notes;
import com.enotes.exception.ResourceNotFoundException;
import com.enotes.repository.CategoryRepository;
import com.enotes.repository.NotesRepository;
import com.enotes.service.NotesService;

@Service
public class NotesServiceImpl implements NotesService {

	@Autowired
	private NotesRepository notesRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public Boolean saveNotes(NotesDto notesDto) throws ResourceNotFoundException {

		//category validation
		checkCategoryExist(notesDto.getCategory());
		
		Notes notes = modelMapper.map(notesDto, Notes.class);
		Notes saveNotes = notesRepository.save(notes);
		
		if(!ObjectUtils.isEmpty(saveNotes)) {
			return true;
		}
		return false;
	}

	@Override
	public List<NotesDto> getAllNotes() {
		List<NotesDto> allNotes = notesRepository.findAll().stream().map(note -> modelMapper.map(note, NotesDto.class)).toList();
		return allNotes;
	}
	
	private void checkCategoryExist(CategoryDto category) throws ResourceNotFoundException {
	
		Category existingCategory = categoryRepository.findById(category.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Category not found with given id"));
		
		
	}


}
