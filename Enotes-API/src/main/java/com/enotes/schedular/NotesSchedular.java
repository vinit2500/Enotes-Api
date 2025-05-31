package com.enotes.schedular;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.enotes.entity.Notes;
import com.enotes.repository.NotesRepository;

@Component
public class NotesSchedular {

	@Autowired
	private NotesRepository notesRepository;
	
	@Scheduled(cron = "0 0 0 * * ?") //Everyday scheduled on 12 pm night
	public void deleteNotesSchedular() {
//		LocalDateTime.now() 20 suppose 
//		then cutoffDate will be 14 
		LocalDateTime cutOffDate = LocalDateTime.now().minusDays(7);
		
	 List<Notes> deleteNotes = notesRepository.findAllByIsDeletedAndDeletedOnBefore(true, cutOffDate);
	 notesRepository.deleteAll(deleteNotes);
	}
}
