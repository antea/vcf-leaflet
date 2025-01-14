package org.vaadin.addons.componentfactory.leaflet.plugins.geoman.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import elemental.json.JsonValue;
import lombok.Getter;
import org.vaadin.addons.componentfactory.leaflet.LeafletMap;
import org.vaadin.addons.componentfactory.leaflet.layer.events.LeafletEvent;
import org.vaadin.addons.componentfactory.leaflet.layer.events.types.LeafletEventType;
import org.vaadin.addons.componentfactory.leaflet.plugins.geoman.events.types.ShapeType;

@Getter
public abstract class BaseClientLayerEvent extends LeafletEvent {

    private final ShapeType shape;

    private final String editedLayerId;

    public BaseClientLayerEvent(LeafletMap source, boolean fromClient, LeafletEventType type,
            String targetLayerId, String layerId, ShapeType shape) {
        super(source, fromClient, targetLayerId, type);
        this.shape = shape;
        this.editedLayerId = layerId;
    }

    public BaseClientLayerEvent(LeafletMap source, boolean fromClient, LeafletEventType type,
            String targetLayerId, ShapeType shape) {
        this(source, fromClient, type, targetLayerId, targetLayerId, shape);
    }

    @Override
    public String toString() {
        return "Client Event [type=" + super.getType() + ", shape=" + getShape() + "]";
    }
}
