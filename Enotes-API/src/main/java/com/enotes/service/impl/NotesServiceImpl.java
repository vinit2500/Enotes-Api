package com.enotes.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.enotes.dto.NotesDto;
import com.enotes.dto.NotesDto.CategoryDto;
import com.enotes.entity.FileDetails;
import com.enotes.entity.Notes;
import com.enotes.exception.ResourceNotFoundException;
import com.enotes.repository.CategoryRepository;
import com.enotes.repository.FileRepository;
import com.enotes.repository.NotesRepository;
import com.enotes.service.NotesService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class NotesServiceImpl implements NotesService {

	@Autowired
	private NotesRepository notesRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private FileRepository fileRepository;

	@Value("${file.upload.path}")
	private String uploadPath;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public Boolean saveNotes(String notes, MultipartFile file) throws ResourceNotFoundException, IOException {

		// convert string into json with help of object mapper
		ObjectMapper ob = new ObjectMapper();

		NotesDto notesDto = ob.readValue(notes, NotesDto.class);

		// category validation
		checkCategoryExist(notesDto.getCategory());

		Notes notesMap = modelMapper.map(notesDto, Notes.class);

		FileDetails fileDtls = saveFileDetails(file);

		if (!ObjectUtils.isEmpty(fileDtls)) {
            notesMap.setFileDetails(fileDtls);
		} else {
			notesMap.setFileDetails(null);
		}
		
		Notes saveNotes = notesRepository.save(notesMap);

		if (!ObjectUtils.isEmpty(saveNotes)) {
			return true;
		}
		return false;
	}

	@Override
	public List<NotesDto> getAllNotes() {
		List<NotesDto> allNotes = notesRepository.findAll().stream().map(note -> modelMapper.map(note, NotesDto.class))
				.toList();
		return allNotes;
	}

	private void checkCategoryExist(CategoryDto category) throws ResourceNotFoundException {

		categoryRepository.findById(category.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Category not found with given id"));

	}

	private FileDetails saveFileDetails(MultipartFile file) throws IOException {

		if (!ObjectUtils.isEmpty(file) && !file.isEmpty()) {
			String originalFilename = file.getOriginalFilename();
			List<String> allowExtension = Arrays.asList("pdf","xlsx","jpg","png");
			
			if(!allowExtension.contains(FilenameUtils.getExtension(originalFilename))) {
				throw new IllegalArgumentException("Invalid format Upload only .pdf, .xlsx, .jpg");
			}
			
			
			String rndString = UUID.randomUUID().toString();
			String extension = FilenameUtils.getExtension(originalFilename);
			String uploadFileName = rndString + "." + extension;
			
			File saveFile = new File(uploadPath);

			if (!saveFile.exists()) {
				saveFile.mkdir();
			}

			// path : enotesapiservice/notes/java.pdf
			String storePath = uploadPath.concat(uploadFileName);

		
			// upload file
			long upload = Files.copy(file.getInputStream(), Paths.get(storePath));

			if (upload != 0) {
				FileDetails fileDtls = new FileDetails();
				fileDtls.setOriginalFileName(originalFilename);
				fileDtls.setDisplayFileName(getDisplayName(originalFilename));
				fileDtls.setFileSize(file.getSize());
				fileDtls.setUploadFileName(uploadFileName);
				fileDtls.setPath(storePath);

				FileDetails save = fileRepository.save(fileDtls);
				return save;
			}

		}

		return null;
	}

	private String getDisplayName(String originalFilename) {
		// java_programming_tutorials.pdf we have to show only 8 characters
		// remove extension
		String extension = FilenameUtils.getExtension(originalFilename);
		String fileName = FilenameUtils.removeExtension(originalFilename);

		if (fileName.length() > 8) {
			fileName = fileName.substring(0, 7);
		}
		fileName = fileName + "." + extension;
		return fileName;
	}
}
