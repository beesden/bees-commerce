package org.beesden.commerce.content.service;

import org.beesden.commerce.common.model.PagedRequest;
import org.beesden.commerce.common.model.PagedResponse;
import org.beesden.commerce.content.model.Content;

public interface ContentService {

	long createContent(Content content);

	void deleteContent(long contentId);

	Content getContent(long contentId);

	PagedResponse<Content> listCategories(PagedRequest pagination);

	long updateContent(long contentId, Content content);
}