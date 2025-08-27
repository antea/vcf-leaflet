import 'leaflet';
import './vcf-easyprint';

class EasyPrintFinishedEvent extends CustomEvent {
    constructor(event) {
        super("easyPrint-finished", {detail: event});
    }
}

class EasyPrintStartedEvent extends CustomEvent {
    constructor(event) {
        super("easyPrint-started", {detail: event});
    }
}

// Global function called by Java method
window.initEasyPrintMap = function (mapContainerElement, options = {
    baseUrl: "https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png",
    center: {lat: 47.070121823, lng: 19.2041015625},
    zoom: 7,
    position: "topleft",
    title: "Print",
    customWindowTitle: "Print",
    sizeModes: ["Current", "A4Landscape", "A4Portrait"],
    exportOnly: false,
    filename: "map",
    hidden: false,
    hideControlContainer: true,
    hideClasses: [],
    spinnerBgColor: "#ff8c00",
}) {
    // If the map was already initialized, return.
    if (mapContainerElement.map) {
        return;
    }

    // Initialize the leaflet map in the light DOM
    const map = L.map(mapContainerElement).setView(options.center, options.zoom);
    mapContainerElement.map = map;

    addEasyPrintPluginListener();

    L.tileLayer(options.baseUrl).addTo(map);

    // 2. Inizializza il plugin easyPrint sulla mappa appena creata
    // Salva un riferimento al plugin per poterlo chiamare in seguito
    mapContainerElement.easyPrint = L.easyPrint({
        title: options.title,
        position: options.position,
        sizeModes: options.sizeModes,
        exportOnly: options.exportOnly,
        hideControlContainer: options.hideControlContainer,
        // Altre opzioni di easyPrint possono essere passate qui
        filename: options.filename,
        hidden: options.hidden,
        hideClasses: options.hideClasses,
        spinnerBgCOlor: options.spinnerBgColor,
        //customSpinnerClass: options.customSpinnerClass
    }).addTo(map);

    mapContainerElement.print = function (filename, size) {
        if (!mapContainerElement.easyPrint) {
            console.error("EasyPrint non è stato inizializzato sull'elemento della mappa.");
        }
        mapContainerElement.easyPrint.printMap(size, filename);
    };

    mapContainerElement.addEasyPrintControl = function () {
        if (!mapContainerElement.easyPrint) {
            return;
        }
        mapContainerElement.easyPrint.addTo(map);
    }

    mapContainerElement.removeEasyPrintControl = function () {
        if (!mapContainerElement.easyPrint) {
            return;
        }
        mapContainerElement.easyPrint.remove();
    }

    function addEasyPrintPluginListener() {
        map.on("easyPrint-finished", event => {
            //Let's force the dispatch event on the standard leaflet-map.
            document.querySelector("leaflet-map").dispatchEvent(new EasyPrintFinishedEvent(event));
        });
        map.on("easyPrint-start", event => {
            //Let's force the dispatch event on the standard leaflet-map.
            document.querySelector("leaflet-map").dispatchEvent(new EasyPrintStartedEvent(event));
        });
    }
};
