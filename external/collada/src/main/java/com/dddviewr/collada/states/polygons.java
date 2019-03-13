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


public class polygons extends State {
	protected PolyList thePolyList;
	protected Vcount theVcount = new Vcount();
	protected List<Input> inputs = new ArrayList<Input>();
	protected StringBuilder dataStr;
	protected int count;
	protected String material;
	protected ArrayList<Integer> data = new ArrayList<Integer>();
	private int pos;
	protected int theTriCount;


	public void init(String name, Attributes attrs, StateManager mngr) {
		count = Integer.parseInt(attrs.getValue("count"));
		material = attrs.getValue("material");
		super.init(name, attrs, mngr);
		theVcount.setData(new int[count]);
	}

	public void endElement(String name) {
		if("polygons".equals(name)) {
			thePolyList = new PolyList(material, count);
			for(Input inp: inputs)
				thePolyList.addInput(inp);
			thePolyList.setVcount(theVcount);
			int[] intData = new int[data.size()];
			for(int i = 0; i < intData.length; ++i) {
				intData[i] = data.get(i);
			}
			thePolyList.setData(intData);

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
		if(pos >= count) {
			return;
		}
		String[] vals = str.toString().split("\\s+");
		int dataCount = 0;
		for (String s : vals) {
			if (s.length() != 0) {
				data.add(Integer.parseInt(s));
				++dataCount;
			}
		}
		theVcount.getData()[pos] = dataCount / getStride();
		++pos;
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
