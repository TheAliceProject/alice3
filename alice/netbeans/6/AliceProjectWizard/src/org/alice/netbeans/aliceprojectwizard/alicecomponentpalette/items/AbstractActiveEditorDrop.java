package org.alice.netbeans.aliceprojectwizard.alicecomponentpalette.items;

import edu.cmu.cs.dennisc.java.util.logging.Logger;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import org.alice.netbeans.aliceprojectwizard.alicecomponentpalette.items.resources.I18nUtilities;
import org.openide.text.ActiveEditorDrop;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractActiveEditorDrop implements ActiveEditorDrop {

	protected boolean prologue(JTextComponent targetComponent) {
		return true;
	}

	protected String epilogue(String source) {
		return source;
	}

	protected String[] getImports() {
		return null;
	}

	private String createBody() {
		return "\n" + I18nUtilities.getCode(this.getClass()) + "\n";
	}

	public final boolean handleTransfer(JTextComponent targetComponent) {
		if (this.prologue(targetComponent)) {
			String[] imports = this.getImports();
			String body = this.createBody();
			body = this.epilogue(body);
			try {
				AliceComponentPaletteUtilities.insert(body, imports, targetComponent);
			} catch (BadLocationException ble) {
				Logger.throwable(ble, body);
				return false;
			}
			return true;
		} else {
			return false;
		}
	}
}
