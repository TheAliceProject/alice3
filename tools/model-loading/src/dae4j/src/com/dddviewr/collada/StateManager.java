package com.dddviewr.collada;

import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.dddviewr.collada.states.COLLADA;


public class StateManager extends DefaultHandler {
	protected Stack<State> states = new Stack<State>();
	protected Collada collada;

	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if (!(this.states.isEmpty()))
			((State) this.states.peek()).characters(ch, start, length);
	}

	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if(localName == null || localName.length() == 0)
			localName = qName;
		((State) this.states.peek()).endElement(localName);
	}

	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if(localName == null || localName.length() == 0)
			localName = qName;
		State current;
		if (this.states.isEmpty()) {
			current = createState(localName);
			pushState(current);
			current.init(localName, attributes, this);
			if (current instanceof COLLADA)
				this.collada = ((COLLADA) current).getCollada();
		} else {
			current = (State) this.states.peek();
			current.startElement(localName, attributes);
		}
	}

	public State popState() {
		return ((State) this.states.pop());
	}

	public void pushState(State state) {
		this.states.push(state);
	}

	public State createState(String name) {
		State result = null;
		try {
			Class<?> theClass = Class.forName("com.dddviewr.collada.states." + name);
			result = (State) theClass.newInstance();
		} catch (ClassNotFoundException e) {
			try {
				Class<?> theClass = Class.forName("com.dddviewr.collada.states." + name
						+ "State");
				result = (State) theClass.newInstance();
			} catch (ClassNotFoundException e1) {
				result = new State();
			} catch (Exception e1) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public State getParent(State state) {
		State result = null;
		int idx = this.states.lastIndexOf(state);
		if (idx > 0)
			return ((State) this.states.get(idx - 1));
		return result;
	}

	public Collada getCollada() {
		return this.collada;
	}
}