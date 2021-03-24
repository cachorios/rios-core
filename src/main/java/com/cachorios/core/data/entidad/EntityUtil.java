package com.cachorios.core.data.entidad;


public final class EntityUtil {

	public static final String getName(Class<? extends EntidadInterface> type) {
		// All main entities have simple one word names, so this is sufficient. Metadata
		// could be added to the class if necessary.
		return type.getSimpleName();

	}
}
