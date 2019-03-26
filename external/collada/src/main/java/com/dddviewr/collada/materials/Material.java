package com.dddviewr.collada.materials;

import java.io.PrintStream;

import com.dddviewr.collada.Base;

public class Material extends Base {
	protected String id;
	protected String name;
	protected InstanceEffect instanceEffect;

	public Material(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public InstanceEffect getInstanceEffect() {
		return this.instanceEffect;
	}

	public void setInstanceEffect(InstanceEffect instanceEffect) {
		this.instanceEffect = instanceEffect;
	}

	public String getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public void dump(PrintStream out, int indent) {
		String prefix = createIndent(indent);
		out.println(prefix + "Material (id: " + this.id + ", name: "
				+ this.name + ")");
		if (this.instanceEffect != null)
			this.instanceEffect.dump(out, indent + 1);
	}
}