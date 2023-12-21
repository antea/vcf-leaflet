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
package org.vaadin.addons.componentfactory.leaflet.controls;

import org.vaadin.addons.componentfactory.leaflet.LeafletMap;
import org.vaadin.addons.componentfactory.leaflet.controls.LayersControl.LayerControlEventType;
import org.vaadin.addons.componentfactory.leaflet.layer.Layer;
import org.vaadin.addons.componentfactory.leaflet.layer.events.LeafletEvent;

public abstract class LayersControlEvent extends LeafletEvent {

    private static final long serialVersionUID = -4047072025354646461L;
    private String name;

    public LayersControlEvent(LeafletMap source, boolean fromClient, String layerId, LayerControlEventType type, String name) {
        super(source, fromClient, layerId, type);
        this.name = name;
    }

    /**
     * The name of the layer that was added or removed.
     * 
     * @return name of the layer that was added or removed.
     */
    public String getName() {
        return name;
    }

}
