/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.palette;
import javax.swing.Action;
import org.netbeans.spi.palette.PaletteActions;
import org.netbeans.spi.palette.PaletteController;
import org.netbeans.spi.palette.PaletteFactory;
import org.openide.nodes.AbstractNode;
import org.openide.util.Lookup;
/**
 *
 * @author Uros
 */
public class PaletteSupport {
    
    public static PaletteController createPalette () {
            AbstractNode paletteRoot= new AbstractNode (new PaletteCategoryChildren());
            paletteRoot.setName("Palette Root");
            return PaletteFactory.createPalette(paletteRoot,new MyPaletteActions(), null, new PaletteDnDHandler());
    }
    private static class MyPaletteActions extends PaletteActions {

        @Override
        public Action[] getImportActions() {
            return null;
        }

        @Override
        public Action[] getCustomPaletteActions() {
            return null;
        }

        @Override
        public Action[] getCustomCategoryActions(Lookup lookup) {
            return null;
        }

        @Override
        public Action[] getCustomItemActions(Lookup lookup) {
            return null;
        }

        @Override
        public Action getPreferredAction(Lookup lookup) {
            return null;
        }
    }
}
