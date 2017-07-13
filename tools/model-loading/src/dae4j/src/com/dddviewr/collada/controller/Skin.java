package com.dddviewr.collada.controller;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.dddviewr.collada.Base;
import com.dddviewr.collada.Input;
import com.dddviewr.collada.Source;


public class Skin extends Base {
	protected String source;
	protected List<Source> sources = new ArrayList<Source>();
	protected Joints joints;
	protected VertexWeights vertexWeights;
	protected Controller controller;
	protected float[] bindShapeMatrix;

	public Skin(String source) {
		this.source = source;
	}

	public String getSource() {
		return this.source;
	}

	public List<Source> getSources() {
		return this.sources;
	}

	public void addSource(Source src) {
		this.sources.add(src);
	}

	public Joints getJoints() {
		return this.joints;
	}

	public void setJoints(Joints joints) {
		this.joints = joints;
	}

	public VertexWeights getVertexWeights() {
		return this.vertexWeights;
	}

	public void setVertexWeights(VertexWeights vertexWeights) {
		this.vertexWeights = vertexWeights;
	}

	public Controller getController() {
		return this.controller;
	}

	public void setController(Controller controller) {
		this.controller = controller;
	}

	public float[] getBindShapeMatrix() {
		return this.bindShapeMatrix;
	}

	public void dump(PrintStream out, int indent) {
		String prefix = createIndent(indent);
		out.println(prefix + "Skin (source: " + this.source + ")");
		if (this.bindShapeMatrix != null)
			out.println(prefix + " BindShapeMatrix: "
					+ Arrays.toString(this.bindShapeMatrix));
		for (Source src : this.sources) {
			src.dump(out, indent + 1);
		}
		if (this.joints != null)
			this.joints.dump(out, indent + 1);
		if (this.vertexWeights != null)
			this.vertexWeights.dump(out, indent + 1);
	}

	public void deindex(int[] newVertList) {
		int i;
		int l;
		if (this.vertexWeights == null)
			return;
		int oldCount = this.vertexWeights.getCount();
		int[] oldVcount = this.vertexWeights.getVcount().getData();
		int[] oldData = this.vertexWeights.getData();

		int vsize = 0;
		for (Input inp : this.vertexWeights.getInputs()) {
			if (inp.getOffset() <= vsize)
				continue;
			vsize = inp.getOffset();
		}
		++vsize;

		int[] oldOffsets = new int[oldCount];

		int offset = 0;
		for (i = 0; i < oldCount; ++i) {
			oldOffsets[i] = offset;
			offset += oldVcount[i] * vsize;
		}

		int newCount = newVertList.length;
		int[] newVcount = new int[newCount];

		int newDataCount = 0;
		for (int idx : newVertList) {
			newDataCount += oldVcount[idx] * vsize;
		}

		int[] newData = new int[newDataCount];

		l = 0;
		for (int j = 0; j < newCount; ++j) {
			int i2;
			newVcount[j] = oldVcount[newVertList[j]];
			offset = oldOffsets[newVertList[j]];
			for (i2 = 0; i2 < newVcount[j] * vsize; ++i2) {
				newData[(l++)] = oldData[(offset + i2)];
			}
		}

		this.vertexWeights.setCount(newCount);
		this.vertexWeights.setData(newData);
		this.vertexWeights.getVcount().setData(newVcount);
	}

	public Source getSource(String id) {
		String searchId = id;
		if (id.startsWith("#"))
			searchId = id.substring(1);
		for (Source s : this.sources) {
			if (s.getId().equals(searchId))
				return s;
		}
		return null;
	}

	public Source getSourceFromSemantic(String sem) {
		for (Input inp:this.joints.getInputs()) {
			if (inp.getSemantic().equals(sem)) {
				return getSource(inp.getSource());
			}
		}

		for (Input inp : this.vertexWeights.getInputs()) {
			if (inp.getSemantic().equals(sem)) {
				return getSource(inp.getSource());
			}
		}

		return null;
	}

	public String[] getJointData() {
		Source src = getSourceFromSemantic("JOINT");
		if (src == null)
			return null;
		if (src.getIdrefArray() != null)
			return src.getIdrefArray().getData();
		if (src.getNameArray() != null)
			return src.getNameArray().getData();
		return null;
	}

	public float[] getInvBindMatrixData() {
		Source src = getSourceFromSemantic("INV_BIND_MATRIX");
		if (src == null)
			return null;
		return src.getFloatArray().getData();
	}

	public float[] getWeightData() {
		Source src = getSourceFromSemantic("WEIGHT");
		if (src == null)
			return null;
		return src.getFloatArray().getData();
	}

	public void parseBindShapeMatrix(StringBuilder str) {
		String[] values = str.toString().split("\\s+");
		float[] data = new float[16];
		int index = 0;
		for (String s : values) {
			if (index >= data.length)
				break;
			if (s.length() != 0)
				data[(index++)] = Float.parseFloat(s);
		}
		this.bindShapeMatrix = data;
	}
}