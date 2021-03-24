package com.cachorios.core.ui.componentes.abm;

import com.cachorios.core.data.entidad.EntidadInterface;
import com.vaadin.flow.component.HasText;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.data.binder.BeanValidationBinder;

@CssImport("./components/lar-abm-form.css")
public class AbmForm<T extends EntidadInterface> extends Div implements Abm.IAbmForm<T> {
    private H3 titulo = new H3();
    private FormLayout form = new FormLayout();

    public AbmForm() {
        addClassName("abm-form");
//        titulo.setText("Edicion de ");
//        titulo.addClassName("titulo");
        add(  form);
    }


    @Override
    public HasText getTitulo() {
        return titulo;
    }

    @Override
    public FormLayout getFormLayuot() {
        return form;
    }

    @Override
    public void setBinder(BeanValidationBinder<T> binder) {

    }
}
