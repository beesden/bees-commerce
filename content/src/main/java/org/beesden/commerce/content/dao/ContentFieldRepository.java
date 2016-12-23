package org.beesden.commerce.content.dao;

import org.beesden.commerce.content.domain.ContentFieldDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentFieldRepository extends JpaRepository<ContentFieldDTO, Long> {
}