package org.beesden.commerce.search.exception;

import lombok.Getter;
import org.beesden.commerce.common.model.EntityReference;

/**
 * An exception which is thrown when an error occurs when adding an entity to the index.
 */
public class SearchEntityException extends RuntimeException {

    @Getter
    private EntityReference entity;

    public SearchEntityException(String message) {
        super(message);
    }

    public SearchEntityException(String message, EntityReference entity, Throwable e) {
        super(message, e);
        this.entity = entity;
    }

}