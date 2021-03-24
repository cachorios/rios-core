package com.cachorios.core.data.entidad;

import java.io.Serializable;

public interface EntidadInterface extends Serializable {

    Long getId();

    int getVersion();
    
    default boolean isNew(){
        return getId() ==  null || !(getId()>0);
    };
    
    
}
