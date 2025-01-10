package org.vaadin.addons.componentfactory.leaflet.demo.view.plugins;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.vaadin.addons.componentfactory.leaflet.LeafletMap;
import org.vaadin.addons.componentfactory.leaflet.controls.LayersControl;
import org.vaadin.addons.componentfactory.leaflet.demo.LeafletDemoApp;
import org.vaadin.addons.componentfactory.leaflet.demo.components.ExampleContainer;
import org.vaadin.addons.componentfactory.leaflet.layer.Layer;
import org.vaadin.addons.componentfactory.leaflet.layer.groups.FeatureGroup;
import org.vaadin.addons.componentfactory.leaflet.layer.groups.LayerGroup;
import org.vaadin.addons.componentfactory.leaflet.layer.map.options.DefaultMapOptions;
import org.vaadin.addons.componentfactory.leaflet.layer.map.options.MapOptions;
import org.vaadin.addons.componentfactory.leaflet.layer.ui.marker.Marker;
import org.vaadin.addons.componentfactory.leaflet.plugins.geoman.events.types.EditEventType;
import org.vaadin.addons.componentfactory.leaflet.plugins.geoman.options.GeomanControlOptions;
import org.vaadin.addons.componentfactory.leaflet.plugins.geoman.GeomanUtils;
import org.vaadin.addons.componentfactory.leaflet.types.Icon;

import java.util.Objects;

import static java.util.stream.IntStream.range;
import static org.vaadin.addons.componentfactory.leaflet.types.Icon.DEFAULT_ICON;
import static org.vaadin.addons.componentfactory.leaflet.types.LatLng.latlng;

@PageTitle("Editable map")
@Route(value = "plugin/geoman", layout = LeafletDemoApp.class)
public class GeomanEditMapPluginExample extends ExampleContainer {
    @Override
    protected void initDemo() {
        MapOptions options = new DefaultMapOptions();
        options.setCenter(latlng(47.070121823, 19.2041015625));
        options.setZoom(7);
        options.setPreferCanvas(true);

        LeafletMap leafletMap = new LeafletMap(options);
        leafletMap.setBaseUrl("https://maps.wikimedia.org/osm-intl/{z}/{x}/{y}.png");
        leafletMap.addLayer(createRandomMarkers(DEFAULT_ICON, 30));

        leafletMap.onCreate(event -> {
            String newLayerId = event.getNewLayerId();
            Layer layer = leafletMap.findLayer(newLayerId);
            String message = layer != null && event.getChild() != null
                    ? "Layer %s of type %s created and added to the server map"
                    : "Layer %s of type %s created BUT NOT added to the server map!";

            Notification.show(String.format(message, newLayerId, event.getShape()));
        });

        leafletMap.onRemove(event -> {
            String removedLayerId = event.getRemovedLayerId();
            Layer layer = leafletMap.findLayer(removedLayerId);
            String message = Objects.equals(layer.getUuid(), removedLayerId)
                    ? "Layer %s of type %s  was NOT deleted on the server map!"
                    : "Layer %s of type %s  was deleted also on the server map";

            Notification.show(String.format(message, removedLayerId, event.getShape()));
        });

        LayersControl leafletControl = new LayersControl();
        leafletControl.addTo(leafletMap);

        FeatureGroup editFeatureGroup = new FeatureGroup();
        editFeatureGroup.setAttribution("Editable group");
        editFeatureGroup.addTo(leafletMap);

        leafletControl.addOverlay(editFeatureGroup, "Editable Layer");

        Button editButton = new Button("Edit feature group");
        editButton.addClickListener(event -> {
            GeomanUtils.setEditableFeatureGroup(leafletMap, editFeatureGroup);
        });

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setSizeFull();
        verticalLayout.add(editButton);
        verticalLayout.add(leafletMap);
        addToContent(verticalLayout);

        GeomanControlOptions geomanControlOptions = new GeomanControlOptions();
        geomanControlOptions.setCutPolygon(false);
        geomanControlOptions.setDrawPolygon(false);
        geomanControlOptions.setDrawCircle(false);
        geomanControlOptions.setDrawCircleMarker(false);
        GeomanUtils.addControls(leafletMap, geomanControlOptions);
    }

    private LayerGroup createRandomMarkers(Icon icon, int limit) {
        LayerGroup layerGroup = new LayerGroup();
        range(0, limit).forEach((i) -> {
            double lat = (Math.random() * 4) + 45;
            double lon = (Math.random() * 7) + 16;
            Marker marker = new Marker(latlng(lat, lon));
            marker.setIcon(icon);
            marker.addTo(layerGroup);
        });
        return layerGroup;
    }
}
