package org.beesden.commerce.model;

import lombok.Data;
import org.beesden.common.Status;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@Data
@MappedSuperclass
public abstract class AbstractEntity implements Serializable {

	private static final long serialVersionUID = 1L;

//	@CreatedDate
//	private LocalDateTime created;

	@CreatedBy
	private String createdBy;

//	@LastModifiedDate
//	private LocalDateTime lastEdited;

	@LastModifiedBy
	private String lastEditedBy;

	@Column
	@Enumerated( EnumType.STRING )
	private Status status = Status.DRAFT;

	/**
	 * Populate timestamps.
	 */
	public void updateTimestamps() {
//		if ( created == null ) {
//			created = LocalDateTime.now();
//		}
//		if ( createdBy == null ) {
//			createdBy = "testuser";
//		}
//		lastEdited = LocalDateTime.now();
		lastEditedBy = "testUser";
	}
}
