package com.dddviewr.collada.states;

import org.xml.sax.Attributes;

import com.dddviewr.collada.Collada;
import com.dddviewr.collada.State;
import com.dddviewr.collada.StateManager;
import com.dddviewr.collada.animation.LibraryAnimations;

public class library_animations extends State {
	protected LibraryAnimations libraryAnimations;

	public void init(String name, Attributes attrs, StateManager mngr) {
		super.init(name, attrs, mngr);
		this.libraryAnimations = new LibraryAnimations();

		Collada collada = ((COLLADA) getParent()).getCollada();
		collada.setLibraryAnimations(this.libraryAnimations);
	}

	public LibraryAnimations getLibrary() {
		return this.libraryAnimations;
	}
}