package com.cachorios.core.ui.componentes.selectCompuesto;

import com.cachorios.core.data.entidad.AbstractEntity;
import com.cachorios.core.ui.componentes.searchbar.SearchBar;
import com.cachorios.core.ui.data.FilterableAbmService;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.data.provider.QuerySortOrderBuilder;
import com.vaadin.flow.data.provider.SortDirection;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@CssImport("./components/sc-find.css")
@CssImport("./styles/my-dialog.css")
public class SCFind<T extends AbstractEntity> extends VerticalLayout {
    private H4 titulo = new H4();

    private SearchBar searchBar = new SearchBar();
    private Grid<T> grid = new Grid<>();
    private FilterableAbmService<T> service;
    private List<QuerySortOrder> defaultSortOrders;


    public SCFind(FilterableAbmService<T> service, Function< T, T> callRet  ) {
        this.addClassName("sc-find");
        this.service = service;
        setSortOrders();

        searchBar.setPlaceHolder("Búsqueda");

        searchBar.getActionButton().setVisible(false);
        searchBar.addFilterChangeListener( e->setFilter(searchBar.getFilter()));

        grid.addClassName("grid");
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.addItemDoubleClickListener( e->  callRet.apply(e.getItem()));

        add(titulo,searchBar,grid);
    }


    public Grid<T> getGrid(){
        return grid;
    }

    public void setTitulo(String titulo){
        this.titulo.setText(titulo);
    }


    private void setFilter(String filtro){
        grid.setItems(
                q -> {
                    Sort springSort = toSpringDataSort( q.getSortOrders());
                    return service.findAnyMatching(Optional.ofNullable(filtro), PageRequest.of(q.getPage(), q.getPageSize(),springSort) ).stream();
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
