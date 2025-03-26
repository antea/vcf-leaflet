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
import org.vaadin.addons.componentfactory.leaflet.controls.LayersControlEvent;
import org.vaadin.addons.componentfactory.leaflet.controls.LayersControl.LayerControlEventType;

@DomEvent("overlayadd")
public class OverlayAddEvent extends LayersControlEvent {

  private static final long serialVersionUID = 3295607427378773699L;

  private final String addedLayerId;

  public OverlayAddEvent(LeafletMap source, boolean fromClient,
      @EventData("event.detail.target.options.uuid") String layerId,
      @EventData("event.detail.name") String name,
      @EventData("event.detail.layer.options.uuid") String addedLayerId) {
    super(source, fromClient, layerId, LayerControlEventType.overlayadd, name);
    this.addedLayerId = addedLayerId;
  }

  /**
   *
   * @return The uuid of the added layer, pay attention that this is the actual UUID of the added layer since
   * {@link layerId} is the UUID of the leafletmap.
   */
  public String getAddedLayerId() {
    return addedLayerId;
  }
}
