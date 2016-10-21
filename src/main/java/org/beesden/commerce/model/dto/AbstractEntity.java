package org.beesden.commerce.model.dto;

import lombok.Data;
import org.beesden.commerce.Status;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDateTime;

@MappedSuperclass
@Data
public abstract class AbstractEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column
	private LocalDateTime created;

	@Column
	private String createdBy;

	@Column
	private LocalDateTime lastEdited;

	@Column
	private String lastEditedBy;

	@Column
	private Status status = Status.DRAFT;

	/**
	 * Populate timestamps.
	 */
	public void updateTimestamps() {
		if ( created == null ) {
			created = LocalDateTime.now();
		}
		if ( createdBy == null ) {
			createdBy = "testuser";
		}
		lastEdited = LocalDateTime.now();
		lastEditedBy = "testUser";
	}
}
