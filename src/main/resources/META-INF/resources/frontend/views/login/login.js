import '@vaadin/vaadin-lumo-styles/all-imports';
const $_documentContainer = document.createElement('template');


$_documentContainer.innerHTML = `
    <dom-module id="login-with-background-image" theme-for="vaadin-login-overlay-wrapper">
        <template>
            <style>
                /* this will add bg image to title part */
                [part="brand"] {
                background-image: url(../images/login-title-banner.png);
                background-size: contain; /*or cover, your choice*/
            }
                /* this will add bg image to surrounding grey area */
                [part="backdrop"] {
                background-image: url(../images/login-background-image.jpg);
                background-size: cover;
            }
            </style>
        </template>
    </dom-module>
 ` ;