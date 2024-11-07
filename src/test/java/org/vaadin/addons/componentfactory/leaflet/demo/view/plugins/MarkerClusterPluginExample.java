// Copyright 2020 Gabor Kokeny and contributors
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
//     http://www.apache.org/licenses/LICENSE-2.0
// 
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.vaadin.addons.componentfactory.leaflet.demo.view.plugins;

import static java.util.stream.IntStream.range;
import static org.vaadin.addons.componentfactory.leaflet.types.LatLng.latlng;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.vaadin.addons.componentfactory.leaflet.LeafletMap;
import org.vaadin.addons.componentfactory.leaflet.demo.LeafletDemoApp;
import org.vaadin.addons.componentfactory.leaflet.demo.components.ExampleContainer;
import org.vaadin.addons.componentfactory.leaflet.layer.groups.LayerGroup;
import org.vaadin.addons.componentfactory.leaflet.layer.map.options.DefaultMapOptions;
import org.vaadin.addons.componentfactory.leaflet.layer.map.options.MapOptions;
import org.vaadin.addons.componentfactory.leaflet.layer.ui.marker.Marker;
import org.vaadin.addons.componentfactory.leaflet.plugins.markercluster.MarkerClusterGroup;
import org.vaadin.addons.componentfactory.leaflet.plugins.markercluster.MarkerClusterOptions;
import org.vaadin.addons.componentfactory.leaflet.types.LatLng;

@PageTitle("Marker cluster example")
@Route(value = "plugins/markercluster", layout = LeafletDemoApp.class)
public class MarkerClusterPluginExample extends ExampleContainer {

    @Override
    protected void initDemo() {

        MapOptions mapOptions = new DefaultMapOptions();
        mapOptions.setCenter(new LatLng(47.070121823, 19.204101562500004));
        mapOptions.setZoom(7);

        LeafletMap leafletMap = new LeafletMap(mapOptions);
        leafletMap.setBaseUrl("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png");

        MarkerClusterOptions options = new MarkerClusterOptions();
        options.setIconCreateFunction("function(cluster) { " +
                    "return L.divIcon({ html: '<img src=\"images/marker-icon-demo.png\" /><span>' + cluster.getChildCount() + '</span>' });" +
                "}");

        MarkerClusterGroup markerClusterGroup = new MarkerClusterGroup(options);
        markerClusterGroup.onClusterClick((e) -> Notification.show("You click the cluster", 1000, Position.TOP_CENTER));

        // wait for cluster added to map
        markerClusterGroup.onAdd((e) -> {
            addRandomMarkers(markerClusterGroup);
        });
        markerClusterGroup.addTo(leafletMap);

        Anchor pluginRepository = new Anchor();
        pluginRepository.setHref("https://github.com/Leaflet/Leaflet.markercluster");
        pluginRepository.setText("Plugin: https://github.com/Leaflet/Leaflet.markercluster");
        pluginRepository.setTarget("_blank");

        HorizontalLayout buttons = new HorizontalLayout();
        
        // addLayer function example
        Button addMarkers = new Button("Add random markers");
        addMarkers.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addMarkers.addClickListener((e) -> addRandomMarkers(markerClusterGroup));

        // remove all markers
        Button removeAllMarkers = new Button("Remove all markers");
        removeAllMarkers.addThemeVariants(ButtonVariant.LUMO_ERROR);
        removeAllMarkers.addClickListener((e) -> markerClusterGroup.clearLayers());
        
        buttons.add(addMarkers, removeAllMarkers);

        addToContent(buttons, leafletMap, pluginRepository);
    }

    private void addRandomMarkers(MarkerClusterGroup markerClusterGroup) {
        LayerGroup layerGroup = new LayerGroup();
        range(0, 100).forEach((i) -> {
            double lat = (Math.random() * 4) + 45;
            double lon = (Math.random() * 7) + 16;
            Marker marker = new Marker(latlng(lat, lon));
            marker.bindPopup("Hi, My location is:</br> latitude: " + lat + "</br>longitude: " + lon);
            marker.addTo(layerGroup);
        });
        layerGroup.addTo(markerClusterGroup);
    }

}
