package org.vaadin.addons.componentfactory.leaflet.layer.events;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.DomEvent;
import com.vaadin.flow.component.EventData;
import lombok.Getter;
import org.vaadin.addons.componentfactory.leaflet.LeafletMap;

@Getter
@DomEvent("pm:globaleditmodetoggled")
public class ClientGeomanEditToggleEvent extends ComponentEvent<LeafletMap> {
    private final boolean enabled;

    public ClientGeomanEditToggleEvent(LeafletMap source, boolean fromClient,
            @EventData("event.detail.enabled") boolean enabled) {
        super(source, fromClient);
        this.enabled = enabled;
    }
}