package com.cachorios.core.ui.componentes.formButtosBar;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.shared.Registration;

@CssImport("./components/form-buttons-bar.css")
public class FormButtonsBar extends Footer {
    private Button save;
    private Button cancel;
    private Button borrar;

    public FormButtonsBar() {
        this.addClassName("form-button-bar");

        save = new Button( "Guardar" );
        save.setIcon(FontAwesome.Regular.SAVE.create());
        save.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        cancel = new Button("Cancelar", VaadinIcon.ARROW_BACKWARD.create());

        borrar = new Button(VaadinIcon.TRASH.create());
        borrar.addClassName("borrar");

        add(save, cancel, borrar);

        for (Button button : new Button[] {  cancel, borrar }) {
            button.addThemeVariants(ButtonVariant.LUMO_CONTRAST, ButtonVariant.LUMO_TERTIARY);
        }
        this.setWidth("100%");
    }

    public FormButtonsBar setSaveText(String saveText) {
        save.setText(saveText == null ? "" : saveText);
        return this;
    }

    public FormButtonsBar setCancelText(String cancelText) {
        cancel.setText(cancelText == null ? "" : cancelText);
        return this;
    }

    public FormButtonsBar setDeleteText(String deleteText) {
        borrar.setText(deleteText == null ? "" : deleteText);
        return this;
    }

    public FormButtonsBar setSaveDisabled(boolean saveDisabled) {
        save.setEnabled(!saveDisabled);
        return this;
    }

    public FormButtonsBar setCancelDisabled(boolean cancelDisabled) {
        cancel.setEnabled(!cancelDisabled);
        return this;
    }

    public FormButtonsBar setDeleteDisabled(boolean deleteDisabled) {
        borrar.setEnabled(!deleteDisabled);
        return this;
    }

    public static class SaveEvent extends ComponentEvent<FormButtonsBar> {
        public SaveEvent(FormButtonsBar source, boolean fromClient) {
            super(source, fromClient);
        }
    }

    public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
        return save.addClickListener(e -> listener.onComponentEvent(new SaveEvent(this, true)));
    }

    public static class CancelEvent extends ComponentEvent<FormButtonsBar> {
        public CancelEvent(FormButtonsBar source, boolean fromClient) {
            super(source, fromClient);
        }
    }

    public Registration addCancelListener(ComponentEventListener<CancelEvent> listener) {
        return cancel.addClickListener(e -> listener.onComponentEvent(new CancelEvent(this, true)));
    }

    public static class DeleteEvent extends ComponentEvent<FormButtonsBar> {
        public DeleteEvent(FormButtonsBar source, boolean fromClient) {
            super(source, fromClient);
        }
    }

    public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
        return borrar.addClickListener(e -> listener.onComponentEvent(new DeleteEvent(this, true)));
    }

}
