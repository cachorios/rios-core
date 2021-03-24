package com.cachorios.core.ui.componentes.abm;

import com.cachorios.core.data.entidad.EntidadInterface;
import com.cachorios.core.data.entidad.IUsuario;
import com.cachorios.core.ui.HasLogger;
import com.cachorios.core.ui.componentes.abm.eventos.*;
import com.cachorios.core.ui.componentes.dialogo.DialogABM;
import com.cachorios.core.ui.componentes.searchbar.SearchBar;
import com.cachorios.core.ui.data.FilterableAbmService;
import com.cachorios.core.ui.utils.Utilidades;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HasText;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.provider.QuerySortOrderBuilder;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.shared.Registration;
import org.claspina.confirmdialog.ConfirmDialog;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;



@CssImport("./components/lar-abm.css")
public abstract class Abm<T extends EntidadInterface> extends Div implements HasLogger, EntityView<T>, HasDynamicTitle, HasUrlParameter<Long> {

    public interface IAbmForm<T> {
        //FormButtonsBar getButtons();

        HasText getTitulo();
        FormLayout getFormLayuot();
        void setBinder(BeanValidationBinder<T> binder);

    }
    private Div titulo = new Div();
    private SearchBar searchBar = new SearchBar();
    private Grid<T> grid = new Grid<>();

    private AbmEntityPresenter<T> presenter;
    private IUsuario currentUser;
    private EntidadInterface padre;

    private final IAbmForm<T> form;
    private final DialogABM dialog = new DialogABM();
    private ConfirmDialog  confirmation;

    private String nombreEntidad;


    @Autowired
    private FilterableAbmService<T> service;

    private BeanValidationBinder<T> binder;


    protected Abm(){
        this.addClassName("lar-abm");
        this.nombreEntidad = getNombreEntidad();

        this.form = new AbmForm<>();

        titulo.addClassName("titulo");
        grid.setClassName("grid");

        add( titulo,searchBar, grid);        
    }
    
    public Abm(String nombreEntidad, FilterableAbmService<T> service) {
        this();
        this.service = service;    
        this.nombreEntidad = nombreEntidad;
        

    }

    @PostConstruct
    protected void inicio(){
        binder = new BeanValidationBinder<>(service.getBeanType());
        configureGrid(grid);
        iniciarComponentes();
        
        iniciar();
        configurarListener();
        if(!searchBar.getFieldSearch().isVisible()) {
            getPresenter().filter(searchBar.getFilter());
        }
    }

    protected abstract String getNombreEntidad();

    protected void iniciarComponentes(){
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();
        service.setPadre(null);

        searchBar.setActionText(this.nombreEntidad);
        searchBar.getActionButton().setIcon(VaadinIcon.PLUS_CIRCLE.create());

        this.titulo.setText(getPageTitle());
        searchBar.setPlaceHolder("Busqueda");


    }

    public void iniciar(){

        grid.getElement().setAttribute("slot", "grid");
        grid.setClassName("grid");

        dialog.setDraggable(true);
        dialog.setModal(false);
        dialog.setResizable(true);

        form.getFormLayuot().setContenedor(this);
        crearForm(form.getFormLayuot() , binder);

        dialog.addContenido((Component) this.form);

    }

    public Grid<T> getGrid() {
        return grid;
    }
    protected abstract void configureGrid(Grid<T> grid);

    protected  void defaultSort(QuerySortOrderBuilder builder){};

    public void actualizarTitulo(boolean nuevo){
        getDialog().setTitle((nuevo ? "Nuevo " : "Editar ") + nombreEntidad );
    }

    public FilterableAbmService<T> getService(){
        return service;
    }

    public SearchBar getSearchBar() {
        return searchBar;
    }

