package org.vaadin.addons.componentfactory.leaflet.plugins.print;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.vaadin.addons.componentfactory.leaflet.layer.map.options.DefaultMapOptions;
import org.vaadin.addons.componentfactory.leaflet.types.LatLng;

@NoArgsConstructor
@Setter
@Getter
public class EasyPrintOptions extends DefaultMapOptions {

    public static final String SIZE_CURRENT = "Current";
    // there is a bug in easyprint https://github.com/rowanwins/leaflet-easyPrint/issues/105
    public static final String SIZE_A4_LANDSCAPE = "A4Landscape";
    //Portrait for an easyprint bug is called Portait.
    public static final String SIZE_A4_PORTRAIT = "A4Portrait";

    //private TileLayer tileLayer;
    private String baseUrl = "https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png";
    private LatLng center = new LatLng(47.070121823, 19.2041015625);
    private Integer zoom = 7;
    private String position = "topleft";
    private String title = "Print";
    private String customWindowTitle = "Print";
    private String[] sizeModes = {SIZE_CURRENT, SIZE_A4_LANDSCAPE, SIZE_A4_PORTRAIT};
    private Boolean exportOnly = false;
    private String filename = "map";
    private Boolean hidden = false;
    private Boolean hideControlContainer = false;
    private String[] hideClasses = {};
    //private String spinnerBgColor = "#ff8c00";
    //private String customSpinnerClass = "epLoader";
}
