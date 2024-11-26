package org.vaadin.addons.componentfactory.leaflet.types;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
/**
 * Crs parameters that models a Leaflet CRS that extends Crs.Simple: it uses a plat-carré projection with the
 * extents given by the min_* and max_* parameters. For the meaning of the
 * affine transform parameters, see: http://leafletjs.com/reference.html#transformation.
 */
public class CustomSimpleCrs implements Serializable {

    public enum BaseCrs {
        L_CRS_EPSG3395, // https://epsg.io/3395 WGS 84 / World Mercator
        L_CRS_EPSG4326, // https://epsg.io/4326 WGS84 in degree, WGS 84 - WGS84 - World Geodetic System 1984, used in GPS
        L_CRS_Simple;

        public String toLeaflet() {
            return name().replaceAll("_", ".")
                    .replaceAll("L.CRS.", "");
        }
    }

    // Name for the new Crs.
    private String name;
    // the min_x value
    private double min_x;
    // the min_y value
    private double min_y;
    // the max_x value
    private double max_x;
    // the max_y value
    private double max_y;
    // a in transformation calculation (a*x + b, c*y + d)
    private double a;
    // b in transformation calculation (a*x + b, c*y + d)
    private double b;
    // c in transformation calculation (a*x + b, c*y + d)
    private double c;
    // d in transformation calculation (a*x + b, c*y + d)
    private double d;

    public CustomSimpleCrs(String name, double min_x, double min_y, double max_x, double max_y, double a, double b, double c, double d) {
        this.name = name;

        this.min_x = min_x;
        this.min_y = min_y;
        this.max_x = max_x;
        this.max_y = max_y;

        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    public CustomSimpleCrs(String name, Point minPoint, Point maxPoint, double a, double b, double c, double d) {
        this(name, minPoint.getX(), minPoint.getY(), maxPoint.getX(), maxPoint.getY(), a, b, c, d);
    }
}
