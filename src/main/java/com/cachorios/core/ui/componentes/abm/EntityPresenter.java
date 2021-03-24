package com.cachorios.core.ui.componentes.abm;


import com.cachorios.core.data.entidad.EntidadInterface;
import com.cachorios.core.data.entidad.EntityUtil;
import com.cachorios.core.data.entidad.IUsuario;
import com.cachorios.core.data.exceptions.UserFriendlyDataException;
import com.cachorios.core.ui.HasLogger;
import com.cachorios.core.ui.data.AbmService;
import com.cachorios.core.ui.utils.Message;
import com.vaadin.flow.data.binder.ValidationException;
import org.claspina.confirmdialog.ButtonOption;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;
import java.util.function.UnaryOperator;

public class EntityPresenter<T extends EntidadInterface, V extends EntityView<T>> implements HasLogger {

    private AbmService<T> abmService;
    private IUsuario currentUser;
    private V view;

    EntityPresenterEstado<T> estado = new EntityPresenterEstado<T>();

    public EntityPresenter(AbmService<T> abmService, IUsuario currentUser) {
        this.abmService = abmService;
        this.currentUser = currentUser;
    }

    public void setView(V view){
        this.view = view;
    }

    public V getView() {
        return view;
    }

    public void delete(AbmOperacionListener<T> onSuccess) {
        Message CONFIRM_DELETE = Message.CONFIRM_DELETE.createMessage();

        confirmIfNecessaryAndExecute(true, CONFIRM_DELETE, () -> {
            if (executeOperation(() -> abmService.delete(currentUser, estado.getEntidad()))) {
                onSuccess.execute(estado.getEntidad());
            }
        }, () -> {
        });
    }

    public void save(AbmOperacionListener<T> onSuccess) {
        if (executeOperation(() -> guardarEntidad())) {
            onSuccess.execute(estado.getEntidad());
        }
    }

    public boolean executeUpdate(UnaryOperator<T> updater) {
        return executeOperation(() -> {
            estado.actualizarEntidad(updater.apply(getEntidad()), esNuevo());
        });
    }

    private boolean executeOperation(Runnable operation) {
        try {
            operation.run();
            return true;
        } catch (UserFriendlyDataException e) {
            // Commit failed because of application-level data constraints
            consumeError(e, e.getMessage(), true);
        } catch (DataIntegrityViolationException e) {
            // Commit failed because of validation errors

            consumeError(e, AbmErrorMessage.OPERATION_PREVENTED_BY_REFERENCES, true);
        } catch (OptimisticLockingFailureException e) {
            consumeError(e, AbmErrorMessage.CONCURRENT_UPDATE, true);
        } catch (EntityNotFoundException e) {
            consumeError(e, AbmErrorMessage.ENTITY_NOT_FOUND, false);
        } catch (ConstraintViolationException e) {
            consumeError(e, AbmErrorMessage.REQUIRED_FIELDS_MISSING, false);
        }
        return false;
    }

    private void consumeError(Exception e, String message, boolean isPersistent) {
        getLogger().debug(message, e);
        view.showError(message, isPersistent);
    }

    private void guardarEntidad() {
        estado.actualizarEntidad( abmService.save(currentUser, estado.getEntidad()), esNuevo());
    }

    //??? donde se usa
    public boolean writeEntity() {
        try {
            view.write(estado.getEntidad());
            return true;
        } catch (ValidationException e) {
            view.showError(AbmErrorMessage.REQUIRED_FIELDS_MISSING, false);
            return false;
        } catch (NullPointerException e) {
            return false;
        }
    }

    public void close() {
        estado.limpiar();
        view.clear();
    }

    public void cancel(Runnable onConfirmed, Runnable onCancelled) {
        confirmIfNecessaryAndExecute(view.isDirty(), Message.UNSAVED_CHANGES.createMessage(estado.getNombreEntidad()), () -> {
            view.clear();
            onConfirmed.run();
        }, onCancelled);
    }

    private void confirmIfNecessaryAndExecute(boolean needsConfirmation, Message message, Runnable onConfirmed,
                                              Runnable onCancelled) {
        if (needsConfirmation) {
            showConfirmationRequest(message, onConfirmed, onCancelled);
        } else {
            onConfirmed.run();
        }
    }

    private void showConfirmationRequest(Message message, Runnable onOk, Runnable onCancel) {

        view.getConfirmDialog()
                .withCaption(message.getCaption())
                .withMessage(message.getMessage())
                .withOkButton(()->onOk.run(), ButtonOption.caption(message.getOkText()))
                .withCancelButton(() -> onCancel.run(), ButtonOption.caption( message.getCancelText()))
                .open();


        ////estado.actualizRegistros(okRegistration, cancelRegistration);
    }

    public boolean cargarEntidad(Long id, AbmOperacionListener<T> onSuccess ){
        return executeOperation(() -> {
            estado.actualizarEntidad(abmService.load(id), false);
            onSuccess.execute(estado.getEntidad());
        });
    }
    public boolean cargarEntidad(T entidad, AbmOperacionListener<T> onSuccess ){
        return executeOperation(() -> {
            estado.actualizarEntidad(entidad, false);
            onSuccess.execute(estado.getEntidad());
        });
    }

    public T crearNuevo() {
        estado.actualizarEntidad(abmService.createNew(currentUser), true);
        return estado.getEntidad();
    }

    public T getEntidad(){
        return estado.getEntidad();
    }

    public boolean esNuevo(){
        return estado.esNuevo();
    }

    @FunctionalInterface
    public interface AbmOperacionListener<T> {
        void execute(T entity);
    }
}


/**
 * Mantiene las variable que cambian.
 */
class EntityPresenterEstado<T extends EntidadInterface> {
    private T entidad;
    private String nombreEntidad;
    private Boolean esNuevo;
//    private Registration okRegistration;
//    private Registration cancelRegistration;

    void actualizarEntidad(T entidad, boolean esNuevo ){
        this.entidad = entidad;
        nombreEntidad = EntityUtil.getName(this.entidad.getClass());
        this.esNuevo = esNuevo;
    }

//    void actualizRegistros(Registration okRegistration, Registration cancelRegistration){
//        limpiarRegistro(this.okRegistration);
//        limpiarRegistro(this.cancelRegistration);
//        this.okRegistration = okRegistration;
//        this.cancelRegistration = cancelRegistration;
//    }

    void limpiar(){
        this.entidad = null;
        this.nombreEntidad = null;
        this.esNuevo = false;
        ////actualizRegistros(null, null);
    }

//    private void limpiarRegistro(Registration registro) {
//        if(registro != null) {
//            registro.remove();
//        }
//    }

    public T getEntidad(){
        return entidad;
    }

    public String getNombreEntidad(){
        return nombreEntidad;
    }

    public boolean esNuevo(){
        return esNuevo;
    }

}