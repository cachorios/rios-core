package com.cachorios.core.ui.componentes.abm;

import com.cachorios.core.ui.componentes.abm.eventos.ValueChangeEvent;
import com.cachorios.core.ui.componentes.tabContents.TabsContent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.dom.Element;

import java.util.Objects;

public class FormLayout extends com.vaadin.flow.component.formlayout.FormLayout {

	private Abm contenedor;

	public FormLayout() {
		super();
		this.getElement().getThemeList().add("my-form-layout");
	}


	public void add(Component component, int colspan) {
		this.add(new Component[]{component});
		if(component instanceof Abm || component instanceof TabsContent){
			ComponentUtil.addListener(component, ValueChangeEvent.class, e ->  contenedor.hayCambios());
		}
		this.setColspan(component, colspan);
	}
	public void add(Component... components) {
		Objects.requireNonNull(components, "Components should not be null");
		Component[] var2 = components;
		int var3 = components.length;

		for(int var4 = 0; var4 < var3; ++var4) {
			Component component = var2[var4];
			if(component instanceof Abm || component instanceof TabsContent){
				ComponentUtil.addListener(component, ValueChangeEvent.class, e ->  contenedor.hayCambios());
			}
			Objects.requireNonNull(component, "Component to add cannot be null");
			this.getElement().appendChild(new Element[]{component.getElement()});
		}
	}

	public Abm getContenedor() {
		return contenedor;
	}

	public void setContenedor(Abm contenedor) {
		this.contenedor = contenedor;
	}
}
