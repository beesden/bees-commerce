package org.beesden.commerce.content.service.impl;

import org.beesden.commerce.common.exception.NotFoundException;
import org.beesden.commerce.common.model.EntityType;
import org.beesden.commerce.common.model.PagedRequest;
import org.beesden.commerce.common.model.PagedResponse;
import org.beesden.commerce.content.dao.ContentRepository;
import org.beesden.commerce.content.domain.ContentDTO;
import org.beesden.commerce.content.model.Content;
import org.beesden.commerce.content.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ContentServiceImpl implements ContentService {

	private ContentRepository contentRepository;

	@Autowired
	public ContentServiceImpl(ContentRepository contentRepository) {
		this.contentRepository = contentRepository;
	}

	@Override
	public long createContent(Content content) {

		ContentDTO createdContent = new ContentDTO();
		createdContent.update(content);
		createdContent.updateTimestamps();
		createdContent = contentRepository.save(createdContent);
		return createdContent.getId();
	}

	@Override
	public void deleteContent(long contentId) {
//		contentRepository.delete(contentId);
	}

	@Override
	public Content getContent(long contentId) {

		ContentDTO content = contentRepository.getOne(contentId);
		if (content == null) {
			throw new NotFoundException(EntityType.CONTENT, String.valueOf(contentId));
		}

		return content.toContent();

	}

	@Override
	public PagedResponse<Content> listCategories(PagedRequest pagination) {

		Page<ContentDTO> pagedCategories = contentRepository.findAll(pagination.toPageable());
		List<Content> contentList = pagedCategories.getContent()
												   .stream()
												   .map(ContentDTO::toContent)
												   .collect(Collectors.toList());

		return new PagedResponse<>(pagination, contentList,pagedCategories.getTotalElements());

	}

	@Override
	public long updateContent(long contentId, Content content) {

		ContentDTO updatedContent = contentRepository.getOne(contentId);
		if (updatedContent == null) {
			throw new NotFoundException(EntityType.CONTENT, String.valueOf(contentId));
		}

		updatedContent.update(content);
		updatedContent.updateTimestamps();
		updatedContent = contentRepository.save(updatedContent);
		return updatedContent.getId();
	}

}