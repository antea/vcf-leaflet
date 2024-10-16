package org.vaadin.addons.componentfactory.leaflet.demo.view.controls;

import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.vaadin.addons.componentfactory.leaflet.LeafletMap;
import org.vaadin.addons.componentfactory.leaflet.controls.LeafletControl;
import org.vaadin.addons.componentfactory.leaflet.demo.LeafletDemoApp;
import org.vaadin.addons.componentfactory.leaflet.demo.components.ExampleContainer;
import org.vaadin.addons.componentfactory.leaflet.layer.map.options.DefaultMapOptions;
import org.vaadin.addons.componentfactory.leaflet.layer.map.options.MapOptions;
import org.vaadin.addons.componentfactory.leaflet.plugins.mouseposition.MousePosition;
import org.vaadin.addons.componentfactory.leaflet.plugins.print.BigImageControl;
import org.vaadin.addons.componentfactory.leaflet.plugins.print.EasyPrintControl;
import org.vaadin.addons.componentfactory.leaflet.types.LatLng;

@PageTitle("Print control")
@Route(value = "controls/print-control", layout = LeafletDemoApp.class)
public class PrintControlExample extends ExampleContainer {

    @Override
    protected void initDemo() {

        MapOptions options = new DefaultMapOptions();
        options.setCenter(new LatLng(47.070121823, 19.2041015625));
        options.setZoom(7);
        LeafletMap leafletMap = new LeafletMap(options);
        leafletMap.setBaseUrl("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png");

        BigImageControl bigImageControl = new BigImageControl();
        bigImageControl.setPosition(LeafletControl.ControlPosition.topleft);
        bigImageControl.addTo(leafletMap);

        EasyPrintControl easyPrintControl = new EasyPrintControl();
        easyPrintControl.setPosition(LeafletControl.ControlPosition.topleft);
        easyPrintControl.addTo(leafletMap);

        MousePosition mousePosition = new MousePosition();
        mousePosition.setPosition(LeafletControl.ControlPosition.bottomleft);
        mousePosition.addTo(leafletMap);
        addToContent(leafletMap);
    }
}