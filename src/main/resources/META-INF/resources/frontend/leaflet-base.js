import * as L from "leaflet/dist/leaflet-src.js";

globalThis.L = {};
for (const [key, value] of Object.entries(L)) {
    globalThis.L[key] = value;
}

import "leaflet/dist/leaflet.css";
