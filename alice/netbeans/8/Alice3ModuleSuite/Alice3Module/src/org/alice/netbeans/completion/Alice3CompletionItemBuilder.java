package org.alice.netbeans.completion;

import org.netbeans.spi.editor.completion.CompletionItem;

/**
 * @author Dennis Cosgrove
 */
class Alice3CompletionItemBuilder {

	public Alice3CompletionItemBuilder searchText(String searchText) {
		this.searchText = searchText;
		return this;
	}

	public Alice3CompletionItemBuilder completionText(String completionText) {
		this.completionText = completionText;
		return this;
	}

	public Alice3CompletionItemBuilder sortPriority(int sortPriority) {
		this.sortPriority = sortPriority;
		return this;
	}

	public boolean isAcceptable(String text) {
		return this.searchText.startsWith(text);
	}

	public CompletionItem build(int startOffset, int caretOffset) {
		return new Alice3CompletionItem(this.searchText, this.completionText, startOffset, caretOffset, this.sortPriority);
	}

	private String searchText;
	private String completionText;
	private int sortPriority;
}
