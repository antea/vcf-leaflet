package org.vaadin.addons.componentfactory.leaflet.layer.groups;

import org.junit.Test;
import org.vaadin.addons.componentfactory.leaflet.layer.Layer;
import org.vaadin.addons.componentfactory.leaflet.layer.ui.marker.Marker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for class {@link LayerGroup}
 */
public class LayerGroupTest {

    /**
     * Tests {@link LayerGroup#findLayer(String)}
     */
    @Test
    public void testFindLayerInDeepHierarchy() {
        Layer layer1 = new Marker();
        Layer layer2 = new Marker();
        LayerGroup childGroup1 = new LayerGroup(layer1);
        LayerGroup childGroup2 = new LayerGroup(layer2, childGroup1);
        LayerGroup rootGroup = new LayerGroup(childGroup2);
        assertTrue("layer should be found", rootGroup.findLayer(rootGroup.getUuid()).isPresent());
        assertEquals("layer should match the searched ID", rootGroup.getUuid(), rootGroup.findLayer(rootGroup.getUuid()).get().getUuid());
        assertTrue("layer should be found", rootGroup.findLayer(childGroup2.getUuid()).isPresent());
        assertEquals("layer should match the searched ID", childGroup2.getUuid(), rootGroup.findLayer(childGroup2.getUuid()).get().getUuid());
        assertTrue("layer should be found", rootGroup.findLayer(childGroup1.getUuid()).isPresent());
        assertEquals("layer should match the searched ID", childGroup1.getUuid(), rootGroup.findLayer(childGroup1.getUuid()).get().getUuid());
        assertTrue("layer should be found", rootGroup.findLayer(layer2.getUuid()).isPresent());
        assertEquals("layer should match the searched ID", layer2.getUuid(), rootGroup.findLayer(layer2.getUuid()).get().getUuid());
        assertTrue("layer should be found", rootGroup.findLayer(layer1.getUuid()).isPresent());
        assertEquals("layer should match the searched ID", layer1.getUuid(), rootGroup.findLayer(layer1.getUuid()).get().getUuid());
    }
}