package org.vaadin.addons.componentfactory.leaflet.plugins.geoman;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import elemental.json.JsonValue;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.vaadin.addons.componentfactory.leaflet.LeafletMap;
import org.vaadin.addons.componentfactory.leaflet.layer.Layer;
import org.vaadin.addons.componentfactory.leaflet.layer.groups.FeatureGroup;
import org.vaadin.addons.componentfactory.leaflet.layer.groups.LayerGroup;
import org.vaadin.addons.componentfactory.leaflet.layer.map.functions.ExecutableFunctions;
import org.vaadin.addons.componentfactory.leaflet.layer.ui.marker.Marker;
import org.vaadin.addons.componentfactory.leaflet.layer.vectors.*;
import org.vaadin.addons.componentfactory.leaflet.layer.vectors.structure.LatLngArray;
import org.vaadin.addons.componentfactory.leaflet.layer.vectors.structure.MultiLatLngArray;
import org.vaadin.addons.componentfactory.leaflet.plugins.geoman.events.ClientLayerAddEvent;
import org.vaadin.addons.componentfactory.leaflet.plugins.geoman.events.types.ShapeType;
import org.vaadin.addons.componentfactory.leaflet.plugins.geoman.options.GeomanControlOptions;
import org.vaadin.addons.componentfactory.leaflet.plugins.geoman.options.GeomanGlobalOptions;
import org.vaadin.addons.componentfactory.leaflet.types.LatLng;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * <h2>Leaflet.geoman</h2> Leaflet Plugin For Creating And Editing Geometry Layers.
 * <a href="https://github.com/geoman-io/leaflet-geoman">leaflet.geoman</a>
 */
@Slf4j
public class GeomanUtils {

    /**
     * Adds the Toolbar to the map. The options are optional.
     * @param leafletMap The Map from where we remove Geoman editing toolbar
     * @param options Options where we can set which button should be visible.
     */
    public static void addControls(LeafletMap leafletMap, GeomanControlOptions options) {
        leafletMap.executeJs(leafletMap, "pm.addControls", options);
    }

    /**
     * Removes the Toolbar from the map.
     * @param leafletMap The Map from where we remove Geoman editing toolbar
     */
    public static void removeControls(LeafletMap leafletMap) {
        leafletMap.executeJs(leafletMap, "pm.removeControls");
    }

    /**
     * This will start the draw mode for the passed shape (so from the serve you can programmatically press one of the
     * buttons to start creating a Layer)
     * @param leafletMap the map where we want to start the draw mode
     * @param shapeType the type of Layer that we want to draw
     */
    public static void enableDraw(LeafletMap leafletMap, ShapeType shapeType) {
        leafletMap.executeJs(leafletMap, "pm.enableDraw", shapeType.getShape());
    }

    /**
     * Change the visibility of one of the buttons that enable drawing
     * @param leafletMap LeafletMap with the controls
     * @param shapeType Type of the shape for the draw button
     * @param visible Boolean that decides the visibility
     */
    public static void setDrawHandlerVisible(LeafletMap leafletMap, ShapeType shapeType, boolean visible) {
        // TODO_VAADIN VL-25
    }

    /**
     * All new layers will be created in the layer
     * @param leafletMap The map that we are modifying
     * @param featureGroup The FeatureGroup layer where we will add new layers.
     */
    public static void setEditableFeatureGroup(LeafletMap leafletMap, FeatureGroup featureGroup) {
        // this will create all new layers in the FeatureGroup layer
        leafletMap.executeJs(leafletMap, "pm.setGlobalOptions", new GeomanGlobalOptions(featureGroup));
    }

    /**
     * Since the Layer was created in the client, here we need to update the state of the Layer hierarchy inside the
     * server
     * We need to decide if we need to raise also the addLayer event or not...
     * <p>
     * <b>
     * Please note that at the moment we support the creation only of Marker, Text, CircleMarker, PolyLine and
     * Rectangle.</b>
     * @param event The event that was raised by the client
     * @param layer The layer in which the new layer was created.
     * @return the new Layer server side, and sync the server map to the client Leaflet map
     */
    public static Layer syncCreatedLayer(ClientLayerAddEvent event, Layer layer) {
        if (!(layer instanceof LayerGroup)) {
            return null;
        }
        LayerGroup parentLayer = (LayerGroup) layer;

        return Optional.ofNullable(event)
                .map(GeomanUtils::createLayer)
                .map(childLayer -> {
                    try {
                        forceUUidAndParent(childLayer, event.getNewLayerId(), parentLayer);
                        // we need to sync uuid on the client
                        event.getSource().executeGeneralJs(childLayer, "setUuid", event.getNewLayerId());
                        parentLayer.getLayers().add(childLayer);
                        log.info("Layer {} created also on the server", childLayer.getUuid());
                        return childLayer;
                    } catch (IllegalAccessException e) {
                        return null;
                    }
                })
                .orElse(null);
    }

