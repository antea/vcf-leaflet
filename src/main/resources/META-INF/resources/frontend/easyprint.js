import 'leaflet';
import 'leaflet-easyprint';

// Funzione globale che viene chiamata da Java
window.initEasyPrintMap = function(mapContainerElement, options = {
    baseUrl: "https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png",
    center:{lat:47.070121823,lng:19.2041015625} ,
    zoom:7,
    position:"topleft",
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
    // Se la mappa è già stata inizializzata su questo elemento, non fare nulla
    if (mapContainerElement.map) {
        return;
    }

    // 1. Inizializza la mappa Leaflet sull'elemento del light DOM
    const map = L.map(mapContainerElement).setView(options.center, options.zoom);
    mapContainerElement.map = map; // Salva un riferimento alla mappa

    L.tileLayer(options.baseUrl).addTo(map);

    // 2. Inizializza il plugin easyPrint sulla mappa appena creata
    L.easyPrint({
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
};
