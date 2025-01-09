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
package org.vaadin.addons.componentfactory.leaflet.layer.events;

import com.vaadin.flow.component.DomEvent;
import com.vaadin.flow.component.EventData;
import lombok.Getter;
import org.vaadin.addons.componentfactory.leaflet.LeafletMap;
import org.vaadin.addons.componentfactory.leaflet.layer.events.types.LayerEventType;

@Getter
@DomEvent("layeradd")
public class LayerAddEvent extends LayerEvent {

    private static final long serialVersionUID = 1541515694999130479L;
    // id of the new Layer, assigned by Leaflet
    private final String newLayerLeafletId;

    public LayerAddEvent(LeafletMap source, boolean fromClient,
            @EventData("event.detail.target.options.uuid") String targetLayerId,
            @EventData("event.detail.layer._leaflet_id") String createdLayerId) {
        super(source, fromClient, targetLayerId, LayerEventType.layeradd, null);
        this.newLayerLeafletId = createdLayerId;
    }
}