    public static void syncDraggedLayer(Layer layer, String shape, JsonValue latLngValue, JsonValue latLngs) {
        LatLng latLng = readValue(latLngValue, new TypeReference<>() {
        });
        final List<LatLng> lineLatLngs = readValue(latLngs, new TypeReference<>() {
        });
        final List<List<LatLng>> rectangleLatLngs = readValue(latLngs, new TypeReference<>() {
        });
        try {
            switch (ShapeType.ofGeomanShape(shape)) {
                case MARKER:
                case TEXT:
                    Marker marker = (Marker) layer;
                    marker.setLatLng(latLng);
                    break;
                case CIRCLE_MARKER:
                case CIRCLE:
                    CircleMarker circleMarker = (CircleMarker) layer;
                    FieldUtils.writeField(circleMarker, "latlng", latLng, true);
                    break;
                case POLYLINE:
                    Polyline polyline = (Polyline) layer;
                    FieldUtils.writeField(polyline, "latlngs", new LatLngArray(lineLatLngs), true);
                    break;
                case RECTANGLE:
                    Rectangle rectangle = (Rectangle) layer;
                    MultiLatLngArray multiLatLngArray = new MultiLatLngArray();
                    multiLatLngArray.add(new LatLngArray(rectangleLatLngs.get(0)));
                    FieldUtils.writeField(rectangle, "latlngs", multiLatLngArray, true);
                    break;
                case POLYGON:
                    Polygon polygon = (Polygon) layer;
                    MultiLatLngArray multiLatLngArrayP = new MultiLatLngArray();
                    multiLatLngArrayP.add(new LatLngArray(rectangleLatLngs.get(0)));
                    FieldUtils.writeField(polygon, "latlngs", multiLatLngArrayP, true);
                    break;
            }
            log.info("Layer {} updated on the server", layer.getUuid());
        } catch (IllegalAccessException e) {
            log.error("Server was unable to log client modification for a layer", e);
        }
    }

    public static void syncEditedLayer(Layer layer, String shape, JsonValue latLng, JsonValue latLngs, Double radius, String modifiedText) {
        if (ShapeType.TEXT.equals(ShapeType.ofGeomanShape(shape))) {
            Marker marker = (Marker) layer;
            marker.setTitle(modifiedText);
        }
        if (ShapeType.CIRCLE.equals(ShapeType.ofGeomanShape(shape)) ||
                ShapeType.CIRCLE_MARKER.equals(ShapeType.ofGeomanShape(shape))) {
            CircleMarker circle = (CircleMarker) layer;
            try {
                FieldUtils.writeField(circle, "radius", radius, true);
            } catch (IllegalAccessException e) {
                log.error("Server was unable to log client modification for a layer of shape " + shape, e);
            }
        }
        syncDraggedLayer(layer, shape, latLng, latLngs);
    }

    /**
     * Since the layer was created by the client, it was already assigned an id and a parent
     */
    private static void forceUUidAndParent(Layer layer, String uid, LayerGroup parent) throws IllegalAccessException {
        FieldUtils.writeField(layer, "uuid", uid, true);
        FieldUtils.writeField(layer, "parent", parent, true);
    }

    public static boolean syncRemovedLayer(Layer targetLayer, Layer removedLayer) {
        try {
            ExecutableFunctions parent = (ExecutableFunctions) FieldUtils.readField(removedLayer, "parent", true);
            if (parent instanceof LayerGroup) {
                ((LayerGroup) parent).removeLayer(removedLayer);
                log.info("Layer {} removed also on the server", removedLayer.getUuid());
                return true;
            }
        } catch (IllegalAccessException ignored) {
        }

        if (targetLayer instanceof LayerGroup) {
            LayerGroup layerGroup = (LayerGroup) targetLayer;
            layerGroup.removeLayer(removedLayer.getUuid());
            log.info("Layer {} removed also on the server", removedLayer.getUuid());
            return true;
        }
        return false;
    }

    private static Layer createLayer(ClientLayerAddEvent event) {
        switch (event.getShape()) {
            case MARKER:
                return new Marker(event.getLatLng());
            case TEXT:
                Marker markerText = new Marker(event.getLatLng());
                markerText.setIcon(null);
                return markerText;
            case CIRCLE_MARKER:
                return new CircleMarker(event.getLatLng());
            case POLYLINE:
                return new Polyline(event.getLineLatLngs());
            case RECTANGLE:
                return new Rectangle(event.getRectangleLatLngs().get(0));
            case POLYGON:
                return new Polygon(event.getRectangleLatLngs().get(0));
            case CIRCLE:
                return new Circle(event.getLatLng(), event.getRadius());
            default:
                return null;
        }
    }

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static <T> T readValue(JsonValue jsonValue, final TypeReference<T> valueType) {
        if (jsonValue == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(jsonValue.toJson(), valueType);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
