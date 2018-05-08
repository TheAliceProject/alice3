package com.dddviewr.collada.visualscene;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import com.dddviewr.collada.Base;

public class InstanceController extends Base {
	protected String url;
	protected String skeleton;
	protected List<InstanceMaterial> instanceMaterials = new ArrayList<InstanceMaterial>();

	public InstanceController(String url) {
		this.url = url;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getSkeleton() {
		return this.skeleton;
	}

	public void setSkeleton(String skeleton) {
		this.skeleton = skeleton;
	}

	public List<InstanceMaterial> getInstanceMaterials() {
		return this.instanceMaterials;
	}

	public void addInstanceMaterial(InstanceMaterial mat) {
		this.instanceMaterials.add(mat);
	}

	public void dump(PrintStream out, int indent) {
		String prefix = createIndent(indent);
		out.println(prefix + "InstanceController (url: " + this.url + ")");
		if (this.skeleton != null) {
			out.println(prefix + " Skeleton: " + this.skeleton);
		}
		for (InstanceMaterial mat : this.instanceMaterials)
			mat.dump(out, indent + 1);
	}
}