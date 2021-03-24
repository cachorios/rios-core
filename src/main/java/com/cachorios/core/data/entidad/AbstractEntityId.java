package com.cachorios.core.data.entidad;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.HashCodeExclude;

import javax.persistence.*;
import java.util.Objects;


@Getter
@Setter
@MappedSuperclass
	public class AbstractEntityId implements EntidadInterface {

		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		@EqualsAndHashCode.Include
		private Long id;

		@Version
		private int version;


	@Override
	public int hashCode() {
		return getClass().hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) return false;
		if (this == o) return true;

		if (!Objects.equals(getClass(), o.getClass())) {
			return false;
		}

		AbstractEntityId that = (AbstractEntityId) o;
		return this.id != null && Objects.equals(this.id, that.id);
	}


//
//		@Override
//		public int hashCode() {
//			if (id == null) {
//				return super.hashCode();
//			}
//
//			return 31 + id.hashCode();
//		}
//
//		@Override
//		public boolean equals(Object other) {
//			if (id == null) {
//				// New entities are only equal if the instance if the same
//				return super.equals(other);
//			}
//
//			if (this == other) {
//				return true;
//			}
//			if (!(other instanceof com.gmail.cachorios.core.ui.data.AbstractEntityId)) {
//				return false;
//			}
//			return id.equals(((AbstractEntityId) other).id);
//		}

	}