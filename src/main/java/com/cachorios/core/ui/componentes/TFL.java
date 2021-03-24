package com.cachorios.core.ui.componentes;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.dom.Element;

public class TFL extends Component {

    Element labelElement = new Element("label");
    Element inputElement = new Element("input");

    public TFL() {
        inputElement.addPropertyChangeListener("value", "change",e->{});
        getElement().appendChild(labelElement, inputElement);
    }
}