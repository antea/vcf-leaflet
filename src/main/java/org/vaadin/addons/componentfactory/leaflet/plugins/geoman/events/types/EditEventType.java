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

package org.vaadin.addons.componentfactory.leaflet.plugins.geoman.events.types;

import org.vaadin.addons.componentfactory.leaflet.layer.events.types.LeafletEventType;

public enum EditEventType implements LeafletEventType {

    /**
     * Fired when a shape is drawn/finished. Payload includes shape type and the drawn layer.
     * <a href="https://geoman.io/docs/leaflet/modes/draw-mode">Draw mode</a>
     */
    create,
    /**
     * Fired when a shape is deleted. Payload includes shape type and the deleted layer.
     * <a href="https://geoman.io/docs/leaflet/modes/removal-mode">Removal mode</a>
     */
    remove,
    /**
     * Fired when a shape is dragged or modified. Payload include shape, latlngs
     */
    change;

    @Override
    public String getLeafletEvent() {
        return "pm:" + name();
    }
}
