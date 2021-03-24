package com.cachorios.core.ui.data.converters;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

public class LongConverter implements Converter<String, Long> {
    @Override
    public Result<Long> convertToModel(String s, ValueContext valueContext) {
        try {
            return Result.ok( Long.parseLong(s)   );
        } catch (NumberFormatException e) {
            return Result.error("Valor Invalido");
        }
    }

    @Override
    public String convertToPresentation(Long aLong, ValueContext valueContext) {
        return aLong == null ? "" : aLong.toString();
    }
}


