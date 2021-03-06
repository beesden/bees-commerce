package org.beesden.commerce.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor( access = AccessLevel.NONE )
public final class Utils {

	@SafeVarargs
	public static <T> Set<T> buildSet(T... items) {
		Set<T> set = new HashSet<>();
		Collections.addAll( set, items );
		return set;
	}

	public static boolean notNullOrEmpty( Collection collection ) {
		return collection != null && !collection.isEmpty();
	}
}
