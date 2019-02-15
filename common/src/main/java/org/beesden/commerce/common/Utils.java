package org.beesden.commerce.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class Utils {

    public static boolean notNullOrEmpty(Collection collection) {
        return collection != null && !collection.isEmpty();
    }

}
