package org.vaadin.addons.componentfactory.leaflet.plugins.geoman.events;

import com.vaadin.flow.component.DomEvent;
import com.vaadin.flow.component.EventData;
import lombok.Getter;
import org.vaadin.addons.componentfactory.leaflet.LeafletMap;
import org.vaadin.addons.componentfactory.leaflet.plugins.geoman.events.types.EditEventType;
import org.vaadin.addons.componentfactory.leaflet.plugins.geoman.events.types.ShapeType;

/**
 * This class represents the event that is raised when a Layer is deleted by the client side using Geoman toolbar.
 * Fired when a layer is removed via <a href="https://geoman.io/docs/leaflet/modes/removal-mode">Removal Mode</a>.
 */
@Getter
@DomEvent("pm:remove")
public class ClientLayerRemoveEvent extends BaseClientLayerEvent {

    public ClientLayerRemoveEvent(LeafletMap source, boolean fromClient,
            @EventData("event.detail.target.options.uuid") String targetLayerId,
            @EventData("event.detail.layer.options.uuid") String removedLayerUuid,
            @EventData("event.detail.layer._leaflet_id") String removedLayerId,
            @EventData("event.detail.shape") String shape) {
        super(source, fromClient, EditEventType.remove, targetLayerId,
                removedLayerUuid == null ? removedLayerId : removedLayerUuid, ShapeType.of(shape));
    }

    public String getRemovedLayerId() {
        return getEditedLayerId();
    }
}
