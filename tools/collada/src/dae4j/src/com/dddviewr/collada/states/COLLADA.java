package com.dddviewr.collada.states;

import org.xml.sax.Attributes;

import com.dddviewr.collada.Collada;
import com.dddviewr.collada.State;
import com.dddviewr.collada.StateManager;

public class COLLADA extends State {
	protected Collada collada = new Collada();

	public void init(String name, Attributes attrs, StateManager mngr) {
		super.init(name, attrs, mngr);
	}

	public Collada getCollada() {
		return this.collada;
	}
}