package org.vaadin.addons.componentfactory.leaflet.plugins.mouseposition;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import lombok.Getter;
import lombok.Setter;
import org.vaadin.addons.componentfactory.leaflet.controls.LeafletControl;

/**
 * <h3>mouse position control</h3>
 * Leaflet.MousePosition is a simple mouse position control that you can drop into your leaflet map.
 * It displays geographic coordinates of the mouse pointer, as it is moved about the map.
 * Web component: <a href="https://github.com/ardhi/Leaflet.MousePosition">Mouseposition</a><br>
 * <br>
 *
 * <p> Since this plugin is pretty old, it is not a npm module, so we use a
 * <a href="https://www.npmjs.com/package/leaflet-mouse-position?activeTab=code">wrapper</a>
 * </p>
 * @author <a href="https://github.com/ardhi">ardhi</a>
 */
@NpmPackage(value = "leaflet-mouse-position", version = "1.2.0")
@JsModule("leaflet-mouse-position/src/L.Control.MousePosition.js")
@CssImport(value = "leaflet-mouse-position/src/L.Control.MousePosition.css", themeFor = "leaflet-map")
@Getter
@Setter
public class MousePosition extends LeafletControl {
    private static final String JAVASCRIPT_CLASS_NAME = "mousePosition";

    // To separate longitude\latitude values. Defaults to ' : '
    private String separator = ":";

    // Initial text to display. Defaults to 'Unavailable'
    private String emptystring = "Unavailable";

    //Number of digits. Defaults to 5
    private int numDigits = 5;

    // Weather to put the longitude first or not. Defaults to false
    private Boolean lngFirst = false;

    //A string to be prepended to the coordinates. Defaults to the empty string ‘’.
    private String prefix = "";

    public MousePosition() {
        super(JAVASCRIPT_CLASS_NAME);
    }
}
