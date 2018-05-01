package com.dddviewr.collada.effects;

import java.io.PrintStream;

public class Blinn extends EffectMaterial {
	public void dump(PrintStream out, int indent) {
		String prefix = createIndent(indent);
		out.println(prefix + "Blinn");

		if (emission != null)
			emission.dump(out, indent + 1);
		if (ambient != null)
			ambient.dump(out, indent + 1);
		if (diffuse != null)
			diffuse.dump(out, indent + 1);
		if (specular != null)
			specular.dump(out, indent + 1);
		if (shininess != null)
			shininess.dump(out, indent + 1);
		if (reflective != null)
			reflective.dump(out, indent + 1);
		if (reflectivity != null)
			reflectivity.dump(out, indent + 1);
		if (transparent != null)
			transparent.dump(out, indent + 1);
		if (transparency != null)
			transparency.dump(out, indent + 1);
		if (bump != null)
			bump.dump(out, indent + 1);
	}
}