    public void configurarListener(){

        searchBar.addFilterChangeListener(e->getPresenter().filter(searchBar.getFilter()));
        searchBar.addActionClickListener(e->  getPresenter().crearNuevo() );


        getGrid().addSelectionListener(e -> {
            e.getFirstSelectedItem().ifPresent(entity -> {
                fireEvent(new RowFocusChangedEvent(this, false, entity ));
                if(padre==null) {
                    navigateToEntity(entity.getId().toString());
                }else{
                    getPresenter().cargarEntidad(entity);
                }
                getGrid().deselectAll();
            });
        });

        getDialog().addSaveListener(e->{
                    //fireEvent(new PreUpdateEvent());
                    getPresenter().save();
                    fireEvent(new ValueChangeEvent( this, false));
        });

        getDialog().addDeleteListener(e -> getPresenter().delete());

        getDialog().addCancelListener(e -> getPresenter().cancel());

        getDialog().getElement().addEventListener("opened-changed", e -> {
            if(!getDialog().isOpened()) {
                getPresenter().cancel();
            }
        });

        getBinder().addValueChangeListener(e -> getPresenter().onValueChange(isDirty()));


    }

    public void setCurrentUser(IUsuario currentUser){
        this.currentUser = currentUser;
    }

    public void hayCambios(){
        dialog.setSaveDisabled(false);
    }



    public AbmEntityPresenter<T>    getPresenter(){
        if(presenter == null){
            presenter = new AbmEntityPresenter<>(service, currentUser);
            QuerySortOrderBuilder builder = new QuerySortOrderBuilder();
            defaultSort(builder);
            presenter.setDefaultSortOrders(builder.build());
            presenter.setView(this);
        }

        return presenter;
    }

    protected abstract String getBasePage();

    public DialogABM getDialog(){
        return dialog;
    }

    public void openDialog() {
        getDialog().setOpened(true);
    }
    public void closeDialog() {
        getDialog().setOpened(false);
        navigateToEntity(null);
    }
    public void setWidth(String w){
        dialog.setWidth(w);
    }

    public void setHeigth(String h){
        dialog.setHeight("h");
    }

    protected void navigateToEntity(String id) {
        getUI().ifPresent(ui -> ui.navigate(Utilidades.generateLocation(getBasePage(), id)));
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter Long id) {
        if (id != null) {
            getPresenter().cargarEntidad(id);
        } else if (getDialog().isOpened()) {
            getPresenter().closeSilently();
        }
    }

    protected abstract void crearForm(FormLayout form, BeanValidationBinder<T> binder);

    public IAbmForm<T> getForm() {
        return form;
    }



    protected BeanValidationBinder<T> getBinder() {
        return binder;
    }

    @Override
    public boolean isDirty() {
        return getBinder().hasChanges();
    }

    @Override
    public void clear() {
        getBinder().readBean(null);
    }

    @Override
    public void write(T entity) throws ValidationException {
        getBinder().writeBean(entity);
    }


    protected void establecerInicioForm(T entidad) {}

    @Override
    public String getEntityName() {
        return nombreEntidad;
    }

    @Override
    public EntidadInterface getPadre() {
        return padre;
    }

    public void setPadre(EntidadInterface padre) {
        if(padre != null){
            this.padre = padre;
            service.setPadre(padre);

            //grid.setDataProvider( getDataProvider() );
            //////accion.addClickListener(e -> getPresenter().crearNuevo() );
        }
    }


    @Override
    public void setConfirmDialog(ConfirmDialog confirmDialog) {
        this.confirmation = confirmDialog;
    }

    @Override
    public ConfirmDialog getConfirmDialog() {
        if(this.confirmation != null){
            return confirmation;
        }
        return ConfirmDialog.create();
    }


    //Eventos
    public void fireEvent(ComponentEvent<?> componentEvent) {
        super.fireEvent(componentEvent);
    }

    public Registration addPreUpdateListener(ComponentEventListener<PreUpdateEvent> listener){
        return addListener(PreUpdateEvent.class, listener);
    }

    public Registration addRowFocusChanged(ComponentEventListener<RowFocusChangedEvent> listener){
        return addListener(RowFocusChangedEvent.class, listener);
    }
    public Registration addLoadFormListener(ComponentEventListener<LoadFormEvent> listener){
        return addListener(LoadFormEvent.class, listener);
    }

    public Registration addPostUpdateListener(ComponentEventListener<PostUpdateEvent> listener){
        return addListener(PostUpdateEvent.class, listener);
    }
    public Registration addPostDeleteListener(ComponentEventListener<PostDeleteEvent> listener){
        return addListener(PostDeleteEvent.class, listener);
    }
    public Registration addSaveListener(ComponentEventListener<SaveEvent> listener){
        return addListener(SaveEvent.class, listener);
    }




}
