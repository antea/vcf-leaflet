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

package org.vaadin.addons.componentfactory.leaflet.layer.groups;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.GeoJsonObject;
import org.geojson.GeometryCollection;
import org.geojson.LineString;
import org.geojson.LngLatAlt;
import org.geojson.MultiLineString;
import org.geojson.MultiPoint;
import org.geojson.MultiPolygon;
import org.geojson.Point;
import org.vaadin.addons.componentfactory.leaflet.layer.HasStyle;
import org.vaadin.addons.componentfactory.leaflet.layer.Identifiable;
import org.vaadin.addons.componentfactory.leaflet.layer.Layer;
import org.vaadin.addons.componentfactory.leaflet.layer.groups.GeoJSONOptions.CoordsToLatLngHandler;
import org.vaadin.addons.componentfactory.leaflet.layer.groups.GeoJSONOptions.OnEachFeatureHandler;
import org.vaadin.addons.componentfactory.leaflet.layer.groups.GeoJSONOptions.PointToLayerHandler;
import org.vaadin.addons.componentfactory.leaflet.layer.groups.GeoJSONOptions.StyleHandler;
import org.vaadin.addons.componentfactory.leaflet.layer.ui.marker.Marker;
import org.vaadin.addons.componentfactory.leaflet.layer.vectors.MultiPolyline;
import org.vaadin.addons.componentfactory.leaflet.layer.vectors.Polygon;
import org.vaadin.addons.componentfactory.leaflet.layer.vectors.Polyline;
import org.vaadin.addons.componentfactory.leaflet.layer.vectors.MultiPolygon.MultiPolygonStructure;
import org.vaadin.addons.componentfactory.leaflet.layer.vectors.structure.LatLngArray;
import org.vaadin.addons.componentfactory.leaflet.layer.vectors.structure.MultiLatLngArray;
import org.vaadin.addons.componentfactory.leaflet.types.LatLng;

/**
 * Represents a GeoJSON object or an array of GeoJSON objects. Allows you to
 * parse GeoJSON data and display it on the map. Extends FeatureGroup.
 * 
 * @author <strong>Gabor Kokeny</strong> Email:
 *         <a href='mailto=kokeny19@gmail.com'>kokeny19@gmail.com</a>
 * @since 2020-03-22
 * @version 1.0
 */
public class GeoJSON extends FeatureGroup implements GeoJSONFunctions {

    private static final long serialVersionUID = -7574772572305688052L;

    private final GeoJSONOptions options;
    private final Map<Identifiable, Feature> layerFeatureMap = new HashMap<>();

    public GeoJSON(GeoJsonObject geoJson) {
        this(geoJson, new GeoJSONOptions());
    }

    public GeoJSON(GeoJsonObject geoJson, GeoJSONOptions options) {
        this.options = options;
        this.addData(geoJson);
    }

    @Override
    public GeoJSON addData(GeoJsonObject geoJsonObject) {
        if (geoJsonObject instanceof FeatureCollection) {
            FeatureCollection featureCollection = (FeatureCollection) geoJsonObject;
            featureCollection.getFeatures().forEach((feature) -> addData(feature));
            return this;
        }

        if (options.filter() != null && !options.filter().test(geoJsonObject)) {
            return this;
        }

        Layer layer = GeoJSON.geometryToLayer(geoJsonObject, options);
        if (layer == null) {
            return this;
        }

        Feature feature = asFeature(geoJsonObject);
        layerFeatureMap.put(layer, feature);

        if (options.style() != null) {
            setLayerStyle(layer, options.style());
        }

        if (options.onEachFeature() != null) {
            OnEachFeatureHandler handler = options.onEachFeature();
            handler.onEachFeature(feature, layer);
        }

        layer.addTo(this);
        return this;
    }

    private void setLayerStyle(Layer layer, StyleHandler styleHandler) {
        if (layer instanceof HasStyle) {
            Feature feature = layerFeatureMap.get(layer);
            ((HasStyle) layer).setStyle(styleHandler.style(feature));
        }
    }

    /**
     * Normalize GeoJSON geometries/features into GeoJSON features.
     * 
     * @param geoJson the
     *            {@link GeoJSON} to be converted as a {@link Feature}
     * @return return a {@link Feature} object which wraps the given {@link GeoJSON}
     */
    public static Feature asFeature(GeoJsonObject geoJson) {
        if (geoJson instanceof Feature) {
            return (Feature) geoJson;
        }
        Feature feature = new Feature();
        feature.setGeometry(geoJson);
        return feature;
    }

