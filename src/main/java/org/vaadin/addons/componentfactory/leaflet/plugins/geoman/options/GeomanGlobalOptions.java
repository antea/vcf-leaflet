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
    /**
     * Edit-Mode for the layer can disabled (`pm.enable()`).
     */
    private final boolean allowEditing;
    /**
     * Removing can be disabled for the layer.
     */
    private final boolean allowRemoval;

    /**
     * Layer can be prevented from cutting.
     */
    private final boolean allowCutting;

    /**
     * Layer can be prevented from rotation.
     */
    private final boolean allowRotation;

    /**
     * Dragging can be disabled for the layer.
     */
    private final boolean draggable;

    public GeomanGlobalOptions(FeatureGroup layerGroup, boolean allowEditing, boolean allowRemoval, boolean allowCutting,
            boolean allowRotation, boolean draggable) {
        this.layerGroup = layerGroup;
        this.allowEditing = allowEditing;
        this.allowRemoval = allowRemoval;
        this.allowCutting = allowCutting;
        this.allowRotation = allowRotation;
        this.draggable = draggable;
    }
}
