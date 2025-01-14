package org.vaadin.addons.componentfactory.leaflet.plugins.geoman.events;

import com.vaadin.flow.component.DomEvent;
import com.vaadin.flow.component.EventData;
import org.vaadin.addons.componentfactory.leaflet.LeafletMap;
import org.vaadin.addons.componentfactory.leaflet.plugins.geoman.events.types.EditEventType;
import org.vaadin.addons.componentfactory.leaflet.plugins.geoman.events.types.ShapeType;

@DomEvent("pm:dragend")
public class ClientLayerDragEndEvent extends BaseClientLayerEvent {

    public ClientLayerDragEndEvent(LeafletMap source, boolean fromClient,
            @EventData("event.detail.layer.options.uuid") String modifiedLayerUuid,
            @EventData("event.detail.layer._leaflet_id") String modifiedLayerId,
            @EventData("event.detail.shape") String shape) {
        super(source, fromClient, EditEventType.dragend,
                modifiedLayerUuid == null ? modifiedLayerId : modifiedLayerUuid, ShapeType.ofGeomanShape(shape));
    }
}
