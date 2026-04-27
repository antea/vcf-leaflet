import { AcApDocManager } from '@mlightcad/cad-simple-viewer';
import { AcGeBox2d } from '@mlightcad/data-model';

const _WORKER_BASE = '/VAADIN/generated/jar-resources/workers/';

// AcEdOpenMode.Write = 8
const OPEN_MODE_WRITE = 8;

let _isInitialized = false;

function ensureInitialized(container, opts) {
    if (_isInitialized) return;
    AcApDocManager.createInstance({
        container,
        autoResize: true,
        baseUrl: opts.baseUrl ?? 'https://cdn.jsdelivr.net/gh/mlightcad/cad-data@main/',
        webworkerFileUrls: {
            dxfParser:   opts.dxfWorkerUrl   ?? _WORKER_BASE + 'dxf-parser-worker.js',
            dwgParser:   opts.dwgWorkerUrl   ?? _WORKER_BASE + 'libredwg-parser-worker.js',
            mtextRender: opts.mtextWorkerUrl ?? _WORKER_BASE + 'mtext-renderer-worker.js',
        }
    });
    _isInitialized = true;
}

/**
 * Derive a filename with a proper .dwg/.dxf extension from a URL.
 * openDocument() detects the file type from the filename extension.
 */
function fileNameFromUrl(url) {
    const base = url.split('/').pop()?.split('?')[0] || '';
    const lower = base.toLowerCase();
    if (lower.endsWith('.dwg') || lower.endsWith('.dxf')) return base;
    return base ? base + '.dwg' : 'drawing.dwg';
}

L.DwgNativeLayer = L.Layer.extend({

    initialize(url, options) {
        this._dwgUrl = url;
        L.setOptions(this, options);
    },

    onAdd(map) {
        this._map = map;
        this._ready = false;

        // Force pointer-events: none on every descendant so the CAD viewer's
        // internal UI elements (command line, overlays) don't block Leaflet pan.
        if (!document.getElementById('dwg-native-layer-style')) {
            const style = document.createElement('style');
            style.id = 'dwg-native-layer-style';
            style.textContent = '.leaflet-dwg-native-layer, .leaflet-dwg-native-layer * { pointer-events: none !important; }';
            document.head.appendChild(style);
        }

        const container = L.DomUtil.create('div', 'leaflet-dwg-native-layer');
        // Attach to the map container (not a pane) so width/height 100% resolves
        // against an element with explicit dimensions. Leaflet panes have no
        // explicit width/height, so percentage sizing inside them produces 0.
        // z-index 400 matches Leaflet's overlayPane so the CAD canvas sits above tiles.
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

        this._initViewer(container).catch(e => console.error('[DwgNativeLayer]', e));
        map.on('move', this._syncView, this);
        map.on('zoom', this._syncView, this);
        return this;
    },

    onRemove(map) {
        map.off('move', this._syncView, this);
        map.off('zoom', this._syncView, this);
        if (this._container) {
            this._container.remove();
            this._container = null;
        }
        this._ready = false;
        this._docManager = null;
    },

    async _initViewer(container) {
        const opts = this.options;

        ensureInitialized(container, opts);

        const fileName = fileNameFromUrl(this._dwgUrl);
        console.log('[DwgNativeLayer] fetching', this._dwgUrl, 'as', fileName);

        const response = await fetch(this._dwgUrl);
        if (!response.ok) throw new Error(`[DwgNativeLayer] HTTP ${response.status} fetching DWG`);
        const buffer = await response.arrayBuffer();
        console.log('[DwgNativeLayer] buffer bytes:', buffer.byteLength);

        const success = await AcApDocManager.instance.openDocument(fileName, buffer, {
            minimumChunkSize: 1000,
            mode: OPEN_MODE_WRITE
        });

        if (!success) throw new Error('[DwgNativeLayer] openDocument returned false');

        const canvas = container.querySelector('canvas');
        if (canvas) canvas.style.pointerEvents = 'none';

        this._docManager = AcApDocManager.instance;
        this._ready = true;
        this._syncView();
    },

    _syncView() {
        if (!this._ready || !this._docManager) return;
        if (this._rafPending) return;
        this._rafPending = true;
        requestAnimationFrame(() => {
            this._rafPending = false;
            const view = this._docManager?.curView;
            if (!view?.activeLayoutView) return;
            const bounds = this._map.getBounds();
            const sw = bounds.getSouthWest();
            const ne = bounds.getNorthEast();
            const box = new AcGeBox2d({ x: sw.lng, y: sw.lat }, { x: ne.lng, y: ne.lat });
            view.zoomTo(box, 1.0);
        });
    }

});

L.dwgNativeLayer = function (url, options) {
    return new L.DwgNativeLayer(url, options);
};
