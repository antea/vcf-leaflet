package org.vaadin.addons.componentfactory.leaflet.layer.raster;

import com.vaadin.flow.component.html.IFrame;

/**
 * Embeds the ODA WebAssembly-based DWG/DXF viewer in an IFrame.
 * Optionally pre-loads a file via the {@code ?url=} query parameter.
 */
public class OdaWebViewer extends IFrame {

    private static final long serialVersionUID = 1L;
    private static final String VIEWER_PATH = "/VAADIN/generated/jar-resources/oda/oda-web.html";
    // private static final String FONT_PATH = "https://downloads.smccd.edu/dl?f=https%3A%2F%2Fsmccd.sharepoint.com%2Fsites%2Fdownloads%2Ffacilities%2F_api%2FWeb%2FGetFileByServerRelativePath%28decodedurl%3D%27%2Fsites%2Fdownloads%2Ffacilities%2FFacilities%2520Public%2FReference%2520Plans%2FCSM%2FBldg%252010%2FOriginal%2520Construction%2FMechanical%2FFonts%2Farial.ttf%27%29&n=arial.ttf";
    private static final String FONT_PATH = "/VAADIN/generated/jar-resources/oda/assets/arial.ttf";

    public OdaWebViewer() {
        setSrc(VIEWER_PATH);
        applyDefaultStyle();
    }

    public OdaWebViewer(String fileUrl) {
        setSrc(VIEWER_PATH + "?url=" + fileUrl + "&fontUrl=" + FONT_PATH);
        applyDefaultStyle();
    }

    private void applyDefaultStyle() {
        getStyle().set("width", "100%");
        getStyle().set("height", "100%");
        getStyle().set("border", "none");
    }
}
