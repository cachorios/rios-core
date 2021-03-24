package com.cachorios.core.data.entidad;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Version;
import javax.persistence.MappedSuperclass;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@MappedSuperclass
public abstract class AbstractEntity implements EntidadInterface {

    @Version
	private int version;


/*	public int getVersion() {
		return version;
	}


	@Override
	public int hashCode() {
		if (getId() == null) {
			return super.hashCode();
		}
		return 31 + getId().hashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (getId() == null) {
			// New entities are only equal if the instance if the same
			return super.equals(other);
		}

		if (this == other) {
			return true;
		}
		if (!(other instanceof AbstractEntity)) {
			return false;
		}
		return getId().equals(((AbstractEntity) other).getId());
	}
*/
}