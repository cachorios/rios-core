package com.cachorios.core.ui.componentes.abm.eventos;

import com.cachorios.core.data.entidad.EntidadInterface;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;

public class PreUpdateEvent extends ComponentEvent<Component> {
	private EntidadInterface registroActivo;
	
	public PreUpdateEvent(Component source, boolean fromClient, EntidadInterface registroActivo) {
		super(source, fromClient);
		this.registroActivo = registroActivo;
	}


	public EntidadInterface getRegistroActivo() {
		return registroActivo;
	}
	public void setRegistroActivo(EntidadInterface registroActivo) {
		this.registroActivo = registroActivo;
	}
}

