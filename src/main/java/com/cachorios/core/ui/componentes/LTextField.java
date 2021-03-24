package com.cachorios.core.ui.componentes;

import com.vaadin.flow.data.value.ValueChangeMode;

public class LTextField extends com.vaadin.flow.component.textfield.TextField {

    public LTextField() {
        super();
        setAutoselect(true);
        setValueChangeMode(ValueChangeMode.LAZY);

    }


    public LTextField(String label) {
        this();
        this.setLabel(label);
    }

    public LTextField(String label, Integer colspan) {
        this(label);
        this.setColspan(colspan);
    }


    public LTextField(String label, String placeholder) {
        this(label);
        this.setPlaceholder(placeholder);
    }

    public LTextField(String label, String initialValue, String placeholder) {
        this(label);
        this.setValue(initialValue);
        this.setPlaceholder(placeholder);
    }

    public LTextField(ValueChangeListener<? super ComponentValueChangeEvent<com.vaadin.flow.component.textfield.TextField, String>> listener) {
        this();
        this.addValueChangeListener(listener);
    }

    public LTextField(String label, ValueChangeListener<? super ComponentValueChangeEvent<com.vaadin.flow.component.textfield.TextField, String>> listener) {
        this(label);
        this.addValueChangeListener(listener);
    }

    public LTextField(String label, String initialValue, ValueChangeListener<? super ComponentValueChangeEvent<com.vaadin.flow.component.textfield.TextField, String>> listener) {
        this(label);
        this.setValue(initialValue);
        this.addValueChangeListener(listener);
    }


    public void setColspan(Integer n){
        getElement().setAttribute("colspan", n.toString() );
    }


}
