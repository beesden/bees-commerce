package org.beesden.commerce.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.beesden.commerce.utils.constants.TableNames;

@Entity
@Table(name = TableNames.CHANNEL)
public class Channel extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@Column
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}