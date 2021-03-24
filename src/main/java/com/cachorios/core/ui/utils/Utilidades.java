package com.cachorios.core.ui.utils;

import com.cachorios.core.ui.data.utils.FormattingUtils;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.ThemableLayout;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.File;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;

public class Utilidades {

	public static String generateLocation(String basePage, String entityId) {
		return basePage + (entityId == null || entityId.isEmpty() ? "" : "/" + entityId);
	}
	
	public static String GenerarNombreArchivoUnico() {
		String filename = "";
		long millis = System.currentTimeMillis();
		String datetime = new Date().toGMTString();
		datetime = datetime.replace(" ", "");
		datetime = datetime.replace(":", "");
		String rndchars = RandomStringUtils.randomAlphanumeric(16);
		filename = rndchars + "_" + datetime + "_" + millis;
		return filename;
	}
	
	public static boolean isFileExist(String nombre ){
		File temp = new File(nombre);
		boolean exists = temp.exists();
		return exists;
	}

	/**
	 * importeParaPresentacion
	 * Formatea un valor para presentacion a dos decimal
	 *
	 * @param importe : importe a formaterar
	 * @param predeterminado : el que retornartia si el monto es nulo
	 * @return String
	 */
	public static String importeParaPresentacion(Double importe, String predeterminado){
		DecimalFormat df = FormattingUtils.getUiPriceFormatter();
		if(importe == null){
			return predeterminado;
		}
		return df.format(new BigDecimal(importe));

	}
	public static Label separador( String clasName) {
		Label emptyLabel = new Label("");
		emptyLabel.addClassName(clasName);
		emptyLabel.setHeight("2em");
		return emptyLabel;
	}

	public static void limpiarBordes(Component c){
		((ThemableLayout) c).setSpacing(false);
		((ThemableLayout) c).setMargin(false);
		((ThemableLayout) c).setPadding(false);
	}

}
