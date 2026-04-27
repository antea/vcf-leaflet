import { AcApDocManager } from '@mlightcad/cad-simple-viewer';

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
 * If the URL has no recognizable extension, fall back to 'drawing.dwg'.
 * This is required because openDocument() detects the file type from the filename extension.
 */
function fileNameFromUrl(url) {
    const base = url.split('/').pop()?.split('?')[0] || '';
    const lower = base.toLowerCase();
    if (lower.endsWith('.dwg') || lower.endsWith('.dxf')) return base;
    return base ? base + '.dwg' : 'drawing.dwg';
}

async function initCadViewer(container, dwgUrl, opts) {
    ensureInitialized(container, opts);

    const fileName = fileNameFromUrl(dwgUrl);
    console.log('[CadSimpleViewer] fetching', dwgUrl, 'as', fileName);

    const response = await fetch(dwgUrl);
    if (!response.ok) throw new Error(`[CadSimpleViewer] HTTP ${response.status} fetching file`);
    const buffer = await response.arrayBuffer();
    console.log('[CadSimpleViewer] buffer bytes:', buffer.byteLength);

    const success = await AcApDocManager.instance.openDocument(fileName, buffer, {
        minimumChunkSize: 1000,
        mode: OPEN_MODE_WRITE
    });

    if (!success) throw new Error('[CadSimpleViewer] openDocument returned false');
    console.log('[CadSimpleViewer] document opened successfully');
}

window.CadSimpleViewerComponent = {
    init(container, dwgUrl, opts) {
        initCadViewer(container, dwgUrl, opts ?? {})
            .catch(e => console.error('[CadSimpleViewer]', e));
    }
};
