package org.vaadin.addons.componentfactory.leaflet.layer.raster;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.html.Div;

/**
 * Standalone cad-simple-viewer component. Renders a DWG/DXF file into a plain
 * div using the WebGL-based cad-simple-viewer library. No Leaflet dependency.
 */
@NpmPackage(value = "@mlightcad/cad-simple-viewer", version = "1.4.13")
@NpmPackage(value = "@mlightcad/data-model", version = "1.7.25")
@JsModule("./cad-simple-viewer-component.js")
public class CadSimpleViewer extends Div {

    private static final long serialVersionUID = 1L;

    private final String url;
    private String baseUrl;
    private String dwgWorkerUrl;
    private String dxfWorkerUrl;
    private String mtextWorkerUrl;

    public CadSimpleViewer(String url) {
        this.url = url;
        getStyle().set("display", "block");
        getStyle().set("width", "100%");
        getStyle().set("height", "100%");
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        getElement().executeJs(
            "window.CadSimpleViewerComponent.init(this, $0, { baseUrl: $1, dwgWorkerUrl: $2, dxfWorkerUrl: $3, mtextWorkerUrl: $4 })",
            url,
            baseUrl,
            dwgWorkerUrl,
            dxfWorkerUrl,
            mtextWorkerUrl
        );
    }

    public String getUrl() { return url; }

    public String getBaseUrl() { return baseUrl; }
    public void setBaseUrl(String baseUrl) { this.baseUrl = baseUrl; }

    public String getDwgWorkerUrl() { return dwgWorkerUrl; }
    public void setDwgWorkerUrl(String dwgWorkerUrl) { this.dwgWorkerUrl = dwgWorkerUrl; }

    public String getDxfWorkerUrl() { return dxfWorkerUrl; }
    public void setDxfWorkerUrl(String dxfWorkerUrl) { this.dxfWorkerUrl = dxfWorkerUrl; }

    public String getMtextWorkerUrl() { return mtextWorkerUrl; }
    public void setMtextWorkerUrl(String mtextWorkerUrl) { this.mtextWorkerUrl = mtextWorkerUrl; }
}
