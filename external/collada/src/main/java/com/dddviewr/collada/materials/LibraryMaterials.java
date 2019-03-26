package com.dddviewr.collada.materials;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import com.dddviewr.collada.Base;

public class LibraryMaterials extends Base {
	List<Material> materials = new ArrayList<Material>();

	public void dump(PrintStream out, int indent) {
		String prefix = createIndent(indent);
		out.println(prefix + "LibraryMaterials");

		for (Material mat : this.materials)
			mat.dump(out, indent + 1);
	}

	public List<Material> getMaterials() {
		return this.materials;
	}

	public void addMaterial(Material mat) {
		this.materials.add(mat);
	}
}