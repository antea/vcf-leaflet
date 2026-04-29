package org.vaadin.addons.componentfactory.leaflet.demo.view.map;

import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.vaadin.addons.componentfactory.leaflet.demo.LeafletDemoApp;
import org.vaadin.addons.componentfactory.leaflet.demo.components.ExampleContainer;
import org.vaadin.addons.componentfactory.leaflet.layer.raster.OdaWebViewer;
import static org.vaadin.addons.componentfactory.leaflet.demo.view.map.MapCrsExample.pidName;

@PageTitle("ODA Web Viewer")
@Route(value = "map/oda-web-viewer", layout = LeafletDemoApp.class)
public class OdaWebViewerExample extends ExampleContainer {

    @Override
    protected void initDemo() {
        OdaWebViewer viewer = new OdaWebViewer(
                "https://tiles.anteash.com/native/" + pidName);
        viewer.setSizeFull();
        addToContent(viewer);
    }
}
