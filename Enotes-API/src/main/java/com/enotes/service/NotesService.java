package com.enotes.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.enotes.dto.NotesDto;
import com.enotes.exception.ResourceNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

public interface NotesService {

	public Boolean saveNotes(String notes, MultipartFile file) throws ResourceNotFoundException, JsonMappingException, JsonProcessingException, IOException;
	
	public List<NotesDto> getAllNotes();
	
}
