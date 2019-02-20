package com.dddviewr.collada.geometry;

import java.io.PrintStream;

import com.dddviewr.collada.Input;
import com.dddviewr.collada.Vcount;


public class PolyList extends Primitives {
	protected Vcount theVcount;
	
	public PolyList(String material, int polyCount) {
		this.material = material;
		this.count = polyCount;
	}	
		
	public Vcount getVcount() {
		return theVcount;
	}

	public void setVcount(Vcount theVcount) {
		this.theVcount = theVcount;
	}

	public void dump(PrintStream out, int indent) {
		String prefix = createIndent(indent);
		out.println(prefix + "PolyList (material: " + this.material
				+ ", count: " + count + ")");
		for (Input i : this.inputs) {
			i.dump(out, indent + 1);
		}
		theVcount.dump(out, indent + 1);
		if (data != null) {
			out.print(prefix);
			for (int d : data) {
				out.print(" " + d);
			}
			out.println();
		}
	}
	
}
