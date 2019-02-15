package org.beesden.commerce.common.domain;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Data
@MappedSuperclass
public abstract class AbstractDomainEntity {

	@CreatedDate
	private LocalDateTime created;

	@CreatedBy
	private String createdBy;

	@LastModifiedDate
	private LocalDateTime lastEdited;

	@LastModifiedBy
	private String lastEditedBy;

	protected void updateTimestamps(String user) {
		lastEdited = LocalDateTime.now();
		lastEditedBy = user;
	}
}
