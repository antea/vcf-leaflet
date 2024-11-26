/*
 *   Copyright (c) 2020 Gabor Kokeny
 *   All rights reserved.

 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at

 *   http://www.apache.org/licenses/LICENSE-2.0

 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

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
export class LeafletTypeConverter {

  constructor(L) {
    this.L = L;
    this.basicTypes = [
      "Point",
      "Bounds",
      "LatLng",
      "LatLngBounds",
      "Icon",
      "DivIcon",
    ];
  }

  toLeafletLayer(layer, map) {
    let leafletLayer;

    if (map.hasLayer(layer.uuid)) {
      return map.getLayer(layer.uuid);
    }

    if (layer.icon) {
      layer.icon = this.convert(layer.icon);
    } else if (layer.layers) {
      layer.layers = layer.layers
        .slice()
        .map((childLayer) => this.toLeafletLayer(childLayer, map));
    }

    if (layer.leafletType === "GeoJSON") {
      leafletLayer = this.L.geoJSON(null, layer);
      leafletLayer._layers = layer.layers;
    } else {
      let factoryFn = this.getLayerFactoryFn(layer.leafletType);
      if (layer.constructorArgumentNames) {
        //console.log("LeafletTypeConverter - toLeafletLayer() constructorArgumentNames", layer.constructorArgumentNames);
        let initArgs = layer.constructorArgumentNames.map((argName) =>
          this.convert(layer[argName], map)
        );
        initArgs.push(layer);
        //console.log("LeafletTypeConverter - toLeafletLayer() initArgs", initArgs);
        leafletLayer = factoryFn.apply(null, initArgs);
      } else {
        leafletLayer = factoryFn.call(layer);
      }
    }

    this._applyOptions(leafletLayer, layer);

    //apply layer event listeners
    if (layer.events) {
      layer.events.forEach((event) =>
        map.registerEventListener(leafletLayer, event)
      );
    }
    // console.log("LeafletTypeConverter - toLeafletLayer() result", leafletLayer);
    return leafletLayer;
  }

  getLayerFactoryFn(leafletType) {
    return L[leafletType.charAt(0).toLowerCase() + leafletType.slice(1)];
  }

  getControlFactoryFn(leafletType) {
    return this.L.control[leafletType];
  }

  toLeafletControl(control, mapTemplate) {
    let leafletControl = control;
    if (control) {
      let controlFn = this.getControlFactoryFn(control.leafletType);
      if (!controlFn) {
        throw "Unsupported control type : " + control.leafletType;
      }
      if (control.constructorArgumentNames) {
        //console.log("LeafletTypeConverter - toLeafletControl() constructorArgumentNames", control.constructorArgumentNames);
        let initArgs = control.constructorArgumentNames.map((argName) =>
          this.convert(control[argName], mapTemplate)
        );
        initArgs.push(control);
        //console.log("LeafletTypeConverter - toLeafletControl() initArgs", initArgs);
        leafletControl = controlFn.apply(null, initArgs);
      } else {
        leafletControl = controlFn(control);
      }
      mapTemplate.map._controls[control.uuid] = leafletControl;
      /*console.log(
        "LeafletTypeConverter - toLeafletControl() result",
        leafletControl
      );*/
    }
    return leafletControl;
  }

  convert(object, map) {
    let converted = object;
    if (this.isLeafletType(object)) {
      if (this.isBasicType(object.leafletType)) {
        converted = this.convertBasicType(object);
      } else if (this.isLeafletLayer(object.leafletType)) {
        converted = this.toLeafletLayer(object, map);
      } else if (this.isLeafletControl(object.leafletType)) {
        converted = this.toLeafletControl(object, map);
      }
      //Override the internal leaflet id
      if (object.uuid) {
        converted._leaflet_id = object.uuid;
      }
    } else if (object instanceof Object) {
      Object.keys(object).forEach((key, index) => {
        if (this.isLeafletType(object[key])) {
          converted[key] = this.convert(object[key], map);
        }
      });
    }

    //console.log("LeafletTypeConverter - convert() result", converted);
    return converted;
  }

  convertBasicType(basicType) {
    let converted = basicType;
    if (basicType.leafletType === "Point") {
      converted = this.toPoint(basicType);
    } else if (basicType.leafletType === "Bounds") {
      converted = this.toBounds(basicType);
    } else if (basicType.leafletType === "LatLng") {
      converted = this.toLatLng(basicType);
    } else if (basicType.leafletType === "LatLngBounds") {
      converted = this.toLatLngBounds(basicType);
    } else if (basicType.leafletType === "Icon") {
      converted = this.toIcon(basicType);
    } else if (basicType.leafletType === "DivIcon") {
      converted = this.toDivIcon(basicType);
    }
    //console.log("LeafletTypeConverter - convertBasicType() result", converted);
    return converted;
  }

  isLeafletType(object) {
    return object && typeof object.leafletType !== "undefined";
  }

  isBasicType(object) {
    return this.basicTypes.indexOf(object) >= 0;
  }

  isLeafletControl(object) {
    return this.getControlFactoryFn(object) != null;
  }

  isLeafletLayer(object) {
    return this.getLayerFactoryFn(object) != null;
  }

  /**
   * Convert the given JsonObject to Leaflet Icon
   */
  toIcon(iconOptions) {
    iconOptions.popupAnchor = iconOptions.popupAnchor ? [iconOptions.popupAnchor.x, iconOptions.popupAnchor.y] : null;
    iconOptions.tooltipAnchor = iconOptions.tooltipAnchor ? [iconOptions.tooltipAnchor.x, iconOptions.tooltipAnchor.y] : null;
    iconOptions.shadowAnchor = iconOptions.shadowAnchor ?[iconOptions.shadowAnchor.x, iconOptions.shadowAnchor.y] : null;
    iconOptions.iconAnchor = iconOptions.iconAnchor ? [iconOptions.iconAnchor.x, iconOptions.iconAnchor.y] : null;
    iconOptions.iconSize = iconOptions.iconSize ? [iconOptions.iconSize.x, iconOptions.iconSize.y] : null;
    iconOptions.shadowSize = iconOptions.shadowSize ? [iconOptions.shadowSize.x, iconOptions.shadowSize.y] : null;
    return this.L.icon(iconOptions);
  }

  /**
   * Convert the given JsonObject to Leaflet DivIcon
   */
  toDivIcon(divIcon) {
    return this.L.divIcon(divIcon);
  }

  /**
   * Convert the given JsonObject to Leaflet Point
   */
  toPoint(point) {
    return point ? this.L.point(point.x, point.y) : point;
  }

  /**
   * Convert the given JsonObject to Leaflet LatLng
   */
  toLatLng(latLng) {
    return latLng ? this.L.latLng(latLng.lat, latLng.lng, latLng.altitude) : latLng;
  }

  /**
   * Convert the given JsonObject to Leaflet Bounds
   */
  toBounds(bounds) {
    let corner1 = this.L.point(bounds.topLeft.x, bounds.topLeft.y);
    let corner2 = this.L.point(bounds.bottomRight.x, bounds.bottomRight.y);
    return this.L.bounds(corner1, corner2);
  }

  /**
   * Convert the given JsonObject to Leaflet LatLngBounds
   */
  toLatLngBounds(bounds) {
    let corner1 = this.L.latLng(bounds._northEast);
    let corner2 = this.L.latLng(bounds._southWest);
    return this.L.latLngBounds(corner1, corner2);
  }

  _applyOptions(layer, options) {
    if (options.tooltip) {
      let leafletTooltip = this.L.tooltip(options.tooltip).setContent(
        options.tooltip.content
      );
      //console.log("LeafletConverter - binding tooltip to layer",leafletTooltip);
      layer.bindTooltip(leafletTooltip);
    }
    if (options.popup) {
      let leafletPopup = this.L.popup(options.popup).setContent(
        options.popup.content
      );
      //console.log("LeafletConverter - binding popup to layer", leafletPopup);
      layer.bindPopup(leafletPopup);
    }
  }


    /**
     * This method trys to set the crs:
     * it looks for the customCrs field in the map and uses is to add a new crs
     * otherwise it looks for crsName, this one supports only the standard Crs ones.
     * @param mapOptions the map options to create this map.
     */
    tryToSetCrs(mapOptions) {
        if (mapOptions["customSimpleCrs"]) {
            mapOptions.crs = this.addSimpleCustomCrs(mapOptions["customSimpleCrs"]);
        } else {
            switch (mapOptions["crsName"]) {
                case "EPSG3395":
                case "EPSG4326":
                case "Simple":
                    mapOptions.crs = this.L.CRS[mapOptions["crsName"]];
            }
        }
        return mapOptions;

    }

    addSimpleCustomCrs(customCrs) {
        return this.addCustomSimpleCrsFrom(customCrs.name, customCrs.min_x, customCrs.min_y, customCrs.max_x, customCrs.max_y,
            customCrs.a, customCrs.b, customCrs.c, customCrs.d)
    }
    /**
     * Adds a new Crs definition and makes it immediately available for use inside a Map. The
     * new Crs extends Crs.Simple, uses a plat-carré projection with the
     * extents given by the min_* and max_* parameters. For the meaning of the
     * affine transform parameters, see: http://leafletjs.com/reference.html#transformation.
     * @param name Name for the new Crs.
     * @param min_x the min_x value
     * @param min_y the min_y value
     * @param max_x the max_x value
     * @param max_y the max_y value
     * @param a a in transformation calculation (a*x + b, c*y + d)
     * @param b b in transformation calculation (a*x + b, c*y + d)
     * @param c c in transformation calculation (a*x + b, c*y + d)
     * @param d d in transformation calculation (a*x + b, c*y + d)
     * @returns {*} a new Crs object, and add it to leaflet map
     */
    addCustomSimpleCrsFrom(name, min_x, min_y, max_x, max_y, a, b, c, d) {
        let thisConverter = this
        let projection = {
            project: function (latlng) {
                return new thisConverter.L.Point(latlng.lng, latlng.lat);
            },
            unproject: function (point) {
                return new thisConverter.L.LatLng(point.y, point.x);
            },
            bounds: thisConverter.L.bounds([min_x, min_y], [max_x, max_y])
        };

        this.L.CRS[name] = this.L.extend({}, this.L.CRS, {
            code: name,
            projection: projection,
            transformation: new this.L.Transformation(a, b, c, d),
            distance: function (t, e) {
                let i = e.lng - t.lng, n = e.lat - t.lat;
                return Math.sqrt(i * i + n * n)
            }
        });
        return this.L.CRS[name];
    }
}
