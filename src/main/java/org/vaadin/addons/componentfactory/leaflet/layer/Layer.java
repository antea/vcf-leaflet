// Copyright 2020 Gabor Kokeny and contributors
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
//     http://www.apache.org/licenses/LICENSE-2.0
// 
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.vaadin.addons.componentfactory.leaflet.layer;

import lombok.Getter;
import lombok.Setter;
import org.vaadin.addons.componentfactory.leaflet.LeafletMap;
import org.vaadin.addons.componentfactory.leaflet.LeafletObject;
import org.vaadin.addons.componentfactory.leaflet.layer.events.*;
import org.vaadin.addons.componentfactory.leaflet.layer.events.types.LeafletEventType;
import org.vaadin.addons.componentfactory.leaflet.layer.events.types.PopupEventType;
import org.vaadin.addons.componentfactory.leaflet.layer.events.types.TooltipEventType;
import org.vaadin.addons.componentfactory.leaflet.layer.groups.LayerGroup;
import org.vaadin.addons.componentfactory.leaflet.layer.map.functions.ExecutableFunctions;
import org.vaadin.addons.componentfactory.leaflet.layer.ui.popup.Popup;
import org.vaadin.addons.componentfactory.leaflet.layer.ui.tooltip.Tooltip;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * A set of methods from the Layer base class that all Leaflet layers use.
 * Inherits all methods, options and events from L.Evented.
 * @author <strong>Gabor Kokeny</strong> Email:
 * <a href='mailto=kokeny19@gmail.com'>kokeny19@gmail.com</a>
 * @version 1.0
 * @since 2020-02-06
 */
public abstract class Layer extends LeafletObject implements Evented, LayerPopupFunctions, LayerTooltipFunctions {

    private static final long serialVersionUID = -1803411573095089760L;

    public static enum LayerEventType implements LeafletEventType {
        add, remove;
    }

    private transient final Map<LeafletEventType, Set<LeafletEventListener<? extends LeafletEvent>>> eventListeners = new HashMap<>();

    public static final String DEFAULT_PANE = "overlayPane";
    @Setter
    @Getter
    private String pane = DEFAULT_PANE;
    @Setter
    @Getter
    private String attribution;
    private Popup popup;
    @Getter
    private Tooltip tooltip;
    /**
     * Event types managed by this layer
     */
    @Getter
    private List<String> events = new ArrayList<>();

    protected Layer() {
    }

    @Override
    public void unbindTooltip() {
        LayerTooltipFunctions.super.unbindTooltip();
        this.tooltip = null;
    }

    @Override
    public void unbindPopup() {
        LayerPopupFunctions.super.unbindPopup();
        this.popup = null;
    }

    /**
     * Fired after the layer is added to a map
     * @param listener the event listener
     */
    public void onAdd(LeafletEventListener<LeafletEvent> listener) {
        on(LayerEventType.add, listener);
    }

    /**
     * Fired after the layer is removed from a map
     * @param listener the event listener
     */
    public void onRemove(LeafletEventListener<LeafletEvent> listener) {
        on(LayerEventType.remove, listener);
    }

    /**
     * Fired when a tooltip bound to this layer is opened.
     * @param listener the event listener
     */
    public void onTooltipOpen(LeafletEventListener<TooltipEvent> listener) {
        on(TooltipEventType.tooltipopen, listener);
    }

    /**
     * Fired when a tooltip bound to this layer is closed.
     * @param listener the event listener
     */
    public void onTooltipClose(LeafletEventListener<TooltipEvent> listener) {
        on(TooltipEventType.tooltipclose, listener);
    }

    /**
     * Fired when a popup bound to this layer is opened
     * @param listener the event listener
     */
    public void onPopupOpen(LeafletEventListener<PopupEvent> listener) {
        on(PopupEventType.popupopen, listener);
    }

    /**
     * Fired when a popup bound to this layer is closed
     * @param listener the event listener
     */
    public void onPopupClose(LeafletEventListener<PopupEvent> listener) {
        on(PopupEventType.popupclose, listener);
    }

