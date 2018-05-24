package com.dddviewr.collada.states;

import org.xml.sax.Attributes;

import com.dddviewr.collada.Collada;
import com.dddviewr.collada.State;
import com.dddviewr.collada.StateManager;
import com.dddviewr.collada.effects.LibraryEffects;

public class library_effects extends State {
	protected LibraryEffects library = new LibraryEffects();

	public void init(String name, Attributes attrs, StateManager mngr) {
		super.init(name, attrs, mngr);
		Collada collada = ((COLLADA) getParent()).getCollada();
		collada.setLibraryEffects(this.library);
	}

	public LibraryEffects getLibrary() {
		return this.library;
	}
}