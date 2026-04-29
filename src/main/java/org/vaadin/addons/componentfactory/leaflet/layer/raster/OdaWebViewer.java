package org.vaadin.addons.componentfactory.leaflet.layer.raster;

import com.vaadin.flow.component.html.IFrame;

/**
 * Embeds the ODA WebAssembly-based DWG/DXF viewer in an IFrame.
 * Optionally pre-loads a file via the {@code ?url=} query parameter.
 */
public class OdaWebViewer extends IFrame {

    private static final long serialVersionUID = 1L;
    private static final String VIEWER_PATH = "/VAADIN/generated/jar-resources/oda/oda-web.html";

    public OdaWebViewer() {
        setSrc(VIEWER_PATH);
        applyDefaultStyle();
    }

    public OdaWebViewer(String fileUrl) {
        setSrc(VIEWER_PATH + "?url=" + fileUrl);
        applyDefaultStyle();
    }

    private void applyDefaultStyle() {
        getStyle().set("width", "100%");
        getStyle().set("height", "100%");
        getStyle().set("border", "none");
    }
}
