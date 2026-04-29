const _ODA_BASE = '/VAADIN/generated/jar-resources/oda/';
const ASSETS_DIR = '/assets';

let _odaReady = null; // Promise<{ mod, appCore }>

function loadOdaModule(canvas) {
    if (_odaReady) return _odaReady;
    _odaReady = new Promise((resolve, reject) => {
        // emscripten_webgl_create_context("#canvas") is hardcoded in the WASM — it calls
        // document.querySelector('#canvas') on the main document. The canvas must be there.
        window.Module = {
            canvas: canvas,
            locateFile: (path) => _ODA_BASE + path,
            FS: {},
            arguments: [],
            ASSETS_FOLDER: ASSETS_DIR,
            preRun: [function () { FS.mkdir(ASSETS_DIR); }],
            postRun: [function () {
                Module.canvas = canvas;
                try {
                    const appCore = new Module.App();
                    console.log('[ODA] App() created successfully');
                    resolve({ mod: Module, appCore });
                } catch (e) {
                    console.error('[ODA] App() threw:', e);
                    _odaReady = null;
                    reject(e);
                }
            }],
            print:    function (text) { console.log('[ODA]', text); },
            printErr: function (text) { console.error('[ODA]', text); },
            setStatus: function () {},
            totalDependencies: 0,
            monitorRunDependencies: function () {},
        };

        const script = document.createElement('script');
        script.src = _ODA_BASE + 'DrawingWeb.js';
        script.onerror = function () {
            _odaReady = null;
            reject(new Error('[OdaNativeLayer] Failed to load DrawingWeb.js'));
        };
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
        this._rafPending = false;

        // Placeholder div inside the Leaflet pane — used only for lifecycle tracking.
        const container = L.DomUtil.create('div', 'leaflet-oda-native-layer');
        Object.assign(container.style, { position: 'absolute', top: '0', left: '0',
            width: '100%', height: '100%', pointerEvents: 'none' });
        this._container = container;
        map.getContainer().appendChild(container);

        // Canvas must live in document.body (light DOM) so that
        // document.querySelector('#canvas') — hardcoded inside DrawingWeb.wasm — can find it.
        const canvas = document.createElement('canvas');
        canvas.id = 'canvas';
        canvas.tabIndex = -1;
        Object.assign(canvas.style, {
            position:      'fixed',
            pointerEvents: 'none',
            display:       'block',
        });
        const mapSize = map.getSize();
        canvas.width  = mapSize.x || 800;
        canvas.height = mapSize.y || 600;
        document.body.appendChild(canvas);
        this._canvas = canvas;

        this._syncCanvasPosition();

        this._initViewer(canvas).catch(e => console.error('[OdaNativeLayer]', e));

        map.on('move zoom', this._syncView,           this);
        map.on('resize',    this._syncCanvasPosition, this);

        return this;
    },

    onRemove(map) {
        map.off('move zoom', this._syncView,           this);
        map.off('resize',    this._syncCanvasPosition, this);

        if (this._canvas)    { this._canvas.remove();    this._canvas    = null; }
        if (this._container) { this._container.remove(); this._container = null; }
        this._ready   = false;
        this._appCore = null;
    },

    _syncCanvasPosition() {
        if (!this._canvas || !this._map) return;
        const rect = this._map.getContainer().getBoundingClientRect();
        const w = Math.round(rect.width);
        const h = Math.round(rect.height);
        Object.assign(this._canvas.style, {
            left:   rect.left + 'px',
            top:    rect.top  + 'px',
            width:  w + 'px',
            height: h + 'px',
        });
        // Keep the canvas buffer in sync so the content isn't stretched.
        if (w && h && (this._canvas.width !== w || this._canvas.height !== h)) {
            this._canvas.width  = w;
            this._canvas.height = h;
            if (this._ready && this._appCore) {
                this._appCore.Resize(w, h);
                this._syncView();
            }
        }
    },

    async _initViewer(canvas) {
        console.log('[OdaNativeLayer] loading ODA module...');
        const { mod, appCore } = await loadOdaModule(canvas);
        this._appCore = appCore;

        const fileName = fileNameFromUrl(this._dwgUrl);
        console.log('[OdaNativeLayer] fetching', this._dwgUrl, 'as', fileName);
        const response = await fetch(this._dwgUrl);
        if (!response.ok) throw new Error(`HTTP ${response.status}`);
        const buffer = await response.arrayBuffer();

        const path = ASSETS_DIR + '/' + fileName;
        const { exists } = FS.analyzePath(path);
        if (!exists) mod.FS_createDataFile(ASSETS_DIR, fileName, new Uint8Array(buffer), true, true, true);

        appCore.OpenFile(path);
        appCore.Resize(canvas.width, canvas.height);
        appCore.ZoomExtents();
        appCore.Update(); // settle the view state before reading it

        // Capture the WCS camera state right after ZoomExtents so we have an
        // absolute reference for all subsequent zoom/pan calculations.
        const view = appCore.getDevice().viewAt(0);
        const pos = view.position();
        const tgt = view.target();
        const up  = view.upVector();
        this._baseView = {
            posX: pos.x, posY: pos.y, posZ: pos.z,
            tgtX: tgt.x, tgtY: tgt.y, tgtZ: tgt.z,
            upX:  up.x,  upY:  up.y,  upZ:  up.z,
            fieldWidth:  view.fieldWidth(),
            fieldHeight: view.fieldHeight(),
            pixelWidth:  canvas.width,
            projection:  view.isPerspective() ? 1 : 0, // Projection.kPerspective=1, kParallel=0
        };
        this._baseZoom      = this._map.getZoom();
        this._initialCenter = this._map.getCenter();

        this._ready = true;
        console.log('[OdaNativeLayer] ready, baseView:', this._baseView);

        const self = this;
        (function render() {
            if (!self._canvas) return;
            requestAnimationFrame(render);
            appCore.Update();
        })();
    },

    // Absolute view sync — mirrors dwg-native-layer's approach.
    // Converts the current Leaflet viewport to a DWG world-space window and
    // calls OdGsView.zoomWindow so that zoom and pan are both set at once,
    // with no incremental delta accumulation.
    _syncView() {
        if (!this._ready) return;
        if (this._rafPending) return;
        this._rafPending = true;
        requestAnimationFrame(() => {
            this._rafPending = false;
            this._doSync();
        });
    },

    _doSync() {
        if (!this._ready || !this._appCore || !this._baseView) return;

        const { posX, posY, posZ, tgtX, tgtY, tgtZ,
                upX, upY, upZ, fieldWidth, fieldHeight, pixelWidth, projection } = this._baseView;

        // Total zoom scale relative to the ZoomExtents baseline.
        const zf = this._map.getZoomScale(this._map.getZoom(), this._baseZoom);

        // Pan offset from the initial center in base-zoom Leaflet pixels, then WCS units.
        // Screen X-right = WCS X-right; screen Y-down = WCS Y-up (flip sign on Y).
        const initPx  = this._map.project(this._initialCenter,   this._baseZoom);
        const currPx  = this._map.project(this._map.getCenter(), this._baseZoom);
        const pxToWcs = fieldWidth / pixelWidth;
        const panX    =  (currPx.x - initPx.x) * pxToWcs;
        const panY    = -(currPx.y - initPx.y) * pxToWcs;

        // New camera position and target — pan both by the same WCS delta.
        const newPos = new Module.OdGePoint3d();
        newPos.set(posX + panX, posY + panY, posZ);
        const newTgt = new Module.OdGePoint3d();
        newTgt.set(tgtX + panX, tgtY + panY, tgtZ);
        const upVec = new Module.OdGeVector3d(upX, upY, upZ);

        // Zoom by shrinking the field of view (fieldWidth / zf: larger zf = more zoomed in).
        const view = this._appCore.getDevice().viewAt(0);
        view.setView(newPos, newTgt, upVec, fieldWidth / zf, fieldHeight / zf, projection);
    },

});

L.odaNativeLayer = function (url, options) {
    return new L.OdaNativeLayer(url, options);
};
