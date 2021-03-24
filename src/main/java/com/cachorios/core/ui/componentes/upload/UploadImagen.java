package com.cachorios.core.ui.componentes.upload;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.HasLabel;
import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.internal.AbstractFieldSupport;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.shared.Registration;
import elemental.json.Json;
import org.imgscalr.Scalr;
import org.springframework.web.util.UriUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;

@CssImport("./components/upload-imagen.css")
public class UploadImagen<T extends String> extends VerticalLayout implements HasValueAndElement<AbstractField.ComponentValueChangeEvent<UploadImagen<T>, T>, T> , HasLabel {

    private final AbstractFieldSupport<UploadImagen<T>,T> fieldSupport;

    private Image imagePreview;
    private Upload image;
    private Label imageLabel;

    private String mimeType;

    public UploadImagen() {
        this("Imagen");

    }

    public UploadImagen(String label){
        this(label,2);
    }

    public UploadImagen(String label, Integer colspan){
        this.setColspan(colspan);
        this.addClassNames("upload-image");
        this.setAlignItems(FlexComponent.Alignment.STRETCH);

        this.fieldSupport = new AbstractFieldSupport<>(this, null, Objects::deepEquals, c->{});

        imageLabel= new Label(label);
        imageLabel.setSizeUndefined();

        Button uploadButton = new Button(VaadinIcon.UPLOAD.create());
        uploadButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        mimeType="image/jpeg";
        imagePreview = new Image();
        imagePreview.addClassNames("preview");
        previewWidth("80%");

        image = new Upload();
        image.addClassNames("upload-container");
        image.setUploadButton(uploadButton);

        Span dropLabel = new Span("Arrastre  aqui!");
        image.setDropLabel(dropLabel);

        Span dropIcon = new Span("¸¸.•*¨*•♪");
        image.setDropLabelIcon(dropIcon);



        ////image.getStyle().set("box-sizing", "border-box");

        image.getElement().appendChild(imagePreview.getElement());
        Button btnRotate = new Button("Rotar", e->rotate(image, imagePreview));
        image.getElement().appendChild(btnRotate.getElement());

        this.add(imageLabel, image);
        attachImageUpload(image, imagePreview);
    }

    public void setColspan(Integer n){
        getElement().setAttribute("colspan", n.toString() );
    }

    private void attachImageUpload(Upload upload, Image preview) {
        ByteArrayOutputStream uploadBuffer = new ByteArrayOutputStream();
        upload.setAcceptedFileTypes("image/*");
        upload.setReceiver((fileName, mimeType) -> {
            return uploadBuffer;
        });

        upload.addSucceededListener(e -> {
            mimeType = e.getMIMEType();
            String base64ImageData = "";

            try {
                BufferedImage bi  = toBufferedImage(uploadBuffer.toByteArray());
                BufferedImage bimg = Scalr.resize(bi, Scalr.Method.AUTOMATIC,500 ,400);
                base64ImageData = Base64.getEncoder().encodeToString(toByteArray(bimg,"JPEG"));
            }catch(IOException iox){
                iox.printStackTrace();
            }

            String dataUrl = "data:" + mimeType + ";base64,"
                    + UriUtils.encodeQuery(base64ImageData, StandardCharsets.UTF_8);
            upload.getElement().setPropertyJson("files", Json.createArray());

            this.setValue((T)dataUrl);
            uploadBuffer.reset();
            //fireEvent(this, );
            /////////hayCambios();

        });

        preview.setVisible(false);
    }

    public void rotate(Upload upload, Image preview){
        String fotoSrc = preview.getSrc();
        int l =  "data:image/jpeg;base64,".length();
        String foto = fotoSrc.substring(l);
        byte[] img = Base64.getDecoder().decode(foto);
        try {
            BufferedImage bi = Scalr.rotate(toBufferedImage(img), Scalr.Rotation.CW_90);
            String base64ImageData = Base64.getEncoder().encodeToString(toByteArray(bi,"JPEG"));

            String dataUrl = "data:" + mimeType + ";base64,"
                    + UriUtils.encodeQuery(base64ImageData, StandardCharsets.UTF_8);

            upload.getElement().setPropertyJson("files", Json.createArray());
            preview.setSrc(dataUrl);
            this.setValue((T)preview.getSrc());

        }catch (IOException e){
            e.printStackTrace();
        }
    }


    public void previewWidth(String w){
        imagePreview.setWidth(w);
    }


    @Override
    public void setValue(T value) {
        this.fieldSupport.setValue(value);
        imagePreview.setVisible(true);
        if(value == null){
            imagePreview.setSrc("");
        }else{
            imagePreview.setSrc( value);
        }

    }

    @Override
    public T getValue() {
        return fieldSupport.getValue();
    }

    @Override
    public Registration addValueChangeListener(ValueChangeListener<? super AbstractField.ComponentValueChangeEvent<UploadImagen<T>, T>> valueChangeListener) {
        return fieldSupport.addValueChangeListener(valueChangeListener);
    }

    public static BufferedImage toBufferedImage(byte[] bytes)
            throws IOException {

        InputStream is = new ByteArrayInputStream(bytes);
        BufferedImage bi = ImageIO.read(is);
        return bi;

    }

    public static byte[] toByteArray(BufferedImage bi, String format)  throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bi, format, baos);
        byte[] bytes = baos.toByteArray();
        return bytes;
    }

    @Override
    public void setLabel(String label) {
        imageLabel.setText(label);
    }

    @Override
    public String getLabel() {
        return imageLabel.getText() ;
    }
}
