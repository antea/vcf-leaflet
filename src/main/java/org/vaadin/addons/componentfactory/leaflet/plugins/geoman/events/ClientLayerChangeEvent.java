package org.vaadin.addons.componentfactory.leaflet.plugins.geoman.events;

import com.fasterxml.jackson.core.type.TypeReference;
import com.vaadin.flow.component.DomEvent;
import com.vaadin.flow.component.EventData;
import elemental.json.JsonValue;
import lombok.Getter;
import org.vaadin.addons.componentfactory.leaflet.LeafletMap;
import org.vaadin.addons.componentfactory.leaflet.plugins.geoman.events.types.EditEventType;
import org.vaadin.addons.componentfactory.leaflet.plugins.geoman.events.types.ShapeType;
import org.vaadin.addons.componentfactory.leaflet.types.LatLng;

import java.util.List;

/**
 * This class represents the event that is raised when a Layer is deleted by the client side using Geoman toolbar.
 * Fired when a layer is removed via <a href="https://geoman.io/docs/leaflet/modes/removal-mode">Removal Mode</a>.
 */
@Getter
@DomEvent("pm:change")
public class ClientLayerChangeEvent extends BaseClientLayerEvent {
    // Line
    private final List<LatLng> lineLatLngs;

    // Rectangle
    private final List<List<LatLng>> rectangleLatLngs;

    public ClientLayerChangeEvent(LeafletMap source, boolean fromClient,
            @EventData("event.detail.layer.options.uuid") String modifiedLayerUuid,
            @EventData("event.detail.layer._leaflet_id") String modifiedLayerId,
            @EventData("event.detail.layer._latlngs") JsonValue latLngs,
            @EventData("event.detail.shape") String shape) {
        super(source, fromClient, EditEventType.change,
                modifiedLayerUuid == null ? modifiedLayerId : modifiedLayerUuid,
                modifiedLayerUuid == null ? modifiedLayerId : modifiedLayerUuid,
                ShapeType.of(shape));
        this.lineLatLngs = readValue(latLngs, new TypeReference<>() {
        });
        this.rectangleLatLngs = readValue(latLngs, new TypeReference<>() {
        });
    }
}
