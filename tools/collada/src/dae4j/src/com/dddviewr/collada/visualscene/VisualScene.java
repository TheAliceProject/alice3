package com.dddviewr.collada.visualscene;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import com.dddviewr.collada.Base;
import com.dddviewr.collada.nodes.Node;

public class VisualScene extends Base {
	protected String id;
	protected String name;
	protected List<Node> nodes = new ArrayList<Node>();

	public VisualScene(String id, String name) {
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

	public List<Node> getNodes() {
		return this.nodes;
	}

	public void dump(PrintStream out, int indent) {
		String prefix = createIndent(indent);
		out.println(prefix + "VisualScene (id: " + this.id + ", name: "
				+ this.name + ")");
		for (Node n : this.nodes)
			n.dump(out, indent + 1);
	}

	public void addNode(Node n) {
		this.nodes.add(n);
	}
}