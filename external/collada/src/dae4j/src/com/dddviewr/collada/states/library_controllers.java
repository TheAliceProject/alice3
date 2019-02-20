package com.dddviewr.collada.states;

import org.xml.sax.Attributes;

import com.dddviewr.collada.Collada;
import com.dddviewr.collada.State;
import com.dddviewr.collada.StateManager;
import com.dddviewr.collada.controller.LibraryControllers;

public class library_controllers extends State {
	protected LibraryControllers libraryControllers;

	public void init(String name, Attributes attrs, StateManager mngr) {
		super.init(name, attrs, mngr);
		this.libraryControllers = new LibraryControllers();

		Collada collada = ((COLLADA) getParent()).getCollada();
		collada.setLibraryControllers(this.libraryControllers);
	}

	public LibraryControllers getLibrary() {
		return this.libraryControllers;
	}
}