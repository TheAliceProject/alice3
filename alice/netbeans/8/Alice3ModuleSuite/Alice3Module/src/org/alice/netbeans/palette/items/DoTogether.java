/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.alice.netbeans.palette.items;

import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import org.alice.netbeans.palette.items.views.DoTogetherCustomizer;
import org.openide.text.ActiveEditorDrop;

/**
 *
 * @author Administrator
 */
public class DoTogether implements ActiveEditorDrop {

    private int runnableCount = 2;

    private String createBody() {
        int count = getRunnableCount();
        StringBuffer buffer = new StringBuffer();
		buffer.append( "\n" );
		buffer.append( "\t//start a Thread for each Runnable and wait until they complete\n" );
        buffer.append( "doTogether( ()-> {\n" );
		String separator = "";
        for (int i=0; i<count; i++) {
            buffer.append(separator);
			buffer.append("\t\t//TODO: Code goes here\n" );
			separator = "\t}, ()-> {\n";
        }
        buffer.append( "} );\n" );
		buffer.append( "\n" );
        return buffer.toString();
    }

    public boolean handleTransfer(JTextComponent targetComponent) {
        String[] imports = { "static org.lgna.common.ThreadUtilities.doTogether" };
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

