// eagerly import theme styles so as we can override them
import '@vaadin/vaadin-lumo-styles/all-imports';

const $_documentContainer = document.createElement('template');

$_documentContainer.innerHTML = `
<custom-style>
<style include='lumo-badge'>
    
    .subtitulo{
        display: inline-block;
        padding-top: 0px;
        margin-top: 5px;
        margin-bottom: 0;
        color: #2f395a;
        font-size: 16px;
        font-weight: bold;
        text-decoration: underline;
        width: 100%;
    }
    .line{
        border-bottom: #303069 thin solid;
    }
   
    
    button{
        cursor: pointer;
        color: aqua !important; 
    }
    
    .notificacion { 
        color: #f1792f;"
    }
</style>
</custom-style>


`;

document.head.appendChild($_documentContainer.content);
