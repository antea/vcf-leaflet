const _ODA_BASE = '/oda/';
const ASSETS_DIR = '/assets';

// Singleton: one Emscripten Module, one WebGL context, one App instance.
let _odaReady = null; // Promise<{ mod, appCore }>

/**
 * Load DrawingWeb.js and create the App inside postRun — exactly as the
 * original oda-web.html does — so the GL context is current when App() runs.
 */
function loadOdaModule(canvas) {
    if (_odaReady) return _odaReady;
    _odaReady = new Promise((resolve, reject) => {
        window.Module = {
            canvas,
            FS: {},
            arguments: [],
            ASSETS_FOLDER: ASSETS_DIR,
            preRun: [function () { FS.mkdir(ASSETS_DIR); }],
            postRun: [function () {
                // Mirror original oda-web.html: reassign canvas, create App, Resize — all in postRun
                Module.canvas = canvas;
                try {
                    const appCore = new Module.App();
                    appCore.Resize(canvas.width, canvas.height);
                    resolve({ mod: Module, appCore });
                } catch (e) {
                    reject(e);
                }
            }],
            print:    (...args) => console.log('[ODA]', ...args),
            printErr: (...args) => console.error('[ODA]', ...args),
            setStatus: () => {},
            totalDependencies: 0,
            monitorRunDependencies: () => {},
        };
        const script = document.createElement('script');
        script.src = _ODA_BASE + 'DrawingWeb.js';
        script.async = true;
        script.onerror = () => reject(new Error('[OdaNativeLayer] Failed to load DrawingWeb.js'));
        document.head.appendChild(script);
    });
    return _odaReady;
}

function fileNameFromUrl(url) {
    const base = url.split('/').pop()?.split('?')[0] || '';
    const lower = base.toLowerCase();
    if (lower.endsWith('.dwg') || lower.endsWith('.dxf')) return base;
    return base ? base + '.dwg' : 'drawing.dwg';
}

L.OdaNativeLayer = L.Layer.extend({

    initialize(url, options) {
        this._dwgUrl = url;
        L.setOptions(this, options);
    },

    onAdd(map) {
        this._map = map;
        this._ready = false;
        this._appCore = null;
        this._rafId = null;
        this._zoomStartLevel = null;

        if (!document.getElementById('oda-native-layer-style')) {
            const style = document.createElement('style');
            style.id = 'oda-native-layer-style';
            style.textContent = '.leaflet-oda-native-layer, .leaflet-oda-native-layer * { pointer-events: none !important; }';
            document.head.appendChild(style);
        }

        const container = L.DomUtil.create('div', 'leaflet-oda-native-layer');
        Object.assign(container.style, {
            position:      'absolute',
            top:           '0',
            left:          '0',
            width:         '100%',
            height:        '100%',
            zIndex:        '0',
            pointerEvents: 'none',
            overflow:      'hidden',
        });
        this._container = container;
        map.getContainer().appendChild(container);

        const canvas = document.createElement('canvas');
        canvas.id = 'canvas'; // ODA C++ code calls emscripten_webgl_create_context("#canvas")
        canvas.tabIndex = -1;
        Object.assign(canvas.style, { width: '100%', height: '100%', display: 'block' });
        const mapSize = map.getSize();
        canvas.width  = mapSize.x || 800;
        canvas.height = mapSize.y || 600;
        container.appendChild(canvas);
        this._canvas = canvas;

        this._initViewer(canvas).catch(e => console.error('[OdaNativeLayer]', e));

        map.on('move',      this._onMove,      this);
        map.on('zoomstart', this._onZoomStart, this);
        map.on('zoom',      this._onZoom,      this);
        map.on('zoomend',   this._onZoomEnd,   this);

        return this;
    },

    onRemove(map) {
        map.off('move',      this._onMove,      this);
        map.off('zoomstart', this._onZoomStart, this);
        map.off('zoom',      this._onZoom,      this);
        map.off('zoomend',   this._onZoomEnd,   this);

        if (this._rafId) { cancelAnimationFrame(this._rafId); this._rafId = null; }
        if (this._container) { this._container.remove(); this._container = null; }
        this._ready = false;
        this._appCore = null;
    },

    async _initViewer(canvas) {
        console.log('[OdaNativeLayer] loading ODA module...');
        const { mod, appCore } = await loadOdaModule(canvas);
        this._appCore = appCore;

        // Fetch and write DWG into Emscripten virtual FS
        const fileName = fileNameFromUrl(this._dwgUrl);
        console.log('[OdaNativeLayer] fetching', this._dwgUrl, 'as', fileName);
        const response = await fetch(this._dwgUrl);
        if (!response.ok) throw new Error(`HTTP ${response.status}`);
        const buffer = await response.arrayBuffer();
        console.log('[OdaNativeLayer] buffer bytes:', buffer.byteLength);

        const path = ASSETS_DIR + '/' + fileName;
        const { exists } = FS.analyzePath(path);
        if (!exists) mod.FS_createDataFile(ASSETS_DIR, fileName, new Uint8Array(buffer), true, true, true);

        appCore.OpenFile(path);
        appCore.ZoomExtents();

        this._prevCenterPx = this._map.latLngToContainerPoint(this._map.getCenter());
        this._ready = true;
        console.log('[OdaNativeLayer] ready');

        // Render loop
        const loop = () => {
            if (!this._container) return;
            appCore.Update();
            this._rafId = requestAnimationFrame(loop);
        };
        this._rafId = requestAnimationFrame(loop);
    },

    _onMove() {
        if (!this._ready) return;
        const center = this._map.latLngToContainerPoint(this._map.getCenter());
        const prev   = this._prevCenterPx;
        this._appCore.Dolly(-(center.x - prev.x), center.y - prev.y);
        this._prevCenterPx = center;
    },

    _onZoomStart() {
        this._zoomStartLevel = this._map.getZoom();
        this._prevCenterPx   = this._map.latLngToContainerPoint(this._map.getCenter());
    },

    _onZoom() {
        if (!this._ready || this._zoomStartLevel == null) return;
        const currentZoom = this._map.getZoom();
        const scale  = this._map.getZoomScale(currentZoom, this._zoomStartLevel);
        const center = this._map.latLngToContainerPoint(this._map.getCenter());
        this._appCore.Zoom(Math.log2(scale), center.x, center.y);
        this._zoomStartLevel = currentZoom;
        this._prevCenterPx   = center;
    },

    _onZoomEnd() {
        this._prevCenterPx   = this._map.latLngToContainerPoint(this._map.getCenter());
        this._zoomStartLevel = null;
    },

});

L.odaNativeLayer = function (url, options) {
    return new L.OdaNativeLayer(url, options);
};
