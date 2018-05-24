package com.dddviewr.collada.visualscene;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import com.dddviewr.collada.Base;

public class InstanceGeometry extends Base {
	protected String url;
	protected List<InstanceMaterial> instanceMaterials = new ArrayList<InstanceMaterial>();

	public InstanceGeometry(String url) {
		this.url = url;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<InstanceMaterial> getInstanceMaterials() {
		return this.instanceMaterials;
	}

	public void addInstanceMaterial(InstanceMaterial mat) {
		this.instanceMaterials.add(mat);
	}

	public void dump(PrintStream out, int indent) {
		String prefix = createIndent(indent);
		out.println(prefix + "InstanceGeometry (url: " + this.url + ")");

		for (InstanceMaterial mat : this.instanceMaterials)
			mat.dump(out, indent + 1);
	}
}