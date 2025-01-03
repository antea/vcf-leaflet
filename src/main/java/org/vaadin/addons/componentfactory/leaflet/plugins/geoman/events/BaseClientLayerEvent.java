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
    protected static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final ShapeType shape;

    private final String editedLayerId;

    public BaseClientLayerEvent(LeafletMap source, boolean fromClient, LeafletEventType type,
            String targetLayerId, String layerId, ShapeType shape) {
        super(source, fromClient, targetLayerId, type);
        this.shape = shape;
        this.editedLayerId = layerId;
    }

    protected <T> T readValue(JsonValue jsonValue, final TypeReference<T> valueType) {
        if (jsonValue == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(jsonValue.toJson(), valueType);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return "Create [type=" + super.getType() + ", shape=" + getShape() + "]";
    }
}
