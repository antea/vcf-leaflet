package org.vaadin.addons.componentfactory.leaflet.plugins.geoman;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.vaadin.addons.componentfactory.leaflet.LeafletMap;
import org.vaadin.addons.componentfactory.leaflet.layer.Layer;
import org.vaadin.addons.componentfactory.leaflet.layer.groups.FeatureGroup;
import org.vaadin.addons.componentfactory.leaflet.layer.groups.LayerGroup;
import org.vaadin.addons.componentfactory.leaflet.layer.map.functions.ExecutableFunctions;
import org.vaadin.addons.componentfactory.leaflet.layer.ui.marker.Marker;
import org.vaadin.addons.componentfactory.leaflet.layer.vectors.CircleMarker;
import org.vaadin.addons.componentfactory.leaflet.layer.vectors.Polyline;
import org.vaadin.addons.componentfactory.leaflet.layer.vectors.Rectangle;
import org.vaadin.addons.componentfactory.leaflet.plugins.geoman.events.ClientLayerAddEvent;
import org.vaadin.addons.componentfactory.leaflet.plugins.geoman.events.ClientLayerChangeEvent;
import org.vaadin.addons.componentfactory.leaflet.plugins.geoman.options.GeomanControlOptions;
import org.vaadin.addons.componentfactory.leaflet.plugins.geoman.options.GeomanGlobalOptions;

import java.util.Optional;

/**
 * <h2>Leaflet.geoman</h2> Leaflet Plugin For Creating And Editing Geometry Layers.
 * <a href="https://github.com/geoman-io/leaflet-geoman">leaflet.geoman</a>
 */
public class GeomanUtils {

    public static void addControls(LeafletMap leafletMap, GeomanControlOptions options) {
        leafletMap.executeJs(leafletMap, "pm.addControls", options);
    }

    /**
     * All new layers will be created in the passed layer
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
                        return childLayer;
                    } catch (IllegalAccessException e) {
                        return null;
                    }
                })
                .orElse(null);
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
                return true;
            }
        } catch (IllegalAccessException ignored) {
        }

        if (targetLayer instanceof LayerGroup) {
            LayerGroup layerGroup = (LayerGroup) targetLayer;
            layerGroup.removeLayer(removedLayer.getUuid());
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
            case LINE:
                return new Polyline(event.getLineLatLngs());
            case RECTANGLE:
                return new Rectangle(event.getRectangleLatLngs().get(0));
            default:
                return null;
        }
    }
}
