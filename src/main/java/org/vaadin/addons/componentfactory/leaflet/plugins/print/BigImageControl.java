package org.vaadin.addons.componentfactory.leaflet.plugins.print;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import lombok.Getter;
import lombok.Setter;
import org.vaadin.addons.componentfactory.leaflet.controls.LeafletControl;

/**
 * <h3>Exporter to png for Leaflet Maps</h3>
 *
 * Web component: <a href="https://github.com/pasichnykvasyl/Leaflet.BigImage">BigImage</a><br>
 * <br>
 * @author <a href="https://github.com/pasichnykvasyl">pasichnykvasyl</a>
 */
@NpmPackage(value = "leaflet.bigimage", version = "1.0.1")
@JsModule("leaflet.bigimage/dist/Leaflet.BigImage.min.js")
@CssImport(value = "leaflet.bigimage/dist/Leaflet.BigImage.min.css", themeFor = "leaflet-map")
@Getter
@Setter
public class BigImageControl extends LeafletControl {
    // Sets the text which appears as the tooltip of the control button
    private String title;
    //	Sets icon to the control button
    private String printControlLabel = "🖶";
    //Sets classes to the control button
    private String[] printControlClasses;
    // Max image scale
    private int maxScale = 10;
    // Min image scale
    private int minScale = 1;
    // Title before scale input
    private String inputTitle = "Choose scale:";
    // Text on the download button
    private String downloadTitle = "Download";

    public BigImageControl() {
        super("BigImage");
    }
}
