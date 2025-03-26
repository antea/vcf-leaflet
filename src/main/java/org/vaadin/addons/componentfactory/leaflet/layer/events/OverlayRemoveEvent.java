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
import org.vaadin.addons.componentfactory.leaflet.LeafletMap;
import org.vaadin.addons.componentfactory.leaflet.controls.LayersControlEvent;
import org.vaadin.addons.componentfactory.leaflet.controls.LayersControl.LayerControlEventType;

@DomEvent("overlayremove")
public class OverlayRemoveEvent extends LayersControlEvent {

  private static final long serialVersionUID = 5509563423863560531L;

  private final String removedLayerId;

  public OverlayRemoveEvent(LeafletMap source, boolean fromClient,
      @EventData("event.detail.target.options.uuid") String layerId,
      @EventData("event.detail.name") String name,
      @EventData("event.detail.layer.options.uuid") String removedLayerId) {
    super(source, fromClient, layerId, LayerControlEventType.overlayremove, name);
    this.removedLayerId = removedLayerId;
  }

  /**
   *
   * @return The uuid of the removed layer, pay attention that this is the actual UUID of the removed layer since
   * {@link layerId} is the UUID of the leafletmap.
   */
  public String getRemovedLayerId() {
    return removedLayerId;
  }
}
