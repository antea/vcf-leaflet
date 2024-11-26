/*-
 * #%L
 * Leaflet
 * %%
 * Copyright (C) 2023 Vaadin Ltd
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

/*
 * This file incorporates work licensed under the Apache License, Version 2.0
 * Copyright 2020 Gabor Kokeny and contributors
 */
package org.vaadin.addons.componentfactory.leaflet.demo;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;

import org.vaadin.addons.componentfactory.leaflet.demo.components.AppMenu;
import org.vaadin.addons.componentfactory.leaflet.demo.components.AppMenuItem;
import org.vaadin.addons.componentfactory.leaflet.demo.view.controls.ControlPositionExample;
import org.vaadin.addons.componentfactory.leaflet.demo.view.controls.LayersControlExample;
import org.vaadin.addons.componentfactory.leaflet.demo.view.controls.RemoveDefaultControlsExample;
import org.vaadin.addons.componentfactory.leaflet.demo.view.controls.ScaleControlExample;
import org.vaadin.addons.componentfactory.leaflet.demo.view.layers.MultipleBaseLayersExample;
import org.vaadin.addons.componentfactory.leaflet.demo.view.map.*;
import org.vaadin.addons.componentfactory.leaflet.demo.view.marker.DivOverlayStyleExample;
import org.vaadin.addons.componentfactory.leaflet.demo.view.marker.MarkerDivIconExample;
import org.vaadin.addons.componentfactory.leaflet.demo.view.marker.MarkerMethodCallExample;
import org.vaadin.addons.componentfactory.leaflet.demo.view.marker.MarkersAddAndRemoveExample;
import org.vaadin.addons.componentfactory.leaflet.demo.view.marker.MarkersChangingIconExample;
import org.vaadin.addons.componentfactory.leaflet.demo.view.marker.MarkersEventsExample;
import org.vaadin.addons.componentfactory.leaflet.demo.view.marker.MarkersGroupExample;
import org.vaadin.addons.componentfactory.leaflet.demo.view.marker.MarkersRemoveOnClickExample;
import org.vaadin.addons.componentfactory.leaflet.demo.view.marker.MarkersSimpleExample;
import org.vaadin.addons.componentfactory.leaflet.demo.view.marker.MarkersWithEventsExample;
import org.vaadin.addons.componentfactory.leaflet.demo.view.path.FlyToPolygonBoundsExample;
import org.vaadin.addons.componentfactory.leaflet.demo.view.path.PathSimpleExample;
import org.vaadin.addons.componentfactory.leaflet.demo.view.path.Paths3000Example;
import org.vaadin.addons.componentfactory.leaflet.demo.view.path.PathsEventPropagationExample;
import org.vaadin.addons.componentfactory.leaflet.demo.view.path.PathsStyleExample;
import org.vaadin.addons.componentfactory.leaflet.demo.view.path.TypeOfPathsExample;
import org.vaadin.addons.componentfactory.leaflet.demo.view.plugins.DynamicMapLayerPluginExample;
import org.vaadin.addons.componentfactory.leaflet.demo.view.plugins.FullScreenPluginExample;
import org.vaadin.addons.componentfactory.leaflet.demo.view.plugins.HeatmapPluginExample;
import org.vaadin.addons.componentfactory.leaflet.demo.view.plugins.KmzLayerPluginExample;
import org.vaadin.addons.componentfactory.leaflet.demo.view.plugins.MarkerClusterPluginExample;
import org.vaadin.addons.componentfactory.leaflet.demo.view.plugins.TiledMapLayerPluginExample;
import org.vaadin.addons.componentfactory.leaflet.demo.view.plugins.VectorBasemapLayerPluginExample;

@CssImport(value = "styles/demo-applayout.css", themeFor = "vaadin-app-layout")
public class LeafletDemoApp extends AppLayout implements AfterNavigationObserver {

    private static final long serialVersionUID = -9119767347112138141L;

    private AppMenu appMenu = AppMenu.create();

