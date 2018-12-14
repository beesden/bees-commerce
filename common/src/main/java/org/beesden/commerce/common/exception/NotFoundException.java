package org.beesden.commerce.common.exception;

import lombok.Getter;
import org.beesden.commerce.common.model.EntityReference;
import org.beesden.commerce.common.model.EntityType;

import java.text.MessageFormat;

public class NotFoundException extends RuntimeException {

    public NotFoundException(EntityType type, String entityId) {
        super(MessageFormat.format("Unable to find entity: type = {0}, id = {1}", type.name(), entityId));
    }

}