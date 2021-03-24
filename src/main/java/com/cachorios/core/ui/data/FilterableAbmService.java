package com.cachorios.core.ui.data;

import java.util.Optional;


import com.cachorios.core.data.entidad.EntidadInterface;
import com.cachorios.core.data.entidad.IUsuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FilterableAbmService<T extends EntidadInterface> extends AbmService<T> {

	Page<T> findAnyMatching(Optional<String> filter, Pageable pageable);

///	long countAnyMatching(Optional<String> filter);


	Class<T> getBeanType();

	default String makeForLike(String filtro) {
		return "%" + filtro.toUpperCase() + "%";
	}

	default Optional<T> findById(Long id){
		return null;
	}

	default public IUsuario getUsuario(){
		return null;
	}
}