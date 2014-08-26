/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alice.netbeans.aliceprojectwizard.alicecomponentpalette;

import java.io.IOException;
import javax.swing.Action;
import org.netbeans.spi.palette.PaletteActions;
import org.netbeans.spi.palette.PaletteController;
import org.netbeans.spi.palette.PaletteFactory;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;

/**
 *
 * @author Administrator
 */
public class AliceComponentPaletteFactory {

	//Name of the folder in the layer.xml file that is the root of the palette:
	public static final String JAVA_PALETTE_FOLDER = "AlicePalette";
	private static PaletteController palette = null;

	public AliceComponentPaletteFactory() {
	}

	public static PaletteController createPalette() {
		try {
			if (null == palette) {
				palette = PaletteFactory.createPalette(JAVA_PALETTE_FOLDER, new MyActions());
			}
			return palette;
		} catch (IOException ex) {
			Exceptions.printStackTrace(ex);
		}
		return null;
	}

	private static class MyActions extends PaletteActions {

		//Add new buttons to the Palette Manager here:
		public Action[] getImportActions() {
			return null;
		}

		//Add new contextual menu items to the palette here:
		public Action[] getCustomPaletteActions() {
			return null;
		}

		//Add new contextual menu items to the categories here:
		public Action[] getCustomCategoryActions(Lookup arg0) {
			return null;
		}

		//Add new contextual menu items to the items here:
		public Action[] getCustomItemActions(Lookup arg0) {
			return null;
		}

		//Define the default action here:
		public Action getPreferredAction(Lookup arg0) {
			return null;
		}
	}
}
