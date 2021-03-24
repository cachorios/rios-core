package com.cachorios.core.ui.data.converters;


import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;


import com.cachorios.core.ui.data.utils.FormattingUtils;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

import static com.cachorios.core.ui.data.utils.DataProviderUtil.convertIfNotNull;


public class ImporteConverter implements Converter<String, Double> {

	private final DecimalFormat df = FormattingUtils.getUiPriceFormatter();

	@Override
	public Result<Double> convertToModel(String presentationValue, ValueContext valueContext) {
		try {
			return Result.ok( df.parse(presentationValue).doubleValue());
			//return Result.ok((Double) Math.round(df.parse(presentationValue).doubleValue() ));
		} catch (ParseException e) {
			return Result.error("Invalid value");
		}
	}

	@Override
	public String convertToPresentation(Double modelValue, ValueContext valueContext) {
		//return convertIfNotNull(modelValue, i -> df.format(BigDecimal.valueOf(i, 2)), () -> "");
		return convertIfNotNull(modelValue, i -> df.format(new BigDecimal(i)), () -> "0.00");
	}
}