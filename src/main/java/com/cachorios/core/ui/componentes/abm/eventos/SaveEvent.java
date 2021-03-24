package com.cachorios.core.ui.componentes.abm.eventos;


import com.cachorios.core.data.entidad.EntidadInterface;
import com.cachorios.core.ui.componentes.abm.Abm;
import com.vaadin.flow.component.ComponentEvent;

public class SaveEvent extends ComponentEvent<Abm> {
	private EntidadInterface registroActivo;
	
	public SaveEvent(Abm source, boolean fromClient, EntidadInterface registroActivo) {
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

