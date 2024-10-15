package org.vaadin.addons.componentfactory.leaflet.layer.raster;

/**
 * <h3>Utility class to create Layers to show Google Maps</h3>
 * Let's try to use google layer as suggested
 * <a href="https://stackoverflow.com/questions/9394190/leaflet-map-api-with-google-satellite-layer">here</a>
 */
public class GoogleLayerProducer {
    public enum LayerType {
        STREET {
            @Override
            public String getUrlParameter() {
                return "m";
            }
        },
        HYBRID {
            @Override
            public String getUrlParameter() {
                return "s,h";
            }
        },
        SATELLITE {
            @Override
            public String getUrlParameter() {
                return "s";
            }
        },
        TERRAIN {
            @Override
            public String getUrlParameter() {
                return "p";
            }
        };

        String getRealUrl() {
            return String.format("http://{s}.google.com/vt/lyrs=%s&x={x}&y={y}&z={z}", getUrlParameter());
        }
        protected abstract String getUrlParameter();
    }

    /**
     * This method can return a TileLayer that set the URL to GoogleMaps, depending on the type needed
     * {@link LayerType}
     */
    public static TileLayer of(LayerType layerType) {
        TileLayer layer = new TileLayer(layerType.getRealUrl());
        layer.setAttribution("Google " + layerType.toString().toUpperCase());
        layer.setSubdomains("mt0", "mt1", "mt2", "mt3");
        layer.setMinZoom(1);
        layer.setMaxZoom(20);
        return layer;
    }
}
