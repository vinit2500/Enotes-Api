package com.enotes.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import com.enotes.dto.NotesDto;
import com.enotes.dto.NotesDto.CategoryDto;
import com.enotes.dto.NotesDto.FileDto;
import com.enotes.dto.NotesResponse;
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
	public Boolean saveNotes(String notes, MultipartFile file) throws Exception {

		// convert string into json with help of object mapper
		ObjectMapper ob = new ObjectMapper();

		NotesDto notesDto = ob.readValue(notes, NotesDto.class);
		notesDto.setIsDeleted(false);
		notesDto.setDeletedOn(null);

		if (!ObjectUtils.isEmpty(notesDto.getId())) {
			updateNotes(notesDto, file);
		}

		// category validation
		checkCategoryExist(notesDto.getCategory());

		Notes notesMap = modelMapper.map(notesDto, Notes.class);

		FileDetails fileDtls = saveFileDetails(file);

		if (!ObjectUtils.isEmpty(fileDtls)) {
			notesMap.setFileDetails(fileDtls);
		} else {
//			notesMap.setFileDetails(null);
			if (ObjectUtils.isEmpty(notesDto.getId())) {
				notesMap.setFileDetails(null);
			}
		}

		Notes saveNotes = notesRepository.save(notesMap);

		if (!ObjectUtils.isEmpty(saveNotes)) {
			return true;
		}
		return false;
	}

	private void updateNotes(NotesDto notesDto, MultipartFile file) throws Exception {

		Notes existsNotes = notesRepository.findById(notesDto.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Invalid notes id"));

		if (ObjectUtils.isEmpty(file)) {
			notesDto.setFileDetails(modelMapper.map(existsNotes.getFileDetails(), FileDto.class));
		}

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
			List<String> allowExtension = Arrays.asList("pdf", "xlsx", "jpg", "png", "docx");

			if (!allowExtension.contains(FilenameUtils.getExtension(originalFilename))) {
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

	@Override
	public byte[] downloadFile(FileDetails fileDtls) throws Exception {

		InputStream io = new FileInputStream(fileDtls.getPath());

		byte[] byteData = StreamUtils.copyToByteArray(io);
		return byteData;
	}

	@Override
	public FileDetails getFileDetails(Integer id) throws Exception {
		FileDetails fileDtls = fileRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("File not present with " + id + "id"));
		return fileDtls;
	}

	@Override
	public NotesResponse getAllNotesByUser(Integer userId, Integer pageNo, Integer pageSize) {

		Pageable pageable = PageRequest.of(pageNo, pageSize); // 0 means first page
		Page<Notes> pageNotes = notesRepository.findByCreatedByAndIsDeletedFalse(userId, pageable);

		List<NotesDto> notesDto = pageNotes.get().map(n -> modelMapper.map(n, NotesDto.class)).toList();

		NotesResponse notes = new NotesResponse();
		notes.setNotes(notesDto);
		notes.setPageNo(pageNotes.getNumber());
		notes.setPageSize(pageNotes.getSize());
		notes.setTotalElements(pageNotes.getTotalElements());
		notes.setTotalPages(pageNotes.getTotalPages());
		notes.setIsFirst(pageNotes.isFirst());
		notes.setIsLast(pageNotes.isLast());
		return notes;
	}

	@Override
	public void softDeleteNotes(Integer id) throws Exception {
		Notes notes = notesRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Notes not found withh given id"));

		notes.setIsDeleted(true);
		notes.setDeletedOn(LocalDateTime.now());
		notesRepository.save(notes);
	}

	@Override
	public void restoreNotes(Integer id) throws Exception {
		Notes notes = notesRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Notes not found withh given id"));

		notes.setIsDeleted(false);
		notes.setDeletedOn(null);
		notesRepository.save(notes);
	}

	@Override
	public List<NotesDto> getUserRecycleBinNotes(Integer userId) {
		List<Notes> recycleNotes = notesRepository.findByCreatedByAndIsDeletedTrue(userId);
		List<NotesDto> notesDtoList = recycleNotes.stream().map(note -> modelMapper.map(note, NotesDto.class)).toList();
		return notesDtoList;
	}

	@Override
	public void hardDeleteNotes(Integer id) throws Exception {
		Notes notes = notesRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Notes not found withh given id"));
		
		if(notes.getIsDeleted()) {
			notesRepository.delete(notes);
		}
		else 
		{
		  throw new IllegalArgumentException("You can not hard delete directly");	
		}
	}

	@Override
	public void emptyRecycleBin(Integer userId) {
	
		List<Notes> recycleNotes = notesRepository.findByCreatedByAndIsDeletedTrue(userId);
		
		if(!CollectionUtils.isEmpty(recycleNotes)) {
			notesRepository.deleteAll(recycleNotes);
		}
	}

}
