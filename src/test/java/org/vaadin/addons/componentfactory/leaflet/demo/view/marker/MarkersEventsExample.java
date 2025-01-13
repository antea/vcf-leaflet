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

package org.vaadin.addons.componentfactory.leaflet.demo.view.marker;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.extern.slf4j.Slf4j;
import org.vaadin.addons.componentfactory.leaflet.LeafletMap;
import org.vaadin.addons.componentfactory.leaflet.demo.LeafletDemoApp;
import org.vaadin.addons.componentfactory.leaflet.demo.components.ExampleContainer;
import org.vaadin.addons.componentfactory.leaflet.layer.Layer;
import org.vaadin.addons.componentfactory.leaflet.layer.events.LeafletEvent;
import org.vaadin.addons.componentfactory.leaflet.layer.events.LeafletEventListener;
import org.vaadin.addons.componentfactory.leaflet.layer.events.MouseEvent;
import org.vaadin.addons.componentfactory.leaflet.layer.events.types.LeafletEventType;
import org.vaadin.addons.componentfactory.leaflet.layer.events.types.MouseEventType;
import org.vaadin.addons.componentfactory.leaflet.layer.map.options.DefaultMapOptions;
import org.vaadin.addons.componentfactory.leaflet.layer.map.options.MapOptions;
import org.vaadin.addons.componentfactory.leaflet.layer.ui.marker.Marker;
import org.vaadin.addons.componentfactory.leaflet.types.LatLng;
import com.vaadin.flow.component.button.Button;

@Slf4j
@PageTitle("Marker events")
@Route(value = "marker/events", layout = LeafletDemoApp.class)
public class MarkersEventsExample extends ExampleContainer {

    private Label eventLabel;

    @Override
    protected void initDemo() {

        eventLabel = new Label();
        eventLabel.getStyle().set("font-weight", "bold");
        Button buttonAdd = new Button("Add click listener later");
        Button buttonRemove = new Button("Remove listener");
        Button buttonAddToMap = new Button("Add click listener to Map later");
        Button buttonRemoveFromMap = new Button("Remove listener from Map");
        HorizontalLayout buttonLayout = new HorizontalLayout(buttonAdd, buttonRemove, buttonAddToMap, buttonRemoveFromMap);

        MapOptions options = new DefaultMapOptions();
        options.setCenter(new LatLng(47.070121823, 19.2041015625));
        options.setZoom(7);
        LeafletMap leafletMap = new LeafletMap(options);
        leafletMap.setBaseUrl("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png");

        final Marker marker = new Marker(new LatLng(46.470121823, 18.3041015625));
        marker.setDraggable(true);
        marker.bindPopup("Hi, I'am an interactive marker!");
        marker.bindTooltip("hey, drag me if you want.");
        //		marker.onClick(this::logEvent);
        marker.onAdd(this::logEvent);
        marker.onDoubleClick(this::logEvent);
        marker.onPopupOpen(this::logEvent);
        marker.onPopupClose(this::logEvent);
        marker.onTooltipOpen(this::logEvent);
        marker.onTooltipClose(this::logEvent);
        marker.onDragStart(this::logEvent);
        marker.onDragEnd(this::logEvent);
        marker.onDrag(this::logEvent);
        marker.onContextMenuOpened(this::logEvent);
        marker.onMouseDown(this::logEvent);
        marker.onMouseUp(this::logEvent);
        marker.addTo(leafletMap);

        addToContent(eventLabel, buttonLayout, leafletMap);

        LeafletEventListener<MouseEvent> logClickEvent = this::logClickEvent;
        buttonAdd.addClickListener(e -> marker.onClick(logClickEvent));
        buttonRemove.addClickListener(e -> marker.removeEventListener(MouseEventType.click, logClickEvent));

        buttonAddToMap.addClickListener(e -> leafletMap.onClick(logClickEvent));
        buttonRemoveFromMap.addClickListener(e -> leafletMap.removeEventListener(MouseEventType.click, logClickEvent));
    }

    protected void logEvent(LeafletEvent leafletEvent) {
        this.eventLabel.setText("'" + leafletEvent.getType().name() + "' event caught in listener.");
    }

    protected void logClickEvent(LeafletEvent leafletEvent) {
        this.eventLabel.setText("'This is the only click listener and was added later" +
                leafletEvent.getType().name() + "' event caught in listener.");
        log.info("Clicked and this listener was added later");
        Notification.show("Clicked and this listener was added later");
    }
}
