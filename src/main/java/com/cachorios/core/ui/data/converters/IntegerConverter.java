package com.cachorios.core.ui.data.converters;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

public class IntegerConverter implements Converter<String, Integer> {
    @Override
    public Result<Integer> convertToModel(String s, ValueContext valueContext) {
        try {

            return Result.ok( Integer.parseInt(s)   );
        } catch (NumberFormatException e) {
            return Result.error("Valor Invalido");
        }

    }

    @Override
    public String convertToPresentation(Integer integer, ValueContext valueContext) {
        return integer == null ? "" : integer.toString();

    }
}

