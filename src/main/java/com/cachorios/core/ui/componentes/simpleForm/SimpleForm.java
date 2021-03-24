package com.cachorios.core.ui.componentes.simpleForm;

import com.cachorios.core.data.entidad.EntidadInterface;
import com.cachorios.core.ui.componentes.abm.FormLayout;
import com.cachorios.core.ui.componentes.formButtosBar.FormButtonsBar;
import com.cachorios.core.ui.data.FilterableAbmService;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.router.HasDynamicTitle;
import org.springframework.beans.factory.annotation.Autowired;
import javax.annotation.PostConstruct;

@CssImport("./components/simple-form.css")
public abstract class SimpleForm<T extends EntidadInterface> extends FlexLayout  implements HasDynamicTitle {


    @Autowired
    private FilterableAbmService<T> service;
    private BeanValidationBinder<T> binder;

    private FormLayout formulario = new FormLayout();

    private FormButtonsBar formButonsBar = new FormButtonsBar();

    public SimpleForm() {
        this.addClassName("simple-form");
        this.add(formulario, formButonsBar);
        getFormButonsBar()
                .setSaveDisabled(true)
                .setCancelDisabled(true)
                .setDeleteDisabled(true);
    }

    @PostConstruct
    private void iniciar(){
        binder = new BeanValidationBinder<>(service.getBeanType());
        crearForm(formulario,binder);

        binder.setBean( setData() );

        binder.addValueChangeListener( e->{
            getFormButonsBar().setSaveDisabled(false);
        } );
        /*getForm().getButtons().addSaveListener(e -> getPresenter().save());
        getForm().getButtons().addDeleteListener(e -> getPresenter().delete());
        getForm().getButtons().addCancelListener(e -> getPresenter().cancel());*/
    }

    public FilterableAbmService<T> getService() {
        return service;
    }

    protected abstract T setData();

    public T getEntity() throws Exception {
        if( binder.isValid() ) {
            return binder.getBean();
        }

        throw  new Exception("Registro invalido");
    }

    protected abstract void crearForm(FormLayout form,BeanValidationBinder<T> binder);

    public FormLayout getForm() {
        return formulario;
    }

    public FormButtonsBar getFormButonsBar() {
        return formButonsBar;
    }
}
