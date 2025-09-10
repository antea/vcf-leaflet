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

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.vaadin.addons.componentfactory.leaflet.LeafletMap;
import org.vaadin.addons.componentfactory.leaflet.demo.LeafletDemoApp;
import org.vaadin.addons.componentfactory.leaflet.demo.components.ExampleContainer;
import org.vaadin.addons.componentfactory.leaflet.layer.map.options.DefaultMapOptions;
import org.vaadin.addons.componentfactory.leaflet.layer.map.options.MapOptions;
import org.vaadin.addons.componentfactory.leaflet.plugins.fullscreen.FullScreenControl;
import org.vaadin.addons.componentfactory.leaflet.plugins.fullscreen.WithFullScreenControl;
import org.vaadin.addons.componentfactory.leaflet.types.LatLng;

@PageTitle("Full screen example")
@Route(value = "plugins/fullscreen", layout = LeafletDemoApp.class)
public class FullScreenPluginExample extends ExampleContainer {

    @Override
    protected void initDemo() {

        MapOptions options = new DefaultMapOptions();
        options.setCenter(new LatLng(47.070121823, 19.204101562500004));
        options.setZoom(7);

        LeafletMap leafletMap = new LeafletMap(options);
        leafletMap.setBaseUrl("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png");

        FullScreenControl fullScreenControl = new FullScreenControl();
        fullScreenControl.addTo(leafletMap);

        WithFullScreenControl mapWithFullScreenControl = FullScreenControl.wrap(leafletMap);
        mapWithFullScreenControl.onEnterFullscreen((e) -> {
            Notification.show("Map entered to fullscreen mode.", 3000, Position.MIDDLE);
        });
        mapWithFullScreenControl.onExitFullscreen((e) -> {
            Notification.show("Map exited from fullscreen mode.", 3000, Position.MIDDLE);
        });

        Button toogleFullscreen = new Button("Toggle fullscreen");
        toogleFullscreen.addClickListener((e)-> mapWithFullScreenControl.toggleFullscreen());

        Anchor pluginRepository = new Anchor();
        pluginRepository.setHref("https://github.com/brunob/leaflet.fullscreen");
        pluginRepository.setText("Plugin: https://github.com/brunob/leaflet.fullscreen");
        pluginRepository.setTarget("_blank");

        addToContent(toogleFullscreen, leafletMap, pluginRepository);
    }

}
