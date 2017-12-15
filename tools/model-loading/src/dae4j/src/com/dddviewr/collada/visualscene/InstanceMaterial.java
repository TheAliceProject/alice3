package com.dddviewr.collada.visualscene;

import java.io.PrintStream;

import com.dddviewr.collada.Base;

public class InstanceMaterial extends Base {
	protected String symbol;
	protected String target;

	public InstanceMaterial(String symbol, String target) {
		this.symbol = symbol;
		this.target = target;
	}

	public String getSymbol() {
		return this.symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getTarget() {
		return this.target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public void dump(PrintStream out, int indent) {
		String prefix = createIndent(indent);
		out.println(prefix + "InstanceMaterial (symbol: " + this.symbol
				+ ", target: " + this.target + ")");
	}
}