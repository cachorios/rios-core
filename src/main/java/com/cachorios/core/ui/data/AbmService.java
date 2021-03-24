package com.cachorios.core.ui.data;

import javax.persistence.EntityNotFoundException;

import com.cachorios.core.data.entidad.EntidadInterface;
import com.cachorios.core.data.entidad.IUsuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AbmService<T extends EntidadInterface> {

	JpaRepository<T, Long> getRepository();


	default T save(IUsuario currentUsuario, T entity) {
		if(getPadre() == null) {
			return getRepository().saveAndFlush(entity);
		}
		if(entity.isNew()) {
			getList().add(entity);
		}
		return entity;
	}

	default void delete(IUsuario currentUsuario, T entity) {
		if (entity == null) {
			throw new EntityNotFoundException();
		}
		if(getPadre() == null) {
			getRepository().delete(entity);
		}else{
			getList().remove(entity);
		}

	}

	default void delete(IUsuario currentUsuario, long id) {
		delete(currentUsuario, load(id));
	}

	default long count() {
		if(getPadre() == null) {
			return getRepository().count();
		}
		return getList().size();
	}

	default T load(long id) {
		T entity = getRepository().findById(id).orElse(null);
		if (entity == null) {
			throw new EntityNotFoundException();
		}
		return entity;
	}

	T createNew(IUsuario usuarioActivo);

	default void setPadre(EntidadInterface padre){}

	default EntidadInterface getPadre(){
		return null;
	}

	default <T extends EntidadInterface> List<T> getList(){return null; }
}
