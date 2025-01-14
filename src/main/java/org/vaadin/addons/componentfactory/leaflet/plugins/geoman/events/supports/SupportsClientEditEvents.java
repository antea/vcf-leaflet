package org.vaadin.addons.componentfactory.leaflet.plugins.geoman.events.supports;

import org.vaadin.addons.componentfactory.leaflet.layer.events.Evented;
import org.vaadin.addons.componentfactory.leaflet.layer.events.LeafletEventListener;
import org.vaadin.addons.componentfactory.leaflet.plugins.geoman.events.ClientLayerUpdateInMapEvent;
import org.vaadin.addons.componentfactory.leaflet.plugins.geoman.events.types.EditEventType;

/**
 * This interface represent the listeners for editing
 */
public interface SupportsClientEditEvents extends Evented {
    /**
     * Fired when a shape is edited by the user. Payload includes shape type and the drawn layer id.
     *
     * @param listener the listener to call when the event occurs, not {@code null}
     */
    default void onLayerUpdated(LeafletEventListener<ClientLayerUpdateInMapEvent> listener) {
        on(EditEventType.layerupdate, listener);
    }
}
