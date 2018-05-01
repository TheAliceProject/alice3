package com.dddviewr.collada.states;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;

import com.dddviewr.collada.Input;
import com.dddviewr.collada.State;
import com.dddviewr.collada.StateManager;
import com.dddviewr.collada.Vcount;
import com.dddviewr.collada.geometry.Mesh;
import com.dddviewr.collada.geometry.PolyList;



public class polylist extends State {
	protected PolyList thePolyList;
	protected Vcount theVcount;
	protected List<Input> inputs = new ArrayList<Input>();
	protected StringBuilder dataStr;
	protected int count;
	protected String material;
	protected int[] data;
	private int pos;
	protected int theTriCount;


	public void init(String name, Attributes attrs, StateManager mngr) {
		count = Integer.parseInt(attrs.getValue("count"));
		material = attrs.getValue("material");
		super.init(name, attrs, mngr);
	}

	public void endElement(String name) {
		if("polylist".equals(name)) {
			thePolyList = new PolyList(material, count);
			for(Input inp: inputs)
				thePolyList.addInput(inp);
			thePolyList.setVcount(theVcount);
			thePolyList.setData(data);

			Mesh mesh = ((mesh) getParent()).getMesh();
			mesh.addPrimitives(thePolyList);
		}
		super.endElement(name);
	}
	
	public PolyList getPolyList() {
		return thePolyList;
	}

	public void addInput(Input i) {
		inputs.add(i);
	}

	public void addData(StringBuilder str) {
		if (data == null) {
			int count = 0;
			for(int c: theVcount.getData())
				count += c;
			data = new int[count * getStride()];
			pos = 0;
		}

		String[] vals = str.toString().split("\\s+");
		for (String s : vals) {
			if (pos >= data.length)
				return;
			if (s.length() != 0)
				data[(pos++)] = Integer.parseInt(s);
		}
	}

	public Vcount getVcount() {
		return theVcount;
	}

	public void setVcount(Vcount theVcount) {
		this.theVcount = theVcount;
	}
	
	public int getStride() {
		int maxOffset = -1;
		for (Input i : inputs) {
			if (i.getOffset() > maxOffset)
				maxOffset = i.getOffset();
		}
		return (maxOffset + 1);
	}
}
