package com.enotes.entity;

import java.util.Date;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseModel {

	@CreatedBy //2nd step 
	@Column(updatable = false)
	private Integer createdBy;
	
	@CreatedDate
	@Column(updatable = false)
	private Date createdOn;
	
	@LastModifiedBy
	@Column(insertable = false)
	private Integer updatedBy;
	
	@LastModifiedDate
	@Column(insertable = false)
	private Date updatedOn;
	
	//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	
	public Integer getCreatedby() {
		return createdBy;
	}
	public void setCreatedby(Integer createdBy) {
		this.createdBy = createdBy;
	}
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	public Integer getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}
	public Date getUpdatedOn() {
		return updatedOn;
	}
	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}
	
}
