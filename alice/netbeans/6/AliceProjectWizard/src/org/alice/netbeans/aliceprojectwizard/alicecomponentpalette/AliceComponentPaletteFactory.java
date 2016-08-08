/*******************************************************************************
 * Copyright (c) 2006, 2016, Carnegie Mellon University. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may
 *    "Alice" appear in their name, without prior written permission of
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is
 *    contributed by Electronic Arts Inc. and may be used for personal,
 *    non-commercial, and academic use only. Redistributions of any program
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in
 *    The Alice 3.0 Art Gallery License.
 *
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *******************************************************************************/

package org.alice.netbeans.aliceprojectwizard.alicecomponentpalette;

import java.io.IOException;
import javax.swing.Action;
import org.netbeans.spi.palette.PaletteActions;
import org.netbeans.spi.palette.PaletteController;
import org.netbeans.spi.palette.PaletteFactory;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;

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
