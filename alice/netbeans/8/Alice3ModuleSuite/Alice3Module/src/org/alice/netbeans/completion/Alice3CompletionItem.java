package org.alice.netbeans.completion;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.StyledDocument;
import org.netbeans.api.editor.completion.Completion;
import org.netbeans.spi.editor.completion.CompletionItem;
import org.netbeans.spi.editor.completion.CompletionTask;
import org.netbeans.spi.editor.completion.support.CompletionUtilities;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;

/**
 * @author Dennis Cosgrove
 */
public class Alice3CompletionItem implements CompletionItem {

	private static final ImageIcon fieldIcon = new ImageIcon(ImageUtilities.loadImage("org/alice/netbeans/aliceIcon.png"));
	private static final Color fieldColor = Color.decode("0x0000B2");

	public Alice3CompletionItem(String searchText, String completionText, int startOffset, int caretOffset, int sortPriority) {
		this.searchText = searchText;
		this.completionText = completionText;
		this.startOffset = startOffset;
		this.caretOffset = caretOffset;
		this.sortPriority = sortPriority;
	}

	@Override
	public int getPreferredWidth(Graphics graphics, Font font) {
		return CompletionUtilities.getPreferredWidth(searchText, null, graphics, font);
	}

	@Override
	public void render(Graphics g, Font defaultFont, Color defaultColor, Color backgroundColor, int width, int height, boolean selected) {
		CompletionUtilities.renderHtml(fieldIcon, searchText, null, g, defaultFont, (selected ? Color.white : fieldColor), width, height, selected);
	}

	@Override
	public CharSequence getSortText() {
		return this.searchText;
	}

	@Override
	public CharSequence getInsertPrefix() {
		return this.searchText;
	}

	@Override
	public CompletionTask createDocumentationTask() {
		return null;
	}

	@Override
	public CompletionTask createToolTipTask() {
		return null;
	}

	@Override
	public boolean instantSubstitution(JTextComponent jtc) {
		return false;
	}

	@Override
	public int getSortPriority() {
		return this.sortPriority;
	}

	@Override
	public void defaultAction(JTextComponent component) {
		try {
			StyledDocument doc = (StyledDocument) component.getDocument();
			//Here we remove the characters starting at the start offset
			//and ending at the point where the caret is currently found:
			doc.remove(this.startOffset, this.caretOffset - this.startOffset);
			doc.insertString(this.startOffset, this.completionText, null);
			Completion.get().hideAll();
		} catch (BadLocationException ble) {
			Exceptions.printStackTrace(ble);
		}
	}

	@Override
	public void processKeyEvent(KeyEvent ke) {
	}

	private final String searchText;
	private final String completionText;
	private final int startOffset;
	private final int caretOffset;
	private final int sortPriority;
}
