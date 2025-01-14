package org.vaadin.addons.componentfactory.leaflet.plugins.geoman.events;

import com.fasterxml.jackson.core.type.TypeReference;
import com.vaadin.flow.component.DomEvent;
import com.vaadin.flow.component.EventData;
import elemental.json.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.vaadin.addons.componentfactory.leaflet.LeafletMap;
import org.vaadin.addons.componentfactory.leaflet.layer.events.types.LeafletEventType;
import org.vaadin.addons.componentfactory.leaflet.plugins.geoman.events.types.EditEventType;
import org.vaadin.addons.componentfactory.leaflet.plugins.geoman.events.types.ShapeType;
import org.vaadin.addons.componentfactory.leaflet.types.LatLng;

import java.util.List;

import static org.vaadin.addons.componentfactory.leaflet.plugins.geoman.GeomanUtils.readValue;

/**
 * This class represents the event that is raised when a Layer is modified (or dragged) by the client side using Geoman toolbar.
 * Fired when a layer is edited
 * Attention! this can be raised only by the layer that was edited.
 */
@Getter
@DomEvent("pm:update")
public class ClientLayerUpdateEvent extends BaseClientLayerEvent {
    // New LatLng for a Marker
    private final LatLng latLng;

    // New LatLngs for a PolyLine
    private final List<LatLng> lineLatLngs;

    // New LatLngs for a Rectangle
    private final List<List<LatLng>> rectangleLatLngs;

    // New text for the title of a Marker
    private final String text;


    public ClientLayerUpdateEvent(LeafletMap source, boolean fromClient,
            @EventData("event.detail.layer.options.uuid") String modifiedLayerUuid,
            @EventData("event.detail.layer._leaflet_id") String modifiedLayerId,
            @EventData("event.detail.layer._latlng") JsonValue latLng,
            @EventData("event.detail.layer._latlngs") JsonValue latLngs,
            @EventData("event.detail.layer.options.text") String text,
            @EventData("event.detail.shape") String shape) {
        super(source, fromClient, EditEventType.update,
                modifiedLayerUuid == null ? modifiedLayerId : modifiedLayerUuid,
                ShapeType.ofGeomanShape(shape));
        this.latLng = readValue(latLngs, new TypeReference<>() {});
        this.lineLatLngs = readValue(latLngs, new TypeReference<>() {});
        this.rectangleLatLngs = readValue(latLngs, new TypeReference<>() {});
        this.text = text;
    }
}
