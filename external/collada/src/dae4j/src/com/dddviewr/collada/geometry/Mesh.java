package com.dddviewr.collada.geometry;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.dddviewr.collada.Base;
import com.dddviewr.collada.Collada;
import com.dddviewr.collada.Input;
import com.dddviewr.collada.Source;
import com.dddviewr.collada.controller.Skin;


public class Mesh extends Base {
	protected List<Source> sources = new ArrayList<Source>();
	protected Vertices vertices;
	protected List<Primitives> primitives = new ArrayList<Primitives>();
	protected Geometry geometry;

	public void dump(PrintStream out, int indent) {
		String prefix = createIndent(indent);
		out.println(prefix + "Mesh");
		for (Source s : this.sources) {
			s.dump(out, indent + 1);
		}
		if (this.vertices != null)
			this.vertices.dump(out, indent + 1);
		for (Primitives p : primitives)
			p.dump(out, indent + 1);
	}

	public void addSource(Source s) {
		this.sources.add(s);
	}

	public Vertices getVertices() {
		return this.vertices;
	}

	public void setVertices(Vertices vertices) {
		this.vertices = vertices;
	}

	public void addPrimitives(Primitives p) {
		primitives.add(p);
	}

	public List<Primitives> getPrimitives() {
		return primitives;
	}

	public List<Source> getSources() {
		return this.sources;
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

	public Geometry getGeometry() {
		return this.geometry;
	}

	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}

	public void deindex(Collada collada) {
		int[] newIndexData;
		int i;
		int stride;
		float[] data;
		ArrayList<String> inputList = new ArrayList<String>();
		for (Primitives t : primitives) {
			for (Input inp : t.getInputs()) {
				String source = inp.getSource();
				if (!(inputList.contains(source)))
					inputList.add(source);
			}
		}
		int inputCount = inputList.size();

		Map<List<Integer>,Integer> indexMap = new LinkedHashMap<List<Integer>,Integer>();
		List<Integer[]> indexList = new ArrayList<Integer[]>();
		for (Primitives pr : this.primitives) {
			stride = pr.getStride();

			int[] oldData = pr.getData();
			int count = oldData.length / stride;
			
			List<Integer> newIndexList = new ArrayList<Integer>();
			for (i = 0; i < count; ++i) {
				Integer[] indices = new Integer[inputCount];
				for (Input inp : pr.getInputs()) {
					int value = oldData[i * stride + inp.getOffset()];
					indices[inputList.indexOf(inp.getSource())] = Integer
							.valueOf(value);
				}
				List<Integer> indicesList = Arrays.asList(indices);
				if (indexMap.containsKey(indicesList)) {
					newIndexList.add(indexMap
							.get(indicesList));
				} else {
					newIndexList.add(Integer.valueOf(indexList.size()));
					indexMap.put(indicesList, Integer.valueOf(indexList
							.size()));
					indexList.add(indices);
				}
			}
			
			newIndexData = new int[newIndexList.size()];
			i = 0;
			for (Integer value : newIndexList) {
				int val = value.intValue();
				newIndexData[(i++)] = val;
			}
			
			// zero input offsets
			for (Input inp :pr.getInputs()) {
				inp.setOffset(0);
			}

			pr.setData(newIndexData);
		}

		String verticesUrl = "#" + getVertices().getId();
		int vertPos = inputList.indexOf(verticesUrl);
		List<Input> vertInputs = this.vertices.getInputs();
		Set<String> vertSources = new HashSet<String>();
		for (Input inp : vertInputs) {
			vertSources.add(inp.getSource());
		}
		int newCount = indexList.size();
		for (Source src : this.sources) {
			int idxPos;
			String srcUrl = "#" + src.getId();
			if (vertSources.contains(srcUrl))
				idxPos = vertPos;
			else {
				idxPos = inputList.indexOf(srcUrl);
			}

			stride = src.getAccessor().getStride();
			data = src.getFloatArray().getData();
			float[] newData = new float[newCount * stride];
			for (int pos = 0; pos < newCount; ++pos) {
				int index = ((Integer[]) indexList.get(pos))[idxPos].intValue();
				for (int offset = 0; offset < stride; ++offset) {
					newData[(pos * stride + offset)] = data[(index * stride + offset)];
				}
			}
			src.getFloatArray().setData(newData);
			src.getFloatArray().setCount(stride * newCount);

			src.getAccessor().setCount(newCount);
		}

		int[] newVertList = new int[newCount];
		i = 0;
		for (Integer[] indices : indexList) {
			newVertList[(i++)] = indices[vertPos].intValue();
		}

		List<Skin> skins = collada.findSkins(this.geometry.getId());
		for (Skin skin : skins)
			skin.deindex(newVertList);
		
	}

	public Source getSourceFromSemantic(String sem) {
		for (Input inp : vertices.getInputs()) {
			if (inp.getSemantic().equals(sem)) {
				return getSource(inp.getSource());
			}
		}

		for (Primitives p : primitives) {
			for (Input inp : p.getInputs()) {
				if (inp.getSemantic().equals(sem)) {
					return getSource(inp.getSource());
				}
			}
		}

		return null;
	}

	public float[] getPositionData() {
		Source src = getSourceFromSemantic("POSITION");
		if (src == null)
			return null;
		return src.getFloatArray().getData();
	}

	public float[] getNormalData() {
		Source src = getSourceFromSemantic("NORMAL");
		if (src == null)
			return null;
		return src.getFloatArray().getData();
	}

	public float[] getTexCoordData() {
		Source src = getSourceFromSemantic("TEXCOORD");
		if (src == null)
			return null;
		return src.getFloatArray().getData();
	}

	public float[] getTangentData() {
		Source src = getSourceFromSemantic("TEXTANGENT");
		if (src == null)
			return null;
		return src.getFloatArray().getData();
	}
}