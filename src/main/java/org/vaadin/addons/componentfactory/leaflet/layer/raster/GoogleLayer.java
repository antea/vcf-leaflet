package org.vaadin.addons.componentfactory.leaflet.layer.raster;

public class GoogleLayer extends TileLayer {

    /**
     * Instantiates a tile layer object given  the type of the Google layer
     * @param layerType the type correspondent to the layer template
     */
    public GoogleLayer(GoogleLayerType layerType) {
        super(layerType.getRealUrl());
        setAttribution("Google " + layerType.toString().toUpperCase());
        setSubdomains("mt0", "mt1", "mt2", "mt3");
        setMinZoom(1);
        setMaxZoom(20);
    }

    public enum GoogleLayerType {
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

    @Override
    public String getLeafletType() {
        return TileLayer.class.getSimpleName();
    }
}
