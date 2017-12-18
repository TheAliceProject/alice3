package com.dddviewr.collada.nodes;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import com.dddviewr.collada.Base;

public class LibraryNodes extends Base {

	protected List<Node> nodes = new ArrayList<Node>();

	public void addNode(Node geo) {
		this.nodes.add(geo);
	}

	public List<Node> getNodes() {
		return this.nodes;
	}
	
	@Override
	public void dump(PrintStream out, int indent) {
		String prefix = createIndent(indent);
		out.println(prefix + "LibraryNodes");
		for (Node n : this.nodes)
			n.dump(out, indent + 1);
		
	}

}
