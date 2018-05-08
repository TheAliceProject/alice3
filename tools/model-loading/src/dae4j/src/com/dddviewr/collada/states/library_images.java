package com.dddviewr.collada.states;

import org.xml.sax.Attributes;

import com.dddviewr.collada.Collada;
import com.dddviewr.collada.State;
import com.dddviewr.collada.StateManager;
import com.dddviewr.collada.images.LibraryImages;

public class library_images extends State {
	protected LibraryImages library = new LibraryImages();

	public void init(String name, Attributes attrs, StateManager mngr) {
		super.init(name, attrs, mngr);
		Collada collada = ((COLLADA) getParent()).getCollada();
		collada.setLibraryImages(this.library);
	}

	public LibraryImages getLibrary() {
		return this.library;
	}
}