package org.vaadin.addons.componentfactory.leaflet.plugins.print;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.vaadin.addons.componentfactory.leaflet.layer.map.options.DefaultMapOptions;

@NoArgsConstructor
@Setter
@Getter
public class EasyPrintOptions extends DefaultMapOptions {
    // Size current is currently not working, we remove it from the default sizeModes.
    public static final String SIZE_CURRENT = "Current";
    // there is a bug in easyprint https://github.com/rowanwins/leaflet-easyPrint/issues/105
    public static final String SIZE_A4_LANDSCAPE = "A4Landscape";
    //Portrait for an easyprint bug is called Portait.
    public static final String SIZE_A4_PORTRAIT = "A4Portrait";

    private String position = "topleft";
    private String title = "Print";
    private String customWindowTitle = "Print";
    private String[] sizeModes = {SIZE_A4_LANDSCAPE, SIZE_A4_PORTRAIT};
    private Boolean exportOnly = false;
    private String filename = "map";
    private Boolean hidden = false;
    private Boolean hideControlContainer = true;
    private String[] hideClasses = {};
}
