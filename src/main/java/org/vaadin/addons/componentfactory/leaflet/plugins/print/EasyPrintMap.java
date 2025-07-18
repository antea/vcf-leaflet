package org.vaadin.addons.componentfactory.leaflet.plugins.print;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.html.Div;

@NpmPackage(value = "leaflet", version = "1.9.4")
@NpmPackage(value = "leaflet-easyprint", version = "2.1.9")
@JsModule("leaflet/dist/leaflet-src.js")
@JsModule("./leaflet-map.js")
@CssImport(value = "leaflet/dist/leaflet.css", themeFor = "leaflet-map")
@CssImport(value = "./styles/leaflet-lumo-theme.css", themeFor = "leaflet-map")
@JsModule("./easyprint.js")
public class EasyPrintMap extends Div {

    private final Div mapContainer;
    private final EasyPrintOptions easyPrintOptions;
    //private final JsonObject options;

    public EasyPrintMap() {
        this.easyPrintOptions = new EasyPrintOptions();
        //this.options = options;

        mapContainer = new Div();
        mapContainer.setId("map-container-" + System.currentTimeMillis());
        mapContainer.setSizeFull();
        setSizeFull();

        add(mapContainer);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        getElement().executeJs("window.initEasyPrintMap($0)",
                mapContainer.getElement());
    }
}
