package com.dddviewr.collada;

import org.xml.sax.Attributes;

public class State {
	protected String name;
	protected StateManager stateManager;
	protected StringBuilder content = new StringBuilder();
	protected Attributes attrs;
	protected boolean contentNeeded = false;
	private boolean firstChars = true;

	public void init(String name, Attributes attrs, StateManager mngr) {
		this.name = name;
		this.attrs = attrs;
		this.stateManager = mngr;
	}

	public void startElement(String name, Attributes attrs) {
		State newState = this.stateManager.createState(name);
		this.stateManager.pushState(newState);
		newState.init(name, attrs, this.stateManager);
		this.firstChars = true;
	}

	public void endElement(String name) {
		this.stateManager.popState();
	}

	public void characters(char[] ch, int start, int length) {
		if (!(this.contentNeeded))
			return;

		int newStart = start;
		int newLength = length;
		boolean foundPreSpace = false;
		while (newStart < start + length) {
			if (!(Character.isWhitespace(ch[newStart])))
				break;
			foundPreSpace = true;
			++newStart;
			--newLength;
		}
		int pos = newStart + newLength - 1;
		while (newLength > 0) {
			if (!(Character.isWhitespace(ch[pos])))
				break;
			--pos;
			--newLength;
		}

		if (newLength > 0) {
			if ((!(firstChars)) && (foundPreSpace))
				content.append(' ');
			content.append(ch, start, length);
		}

		firstChars = false;
	}

	public boolean isContentNeeded() {
		return this.contentNeeded;
	}

	public void setContentNeeded(boolean contentNeeded) {
		this.contentNeeded = contentNeeded;
	}

	public State getParent() {
		return this.stateManager.getParent(this);
	}

	public String getName() {
		return this.name;
	}
}