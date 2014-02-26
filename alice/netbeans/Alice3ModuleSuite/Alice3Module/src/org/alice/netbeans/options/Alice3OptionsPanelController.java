/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
