package com.cachorios.core.ui.componentes.searchbar;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.dom.DebouncePhase;

@CssImport("./components/searchbar.css")
public class SearchBar extends Div {
    private TextField   textField       = new TextField();
    private Button      actionButton    = new Button("");
//    private Button      clearButton     = new Button("Limpíar búsqueda");

    public SearchBar() {
        var row = new Div();
        row.addClassName("row");

        textField.addClassName("field");
        actionButton.addClassNames("action" );
        actionButton.setThemeName( ButtonVariant.LUMO_ICON.getVariantName() ) ;


        //textField.getPlaceholder()
        Icon icon = new Icon(VaadinIcon.SEARCH);
        textField.setPrefixComponent(icon);
        addClassName("search-bar");

        row.add(textField, actionButton);
        add(row);

        textField.setValueChangeMode(ValueChangeMode.EAGER);
        ComponentUtil.addListener(textField, SearchValueChanged.class,
                e -> fireEvent(new FilterChanged(this, false)));

        textField.setClearButtonVisible(true);
        textField.addBlurListener(e -> inFocus(false));
        textField.addFocusListener(e -> inFocus(true));

//        clearButton.addClickListener(e -> {
//            textField.clear();
//        });

        inFocus(false);
    }

    public TextField getFieldSearch() {
        return textField;
    }

    public  Button getActionButton(){
        return actionButton;
    }

    private void inFocus(boolean inFocus){
        this.actionButton.setVisible(! inFocus);
    }

    public String getFilter() {
        return textField.getValue();
    }

    public void setActionText(String actionText) {
        actionButton.setText(actionText);
    }

    public void addFilterChangeListener(ComponentEventListener<FilterChanged> listener) {
        this.addListener(FilterChanged.class, listener);
    }
    public void addActionClickListener(ComponentEventListener<ClickEvent<Button>> listener) {
        actionButton.addClickListener(listener);
    }

    @DomEvent(value = "value-changed", debounce = @DebounceSettings(timeout = 300, phases = DebouncePhase.TRAILING))
    public static class SearchValueChanged extends ComponentEvent<TextField> {
        public SearchValueChanged(TextField source, boolean fromClient) {
            super(source, fromClient);
        }
    }
    public static class FilterChanged extends ComponentEvent<SearchBar> {
        public FilterChanged(SearchBar source, boolean fromClient) {
            super(source, fromClient);
        }
    }

    public void setPlaceHolder(String placeHolder) {
        textField.setPlaceholder(placeHolder);
    }

}
