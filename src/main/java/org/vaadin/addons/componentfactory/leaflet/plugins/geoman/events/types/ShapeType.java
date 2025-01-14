package org.vaadin.addons.componentfactory.leaflet.plugins.geoman.events.types;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ShapeType {

    MARKER("Marker"),
    TEXT("Text"),
    CIRCLE_MARKER("CircleMarker"),
    POLYLINE("Polyline"),
    RECTANGLE("Rectangle"),
    POLYGON("Polygon"),
    CIRCLE("Circle");

    private final String shape;

    ShapeType(String shape) {
        this.shape = shape;
    }

    public static ShapeType ofGeomanShape(String shape) {
        if ("Line".equals(shape)) {
            return ShapeType.POLYLINE;
        }
        return Arrays.stream(ShapeType.values())
                .filter(type -> type.getShape().equals(shape))
                .findFirst().orElse(null);
    }
}
