package com.dddviewr.collada.controller;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import com.dddviewr.collada.Base;

public class LibraryControllers extends Base {
	protected List<Controller> controllers = new ArrayList<Controller>();

	public void dump(PrintStream out, int indent) {
		String prefix = createIndent(indent);
		out.println(prefix + "LibraryControllers");
		for (Controller c : this.controllers)
			c.dump(out, indent + 1);
	}

	public List<Controller> getControllers() {
		return this.controllers;
	}

	public void addController(Controller c) {
		this.controllers.add(c);
	}

	public List<Skin> findSkins(String source) {
		List<Skin> result = new ArrayList<Skin>();
		for (Controller c : this.controllers) {
			if (c.getSkin() == null)
				continue;
			String skinSrc = c.getSkin().getSource();
			if (skinSrc == null)
				continue;
			String tmpSrc = (skinSrc.indexOf(35) == 0) ? skinSrc.substring(1)
					: skinSrc;
			if (!(tmpSrc.equals(source)))
				continue;
			result.add(c.getSkin());
		}
		return result;
	}
}