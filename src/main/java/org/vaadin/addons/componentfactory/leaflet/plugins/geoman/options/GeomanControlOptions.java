package org.vaadin.addons.componentfactory.leaflet.plugins.geoman.options;

import lombok.Getter;
import lombok.Setter;
import org.vaadin.addons.componentfactory.leaflet.layer.map.options.ControlOptions;

/**
 * Options to initialize geoman control toolbar.
 * See <a href="https://geoman.io/docs/leaflet/toolbar">geoman options</a>
 */
@Getter
@Setter
public class GeomanControlOptions implements ControlOptions {

    private boolean zoomControl;
    private boolean attributionControl;

    private String position = "topleft"; // Toolbar position, possible values are 'topleft', 'topright', 'bottomleft', 'bottomright'
    private boolean drawMarker = true; //Adds button to draw Markers.
    private boolean drawCircleMarker = true; //Adds button to draw CircleMarkers.
    private boolean drawPolyline = true; //Adds button to draw Line.
    private boolean drawRectangle = true; //Adds button to draw Rectangle.
    private boolean drawPolygon = true; //Adds button to draw Polygon.
    private boolean drawCircle = true; //Adds button to draw Circle.
    private boolean drawText = true; //Adds button to draw Text.
    private boolean editMode = true; //Adds button to toggle Edit Mode for all layers.
    private boolean dragMode = true; //Adds button to toggle Drag Mode for all layers.
    private boolean cutPolygon = true; //Adds button to cut a hole in a Polygon or Line.
    private boolean removalMode = true; //Adds a button to remove layers.
    private boolean rotateMode = true; //Adds a button to rotate layers.
    private boolean oneBlock = false; //All buttons will be displayed as one block Customize Controls.
    private boolean drawControls = true; //Shows all draw buttons / buttons in the draw block.
    private boolean editControls = true; //Shows all edit buttons / buttons in the edit block.
    private boolean customControls = true; //Shows all buttons in the custom block.
}
