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

package org.vaadin.addons.componentfactory.leaflet.plugins.markercluster;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import org.vaadin.addons.componentfactory.leaflet.layer.vectors.PathOptions;

/**
 * Possible {@link MarkerClusterGroup} options
 * 
 * <ul>
 * <li><b>minOpacity</b> -</li>
 * </ul>
 * *
 * 
 * @author <strong>Gabor Kokeny</strong> Email:
 *         <a href='mailto=kokeny19@gmail.com'>kokeny19@gmail.com</a>
 * @since 2020-05-25
 * @version 1.0
 * @see MarkerClusterGroup
 */
@Setter
@Getter
public class MarkerClusterOptions implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 6006827958359411349L;

    private int maxClusterRadius = 80;
    private String clusterPane = "markerPane";

    private boolean spiderfyOnMaxZoom = true;
    private boolean showCoverageOnHover = true;
    private boolean zoomToBoundsOnClick = true;
    private boolean singleMarkerMode = false;

    private Integer disableClusteringAtZoom = null;
    private boolean removeOutsideVisibleBounds = true;
    private boolean animate = true;
    private boolean animateAddingMarkers = false;
    private double spiderfyDistanceMultiplier = 1;
    private PathOptions spiderLegPolylineOptions;

    private boolean chunkedLoading = false;
    private int chunkInterval = 200;
    private int chunkDelay = 50;
    private PathOptions polygonOptions;

    private String iconCreateFunction;
}
