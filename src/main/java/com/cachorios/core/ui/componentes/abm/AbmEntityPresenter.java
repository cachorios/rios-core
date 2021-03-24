package com.cachorios.core.ui.componentes.abm;


import com.cachorios.core.data.entidad.EntidadInterface;
import com.cachorios.core.data.entidad.IUsuario;
import com.cachorios.core.ui.componentes.abm.eventos.LoadFormEvent;
import com.cachorios.core.ui.componentes.abm.eventos.PostUpdateEvent;
import com.cachorios.core.ui.componentes.abm.eventos.PreUpdateEvent;
import com.cachorios.core.ui.data.FilterableAbmService;
import com.vaadin.flow.component.grid.dataview.GridLazyDataView;
import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.data.provider.QuerySortOrderBuilder;
import com.vaadin.flow.data.provider.SortDirection;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class AbmEntityPresenter<T extends EntidadInterface>  extends EntityPresenter<T,Abm<T>>  {

    GridLazyDataView<T> gridDataView;
    private FilterableAbmService<T> crudService;
    private String filter = "";

    private List<QuerySortOrder> defaultSortOrders;

    public AbmEntityPresenter(FilterableAbmService<T> crudService, IUsuario currentUsuario) {
        super(crudService, currentUsuario);
        this.crudService = crudService;
        setSortOrders();
    }




    @Override
    public void setView(Abm<T> view) {
        super.setView(view);
    }

    public void filter(String filter) {
        this.filter = filter;
        gridDataView = getView().getGrid().setItems(
                q -> {
                    Sort springSort = toSpringDataSort( q.getSortOrders());
                    return crudService.findAnyMatching(Optional.ofNullable(filter), PageRequest.of(q.getPage(), q.getPageSize(),springSort) ).stream();
                }
        );

    }

    private Sort toSpringDataSort(List<QuerySortOrder> sortOrders) {
        if(sortOrders.size() == 0){
            sortOrders.addAll(defaultSortOrders);
        }
        return Sort.by(
                sortOrders.stream()
                        .map(sortOrder ->
                                        sortOrder.getDirection() == SortDirection.ASCENDING ?
                                                Sort.Order.asc(sortOrder.getSorted()) :
                                Sort.Order.desc(sortOrder.getSorted())
                        )
                        .collect(Collectors.toList())
        );
    }

    public void cancel() {
        cancel(this::closeSilently, getView()::openDialog);
    }

    public void closeSilently() {
        getView().closeDialog();

        getView().getConfirmDialog().close();

    }

    @Override
    public T crearNuevo() {
        return  open(super.crearNuevo());
    }

    public void cargarEntidad(Long id){
        cargarEntidad(id, this::open);
    }
    public void cargarEntidad(T entidad){
        cargarEntidad(entidad, this::open);
    }

    private T open(T entidad){
        getView().getBinder().readBean(entidad);
        getView().getDialog().setSaveDisabled(true);
        getView().getDialog().setDeleteDisabled(esNuevo());
        getView().actualizarTitulo(esNuevo());

        getView().establecerInicioForm(entidad);
        getView().fireEvent(new LoadFormEvent(getView(),false, entidad));

        getView().openDialog();

        return entidad;
    }

    public void save() {
        if (writeEntity()) {
            getView().fireEvent(new PreUpdateEvent(getView(), false, getEntidad()));
            super.save(e -> {
                if (esNuevo()) {
                    getView().showCreatedNotification();
                    gridDataView.refreshAll();
                } else {
                    gridDataView.refreshItem(e);
                    getView().showUpdatedNotification();
                }
                getView().fireEvent(new PostUpdateEvent(getView(), false, e));
                closeSilently();
            });
        }
    }

    public void delete() {
        super.delete(e -> {
            getView().showDeletedNotification();
            gridDataView.refreshAll();
            closeSilently();
        });
    }

    public void onValueChange(boolean isDirty) {
        getView().getDialog().setSaveDisabled(!isDirty);
    }

    private void setSortOrders() {
        QuerySortOrderBuilder builder = new QuerySortOrderBuilder();
        builder.thenAsc("id");
        this.defaultSortOrders = builder.build();
    }

    public void setDefaultSortOrders(List<QuerySortOrder> defaultSortOrders){
        if (defaultSortOrders.size()>0) {
            this.defaultSortOrders = defaultSortOrders;
        }
    }

    protected List<QuerySortOrder> getDefaultSortOrders() {
        return defaultSortOrders;
    }

}
