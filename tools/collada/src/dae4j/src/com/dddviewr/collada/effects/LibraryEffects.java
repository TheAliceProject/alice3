package com.dddviewr.collada.effects;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import com.dddviewr.collada.Base;

public class LibraryEffects extends Base {
	protected List<Effect> effects = new ArrayList<Effect>();

	public void dump(PrintStream out, int indent) {
		String prefix = createIndent(indent);
		out.println(prefix + "LibraryEffects");

		for (Effect e : this.effects)
			e.dump(out, indent + 1);
	}

	public List<Effect> getEffects() {
		return this.effects;
	}

	public void setEffects(List<Effect> effects) {
		this.effects = effects;
	}

	public void addEffect(Effect effect) {
		this.effects.add(effect);
	}
}