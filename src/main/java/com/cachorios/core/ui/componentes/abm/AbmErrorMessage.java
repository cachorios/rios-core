package com.cachorios.core.ui.componentes.abm;

public final class AbmErrorMessage {
	public static final String ENTITY_NOT_FOUND = "La entidad seleccionada no fue encontrada..";

	public static final String CONCURRENT_UPDATE = "Alguien más podría haber actualizado los datos. Por favor, actualice y pruebe de nuevo.";

	public static final String OPERATION_PREVENTED_BY_REFERENCES = "La operación no se puede ejecutar ya que hay referencias a la entidad en la base de datos.";

	public static final String REQUIRED_FIELDS_MISSING = "Por favor, complete todos los campos obligatorios antes de continuar..";

	private AbmErrorMessage() {
	}
}