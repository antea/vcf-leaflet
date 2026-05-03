package org.vaadin.addons.componentfactory.leaflet.layer.raster;

import com.vaadin.flow.component.dependency.JsModule;
import lombok.Getter;
import lombok.Setter;
import org.vaadin.addons.componentfactory.leaflet.annotations.LeafletArgument;
import org.vaadin.addons.componentfactory.leaflet.layer.Layer;

/**
 * Displays a DWG/DXF file using the ODA (Open Design Alliance) WebAssembly
 * renderer inside a Leaflet overlay. Pan and zoom are driven by Leaflet;
 * relative pixel deltas are forwarded to the ODA camera via Dolly/Zoom.
 */
@JsModule("./oda-native-layer.js")
public class OdaNativeLayer extends Layer {

    private static final long serialVersionUID = 1L;

    @LeafletArgument(index = 0)
    private String url;

    @Getter
    @Setter
    @LeafletArgument(index = 1)
    private String fontUrl;

    public OdaNativeLayer(String url) {
        super();
        this.url = url;
        this.fontUrl = "/VAADIN/generated/jar-resources/oda/assets/arial.ttf";
    }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
}
