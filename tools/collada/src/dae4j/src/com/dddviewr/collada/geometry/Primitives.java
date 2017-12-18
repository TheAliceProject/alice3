package com.dddviewr.collada.geometry;
import java.util.ArrayList;
import java.util.List;

import com.dddviewr.collada.Base;
import com.dddviewr.collada.Input;


public abstract class Primitives extends Base {

	protected String material;
	protected List<Input> inputs = new ArrayList<Input>();
	protected int count;
	protected int[] data;

	
	public String getMaterial() {
		return this.material;
	}

	public void setMaterial(String material) {
		this.material = material;
	}

	public void addInput(Input i) {
		this.inputs.add(i);
	}

	public List<Input> getInputs() {
		return this.inputs;
	}

	public int getStride() {
		int maxOffset = -1;
		for (Input i : this.inputs) {
			if (i.getOffset() > maxOffset)
				maxOffset = i.getOffset();
		}
		return (maxOffset + 1);
	}

	public int getCount() {
		return this.count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	public int[] getData() {
		return data;
	}

	public void setData(int[] data) {
		this.data = data;
	}	
}
