package com.cachorios.core.ui.componentes.dialogo;


import com.cachorios.core.ui.componentes.abm.Abm;
import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Header;

import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.theme.lumo.Lumo;

@CssImport("./styles/my-dialog.css")
public class DialogABM extends Dialog {
    public String DOCK = "dock";
    public String FULLSCREEN = "fullscreen";

    private boolean isDocked = false;
    private boolean isFullScreen = false;

    private Abm view;

    private Header header;
    private Button min;
    private Button max;

    private H2 title;
    private VerticalLayout contenido;
    private Footer footer;

    private String sWidth = "600px";

    private Button save;
    private Button cancel;
    private Button borrar;

    public DialogABM() {

        setDraggable(true);
        setModal(true);
        setResizable(true);
        this.setCloseOnEsc(true);

        // Dialog theming
        getElement().getThemeList().add("my-dialog");

        setWidth(sWidth);

        // Accessibility
        getElement().setAttribute("aria-labelledby", "dialog-title");

        // Header
        title = new H2("Dialog");
        title.addClassName("dialog-title");

        //min = new Button(VaadinIcon.DOWNLOAD_ALT.create());
        min = new Button(VaadinIcon.DOWNLOAD_ALT.create());
        min.addClickListener(event -> minimise());

        max = new Button(VaadinIcon.EXPAND_SQUARE.create());
        max.addClickListener(event -> maximise());

        Button close = new Button(VaadinIcon.CLOSE_SMALL.create());
        close.addClickListener(event -> close());

        header = new Header(title, min, max, close);
        header.getElement().getThemeList().add(Lumo.DARK);
        add(header);

        //Contenido

        contenido = new VerticalLayout();
        contenido.addClassName("dialog-content");
        contenido.setAlignItems(FlexComponent.Alignment.STRETCH);

        add(contenido);

        //Pie
        save = new Button( "Guardar" );
        save.setIcon(FontAwesome.Regular.SAVE.create());
        save.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        cancel = new Button("Cancelar",VaadinIcon.ARROW_BACKWARD.create());

        borrar = new Button(VaadinIcon.TRASH.create());
        borrar.addClassName("borrar");

        footer = new Footer(save, cancel, borrar);
        add(footer);

        // Button theming
        for (Button button : new Button[] { min, max, close, cancel, borrar }) {
            button.addThemeVariants(ButtonVariant.LUMO_CONTRAST, ButtonVariant.LUMO_TERTIARY);
        }

    }

    public void setView(Abm view){
        this.view = view;
    }

    public void addContenido(Component... components){
        contenido.add(components);
    }

    public void setTitulo(String titulo){
        title.setText(titulo);
    }

    public void setTitle(String titulo){
        title.setText(titulo);
    }

    private void initialSize() {
        min.setIcon(VaadinIcon.DOWNLOAD_ALT.create());
        getElement().getThemeList().remove(DOCK);
        max.setIcon(VaadinIcon.EXPAND_SQUARE.create());
        getElement().getThemeList().remove(FULLSCREEN);
        setHeight("auto");
        setWidth(sWidth);
    }

    private void minimise() {
        if (isDocked) {
            initialSize();
        } else {
            if (isFullScreen) {
                initialSize();
            }
            min.setIcon(VaadinIcon.UPLOAD_ALT.create());

            getElement().getThemeList().add(DOCK);
            setWidth("320px");
        }
        isDocked = !isDocked;
        isFullScreen = false;
        contenido.setVisible(!isDocked);
        footer.setVisible(!isDocked);
    }
    private void maximise() {
        if (isFullScreen) {
            initialSize();
        } else {
            if (isDocked) {
                initialSize();
            }
            max.setIcon(VaadinIcon.COMPRESS_SQUARE.create());
            getElement().getThemeList().add(FULLSCREEN);
            setSizeFull();
            contenido.setVisible(true);
            footer.setVisible(true);
        }
        isFullScreen = !isFullScreen;
        isDocked = false;
    }

    //Botones accion
    public void setSaveText(String saveText) {
        save.setText(saveText == null ? "" : saveText);
    }

    public void setCancelText(String cancelText) {
        cancel.setText(cancelText == null ? "" : cancelText);
    }

    public void setDeleteText(String deleteText) {
        borrar.setText(deleteText == null ? "" : deleteText);
    }

    public void setSaveDisabled(boolean saveDisabled) {
        save.setEnabled(!saveDisabled);
    }

    public void setCancelDisabled(boolean cancelDisabled) {
        cancel.setEnabled(!cancelDisabled);
    }

    public void setDeleteDisabled(boolean deleteDisabled) {
        borrar.setEnabled(!deleteDisabled);
    }


    //Eventos

    public static class SaveEvent extends ComponentEvent<DialogABM> {
        public SaveEvent(DialogABM source, boolean fromClient) {
            super(source, fromClient);
        }
    }
    public Registration addSaveListener(ComponentEventListener<SaveEvent> listener){
        return save.addClickListener( e-> listener.onComponentEvent( new SaveEvent(this, true)));
    }

    public static class DeleteEvent extends ComponentEvent<DialogABM> {
        public DeleteEvent(DialogABM source, boolean fromClient) {
            super(source, fromClient);
        }
    }
    public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
        return borrar.addClickListener(e -> listener.onComponentEvent(new DeleteEvent(this, true)));
    }

    public static class CancelEvent extends ComponentEvent<DialogABM> {
        public CancelEvent(DialogABM source, boolean fromClient) {
            super(source, fromClient);
        }
    }

    public Registration addCancelListener(ComponentEventListener<CancelEvent> listener) {
        return cancel.addClickListener(e -> listener.onComponentEvent(new CancelEvent(this, true)));
    }
}
