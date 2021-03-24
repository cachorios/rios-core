package com.cachorios.core.ui.componentes.abm;


import com.cachorios.core.data.entidad.EntidadInterface;

public interface IAbmDataProvider {
	default void setPadre(EntidadInterface padre){};
	default EntidadInterface getPadre() {
		return null;
	}

}
