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

import org.vaadin.addons.componentfactory.leaflet.LeafletMap;
import org.vaadin.addons.componentfactory.leaflet.layer.events.types.DragEventType;
import org.vaadin.addons.componentfactory.leaflet.types.LatLng;

public abstract class BaseMoveEvent extends LeafletEvent {

  private static final long serialVersionUID = -7059868738932322793L;
  private final LatLng oldLatLng;
  private final LatLng latLng;

  public BaseMoveEvent(LeafletMap source, boolean fromClient, String layerId,
      DragEventType eventType, LatLng oldLatLng, LatLng latLng) {
    super(source, fromClient, layerId, eventType);
    this.oldLatLng = oldLatLng;
    this.latLng = latLng;
  }

  /**
   * @return the new coordinates
   */
  public LatLng getLatLng() {
    return latLng;
  }

  /**
   * @return the old coordinates
   */
  public LatLng getOldLatLng() {
    return oldLatLng;
  }

  @Override
  public String toString() {
    return "MoveEvent [type=" + super.getType() + ",latLng=" + latLng + ", oldLatLng=" + oldLatLng
        + "]";
  }



}
