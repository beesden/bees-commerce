package org.beesden.commerce.content.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.beesden.commerce.common.domain.AbstractDomainEntity;
import org.beesden.commerce.content.model.Content;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "bees_content")
public class ContentDTO extends AbstractDomainEntity {

	@Id
	@Column
	@GeneratedValue
	private long id;

	@Column(nullable = false)
	private String title;

	@Lob
	private String description;

	@OneToMany(cascade = CascadeType.ALL)
	private Set<ContentFieldValueDTO> values;

	/**
	 * Convert the DTO to a content object.
	 *
	 * @return content
	 */
	public Content toContent() {
		Content content = new Content();

		content.setId(id);
		content.setTitle(title);
		content.setDescription(description);

		return content;
	}

	/**
	 * Update the DTO with data from a content object.
	 *
	 * @param content content object
	 */
	public void update(Content content) {
		id = content.getId();
		title = content.getTitle();
		description = content.getDescription();
	}

}