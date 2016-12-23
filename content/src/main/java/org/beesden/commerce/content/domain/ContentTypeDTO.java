package org.beesden.commerce.content.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.beesden.commerce.common.domain.AbstractDomainEntity;

import javax.persistence.*;
import java.util.Set;

// @Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "bees_content_type")
public class ContentTypeDTO extends AbstractDomainEntity {

	@Id
	@Column
	@GeneratedValue
	private long id;

	@Column(nullable = false)
	private String name;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "bees_content_type_fields", joinColumns = @JoinColumn(name = "content_field_id"), inverseJoinColumns = @JoinColumn(name = "content_type_id"))
	private Set<ContentFieldDTO> fields;

}