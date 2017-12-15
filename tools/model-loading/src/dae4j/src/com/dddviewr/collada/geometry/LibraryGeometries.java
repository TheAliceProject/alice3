package com.dddviewr.collada.geometry;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import com.dddviewr.collada.Base;

public class LibraryGeometries extends Base {
	protected List<Geometry> geometries = new ArrayList<Geometry>();

	public void addGeometry(Geometry geo) {
		this.geometries.add(geo);
	}

	public List<Geometry> getGeometries() {
		return this.geometries;
	}

	public void dump(PrintStream out, int indent) {
		String prefix = createIndent(indent);
		out.println(prefix + "LibraryGeometries");
		for (Geometry g : this.geometries)
			g.dump(out, indent + 1);
	}
}