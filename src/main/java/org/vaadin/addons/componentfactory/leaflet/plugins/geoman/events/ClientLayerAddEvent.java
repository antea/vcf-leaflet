/*-
 * #%L
 * Leaflet
 * %%
 * Copyright (C) 2023 Vaadin Ltd
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

/*
 * This file incorporates work licensed under the Apache License, Version 2.0
 * Copyright 2020 Gabor Kokeny and contributors
 */
package org.vaadin.addons.componentfactory.leaflet.plugins.geoman.events;

import com.fasterxml.jackson.core.type.TypeReference;
import com.vaadin.flow.component.DomEvent;
import com.vaadin.flow.component.EventData;
import elemental.json.JsonValue;
import lombok.Getter;
import lombok.Setter;
import org.vaadin.addons.componentfactory.leaflet.LeafletMap;
import org.vaadin.addons.componentfactory.leaflet.layer.Layer;
import org.vaadin.addons.componentfactory.leaflet.plugins.geoman.events.types.EditEventType;
import org.vaadin.addons.componentfactory.leaflet.plugins.geoman.events.types.ShapeType;
import org.vaadin.addons.componentfactory.leaflet.types.LatLng;

import java.util.List;

import static org.vaadin.addons.componentfactory.leaflet.plugins.geoman.GeomanUtils.readValue;

/**
 * This class represents the event that is raised when a Layer is drawn by the client side using Geoman toolbar
 * Fired when a layer is created via <a href="https://geoman.io/docs/leaflet/modes/draw-mode">Draw Mode</a>.
 */
@Getter
@DomEvent("pm:create")
public class ClientLayerAddEvent extends BaseClientLayerEvent {

    // if a Marker or Text or CircleMarker or a Circle was created, this is its coordinates
    private final LatLng latLng;

    private final Double radius;
    // Line
    private final List<LatLng> lineLatLngs;

    // Rectangle
    private final List<List<LatLng>> rectangleLatLngs;

    @Setter
    private transient Layer child;

    public ClientLayerAddEvent(LeafletMap source, boolean fromClient,
            @EventData("event.detail.target.options.uuid") String targetLayerId,
            // id of the new Layer, assigned by Leaflet
            @EventData("event.detail.layer._leaflet_id") String createdLayerId,
            @EventData("event.detail.layer._latlng") JsonValue latLng,
            @EventData("event.detail.layer._latlngs") JsonValue latLngs,
            @EventData("event.detail.layer._radius") Double radius,
            @EventData("event.detail.shape") String shape) {
        super(source, fromClient, EditEventType.create, targetLayerId, createdLayerId, ShapeType.ofGeomanShape(shape));
        this.radius = radius;
        this.latLng = readValue(latLng, new TypeReference<>() {});
        this.lineLatLngs = readValue(latLngs, new TypeReference<>() {});
        this.rectangleLatLngs = readValue(latLngs, new TypeReference<>() {});
    }

    public String getNewLayerId() {
        return getEditedLayerId();
    }
}
