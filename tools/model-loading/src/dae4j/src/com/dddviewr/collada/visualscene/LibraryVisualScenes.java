package com.dddviewr.collada.visualscene;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import com.dddviewr.collada.Base;

public class LibraryVisualScenes extends Base {
	List<VisualScene> visualScenes = new ArrayList<VisualScene>();

	public void addVisualScene(VisualScene vs) {
		this.visualScenes.add(vs);
	}

	public void dump(PrintStream out, int indent) {
		String prefix = createIndent(indent);
		out.println(prefix + "LibraryVisualScenes");
		for (VisualScene vs : this.visualScenes)
			vs.dump(out, indent + 1);
	}

	public VisualScene getScene(String id) {
		String searchId = id;
		if (searchId.startsWith("#"))
			searchId = id.substring(1);
		for (VisualScene sc : this.visualScenes) {
			if (sc.getId().equals(searchId))
				return sc;
		}
		return null;
	}
}