    /**
     * Creates a Layer from a given GeoJsonObject object. Can use a custom
     * pointToLayer and/or coordsToLatLng functions if provided as options.
     * 
     * @param geoJsonObject
     *            the geojson to convert
     * @param options
     *            a custom options
     * 
     * @see GeoJSONOptions
     * @return return a {@link Layer} which has been added to this
     *         {@link GeoJSON} layer, the type of the added layer depends on
     *         type of the given {@link GeoJSON}
     */
    public static Layer geometryToLayer(GeoJsonObject geoJsonObject, GeoJSONOptions options) {
        Layer layer = null;

        CoordsToLatLngHandler coordsToLatLng = options.coordsToLatLngHandler() != null ? options.coordsToLatLngHandler() : GeoJSON::coordinateToLatLng;

        if (geoJsonObject instanceof Feature) {
            Feature feature = (Feature) geoJsonObject;
            return geometryToLayer(feature.getGeometry(), options);
        } else if (geoJsonObject instanceof Point) {
            Point point = (Point) geoJsonObject;
            LngLatAlt coordinates = point.getCoordinates();
            LatLng latLng = new LatLng(coordinates.getLatitude(), coordinates.getLongitude());
            layer = pointToLayer(options.pointToLayer(), point, latLng);
        } else if (geoJsonObject instanceof LineString) {
            LineString lineString = (LineString) geoJsonObject;
            List<LatLng> latLngs = lineString.getCoordinates().stream().map(coordsToLatLng::convert).collect(Collectors.toList());
            layer = new Polyline(latLngs);
        } else if (geoJsonObject instanceof MultiPoint) {
            MultiPoint multiPoint = (MultiPoint) geoJsonObject;
            FeatureGroup featureGroup = new FeatureGroup();
            multiPoint.getCoordinates().stream().map(coordsToLatLng::convert).map((latLng) -> pointToLayer(options.pointToLayer(), multiPoint, latLng)).forEach((l) -> l.addTo(featureGroup));
            layer = featureGroup;
        } else if (geoJsonObject instanceof MultiLineString) {
            MultiLineString multiLineString = (MultiLineString) geoJsonObject;
            MultiLatLngArray latLngs = multiCoordinateToLatLng(multiLineString.getCoordinates(), coordsToLatLng);
            layer = new MultiPolyline(latLngs);
        } else if (geoJsonObject instanceof org.geojson.Polygon) {
            org.geojson.Polygon polygon = (org.geojson.Polygon) geoJsonObject;
            List<LatLng> exteriorLatlngs = polygon.getExteriorRing().stream().map(coordsToLatLng::convert).collect(Collectors.toList());
            MultiLatLngArray interiorLatLngs = multiCoordinateToLatLng(polygon.getInteriorRings(), coordsToLatLng);
            layer = new Polygon(exteriorLatlngs, interiorLatLngs);
        } else if (geoJsonObject instanceof MultiPolygon) {
            MultiPolygon multiPolygon = (MultiPolygon) geoJsonObject;

            MultiPolygonStructure multiPolygonStructure = new MultiPolygonStructure();
            for (List<List<LngLatAlt>> polygonCoordinates : multiPolygon.getCoordinates()) {
                multiPolygonStructure.add(multiCoordinateToLatLng(polygonCoordinates, coordsToLatLng));
            }
            layer = new org.vaadin.addons.componentfactory.leaflet.layer.vectors.MultiPolygon(multiPolygonStructure);
        } else if (geoJsonObject instanceof GeometryCollection) {
            FeatureGroup geometryFeatureGroup = new FeatureGroup();
            GeometryCollection geometryCollection = (GeometryCollection) geoJsonObject;
            geometryCollection.getGeometries().stream().map((geometry) -> geometryToLayer(geometry, options)).forEach(geometryLayer -> geometryLayer.addTo(geometryFeatureGroup));
            layer = geometryFeatureGroup;
        }

        return layer;
    }

    private static MultiLatLngArray multiCoordinateToLatLng(List<List<LngLatAlt>> multiLngLatAlts, CoordsToLatLngHandler coordsToLatLng) {
        MultiLatLngArray multiLatLngArray = new MultiLatLngArray();
        for (List<LngLatAlt> coords : multiLngLatAlts) {
            List<LatLng> latLngs = coords.stream().map(coordsToLatLng::convert).collect(Collectors.toList());
            multiLatLngArray.add(new LatLngArray(latLngs));
        }
        return multiLatLngArray;
    }

    public static LatLng coordinateToLatLng(LngLatAlt latAlt) {
        return new LatLng(latAlt.getLatitude(), latAlt.getLongitude());
    }

    private static Layer pointToLayer(PointToLayerHandler pointToLayerHandler, GeoJsonObject geoJson, LatLng latLng) {
        if (pointToLayerHandler != null) {
            return pointToLayerHandler.pointToLayer(geoJson, latLng);
        } else {
            return new Marker(latLng); // TODO inherit marker options
        }
    }
}
