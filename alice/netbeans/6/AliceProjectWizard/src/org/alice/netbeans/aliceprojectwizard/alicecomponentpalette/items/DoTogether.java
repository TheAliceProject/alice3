package org.alice.netbeans.aliceprojectwizard.alicecomponentpalette.items;

import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import org.alice.netbeans.aliceprojectwizard.alicecomponentpalette.items.resources.DoTogetherCustomizer;
import org.openide.text.ActiveEditorDrop;

public class DoTogether implements ActiveEditorDrop {

	private int runnableCount = 2;
	private String DO_TOGETHER_BODY =
			"new Runnable(){\n"
			+ "\tpublic void run() {\n"
			+ "\t\t//TODO: Code goes here\n"
			+ "\t}\n"
			+ "}";

	private String createBody() {
		int count = getRunnableCount();
		StringBuffer buffer = new StringBuffer();
		buffer.append("\n");
		buffer.append("//start a Thread for each Runnable and wait until they complete\n");
		buffer.append("doTogether(\n");
		for (int i = 0; i < count; i++) {
			String separator = (i < count - 1) ? ",\n" : "\n";
			buffer.append(DO_TOGETHER_BODY);
			buffer.append(separator);
		}
		buffer.append(");\n");
		buffer.append("\n");
		return buffer.toString();
	}

	public boolean handleTransfer(JTextComponent targetComponent) {
		String[] imports = {"static org.lgna.common.ThreadUtilities.doTogether"};
		DoTogetherCustomizer c = new DoTogetherCustomizer(this, targetComponent);
		boolean accept = c.showDialog();
		if (accept) {
			String body = createBody();
			try {
				AliceComponentPaletteUtilities.insert(body, imports, targetComponent);
			} catch (BadLocationException ble) {
				accept = false;
			}
		}
		return accept;

	}

	public int getRunnableCount() {
		return runnableCount;
	}

	public void setRunnableCount(int runnableCount) {
		this.runnableCount = runnableCount;
	}
}
