package org.vaadin.addons.componentfactory.leaflet.demo.view.map;

import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.vaadin.addons.componentfactory.leaflet.demo.LeafletDemoApp;
import org.vaadin.addons.componentfactory.leaflet.demo.components.ExampleContainer;
import org.vaadin.addons.componentfactory.leaflet.plugins.print.EasyPrintMap;

@PageTitle("Easy Print Map")
@Route(value = "map/easyprint", layout = LeafletDemoApp.class)
public class EasyPrintMapExample extends ExampleContainer {
    @Override
    protected void initDemo() {
        EasyPrintMap easyPrintMap = new EasyPrintMap();
        //easyPrintMap.print();
        addToContent(easyPrintMap);
    }
}
