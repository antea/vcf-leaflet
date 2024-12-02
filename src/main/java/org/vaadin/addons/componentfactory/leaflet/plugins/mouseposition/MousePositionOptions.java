package org.vaadin.addons.componentfactory.leaflet.plugins.mouseposition;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class MousePositionOptions implements Serializable {
    //A string to be prepended to the coordinates. Defaults to the empty string ‘’.
    private String prefix = "";

    // To separate longitude\latitude values. Defaults to ' : '
    private String separator = " : ";

    // Initial text to display. Defaults to 'Unavailable'
    private String emptystring = "Unavailable";

    //Number of digits. Defaults to 5
    private int numDigits = 5;

    // Weather to put the longitude first or not. Defaults to false
    private Boolean lngFirst = false;
}
