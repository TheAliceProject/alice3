package com.dddviewr.collada.states;

import org.xml.sax.Attributes;

import com.dddviewr.collada.State;
import com.dddviewr.collada.StateManager;
import com.dddviewr.collada.materials.LibraryMaterials;
import com.dddviewr.collada.materials.Material;

public class material extends State {
	protected Material theMaterial;

	public void init(String name, Attributes attrs, StateManager mngr) {
		super.init(name, attrs, mngr);

		this.theMaterial = new Material(attrs.getValue("id"), attrs
				.getValue("name"));
		LibraryMaterials lib = ((library_materials) getParent()).getLibrary();
		lib.addMaterial(this.theMaterial);
	}

	public Material getMaterial() {
		return this.theMaterial;
	}
}