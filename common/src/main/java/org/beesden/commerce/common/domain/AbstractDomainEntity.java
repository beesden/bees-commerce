package org.beesden.commerce.common.domain;

import lombok.Data;
import org.beesden.commerce.common.Status;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@MappedSuperclass
public abstract class AbstractDomainEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@CreatedDate
	private LocalDateTime created;

	@CreatedBy
	private String createdBy;

	@LastModifiedDate
	private LocalDateTime lastEdited;

	@LastModifiedBy
	private String lastEditedBy;

	@Column
	@Enumerated( EnumType.STRING )
	private Status status = Status.DRAFT;

	public void updateTimestamps(String user) {
		lastEdited = LocalDateTime.now();
		lastEditedBy = user;
	}
}
