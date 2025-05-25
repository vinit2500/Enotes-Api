package com.enotes.service;

import java.util.List;

import com.enotes.dto.NotesDto;
import com.enotes.exception.ResourceNotFoundException;

public interface NotesService {

	public Boolean saveNotes(NotesDto notesDto) throws ResourceNotFoundException;
	
	public List<NotesDto> getAllNotes();
	
}
