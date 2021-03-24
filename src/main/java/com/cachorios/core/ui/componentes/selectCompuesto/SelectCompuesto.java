package com.cachorios.core.ui.componentes.selectCompuesto;

import com.cachorios.core.data.entidad.AbstractEntity;
import com.cachorios.core.ui.data.FilterableAbmService;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.internal.AbstractFieldSupport;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.polymertemplate.EventHandler;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.shared.Registration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.GenericApplicationContext;

import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

@CssImport("./components/select-compuesto.css")
public class SelectCompuesto<T extends AbstractEntity> extends HorizontalLayout
        implements HasValueAndElement<AbstractField.ComponentValueChangeEvent<SelectCompuesto<T>, T>, T>
{

    @Autowired
    private GenericApplicationContext context;

    private TextField input = new TextField();
    private TextField leyenda = new TextField();

    private Button bt_find, btnClear;

    private final AbstractFieldSupport<SelectCompuesto<T>,T> fieldSupport;


    private FilterableAbmService<T> service;

    private Function<T, String> obtenerVarlor;
    private Function<T, String> obtenerLeyenda;
    private BiFunction<FilterableAbmService<T>,  String, Optional<T>> consultarCodigo;

    private Boolean withFind = false;
    private Dialog dialog;
    private Grid<T> grilla;

    public SelectCompuesto() {
        this.addClassName("select-compuesto");
        this.setWidthFull();
        this.setAlignItems(FlexComponent.Alignment.STRETCH);
        this.fieldSupport = new AbstractFieldSupport<>(this, null, Objects::deepEquals, c->{});

        obtenerVarlor = entidad -> String.valueOf(entidad.getId());
        obtenerLeyenda = entidad -> String.valueOf(entidad.toString());
        consultarCodigo = (service, string) -> service.findById( Long.valueOf(string));

        input.addValueChangeListener(e->changeInput(e.getValue()));

        btnClear= new Button(VaadinIcon.CLOSE_SMALL.create());
        btnClear.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btnClear.setVisible(false);

        bt_find = new Button(VaadinIcon.ANGLE_DOWN.create(), e->dialog.open());
        bt_find.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        bt_find.addThemeVariants(ButtonVariant.LUMO_SMALL);
        bt_find.setVisible(false);

        input.setSuffixComponent(new Div(bt_find));



        leyenda.setLabel("                                                                                                                                                                                                                                                                  .");
        leyenda.setReadOnly(true);
        leyenda.setAutofocus(false);


        this.add(input, leyenda);
        this.setFlexGrow(0,input);
        this.setFlexGrow(1,leyenda);
    }

    public SelectCompuesto(Class serviceClass) {
        this();
        this.service = (FilterableAbmService<T>) context.getBean(serviceClass);
    }

    public SelectCompuesto(String label, FilterableAbmService service) {
        this();

        this.service = service;
        this.setLabel(label);
    }

    public SelectCompuesto(String label,Class serviceClass){
        this(serviceClass);
        this.setLabel(label);
    }

    public SelectCompuesto<T> setLabel(String label){
        input.setLabel(label);
        return this;
    }
    public SelectCompuesto<T> setWidthInput(String w){
        this.setWidth(w);
        return this;
    }

    private SelectCompuesto<T> setLeyenda(String leyenda){
        this.leyenda.setValue(leyenda);
        return this;
    }


    /**
     * Establenet ColSpan
     * @param nCol
     * @return
     */
    public SelectCompuesto<T> setColSpan(Integer nCol){
        getElement().setAttribute("colspan", nCol.toString());
        return this;
    }



    @Override
    public void setValue(T value) {
        this.fieldSupport.setValue(value);
        refresh();
    }

    @Override
    public T getValue() {
        return fieldSupport.getValue();
    }

    @Override
    public Registration addValueChangeListener(ValueChangeListener<? super AbstractField.ComponentValueChangeEvent<SelectCompuesto<T>, T>> valueChangeListener) {
        return fieldSupport.addValueChangeListener(valueChangeListener);
    }

    public T CallBack(T value){
        this.setValue(value);
        dialog.close();
        return value;
    }

    //-------------------find------------------//
    private SelectCompuesto<T> setFindDisplay(String titulo){

        bt_find.setVisible(true);

        SCFind<T> contenido = new SCFind<>(service, v -> this.CallBack(v));
        dialog = new Dialog(contenido);
        dialog.getElement().getThemeList().add("scfind-dialog");

        contenido.setTitulo("");
        if(bt_find.isVisible()){
            grilla = contenido.getGrid();
            contenido.setTitulo(titulo);
        }
        dialog.setHeight("60vh");
        dialog.setWidth("650px");
        return this;
    }

    public SelectCompuesto<T> withFind(String titulo){
        setFindDisplay(titulo);
        return this;
    }



    ///Grilla
    public <V> SelectCompuesto<T> addColumn(final ValueProvider<T,V> columna, final String titulo){
        if(grilla != null) {
            grilla.addColumn(columna).setHeader(titulo).setAutoWidth(true).setSortable(true);
        }else{
            System.out.println("Grilla no definida!");
        }

        return this;
    }

    //--------------- Show ----------------//
    private SelectCompuesto<T> setVerDisplay(String modo){
//        getModel().setVerDisplay(modo);
        return this;
    }
    public SelectCompuesto<T> withVer(){
        setVerDisplay("block");
        return this;
    }

    @EventHandler
    private void verClick(){
        Notification.show("Ver!!!", 1000, Notification.Position.BOTTOM_END);
    }


    //--------------- Clear ----------------//
    private SelectCompuesto<T> setClearDisplay(){
        //btnClear.setVisible(true);
        input.setClearButtonVisible(true);
        return this;
    }
    public SelectCompuesto<T> withClear(){
        setClearDisplay();
        return this;
    }
    @EventHandler
    private void clearClick(){
        // Notification.show("clear!!!", 1000, Notification.Position.BOTTOM_END);
        fieldSupport.setValue(null);
        refresh();
    }

    //------------------
    public SelectCompuesto<T> withSetObtenerVarlor(Function<T, String> obtenerVarlor) {
        this.obtenerVarlor = obtenerVarlor;
        return this;
    }
    public SelectCompuesto<T> withSetObtenerLeyenda(Function<T, String> obtenerLeyenda) {
        this.obtenerLeyenda = obtenerLeyenda;
        return this;
    }
    public SelectCompuesto<T> withSetConsultarCodigo(BiFunction<FilterableAbmService<T>,  String, Optional<T>> consultarCodigo) {
        this.consultarCodigo = consultarCodigo;
        return this;
    }

    public String obtener(Function<T,String> function){
        if(function == null){
            return "";
        }

        if(this.fieldSupport.getValue() != null) {
            return (function.apply(this.fieldSupport.getValue())).toString();
        }
        return "";
    }

    public void changeInput(String valor){
        if(valor.isEmpty()){
            return;
        }
        Optional<T> aux = consultarCodigo.apply(service, valor);
        if(aux.isPresent()) {
            this.setValue(aux.get());
        }else{
            this.setValue(null);
            input.clear();
            setLeyenda("Valor invalido");
            input.focus();
        }
    }

    protected void refresh() {
        input.setValue( obtener(obtenerVarlor) );
        setLeyenda(obtener(obtenerLeyenda));
    }



    private void configureFind(){

    }


}
