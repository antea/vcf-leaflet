package org.vaadin.addons.componentfactory.leaflet.plugins.print;

import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import lombok.Getter;
import lombok.Setter;
import org.vaadin.addons.componentfactory.leaflet.annotations.LeafletArgument;
import org.vaadin.addons.componentfactory.leaflet.controls.LeafletControl;

// dom-to-image and file-saver are both required by easyPrint.
// Since we do not import easyPrint via npm, but we create our version, adapting the original,
// any required dependency of easyPrint needs to be required this way.
@NpmPackage(value = "dom-to-image", version = "^2.5.2")
@NpmPackage(value = "file-saver", version = "^1.3.3")
@JsModule("./vcf-easyprint.js")
@Getter
@Setter
public class EasyPrintControl extends LeafletControl {

    @LeafletArgument
    private EasyPrintOptions easyPrintOptions;

    public EasyPrintControl(EasyPrintOptions easyPrintOptions) {
        super("EasyPrint");
        this.easyPrintOptions = easyPrintOptions;
    }

    public EasyPrintControl() {
        this(new EasyPrintOptions());
    }
}
