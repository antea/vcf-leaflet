package org.vaadin.addons.componentfactory.leaflet.plugins.geoman.options;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.vaadin.addons.componentfactory.leaflet.layer.groups.FeatureGroup;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class GeomanGlobalOptions implements Serializable {
    // Add the created layers to a layerGroup instead to the map.
    private final FeatureGroup layerGroup; // in the client the default for this is the map, here we pass the uuid

    public GeomanGlobalOptions(FeatureGroup layerGroup) {
        this.layerGroup = layerGroup;
    }
}
