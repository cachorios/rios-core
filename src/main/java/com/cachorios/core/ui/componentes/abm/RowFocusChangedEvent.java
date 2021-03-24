package com.cachorios.core.ui.componentes.abm;


import com.cachorios.core.data.entidad.EntidadInterface;
import com.vaadin.flow.component.ComponentEvent;

public class RowFocusChangedEvent extends ComponentEvent<Abm> {
	private EntidadInterface registroActivo;
	
	public RowFocusChangedEvent(Abm source, boolean fromClient, EntidadInterface registroActivo) {
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
