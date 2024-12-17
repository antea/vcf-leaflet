package org.vaadin.addons.componentfactory.leaflet.plugins.geoman.options;

import org.vaadin.addons.componentfactory.leaflet.LeafletMap;

/**
 * <h2>Leaflet.geoman</h2> Leaflet Plugin For Creating And Editing Geometry Layers.
 * <a href="https://github.com/geoman-io/leaflet-geoman">leaflet.geoman</a>
 */
public class GeomanUtils {

    public static void addControls(LeafletMap leafletMap, GeomanControlOptions options) {
        leafletMap.executeJs(leafletMap, "pm.addControls", options);
    }
}
