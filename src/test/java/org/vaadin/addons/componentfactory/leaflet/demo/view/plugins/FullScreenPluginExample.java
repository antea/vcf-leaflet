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

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.vaadin.addons.componentfactory.leaflet.LeafletMap;
import org.vaadin.addons.componentfactory.leaflet.demo.LeafletDemoApp;
import org.vaadin.addons.componentfactory.leaflet.demo.components.ExampleContainer;
import org.vaadin.addons.componentfactory.leaflet.layer.events.types.EasyPrintEventType;
import org.vaadin.addons.componentfactory.leaflet.layer.map.options.DefaultMapOptions;
import org.vaadin.addons.componentfactory.leaflet.layer.map.options.MapOptions;
import org.vaadin.addons.componentfactory.leaflet.plugins.fullscreen.FullScreenControl;
import org.vaadin.addons.componentfactory.leaflet.plugins.fullscreen.WithFullScreenControl;
import org.vaadin.addons.componentfactory.leaflet.plugins.print.EasyPrintMap;
import org.vaadin.addons.componentfactory.leaflet.plugins.print.EasyPrintOptions;
import org.vaadin.addons.componentfactory.leaflet.types.LatLng;
import org.vaadin.addons.componentfactory.leaflet.types.LatLngBounds;
import org.vaadin.addons.componentfactory.leaflet.types.Point;

import java.util.concurrent.CompletableFuture;

@Slf4j
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

        HorizontalLayout layout = new HorizontalLayout();
        layout.setWidthFull();
        layout.add(new Button("Print Landscape", printEventListener(leafletMap, "landscapePrint", EasyPrintOptions.SIZE_A4_LANDSCAPE)));
        layout.add(new Button("Print Portrait", printEventListener(leafletMap, "portraitPrint", EasyPrintOptions.SIZE_A4_PORTRAIT)));
        addToContent(layout);

        FullScreenControl fullScreenControl = new FullScreenControl();
        fullScreenControl.addTo(leafletMap);

        Button toogleFullscreen = getToogleFullscreenButton(leafletMap);

        Anchor pluginRepository = new Anchor();
        pluginRepository.setHref("https://github.com/brunob/leaflet.fullscreen");
        pluginRepository.setText("Plugin: https://github.com/brunob/leaflet.fullscreen");
        pluginRepository.setTarget("_blank");

        addToContent(toogleFullscreen, leafletMap);
    }

    @NotNull
    private static Button getToogleFullscreenButton(LeafletMap leafletMap) {
        WithFullScreenControl mapWithFullScreenControl = FullScreenControl.wrap(leafletMap);
        mapWithFullScreenControl.onEnterFullscreen((e) -> {
            Notification.show("Map entered to fullscreen mode.", 3000, Position.MIDDLE);
        });
        mapWithFullScreenControl.onExitFullscreen((e) -> {
            Notification.show("Map exited from fullscreen mode.", 3000, Position.MIDDLE);
        });

        Button toogleFullscreen = new Button("Toggle fullscreen");
        toogleFullscreen.addClickListener((e) -> mapWithFullScreenControl.toggleFullscreen());
        return toogleFullscreen;
    }

    @NotNull
    private ComponentEventListener<ClickEvent<Button>> printEventListener(
            LeafletMap leafletMap, String filename, String easyPrintSize) {
        return event -> {
            EasyPrintOptions opts = new EasyPrintOptions();
            opts.setHideControlContainer(true);
            CompletableFuture<Integer> zoomCF = leafletMap.getZoom();
            CompletableFuture<LatLng> centerCF = leafletMap.getCenter();
            CompletableFuture<Point> pixelSizeCF = leafletMap.getSize();
            CompletableFuture<LatLngBounds> boundsCF = leafletMap.getBounds();
            CompletableFuture.allOf(zoomCF, centerCF, boundsCF, pixelSizeCF)
                    .thenRun(() -> {
                        opts.setZoom(zoomCF.join());
                        opts.setCenter(centerCF.join());
                        opts.setBounds(boundsCF.join());
                        Point mapSize = pixelSizeCF.join();
                        double height = mapSize.getY();
                        double width = mapSize.getX();
                        EasyPrintMap map = new EasyPrintMap(opts, height, width);
                        addToContent(map);
                        leafletMap.addEventListener(EasyPrintEventType.finished, leafletEvent -> {
                            removeFromContent(map);
                        });
                        map.print(filename, easyPrintSize);
                    });
        };
    }
}
