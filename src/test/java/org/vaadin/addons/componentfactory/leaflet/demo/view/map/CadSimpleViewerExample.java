package org.vaadin.addons.componentfactory.leaflet.demo.view.map;

import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.vaadin.addons.componentfactory.leaflet.demo.LeafletDemoApp;
import org.vaadin.addons.componentfactory.leaflet.demo.components.ExampleContainer;
import org.vaadin.addons.componentfactory.leaflet.layer.raster.CadSimpleViewer;

import static org.vaadin.addons.componentfactory.leaflet.demo.view.map.MapCrsExample.pidName;

@PageTitle("CAD Simple Viewer")
@Route(value = "map/cad-simple-viewer", layout = LeafletDemoApp.class)
public class CadSimpleViewerExample extends ExampleContainer {

    @Override
    protected void initDemo() {
        CadSimpleViewer viewer = new CadSimpleViewer(
                "https://tiles.anteash.com/native/" + pidName);
        viewer.setSizeFull();
        addToContent(viewer);
    }
}
