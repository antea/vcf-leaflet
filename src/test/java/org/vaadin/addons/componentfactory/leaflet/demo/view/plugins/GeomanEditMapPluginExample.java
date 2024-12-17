package org.vaadin.addons.componentfactory.leaflet.demo.view.plugins;

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
import org.vaadin.addons.componentfactory.leaflet.plugins.geoman.options.GeomanControlOptions;
import org.vaadin.addons.componentfactory.leaflet.plugins.geoman.options.GeomanUtils;
import org.vaadin.addons.componentfactory.leaflet.types.Icon;

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

        LeafletMap leafletMapRight = new LeafletMap(options);
        leafletMapRight.setBaseUrl("https://maps.wikimedia.org/osm-intl/{z}/{x}/{y}.png");
        leafletMapRight.addLayer(createRandomMarkers(DEFAULT_ICON, 30));

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setSizeFull();
        horizontalLayout.add(leafletMapRight);
        addToContent(horizontalLayout);

        GeomanControlOptions geomanControlOptions = new GeomanControlOptions();
        geomanControlOptions.setCutPolygon(false);
        geomanControlOptions.setDrawPolygon(false);
        geomanControlOptions.setDrawCircle(false);
        GeomanUtils.addControls(leafletMapRight, geomanControlOptions);


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
