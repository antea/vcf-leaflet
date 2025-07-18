package org.vaadin.addons.componentfactory.leaflet.plugins.print;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.vaadin.addons.componentfactory.leaflet.layer.raster.TileLayer;

import java.io.Serializable;

@NoArgsConstructor
@Setter
@Getter
public class EasyPrintOptions implements Serializable {

    public static final String SIZE_CURRENT = "Current";
    // there is a bug in easyprint https://github.com/rowanwins/leaflet-easyPrint/issues/105
    public static final String SIZE_A4_LANDSCAPE = "A4Landscape";
    public static final String SIZE_A4_PORTRAIT = "A4Portrait";

    private TileLayer tileLayer;
    private String title = "Print";
    private String customWindowTitle = "Print";
    private String[] sizeModes = {SIZE_CURRENT, SIZE_A4_LANDSCAPE, SIZE_A4_PORTRAIT};
    private Boolean exportOnly = false;
    private String filename = "map";
    private Boolean hidden = false;
    private Boolean hideControlContainer = false;
    private String[] hideClasses = {};
    private String spinnerBgColor = "#ff8c00";
    private String customSpinnerClass = "epLoader";
}
