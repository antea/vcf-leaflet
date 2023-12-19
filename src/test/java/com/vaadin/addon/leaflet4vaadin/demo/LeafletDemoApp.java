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

package com.vaadin.addon.leaflet4vaadin.demo;

import com.vaadin.addon.leaflet4vaadin.demo.components.AppMenu;
import com.vaadin.addon.leaflet4vaadin.demo.components.AppMenuItem;
import com.vaadin.addon.leaflet4vaadin.demo.view.controls.ControlPositionExample;
import com.vaadin.addon.leaflet4vaadin.demo.view.controls.LayersControlExample;
import com.vaadin.addon.leaflet4vaadin.demo.view.controls.RemoveDefaultControlsExample;
import com.vaadin.addon.leaflet4vaadin.demo.view.controls.ScaleControlExample;
import com.vaadin.addon.leaflet4vaadin.demo.view.layers.GeoJSONEventsExample;
import com.vaadin.addon.leaflet4vaadin.demo.view.layers.GeoJSONFilterExample;
import com.vaadin.addon.leaflet4vaadin.demo.view.layers.GeoJSONLayerExample;
import com.vaadin.addon.leaflet4vaadin.demo.view.layers.GeoJSONStyleExample;
import com.vaadin.addon.leaflet4vaadin.demo.view.layers.MultipleBaseLayersExample;
import com.vaadin.addon.leaflet4vaadin.demo.view.map.MapConversionMethodsExample;
import com.vaadin.addon.leaflet4vaadin.demo.view.map.MapDarkThemeExample;
import com.vaadin.addon.leaflet4vaadin.demo.view.map.MapDialogExample;
import com.vaadin.addon.leaflet4vaadin.demo.view.map.MapEventsExample;
import com.vaadin.addon.leaflet4vaadin.demo.view.map.MapFunctionsExample;
import com.vaadin.addon.leaflet4vaadin.demo.view.map.MapGeolocationExample;
import com.vaadin.addon.leaflet4vaadin.demo.view.map.MapMaxBoundsExample;
import com.vaadin.addon.leaflet4vaadin.demo.view.map.MapPollListenerExample;
import com.vaadin.addon.leaflet4vaadin.demo.view.map.MapStateFuncionsExmple;
import com.vaadin.addon.leaflet4vaadin.demo.view.map.MultipleMapsExample;
import com.vaadin.addon.leaflet4vaadin.demo.view.marker.DivOverlayStyleExample;
import com.vaadin.addon.leaflet4vaadin.demo.view.marker.MarkerDivIconExample;
import com.vaadin.addon.leaflet4vaadin.demo.view.marker.MarkerMethodCallExample;
import com.vaadin.addon.leaflet4vaadin.demo.view.marker.MarkersAddAndRemoveExample;
import com.vaadin.addon.leaflet4vaadin.demo.view.marker.MarkersChangingIconExample;
import com.vaadin.addon.leaflet4vaadin.demo.view.marker.MarkersEventsExample;
import com.vaadin.addon.leaflet4vaadin.demo.view.marker.MarkersGroupExample;
import com.vaadin.addon.leaflet4vaadin.demo.view.marker.MarkersRemoveOnClickExample;
import com.vaadin.addon.leaflet4vaadin.demo.view.marker.MarkersSimpleExample;
import com.vaadin.addon.leaflet4vaadin.demo.view.marker.MarkersWithEventsExample;
import com.vaadin.addon.leaflet4vaadin.demo.view.mixed.WorldMapFlagsExample;
import com.vaadin.addon.leaflet4vaadin.demo.view.path.FlyToPolygonBoundsExample;
import com.vaadin.addon.leaflet4vaadin.demo.view.path.PathSimpleExample;
import com.vaadin.addon.leaflet4vaadin.demo.view.path.Paths3000Example;
import com.vaadin.addon.leaflet4vaadin.demo.view.path.PathsEventPropagationExample;
import com.vaadin.addon.leaflet4vaadin.demo.view.path.PathsStyleExample;
import com.vaadin.addon.leaflet4vaadin.demo.view.path.TypeOfPathsExample;
import com.vaadin.addon.leaflet4vaadin.demo.view.plugins.CanvasIconLayerExample;
import com.vaadin.addon.leaflet4vaadin.demo.view.plugins.DynamicMapLayerPluginExample;
import com.vaadin.addon.leaflet4vaadin.demo.view.plugins.FullScreenPluginExample;
import com.vaadin.addon.leaflet4vaadin.demo.view.plugins.HeatmapPluginExample;
import com.vaadin.addon.leaflet4vaadin.demo.view.plugins.KmzLayerPluginExample;
import com.vaadin.addon.leaflet4vaadin.demo.view.plugins.MarkerClusterPluginExample;
import com.vaadin.addon.leaflet4vaadin.demo.view.plugins.TiledMapLayerPluginExample;
import com.vaadin.addon.leaflet4vaadin.demo.view.plugins.VectorBasemapLayerPluginExample;
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
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

@CssImport(value = "styles/demo-applayout.css", themeFor = "vaadin-app-layout")
@Theme(value = Lumo.class, variant = Lumo.LIGHT)
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
				.addSubMenu(MultipleBaseLayersExample.class).addSubMenu(GeoJSONLayerExample.class)
				.addSubMenu(GeoJSONFilterExample.class).addSubMenu(GeoJSONStyleExample.class)
				.addSubMenu(GeoJSONEventsExample.class).addTo(appMenu);

		// Paths examples
		AppMenuItem.create("Paths", new Icon(VaadinIcon.FILL)).addSubMenu(PathSimpleExample.class)
				.addSubMenu(TypeOfPathsExample.class).addSubMenu(PathsEventPropagationExample.class)
				.addSubMenu(FlyToPolygonBoundsExample.class).addSubMenu(Paths3000Example.class)
				.addSubMenu(PathsStyleExample.class).addTo(appMenu);

		// Controls examples
		AppMenuItem.create("Controls", new Icon(VaadinIcon.ARROWS)).addSubMenu(RemoveDefaultControlsExample.class)
        .addSubMenu(LayersControlExample.class)
				.addSubMenu(ControlPositionExample.class).addSubMenu(ScaleControlExample.class).addTo(appMenu);

		// Mixins examples
		AppMenuItem.create("Mixin", new Icon(VaadinIcon.SHIELD)).addSubMenu(WorldMapFlagsExample.class).addTo(appMenu);

		// Plugins examples
		AppMenuItem.create("Plugins", new Icon(VaadinIcon.PLUG)).addSubMenu(FullScreenPluginExample.class)
        .addSubMenu(HeatmapPluginExample.class)
        .addSubMenu(MarkerClusterPluginExample.class)
        .addSubMenu(CanvasIconLayerExample.class)
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
