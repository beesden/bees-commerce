package org.beesden.commerce.content.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.beesden.commerce.common.domain.AbstractDomainEntity;

import javax.persistence.*;

// @Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "bees_content_field_value")
public class ContentFieldValueDTO extends AbstractDomainEntity {

	@Id
	@Column
	@GeneratedValue
	private long id;

	@ManyToOne
	private ContentFieldDTO field;

	private String value;
}