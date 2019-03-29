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

package org.alice.netbeans.completion;

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
						.sortPriority(0)
//		,
//		new Alice3CompletionItemBuilder()
//			.searchText("<font color=\"#C0C0C0\"><em>add detail: </em></font><font color=\"#000000\">Move.<strong>asSeenBy</strong>(SThing asSeenBy)</font>")
//			.completionText("Move.asSeenBy()")
//			.sortPriority(1),
//		new Alice3CompletionItemBuilder()
//			.searchText("<font color=\"#C0C0C0\"><em>add detail: </em></font><font color=\"#000000\">Move.<strong>duration</strong>(Number duration)</font>")
//			.completionText("Move.duration()")
//			.sortPriority(2),
//		new Alice3CompletionItemBuilder()
//			.searchText("<font color=\"#C0C0C0\"><em>add detail: </em></font><font color=\"#000000\">AnimationStyle.</font><font color=\"#007000\"><strong>BEGIN_AND_END_GENTLY</strong></font>")
//			.completionText("AnimationStyle.BEGIN_AND_END_GENTLY")
//			.sortPriority(3),
//		new Alice3CompletionItemBuilder()
//			.searchText("<font color=\"#C0C0C0\"><em>add detail: </em></font><font color=\"#000000\">AnimationStyle.</font><font color=\"#007000\"><strong>BEGIN_GENTLY_AND_END_ABRUPTLY</strong></font>")
//			.completionText("AnimationStyle.BEGIN_GENTLY_AND_END_ABRUPTLY")
//			.sortPriority(4),
//		new Alice3CompletionItemBuilder()
//			.searchText("<font color=\"#C0C0C0\"><em>add detail: </em></font><font color=\"#000000\">AnimationStyle.</font><font color=\"#007000\"><strong>BEGIN_ABRUPTLY_AND_END_GENTLY</strong></font>")
//			.completionText("AnimationStyle.BEGIN_ABRUPTLY_AND_END_GENTLY")
//			.sortPriority(5),
//		new Alice3CompletionItemBuilder()
//			.searchText("<font color=\"#C0C0C0\"><em>add detail: </em></font><font color=\"#000000\">AnimationStyle.</font><font color=\"#007000\"><strong>BEGIN_AND_END_ABRUPTLY</strong></font>")
//			.completionText("AnimationStyle.BEGIN_AND_END_ABRUPTLY")
//			.sortPriority(6)
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
		//EditorKit editorKit = CloneableEditorSupport.getEditorKit("text/x-java");
		//org.netbeans.modules.editor.java.JavaKit
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
