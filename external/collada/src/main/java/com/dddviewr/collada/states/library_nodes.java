package com.dddviewr.collada.states;

import org.xml.sax.Attributes;

import com.dddviewr.collada.Collada;
import com.dddviewr.collada.State;
import com.dddviewr.collada.StateManager;
import com.dddviewr.collada.nodes.LibraryNodes;
import com.dddviewr.collada.nodes.Node;

public class library_nodes extends State {
	protected LibraryNodes library = new LibraryNodes();

	public void init(String name, Attributes attrs, StateManager mngr) {
		super.init(name, attrs, mngr);

		Collada collada = ((COLLADA) getParent()).getCollada();
		collada.setLibraryNodes(this.library);
	}

	public LibraryNodes getLibrary() {
		return this.library;
	}
	
	public void addNode(Node node) {
		library.addNode(node);
	}
}
