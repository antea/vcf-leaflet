package org.vaadin.addons.componentfactory.leaflet.demo.view.map;

import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.jetbrains.annotations.NotNull;
import org.vaadin.addons.componentfactory.leaflet.LeafletMap;
import org.vaadin.addons.componentfactory.leaflet.controls.LayersControl;
import org.vaadin.addons.componentfactory.leaflet.controls.ScaleControl;
import org.vaadin.addons.componentfactory.leaflet.demo.LeafletDemoApp;
import org.vaadin.addons.componentfactory.leaflet.demo.components.ExampleContainer;
import org.vaadin.addons.componentfactory.leaflet.layer.map.options.DefaultMapOptions;
import org.vaadin.addons.componentfactory.leaflet.layer.map.options.MapOptions;
import org.vaadin.addons.componentfactory.leaflet.layer.raster.TileLayer;
import org.vaadin.addons.componentfactory.leaflet.plugins.mouseposition.MousePosition;
import org.vaadin.addons.componentfactory.leaflet.plugins.mouseposition.MousePositionOptions;
import org.vaadin.addons.componentfactory.leaflet.types.CustomSimpleCrs;
import org.vaadin.addons.componentfactory.leaflet.types.LatLng;
import org.vaadin.addons.componentfactory.leaflet.types.LatLngBounds;
import org.vaadin.addons.componentfactory.leaflet.types.Point;

@PageTitle("Map CRS set method")
@Route(value = "map/crs", layout = LeafletDemoApp.class)
public class MapCrsExample extends ExampleContainer {
    public static final String pidDescription = "LFI PLANIMETRIA GENERALE_05PB";
    public static final String pidName = "6692552428045889120";
    public static final double MIN_X = 309.318027212648;
    public static final double MIN_Y = 352.775475776667;
    public static final double WIDTH = 1398.35408166923;
    public static final double HEIGHT = 841.052374599691;
    public static final double[] crs = {0.000715126456960237, -0.221201504874512, -0.000715126456960237, 1.05300847847737};
    public static final int MAX_ZOOM = 5;

    @Override
    protected void initDemo() {
        CustomSimpleCrs customSimpleCrs = getCustomCrs();

        MapOptions options = new DefaultMapOptions();
        options.setSupportedCrs(CustomSimpleCrs.BaseCrs.L_CRS_Simple);
        options.setCustomSimpleCrs(customSimpleCrs);

        LeafletMap leafletMap = new LeafletMap(options);

        MousePositionOptions mousePositionOptions = new MousePositionOptions();
        mousePositionOptions.setPrefix("Lat ");
        mousePositionOptions.setSeparator(" : Lon ");
        new MousePosition(mousePositionOptions).addTo(leafletMap);
        new ScaleControl().addTo(leafletMap);
        LayersControl layersControl = new LayersControl();
        layersControl.addTo(leafletMap);

        TileLayer layer = new TileLayer("https://tiles.anteash.com/tile/" + pidName + "/lod_{z}/layer_{x}_{y}.png");
        layer.setNoWrap(true);
        layer.addTo(leafletMap);
        layersControl.addBaseLayer(layer, pidDescription);

        // let's try to reset the options <em>after</em> the map was created, but before it is drawn
        leafletMap.setMapOptions(options);
        leafletMap.setMaxBounds(new LatLngBounds(new LatLng(MIN_Y, MIN_X), new LatLng(MIN_Y + HEIGHT, MIN_X + WIDTH)));
        leafletMap.setMinZoom(0);
        leafletMap.setMaxZoom(MAX_ZOOM);
        leafletMap.setView(new LatLng(MIN_Y + HEIGHT / 2D, MIN_X + WIDTH / 2D), 0);
        addToContent(leafletMap);
    }

    @NotNull
    private CustomSimpleCrs getCustomCrs() {
        Point point = new Point(MIN_X, MIN_Y);
        Point point2 = new Point(point.getX() + WIDTH, point.getY() + HEIGHT);

        return new CustomSimpleCrs("officeCrs", point, point2, crs[0], crs[1], crs[2], crs[3]);
    }

    /**
     *
     * Map:
     * {
     *   "name": "6692552428045889120",
     *   "extension": "dwg",
     *   "description": "LFI PLANIMETRIA GENERALE_05PB",
     *   "nums_lods": 6,
     *   "native_bbox": {
     *     "x": 309.318027212648,
     *     "y": 352.775475776667,
     *     "w": 1398.35408166923,
     *     "h": 841.052374599691
     *   },
     *   "degrees_bbox": {
     *     "x": -149.5,
     *     "y": -90,
     *     "w": 299,
     *     "h": 180
     *   },
     *   "resolutions": [1.16796875, 0.583984375, 0.2919921875, 0.14599609375, 0.072998046875, 0.0364990234375],
     *   "crs": [0.000715126456960237, -0.221201504874512, -0.000715126456960237, 1.05300847847737],
     *   "pixels": {
     *     "x": 8192,
     *     "y": 4940
     *   },
     *   "version": "17",
     *   "root": "DEMO",
     *   "path": "LFI PLANIMETRIA GENERALE_05PB",
     *   "has_pdf": false
     * }
     *
     * PID
     * {
     *   "name": "4934540697340134666",
     *   "extension": "dwg",
     *   "description": "DN813-405",
     *   "nums_lods": 6,
     *   "native_bbox": {
     *     "x": -0.0367730818061318,
     *     "y": -0.0255275322654143,
     *     "w": 1205.14583397223,
     *     "h": 836.601058960043
     *   },
     *   "degrees_bbox": {
     *     "x": -129.5,
     *     "y": -90,
     *     "w": 259,
     *     "h": 180
     *   },
     *   "resolutions": [1.01171875, 0.505859375, 0.2529296875, 0.12646484375, 0.063232421875, 0.0316162109375],
     *   "crs": [0.000829775095935026, 0.000030513387483509, -0.000829775095935026, 0.847074179868423],
     *   "pixels": {
     *     "x": 8192,
     *     "y": 5704
     *   },
     *   "version": "17",
     *   "root": "DEMO",
     *   "path": "DN813-405",
     *   "has_pdf": false
     * }
     */
}
