package com.dddviewr.collada.states;

import org.xml.sax.Attributes;

import com.dddviewr.collada.State;
import com.dddviewr.collada.StateManager;
import com.dddviewr.collada.controller.Controller;
import com.dddviewr.collada.controller.LibraryControllers;

public class controller extends State {
	protected Controller theController;

	public void init(String name, Attributes attrs, StateManager mngr) {
		super.init(name, attrs, mngr);
		this.theController = new Controller(attrs.getValue("id"), attrs
				.getValue("name"));

		LibraryControllers library = ((library_controllers) getParent())
				.getLibrary();
		library.addController(this.theController);
	}

	public Controller getController() {
		return this.theController;
	}
}