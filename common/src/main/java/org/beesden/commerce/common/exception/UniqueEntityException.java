package org.beesden.commerce.common.exception;

import org.beesden.commerce.common.model.EntityType;

import java.text.MessageFormat;

public class UniqueEntityException extends RuntimeException {

    public UniqueEntityException(EntityType type, String entityId) {
        super(MessageFormat.format("Entity already exists: type = {0}, id = {1}", type.name(), entityId));

    }

}