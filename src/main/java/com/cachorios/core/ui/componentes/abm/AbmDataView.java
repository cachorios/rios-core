package com.cachorios.core.ui.componentes.abm;


import com.cachorios.core.data.entidad.EntidadInterface;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.Query;

import java.util.stream.Stream;

public class AbmDataView<T extends EntidadInterface> implements CallbackDataProvider.FetchCallback<T,String>, IAbmDataProvider {
    private T padre;
    private String textFilter = "";
    @Override
    public Stream<T> fetch(Query query) {
        return null;
    }

    @Override
    public void setPadre(EntidadInterface padre) {
        this.padre = (T) padre;
    }

    @Override
    public EntidadInterface getPadre() {
        return this.padre;
    }
}
