package org.vaadin.addons.componentfactory.leaflet.demo.view.map;

import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.vaadin.addons.componentfactory.leaflet.LeafletMap;
import org.vaadin.addons.componentfactory.leaflet.controls.LayersControl;
import org.vaadin.addons.componentfactory.leaflet.controls.ScaleControl;
import org.vaadin.addons.componentfactory.leaflet.demo.LeafletDemoApp;
import org.vaadin.addons.componentfactory.leaflet.demo.components.ExampleContainer;
import org.vaadin.addons.componentfactory.leaflet.layer.map.options.DefaultMapOptions;
import org.vaadin.addons.componentfactory.leaflet.layer.map.options.MapOptions;
import org.vaadin.addons.componentfactory.leaflet.layer.raster.OdaNativeLayer;
import org.vaadin.addons.componentfactory.leaflet.types.CustomSimpleCrs;
import org.vaadin.addons.componentfactory.leaflet.types.LatLng;
import org.vaadin.addons.componentfactory.leaflet.types.LatLngBounds;
import org.vaadin.addons.componentfactory.leaflet.types.Point;

import static org.vaadin.addons.componentfactory.leaflet.demo.view.map.MapCrsExample.*;

@PageTitle("ODA Native Layer")
@Route(value = "map/oda-native", layout = LeafletDemoApp.class)
public class OdaNativeExample extends ExampleContainer {

    @Override
    protected void initDemo() {
        Point point1 = new Point(MIN_X, MIN_Y);
        Point point2 = new Point(MIN_X + WIDTH, MIN_Y + HEIGHT);
        CustomSimpleCrs customSimpleCrs = new CustomSimpleCrs(
                "officeCrs", point1, point2, crs[0], crs[1], crs[2], crs[3]);

        MapOptions options = new DefaultMapOptions();
        options.setSupportedCrs(CustomSimpleCrs.BaseCrs.L_CRS_Simple);
        options.setCustomSimpleCrs(customSimpleCrs);

        LeafletMap leafletMap = new LeafletMap(options);

        new ScaleControl().addTo(leafletMap);
        LayersControl layersControl = new LayersControl();
        layersControl.addTo(leafletMap);

        OdaNativeLayer layer = new OdaNativeLayer(
                "https://tiles.anteash.com/native/" + pidName);
        layer.addTo(leafletMap);
        layersControl.addBaseLayer(layer, pidDescription);

        leafletMap.setMapOptions(options);
        leafletMap.setMaxBounds(new LatLngBounds(
                new LatLng(MIN_Y, MIN_X),
                new LatLng(MIN_Y + HEIGHT, MIN_X + WIDTH)));
        leafletMap.setMinZoom(0);
        leafletMap.setMaxZoom(MAX_ZOOM);
        leafletMap.setView(new LatLng(MIN_Y + HEIGHT / 2D, MIN_X + WIDTH / 2D), 0);

        addToContent(leafletMap);
    }
}
