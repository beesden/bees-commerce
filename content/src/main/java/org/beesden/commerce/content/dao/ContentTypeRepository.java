package org.beesden.commerce.content.dao;

import org.beesden.commerce.content.domain.ContentTypeDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentTypeRepository extends JpaRepository<ContentTypeDTO, Long> {
}