    @SuppressWarnings("unchecked")
    public <T extends LeafletEvent> void fireEvent(T leafletEvent) {
        Optional<LeafletEventType> event = eventListeners.keySet().stream().filter(type -> type.equals(leafletEvent.getType())).findFirst();

        event.ifPresent(leafletEventType ->
                eventListeners.get(leafletEventType)
                        .forEach(listener ->
                                ((LeafletEventListener<T>) listener).handleEvent(leafletEvent)));
    }

    /**
     * Adds the layer to the given layer group
     * @param layerGroup the layer group
     */
    public void addTo(LayerGroup layerGroup) {
        setParent(layerGroup);
        layerGroup.addLayer(this);
    }

    /**
     * Adds the layer to the given map
     * @param leafletMap the leaflet map
     */
    public void addTo(LeafletMap leafletMap) {
        setParent(leafletMap);
        leafletMap.addLayer(this);
    }

    @Override
    public Popup getPopup() {
        return this.popup;
    }

    public void bindPopup(String popupContent) {
        bindPopup(new Popup(popupContent));
    }

    public void bindPopup(Popup popup) {
        popup.setParent(this);
        this.popup = popup;
    }

    public void bindTooltip(String tooltipContent) {
        bindTooltip(new Tooltip(tooltipContent));
    }

    public void bindTooltip(Tooltip tooltip) {
        tooltip.setParent(this);
        this.tooltip = tooltip;
    }

    public <T> void set(Supplier<CompletableFuture<T>> futureResult, Consumer<T> handler) {
        if (futureResult.get() != null) {
            futureResult.get().thenAccept(handler);
        }
    }

    @Override
    public <T extends LeafletEvent> void addEventListener(LeafletEventType eventType, LeafletEventListener<T> listener) {
        if (!events.contains(eventType.getLeafletEvent())) {
            // if this layer was already added to the map, and the map did not register a
            // listener for these kind of events, now we tell the map to start listening for them
            Optional.ofNullable(findLeafletMap(this))
                    .ifPresent(map -> doRegisterEventListener(this, map, eventType));
            events.add(eventType.getLeafletEvent());
        }
        Set<LeafletEventListener<?>> listeners = eventListeners.get(eventType);
        if (listeners == null) {
            listeners = new HashSet<>();
            eventListeners.putIfAbsent(eventType, listeners);
        }
        listeners.add(listener);
    }

    protected void doRegisterEventListener(Identifiable target, LeafletMap leafletMap, LeafletEventType eventType) {
        leafletMap.executeGeneralJs(target, "registerMapEventListener", eventType.getLeafletEvent());
    }

    @Override
    public boolean removeEventListener(LeafletEventType eventType, LeafletEventListener<?> listener) {
        Set<LeafletEventListener<?>> listeners = this.eventListeners.get(eventType);
        if (listeners != null) {
            boolean remove = listeners.remove(listener);
            if (listeners.isEmpty()) {
                removeEventListener(eventType);
            }
            return remove;
        }
        return false;
    }

    @Override
    public void clearAllEventListeners() {
        this.eventListeners.clear();
        this.events.clear();
        executeJs(this, "clearAllEventListeners");
    }

    @Override
    public boolean hasEventListeners(LeafletEventType eventType) {
        Set<LeafletEventListener<?>> listeners = this.eventListeners.get(eventType);
        return listeners != null && !listeners.isEmpty();
    }

    @Override
    public void removeEventListener(LeafletEventType eventType) {
        this.eventListeners.remove(eventType);
        this.events.remove(eventType.getLeafletEvent());
        executeJs(this, "removeEventListener", eventType.getLeafletEvent());
    }

    /**
     * Removes the layer from the map it is currently active on
     */
    public void remove() {
        if (getParent() instanceof LeafletMap) {
            LeafletMap map = (LeafletMap) getParent();
            map.removeLayer(this);
        } else if (getParent() instanceof LayerGroup) {
            LayerGroup parentLayerGroup = (LayerGroup) getParent();
            parentLayerGroup.removeLayer(this);
        }
    }

    private LeafletMap findLeafletMap(Layer layer) {
        ExecutableFunctions parent = layer == null
                ? null
                : layer.getParent();

        if (parent instanceof LeafletMap) {
            return (LeafletMap) parent;
        }

        if (parent instanceof Layer) {
            return findLeafletMap((Layer) parent);
        }

        return null;
    }
}