    public LeafletDemoApp() {
        DrawerToggle drawerToggle = new DrawerToggle();
        drawerToggle.setIcon(new Icon(VaadinIcon.MENU));
        addToNavbar(true, drawerToggle);

        // Leaflet Icon
        Image image = new Image("https://leafletjs.com/docs/images/logo.png", "icon");
        image.setHeight("30px");
        image.getStyle().set("margin", "10px");
        image.addClickListener((e) -> UI.getCurrent().getPage().setLocation("https://leafletjs.com/"));
        addToNavbar(image);

        // App title
        Label appTitle = new Label("Vaadin - Leaflet examples");
        appTitle.setWidthFull();
        appTitle.getStyle().set("font-size", "20px");
        appTitle.getStyle().set("margin-left", "10px");
        appTitle.getStyle().set("font-weight", "300");
        addToNavbar(appTitle);

        // Application menubar
        initializeDemoMenu();
    }

    private void initializeDemoMenu() {

        // Map examples
        AppMenuItem.create("Map", new Icon(VaadinIcon.GLOBE)).addSubMenu(MapEventsExample.class)
                .addSubMenu(MapMaxBoundsExample.class).addSubMenu(MapDarkThemeExample.class)
                .addSubMenu(MapPollListenerExample.class).addSubMenu(MapGeolocationExample.class)
                .addSubMenu(MapFunctionsExample.class).addSubMenu(MapConversionMethodsExample.class)
                .addSubMenu(MapDialogExample.class)
                .addSubMenu(MultipleMapsExample.class)
                .addSubMenu(MapStateFuncionsExmple.class)
                .addSubMenu(MapCrsExample.class)
                .addTo(appMenu);

        // Marker examples
        AppMenuItem.create("Markers", new Icon(VaadinIcon.MAP_MARKER)).addSubMenu(MarkersSimpleExample.class)
                .addSubMenu(MarkersEventsExample.class).addSubMenu(MarkersWithEventsExample.class)
                .addSubMenu(MarkersGroupExample.class).addSubMenu(MarkersAddAndRemoveExample.class)
                .addSubMenu(MarkersChangingIconExample.class).addSubMenu(MarkersRemoveOnClickExample.class)
                .addSubMenu(MarkerMethodCallExample.class)
                .addSubMenu(MarkerDivIconExample.class)
                .addSubMenu(DivOverlayStyleExample.class)
                .addTo(appMenu);

        // Layers examples
        AppMenuItem.create("Layers", new Icon(VaadinIcon.GRID_SMALL))
                .addSubMenu(MultipleBaseLayersExample.class);

        // Paths examples
        AppMenuItem.create("Paths", new Icon(VaadinIcon.FILL)).addSubMenu(PathSimpleExample.class)
                .addSubMenu(TypeOfPathsExample.class).addSubMenu(PathsEventPropagationExample.class)
                .addSubMenu(FlyToPolygonBoundsExample.class).addSubMenu(Paths3000Example.class)
                .addSubMenu(PathsStyleExample.class).addTo(appMenu);

        // Controls examples
        AppMenuItem.create("Controls", new Icon(VaadinIcon.ARROWS))
                .addSubMenu(RemoveDefaultControlsExample.class)
                .addSubMenu(LayersControlExample.class)
                .addSubMenu(ControlPositionExample.class)
                .addSubMenu(ScaleControlExample.class).addTo(appMenu);

        // Mixins examples

        // Plugins examples
        AppMenuItem.create("Plugins", new Icon(VaadinIcon.PLUG)).addSubMenu(FullScreenPluginExample.class)
                .addSubMenu(HeatmapPluginExample.class)
                .addSubMenu(MarkerClusterPluginExample.class)
                .addSubMenu(KmzLayerPluginExample.class)
                .addSubMenu(DynamicMapLayerPluginExample.class)
                .addSubMenu(TiledMapLayerPluginExample.class)
                .addSubMenu(VectorBasemapLayerPluginExample.class)
                .addTo(appMenu);

        addToDrawer(appMenu);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        appMenu.setActivePath(event.getLocation().getPath());
    }
}
