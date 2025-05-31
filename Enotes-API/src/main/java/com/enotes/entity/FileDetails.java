package com.enotes.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class FileDetails {

	//java_programming_tutorials.pdf displayname will be java_prog.pdf 
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String uploadFileName;
	private String originalFileName;
	private String displayFileName;
	private String path;
	private Long fileSize;
	
	public FileDetails() {
		
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUploadFileName() {
		return uploadFileName;
	}
	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}
	public String getOriginalFileName() {
		return originalFileName;
	}
	public void setOriginalFileName(String originalFileName) {
		this.originalFileName = originalFileName;
	}
	public String getDisplayFileName() {
		return displayFileName;
	}
	public void setDisplayFileName(String displayFileName) {
		this.displayFileName = displayFileName;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public Long getFileSize() {
		return fileSize;
	}
	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}
}
