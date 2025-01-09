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

package org.vaadin.addons.componentfactory.leaflet.layer.events.supports;

import static org.vaadin.addons.componentfactory.leaflet.layer.events.types.LayerEventType.layeradd;
import static org.vaadin.addons.componentfactory.leaflet.layer.events.types.LayerEventType.layerremove;

import org.vaadin.addons.componentfactory.leaflet.layer.events.*;

public interface SupportsLayerEvents extends Evented {

	/**
	 * Fired when a layer is added to this layer
	 * 
	 * @param listener the listener to call when the event occurs, not {@code null}
	 */
	default void onLayerAdd(LeafletEventListener<LayerAddEvent> listener) {
		on(layeradd, listener);
	}

	/**
	 * Fired when a layer is removed from this layer
	 * 
	 * @param listener the listener to call when the event occurs, not {@code null}
	 */
	default void onLayerRemove(LeafletEventListener<LayerRemoveEvent> listener) {
		on(layerremove, listener);
	}
}
