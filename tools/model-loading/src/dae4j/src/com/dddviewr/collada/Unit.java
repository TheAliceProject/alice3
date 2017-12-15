package com.dddviewr.collada;

import java.io.PrintStream;

public class Unit extends Base {
	protected float meter = 1;
	protected String name = "meters";

	public Unit(float meter, String name) {
		this.meter = meter;
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getMeter() {
		return this.meter;
	}

	public void setMeter(float meter) {
		this.meter = meter;
	}
	
	public void dump(PrintStream out, int indent) {
		String prefix = createIndent(indent);
		out.print(prefix + "Unit (meter: " + this.meter + ", name: " + this.name);
	}
}
