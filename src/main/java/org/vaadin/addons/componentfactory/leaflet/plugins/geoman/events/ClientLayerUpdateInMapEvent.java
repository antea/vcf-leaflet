package org.vaadin.addons.componentfactory.leaflet.plugins.geoman.events;

import com.fasterxml.jackson.core.type.TypeReference;
import com.vaadin.flow.component.DomEvent;
import com.vaadin.flow.component.EventData;
import elemental.json.JsonValue;
import lombok.Getter;
import lombok.Setter;
import org.vaadin.addons.componentfactory.leaflet.LeafletMap;
import org.vaadin.addons.componentfactory.leaflet.layer.Layer;
import org.vaadin.addons.componentfactory.leaflet.plugins.geoman.events.types.EditEventType;
import org.vaadin.addons.componentfactory.leaflet.plugins.geoman.events.types.ShapeType;
import org.vaadin.addons.componentfactory.leaflet.types.LatLng;

import java.util.List;

import static org.vaadin.addons.componentfactory.leaflet.plugins.geoman.GeomanUtils.readValue;

/**
 * This class represents the event that is raised when a Layer is modified (or dragged) by the client side using Geoman toolbar.
 * Fired when a layer is edited
 * Attention! this can can be observed only from the map. So please add a listener on the map.
 * If instead you want to listen to the layer update adding a listener to that layer, use {@link ClientLayerUpdateEvent}
 */
@Getter
@DomEvent("pm:layerupdate")
public class ClientLayerUpdateInMapEvent extends BaseClientLayerEvent {
    // New LatLng for a Marker
    private final LatLng latLng;

    // New LatLngs for a PolyLine
    private final List<LatLng> lineLatLngs;

    // New LatLngs for a Rectangle
    private final List<List<LatLng>> rectangleLatLngs;

    // New text for the title of a Marker
    private final String text;

    @Setter
    private transient Layer editedLayer;

    public ClientLayerUpdateInMapEvent(LeafletMap source, boolean fromClient,
            @EventData("event.detail.layer.options.uuid") String modifiedLayerUuid,
            @EventData("event.detail.layer._leaflet_id") String modifiedLayerId,
            @EventData("event.detail.layer._latlng") JsonValue latLng,
            @EventData("event.detail.layer._latlngs") JsonValue latLngs,
            @EventData("event.detail.layer.options.text") String text,
            @EventData("event.detail.shape") String shape) {
        super(source, fromClient, EditEventType.layerupdate,
                modifiedLayerUuid == null ? modifiedLayerId : modifiedLayerUuid,
                ShapeType.ofGeomanShape(shape));
        this.latLng = readValue(latLngs, new TypeReference<>() {});
        this.lineLatLngs = readValue(latLngs, new TypeReference<>() {});
        this.rectangleLatLngs = readValue(latLngs, new TypeReference<>() {});
        this.text = text;
    }
}
