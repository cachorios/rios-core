package com.cachorios.core.ui.componentes.abm.eventos;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;

public class ValueChangeEvent extends ComponentEvent<Component> {
	public ValueChangeEvent(Component source, boolean fromClient) {
		super(source, fromClient);
	}
}
