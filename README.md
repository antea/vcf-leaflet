# Leaflet for Vaadin

Leaflet for Vaadin component provides a JAVA API for [Leaflet maps library](https://github.com/Leaflet/Leaflet).

This component is based on [leaflet4vaadin](https://github.com/Gubancs/leaflet4vaadin).

## Features
- map configuration
- interactive layers
- map and layer events
- markers
- tooltip and popup binding
- layer groups
- vector layers
- dark theme
- map controls (zoom, layers, scale)
- map state functions
- tile layers
- GeoJSON support
- support for Leaflet plugins: 
	- [Leaflet.heat](https://github.com/Leaflet/Leaflet.heat)
	- [leaflet.fullscreen](https://github.com/brunob/leaflet.fullscreen)
	- [leaflet-kmz](https://github.com/Raruto/leaflet-kmz)
	- [Leaflet.markercluster](https://github.com/Leaflet/Leaflet.markercluster)
	- [Leaflet.Canvas-Markers](https://github.com/eJuke/Leaflet.Canvas-Markers)
- support for [Esri Leaflet plugins](https://github.com/Esri/esri-leaflet): 
	- L.esri.DynamicMapLayer
	- L.esri.TiledMapLayer 
	- L.esri.Vector.vectorBasemapLayer

##  New features on branch "vaadin23"
- support for Vaadin 23 version
- support for [MousePosition plugin]("https://github.com/ardhi/Leaflet.MousePosition")
- Google Layer
- support for a custom CRS
- MarkerCluster now can set options and the function for the icon creation
- Listeners works always even if they are added after a layer is added to the map
- Basic support for [geoman plugin](https://github.com/geoman-io/leaflet-geoman) editing possibilities:
  - Toolbar Options
  - Creating and deleting layers
  - Setting the layer where the new layers will be added

# Using the component in a Flow application with maven

Add the following dependencies in your pom.xml file:

```xml
<dependency>
   <groupId>org.vaadin.addons.componentfactory</groupId>
   <artifactId>vcf-leaflet</artifactId>
   <version>X.Y.Z</version>
</dependency>
```
```xml
<repository>
   <id>vaadin-addons</id>
   <url>https://maven.vaadin.com/vaadin-addons</url>
</repository>
```

## Example usage

```java
MapOptions options = new DefaultMapOptions();
options.setCenter(new LatLng(47.070121823, 19.204101562500004));
options.setZoom(7);
LeafletMap leafletMap = new LeafletMap(options );
leafletMap.setBaseUrl("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png");
add(leafletMap);
```

See more examples on [vcf-leaflet-demo](https://github.com/vaadin-component-factory/vcf-leaflet/tree/master/src/test/java/org/vaadin/addons/componentfactory/leaflet/demo/view).

## Development instructions

Starting the test/demo server:
1. Run `mvn jetty:run`.
2. Open http://localhost:9080 in the browser.

## License 

This Add-on is distributed under Apache Licence 2.0.

### Sponsored development
Major pieces of development of this add-on has been sponsored by multiple customers of Vaadin. Read more about Expert on Demand at: [Support](https://vaadin.com/support) and [Pricing](https://vaadin.com/pricing).
