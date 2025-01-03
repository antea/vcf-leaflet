package org.vaadin.addons.componentfactory.leaflet.plugins.geoman.events.types;

import lombok.Getter;

import java.util.Arrays;

public enum ShapeType {

    MARKER ("Marker"),
    TEXT("Text"),
    CIRCLE_MARKER("CircleMarker"),
    LINE("Line"),
    RECTANGLE("Rectangle");

    @Getter
    private final String shape;

    ShapeType(String shape) {
        this.shape = shape;
    }

    public static ShapeType of(String shape) {
       return Arrays.stream(ShapeType.values())
               .filter(type -> type.getShape().equals(shape))
               .findFirst().orElse(null);
    }
}
