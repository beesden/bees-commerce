package org.beesden.commerce.content.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.beesden.commerce.common.domain.AbstractDomainEntity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

// @Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "bees_content_field")
public class ContentFieldDTO extends AbstractDomainEntity {

	@Id
	@Column
	@GeneratedValue
	private long id;

	@Column(nullable = false)
	private String type;

	@Column(nullable = false)
	private String key;

	@Column(nullable = false)
	private String label;

	private boolean required;
}