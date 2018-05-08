package com.dddviewr.collada.geometry;

import java.io.PrintStream;

import com.dddviewr.collada.Base;

public class Geometry extends Base {
	protected String id;
	protected String name;
	protected Mesh mesh;

	public Geometry(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setMesh(Mesh mesh) {
		this.mesh = mesh;
		mesh.setGeometry(this);
	}

	public Mesh getMesh() {
		return this.mesh;
	}

	public void dump(PrintStream out, int indent) {
		String prefix = createIndent(indent);
		out.println(prefix + "Geometry (id: " + this.id + ", name: "
				+ this.name + ")");
		if (this.mesh != null)
			this.mesh.dump(out, indent + 1);
	}
}