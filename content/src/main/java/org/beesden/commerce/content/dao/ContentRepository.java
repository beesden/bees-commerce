package org.beesden.commerce.content.dao;

import org.beesden.commerce.content.domain.ContentDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentRepository extends JpaRepository<ContentDTO, Long> {
}