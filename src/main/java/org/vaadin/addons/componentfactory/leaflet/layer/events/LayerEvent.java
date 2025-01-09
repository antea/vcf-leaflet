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
package org.vaadin.addons.componentfactory.leaflet.layer.events;

import lombok.Getter;
import lombok.Setter;
import org.vaadin.addons.componentfactory.leaflet.LeafletMap;
import org.vaadin.addons.componentfactory.leaflet.layer.Layer;
import org.vaadin.addons.componentfactory.leaflet.layer.events.types.LayerEventType;

/**
 * Represents the Leaflet LayerEevent
 * 
 * @author <strong>Gabor Kokeny</strong> Email:
 *         <a href='mailto=kokeny19@gmail.com'>kokeny19@gmail.com</a>
 * 
 * @since 2020-03-14
 * @version 1.0
 */
@Setter
@Getter
public abstract class LayerEvent extends LeafletEvent {

  private static final long serialVersionUID = 6978303855963077536L;
    /**
     * -- GETTER --
     *  The layer that was added or removed.
     *
     * @return the child
     */
    private Layer child;

  public LayerEvent(LeafletMap source, boolean fromClient, String layerId, LayerEventType eventType,
      Layer child) {
    super(source, fromClient, layerId, eventType);
  }

    @Override
  public String toString() {
    return "LayerEvent [type=" + super.getType() + ", child=" + child + "]";
  }

}
