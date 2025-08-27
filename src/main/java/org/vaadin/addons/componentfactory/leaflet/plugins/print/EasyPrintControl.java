package org.vaadin.addons.componentfactory.leaflet.plugins.print;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import lombok.Getter;
import lombok.Setter;
import org.vaadin.addons.componentfactory.leaflet.annotations.LeafletArgument;
import org.vaadin.addons.componentfactory.leaflet.controls.LeafletControl;

//@NpmPackage(value = "leaflet-easyprint", version = "2.1.9")
//@JsModule("leaflet-easyprint/dist/bundle.js")
@JsModule("./easyprint.js")
//@CssImport(value = "leaflet-easyprint/libs/leaflet.css", themeFor = "leaflet-map")
@Getter
@Setter
public class EasyPrintControl extends LeafletControl {

    @LeafletArgument
    private EasyPrintOptions easyPrintOptions;

    public EasyPrintControl(EasyPrintOptions easyPrintOptions) {
        super("EasyPrint");
        this.easyPrintOptions = easyPrintOptions;
    }

    public EasyPrintControl() {
        this(new EasyPrintOptions());
    }
}
