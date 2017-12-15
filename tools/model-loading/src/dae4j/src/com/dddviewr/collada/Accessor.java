package com.dddviewr.collada;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class Accessor extends Base {
	protected String source;
	protected int count;
	protected int stride;
	protected List<Param> params = new ArrayList<Param>();

	public Accessor(String source, int count, int stride) {
		this.source = source;
		this.count = count;
		this.stride = stride;
	}

	public int getCount() {
		return this.count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getSource() {
		return this.source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public int getStride() {
		return this.stride;
	}

	public void setStride(int stride) {
		this.stride = stride;
	}

	public void addParam(Param param) {
		this.params.add(param);
	}

	public Param getParam(int index) {
		return ((Param) this.params.get(index));
	}

	public List<Param> getParams() {
		return this.params;
	}

	public void dump(PrintStream out, int indent) {
		String prefix = createIndent(indent);
		out.println(prefix + "Accessor (source: " + this.source + ", count: "
				+ this.count + ", stride: " + this.stride + ")");
		for (Param p : this.params)
			p.dump(out, indent + 1);
	}
}