package org.beesden.commerce.model.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column
	private Date created;

	@Column
	private String createdBy;

	@Column
	private Date lastEdited;

	@Column
	private String lastEditedBy;

	@Column
	private Integer status;

	// Getters and Setters

	public Date getCreated() {
		return created;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public Date getLastEdited() {
		return lastEdited;
	}

	public String getLastEditedBy() {
		return lastEditedBy;
	}

	public Integer getStatus() {
		return status;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public void setLastEdited(Date lastEdited) {
		this.lastEdited = lastEdited;
	}

	public void setLastEditedBy(String lastEditedBy) {
		this.lastEditedBy = lastEditedBy;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}
