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

package org.alice.netbeans.options;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.prefs.Preferences;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import org.netbeans.spi.options.OptionsPanelController;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;

@OptionsPanelController.TopLevelRegistration(
		categoryName = "#OptionsCategory_Name_Alice3",
		iconBase = "org/alice/netbeans/options/aliceLogo32x32.png",
		keywords = "#OptionsCategory_Keywords_Alice3",
		keywordsCategory = "Alice3"
)
@org.openide.util.NbBundle.Messages({"OptionsCategory_Name_Alice3=Alice", "OptionsCategory_Keywords_Alice3=alice"})
public final class Alice3OptionsPanelController extends OptionsPanelController {
	/*package-private*/ static final String COLLAPSE_IMPORTS_KEY = "collapseImports";
	/*package-private*/ static final String COLLAPSE_BOILER_PLATE_METHODS_KEY = "collapseBoilerPlateMethods";
	/*package-private*/ static final String OFFER_CLEAN_SLATE_METHODS_KEY = "offerCleanSlate";
	
	public static boolean isImportCollapsingDesired() {
		Preferences preferences = Preferences.userNodeForPackage(Alice3Panel.class);
		return preferences.getBoolean(COLLAPSE_IMPORTS_KEY, true);
	}
	public static boolean isBoilerPlateMethodCollapsingDesired() {
		Preferences preferences = Preferences.userNodeForPackage(Alice3Panel.class);
		return preferences.getBoolean(COLLAPSE_BOILER_PLATE_METHODS_KEY, true);
	}
	public static boolean isOfferingCleanSlateDesired() {
		Preferences preferences = Preferences.userNodeForPackage(Alice3Panel.class);
		return preferences.getBoolean(OFFER_CLEAN_SLATE_METHODS_KEY, true);
	}
	
	private Alice3Panel panel;
	private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	private boolean changed;

	public void update() {
		getPanel().load();
		changed = false;
	}

	public void applyChanges() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				getPanel().store();
				changed = false;
			}
		});
	}

	public void cancel() {
		// need not do anything special, if no changes have been persisted yet
	}

	public boolean isValid() {
		return getPanel().valid();
	}

	public boolean isChanged() {
		return changed;
	}

	public HelpCtx getHelpCtx() {
		return null; // new HelpCtx("...ID") if you have a help set
	}

	public JComponent getComponent(Lookup masterLookup) {
		return getPanel();
	}

	public void addPropertyChangeListener(PropertyChangeListener l) {
		pcs.addPropertyChangeListener(l);
	}

	public void removePropertyChangeListener(PropertyChangeListener l) {
		pcs.removePropertyChangeListener(l);
	}

	private Alice3Panel getPanel() {
		if (panel == null) {
			panel = new Alice3Panel(this);
		}
		return panel;
	}

	void changed() {
		if (!changed) {
			changed = true;
			pcs.firePropertyChange(OptionsPanelController.PROP_CHANGED, false, true);
		}
		pcs.firePropertyChange(OptionsPanelController.PROP_VALID, null, null);
	}

}
