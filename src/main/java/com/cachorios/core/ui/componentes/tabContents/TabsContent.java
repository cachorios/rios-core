package com.cachorios.core.ui.componentes.tabContents;


import com.cachorios.core.ui.componentes.abm.Abm;
import com.cachorios.core.ui.componentes.abm.eventos.ValueChangeEvent;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.function.SerializableSupplier;
import java.util.HashMap;
import java.util.Map;

public class TabsContent extends Composite<VerticalLayout> implements HasSize {
    protected final com.vaadin.flow.component.tabs.Tabs tabs = new com.vaadin.flow.component.tabs.Tabs();
    protected final VerticalLayout content = new VerticalLayout();
    protected Component selected;
    protected final Map<Tab, SerializableSupplier<Component>> tabsToSuppliers = new HashMap();
    protected SerializableConsumer<Tab> tabCloseListener;

    public TabsContent() {
        this.tabs.addSelectedChangeListener((event) -> {
            Tab selectedTab = this.tabs.getSelectedTab();
            this.select(selectedTab);
        });
        ((VerticalLayout)this.getContent()).add(new Component[]{this.tabs, this.content});
        tabs.setWidth("100%");
        this.content.getElement().getStyle().set("margin", "0").set("padding", "0");
        this.getContent().getElement().getStyle().set("margin", "0").set("padding", "0").set("border-top","#546aff 1px dotted");
        
    }

    public void select(Tab tab) {
        SerializableSupplier<Component> supplier = (SerializableSupplier)this.tabsToSuppliers.get(tab);
        if (supplier != null) {
            Component component = (Component)supplier.get();
            VerticalLayout wrapper = new VerticalLayout(new Component[]{component});
            wrapper.setMargin(false);
            wrapper.setPadding(false);
            wrapper.setSizeFull();
            this.content.removeAll();
            this.content.add(new Component[]{wrapper});
            this.tabs.setSelectedTab(tab);
            this.selected = wrapper;
        } else {
            this.content.remove(new Component[]{this.selected});
        }

    }

    public Tab add(Component component, String caption) {
       
        return this.add(component, caption, false);
    }

    public Tab add(Component component, String caption, boolean closable) {
        if(component instanceof Abm){
            ComponentUtil.addListener(component, ValueChangeEvent.class, e ->  fireEvent(new ValueChangeEvent(this, false)));
        }
        return this.add(() -> {
            return component;
        }, caption, closable);
    }

    public void add(Component component, Tab tab) {
        this.add(() -> {
            return component;
        }, tab);
    }

    public Tab add(SerializableSupplier<Component> componentSupplier, String caption) {
        return this.add(componentSupplier, caption, false);
    }

    public Tab add(SerializableSupplier<Component> componentSupplier, String caption, boolean closable) {
        HorizontalLayout tabLayout = new HorizontalLayout(new Component[]{new Text(caption)});
        tabLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        tabLayout.setSpacing(false);
        Tab tab = new Tab(new Component[]{tabLayout});
        if (closable) {
            Span close = new Span(new Component[]{VaadinIcon.CLOSE_SMALL.create()});
            tabLayout.add(new Component[]{close});
            close.addClickListener((e) -> {
                this.remove(tab);
            });
        }

        this.add(componentSupplier, tab);
        return tab;
    }

    public void add(SerializableSupplier<Component> componentSupplier, Tab tab) {
        this.tabsToSuppliers.put(tab, componentSupplier);
        this.tabs.add(new Tab[]{tab});
        if (this.tabsToSuppliers.size() == 1) {
            this.select(tab);
        }

    }

    public void remove(Tab tab) {
        this.tabs.remove(new Component[]{tab});
        this.tabsToSuppliers.remove(tab);
        this.select(this.tabs.getSelectedTab());
        if (this.tabCloseListener != null) {
            this.tabCloseListener.accept(tab);
        }

    }
    
    
    public void setFlexGrowForEnclosedTabs(double flexGrow) {
        if (flexGrow < 0.0D) {
            throw new IllegalArgumentException("Flex grow property must not be negative");
        } else {
            tabs.getChildren().forEach((tab) -> {
                ((Tab)tab).setFlexGrow(flexGrow);
            });
        }
    }
    
    
    public void setTabCloseListener(SerializableConsumer<Tab> listener) {
        this.tabCloseListener = listener;
    }
}
