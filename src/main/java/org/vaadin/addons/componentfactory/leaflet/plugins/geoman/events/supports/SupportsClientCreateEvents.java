package org.vaadin.addons.componentfactory.leaflet.plugins.geoman.events.supports;

import org.vaadin.addons.componentfactory.leaflet.layer.events.Evented;
import org.vaadin.addons.componentfactory.leaflet.layer.events.LeafletEventListener;
import org.vaadin.addons.componentfactory.leaflet.plugins.geoman.events.ClientLayerAddEvent;
import org.vaadin.addons.componentfactory.leaflet.plugins.geoman.events.types.EditEventType;

public interface SupportsClientCreateEvents extends Evented {
    /**
     * Fired when a shape is drawn/finished. Payload includes shape type and the drawn layer.
     *
     * @param listener the listener to call when the event occurs, not {@code null}
     */
    default void onCreate(LeafletEventListener<ClientLayerAddEvent> listener) {
        on(EditEventType.create, listener);
    }
}
