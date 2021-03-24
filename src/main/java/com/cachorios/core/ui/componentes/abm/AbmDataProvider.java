package com.cachorios.core.ui.componentes.abm;



import com.cachorios.core.data.entidad.EntidadInterface;
import com.cachorios.core.ui.data.FilterableAbmService;
import com.vaadin.flow.data.provider.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.vaadin.artur.spring.dataprovider.FilterablePageableDataProvider;


import java.util.List;

public class AbmDataProvider<T extends EntidadInterface> extends FilterablePageableDataProvider<T, String> implements IAbmDataProvider {
    private final FilterableAbmService<T> crudService;
    private List<QuerySortOrder> defaultSortOrders;


    public AbmDataProvider(FilterableAbmService<T> crudService) {
        this.crudService = crudService;
        setSortOrders();
    }

    private void setSortOrders() {
        QuerySortOrderBuilder builder = new QuerySortOrderBuilder();
        builder.thenAsc("id");
        defaultSortOrders = builder.build();
    }

    @Override
    protected Page<T> fetchFromBackEnd(Query<T, String> query, Pageable pageable) {
        if (crudService.getPadre() == null){
            return crudService.findAnyMatching(query.getFilter(), pageable);
        }
        List l = crudService.getList();
        return new PageImpl<T>(l, pageable, l.size());
    }

    @Override
    protected List<QuerySortOrder> getDefaultSortOrders() {
        return defaultSortOrders;
    }

    @Override
    protected int sizeInBackEnd(Query<T, String> query) {
//        if (crudService.getPadre() == null) {
//            return (int) crudService.countAnyMatching(query.getFilter());
//        }
//        return  crudService.getList().size();
        return 0;
    }

    @Override
    public void refreshItem(T item) {
        super.refreshItem(item,true);
    }
}
