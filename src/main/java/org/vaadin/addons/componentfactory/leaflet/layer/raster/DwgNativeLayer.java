package org.vaadin.addons.componentfactory.leaflet.layer.raster;

import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import org.vaadin.addons.componentfactory.leaflet.annotations.LeafletArgument;
import org.vaadin.addons.componentfactory.leaflet.layer.Layer;

/**
 * Displays a DWG/DXF file using the cad-simple-viewer WebGL renderer inside a
 * Leaflet overlay. Leaflet controls all pan and zoom; the cad-viewer renders
 * whatever the current Leaflet viewport shows.
 * <p>
 * The layer covers the full map container with a transparent-to-input canvas.
 * On every Leaflet {@code moveend}/{@code zoomend} it translates the visible
 * bounds (in CRS.Simple native DWG coordinates) into a {@code zoomTo} call on
 * the cad-viewer so the rendered content stays in sync.
 * <p>
 * Worker URLs are required for DWG/DXF parsing. Set them via
 * {@link #setDwgWorkerUrl}, {@link #setDxfWorkerUrl}, and
 * {@link #setMtextWorkerUrl} before adding the layer to the map.
 */
@NpmPackage(value = "@mlightcad/cad-simple-viewer", version = "1.4.13")
@NpmPackage(value = "@mlightcad/data-model", version = "1.7.25")
@JsModule("./dwg-native-layer.js")
public class DwgNativeLayer extends Layer {

    private static final long serialVersionUID = 1L;

    @LeafletArgument(index = 0)
    private String url;

    private String baseUrl;
    private String dwgWorkerUrl;
    private String dxfWorkerUrl;
    private String mtextWorkerUrl;

    public DwgNativeLayer(String url) {
        super();
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getDwgWorkerUrl() {
        return dwgWorkerUrl;
    }

    public void setDwgWorkerUrl(String dwgWorkerUrl) {
        this.dwgWorkerUrl = dwgWorkerUrl;
    }

    public String getDxfWorkerUrl() {
        return dxfWorkerUrl;
    }

    public void setDxfWorkerUrl(String dxfWorkerUrl) {
        this.dxfWorkerUrl = dxfWorkerUrl;
    }

    public String getMtextWorkerUrl() {
        return mtextWorkerUrl;
    }

    public void setMtextWorkerUrl(String mtextWorkerUrl) {
        this.mtextWorkerUrl = mtextWorkerUrl;
    }
}
