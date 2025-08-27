package org.vaadin.addons.componentfactory.leaflet.plugins.print;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.internal.JsonSerializer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NpmPackage(value = "leaflet", version = "1.9.4")
@NpmPackage(value = "leaflet-easyprint", version = "2.1.9")
@JsModule("leaflet/dist/leaflet-src.js")
@JsModule("./leaflet-map.js")
@CssImport(value = "leaflet/dist/leaflet.css", themeFor = "leaflet-map")
@CssImport(value = "./styles/leaflet-lumo-theme.css", themeFor = "leaflet-map")
// Our easyprint connector
@JsModule("./easyprint.js")
public class EasyPrintMap extends Div {

    private final Div mapContainer;
    private final EasyPrintOptions easyPrintOptions;

    public EasyPrintMap() {
        this(new EasyPrintOptions(), 1024d, 768d);
    }

    public EasyPrintMap(EasyPrintOptions easyPrintOptions, double height, double width) {
        this.easyPrintOptions = easyPrintOptions;
        mapContainer = createEasyPrintContainer(height, width);
    }

    private Div createEasyPrintContainer(double height, double width) {
        final Div divMapContainer;
        // This is the main point. This Div container will live in the light DOM.
        divMapContainer = new Div();
        divMapContainer.setId("map-container-" + System.currentTimeMillis());
        divMapContainer.setHeight((float) height, Unit.PIXELS);
        divMapContainer.setWidth((float) width, Unit.PIXELS);
        setHeight((float) height, Unit.PIXELS);
        setWidth((float) width, Unit.PIXELS);

        add(divMapContainer);
        return divMapContainer;
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        // Let's initialize the map after the div component is attached to the dom.
        getElement().executeJs("window.initEasyPrintMap($0, $1)",
                mapContainer.getElement(), JsonSerializer.toJson(easyPrintOptions));
    }

    /**
     * This method will start a print on the server-side.
     */
    public void print(String filename, String size) {
        mapContainer.getElement().callJsFunction("print", JsonSerializer.toJson(filename), JsonSerializer.toJson(size));
    }

    /**
     * This method will remove the print control from the map.
     * It's not needed if
     */
    public void removeEasyPrintControl() {
        mapContainer.getElement().callJsFunction("removeEasyPrintControl");
    }

    /**
     * This method will add the print control from the map.
     * It's not needed using EasyPrint
     */
    public void addEasyPrintControl() {
        mapContainer.getElement().callJsFunction("addEasyPrintControl");
    }
}
