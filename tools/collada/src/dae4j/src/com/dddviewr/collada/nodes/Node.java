package com.dddviewr.collada.nodes;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import com.dddviewr.collada.Base;
import com.dddviewr.collada.visualscene.BaseXform;
import com.dddviewr.collada.visualscene.InstanceController;
import com.dddviewr.collada.visualscene.InstanceGeometry;
import com.dddviewr.collada.visualscene.InstanceNode;
import com.dddviewr.collada.visualscene.Matrix;

public class Node extends Base {
	protected String id;
	protected String name;
	protected String sid;
	protected String type;
	protected List<BaseXform> xforms = new ArrayList<BaseXform>();
	protected List<InstanceGeometry> instanceGeometry = new ArrayList<InstanceGeometry>();
	protected InstanceController instanceController;
	protected InstanceNode instanceNode;
	protected List<Node> childNodes = new ArrayList<Node>();
	protected Matrix matrix;

	public Node(String id, String name, String sid, String type) {
		this.id = id;
		this.name = name;
		this.sid = sid;
		this.type = type;
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

	public String getSid() {
		return this.sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<BaseXform> getXforms() {
		return this.xforms;
	}

	public List<InstanceGeometry> getInstanceGeometry() {
		return this.instanceGeometry;
	}

	public void setInstanceGeometry(List<InstanceGeometry> instanceGeometry) {
		this.instanceGeometry = instanceGeometry;
	}
	
	public void addInstanceGeometry(InstanceGeometry instanceGeometry) {
		this.instanceGeometry.add(instanceGeometry);
	}

	public InstanceController getInstanceController() {
		return this.instanceController;
	}

	public void setInstanceController(InstanceController instanceController) {
		this.instanceController = instanceController;
	}
	
	public InstanceNode getInstanceNode() {
		return this.instanceNode;
	}

	public void setInstanceNode(InstanceNode instanceNode) {
		this.instanceNode = instanceNode;
	}

	public List<Node> getChildNodes() {
		return this.childNodes;
	}

	public void setChildNodes(List<Node> childNodes) {
		this.childNodes = childNodes;
	}
	
	public Matrix getMatrix() {
		return this.matrix;
	}

	public void setMatrix(Matrix matrix) {
		this.matrix = matrix;
	}

	public void dump(PrintStream out, int indent) {
		String prefix = createIndent(indent);
		out.print(prefix + "Node (id: " + this.id + ", name: " + this.name);
		if (this.sid != null)
			out.print(", sid: " + this.sid);
		out.println(", type: " + this.type + ")");
		for (BaseXform xform : this.xforms) {
			xform.dump(out, indent + 1);
		}
		for (InstanceGeometry geom : this.instanceGeometry) {
			geom.dump(out, indent + 1);
		}
		if (this.instanceController != null)
			this.instanceController.dump(out, indent + 1);
		if (this.instanceNode != null)
			this.instanceNode.dump(out, indent + 1);
		for (Node n : this.childNodes)
			n.dump(out, indent + 1);
	}

	public void addXform(BaseXform xform) {
		this.xforms.add(xform);
	}

	public void addNode(Node n) {
		this.childNodes.add(n);
	}
}