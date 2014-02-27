package org.alice.netbeans.completion;

import java.util.Locale;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.StyledDocument;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.spi.editor.completion.CompletionProvider;
import org.netbeans.spi.editor.completion.CompletionResultSet;
import org.netbeans.spi.editor.completion.CompletionTask;
import org.netbeans.spi.editor.completion.support.AsyncCompletionQuery;
import org.netbeans.spi.editor.completion.support.AsyncCompletionTask;
import org.openide.util.Exceptions;

/**
 * @author Dennis Cosgrove
 */
@MimeRegistration(mimeType = "text/x-java", service = CompletionProvider.class)
public class Alice3CompletionProvider implements CompletionProvider {

	private static final Alice3CompletionItemBuilder[] completionItemBuilders = {
		new Alice3CompletionItemBuilder()
				.searchText("doTogether")
				.completionText("doTogether(() -> {\n\t    //TODO: Code goes here\n\t}, () -> {\n\t    //TODO: Code goes here\n\t}, () -> {\n\t    //TODO: Code goes here\n\t});")
	};

	private static int getRowFirstNonWhite(StyledDocument doc, int offset) throws BadLocationException {
		Element lineElement = doc.getParagraphElement(offset);
		int start = lineElement.getStartOffset();
		while (start + 1 < lineElement.getEndOffset()) {
			try {
				if (doc.getText(start, 1).charAt(0) != ' ') {
					break;
				}
			} catch (BadLocationException ex) {
				throw (BadLocationException) new BadLocationException("calling getText(" + start + ", " + (start + 1) + ") on doc of length: " + doc.getLength(), start).initCause(ex);
			}
			start++;
		}
		return start;
	}

	private static int indexOfWhite(char[] line) {
		int i = line.length;
		while (--i > -1) {
			final char c = line[i];
			if (Character.isWhitespace(c)) {
				return i;
			}
		}
		return -1;
	}

	public Alice3CompletionProvider() {
	}

	@Override
	public CompletionTask createTask(int queryType, JTextComponent jtc) {
		if (queryType != CompletionProvider.COMPLETION_QUERY_TYPE) {
			return null;
		}

		return new AsyncCompletionTask(new AsyncCompletionQuery() {

			@Override
			protected void query(CompletionResultSet completionResultSet, Document document, int caretOffset) {

				String filter = null;
				int startOffset = caretOffset - 1;

				try {
					final StyledDocument bDoc = (StyledDocument) document;
					final int lineStartOffset = getRowFirstNonWhite(bDoc, caretOffset);
					final char[] line = bDoc.getText(lineStartOffset, caretOffset - lineStartOffset).toCharArray();
					final int whiteOffset = indexOfWhite(line);
					filter = new String(line, whiteOffset + 1, line.length - whiteOffset - 1);
					if (whiteOffset > 0) {
						startOffset = lineStartOffset + whiteOffset + 1;
					} else {
						startOffset = lineStartOffset;
					}
				} catch (BadLocationException ble) {
					Exceptions.printStackTrace(ble);
				}

				for (Alice3CompletionItemBuilder completionItemBuilder : completionItemBuilders) {
					if (completionItemBuilder.isAcceptable(filter)) {
						completionResultSet.addItem(completionItemBuilder.build(startOffset, caretOffset));
					}
				}
				completionResultSet.finish();

			}

		}, jtc);
	}

	@Override
	public int getAutoQueryTypes(JTextComponent component, String typedText) {
		return 0;
	}

}
