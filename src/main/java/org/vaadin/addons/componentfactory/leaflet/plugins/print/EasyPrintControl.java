package org.vaadin.addons.componentfactory.leaflet.plugins.print;

import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import lombok.Getter;
import lombok.Setter;
import org.vaadin.addons.componentfactory.leaflet.controls.LeafletControl;
import org.vaadin.addons.componentfactory.leaflet.layer.raster.TileLayer;

@NpmPackage(value = "leaflet-easyprint", version = "2.1.9")
@JsModule("leaflet-easyprint/dist/bundle.js")
@Getter
@Setter
public class EasyPrintControl extends LeafletControl {

    public static final String SIZE_CURRENT = "Current";
    // there is a bug in easyprint https://github.com/rowanwins/leaflet-easyPrint/issues/105
    //public static final String SIZE_A4_LANDSCAPE = "A4Landscape";
    public static final String SIZE_A4_PORTRAIT = "A4Portrait";

    private TileLayer tileLayer;
    private String title = "Print";
    private String customWindowTitle = "Print";
    private String[] sizeModes = {SIZE_CURRENT, SIZE_A4_PORTRAIT};
    private Boolean exportOnly = false;
    private String filename = "map";
    private Boolean hidden = false;
    private Boolean hideControlContainer = false;
    private String[] hideClasses = {};
    private String spinnerBgColor = "#0DC5C1";
    private String customSpinnerClass = "epLoader";


    public EasyPrintControl() {
        super("easyPrint");
    }
}
