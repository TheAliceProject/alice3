package edu.cmu.cs.dennisc.nebulous.javabased;

import java.io.InputStream;
import java.io.OutputStream;

public class BlendShape {
	private VerticesAndNormals verticesAndNormals;
	int[] indices;

	public BlendShape()
	{
		this.verticesAndNormals = null;
		this.indices = new int[0];
	}
	
	public void read(InputStream iStream, int nVersion)
	{
		Element element = Element.construct(iStream, nVersion);
		assert element instanceof VerticesAndNormals;
		this.verticesAndNormals = (VerticesAndNormals)element;
		try
		{
			this.indices = Utilities.readIntArray(iStream);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void write(OutputStream oStream)
	{
		try
		{
			this.verticesAndNormals.writeWithoutVersion(oStream);
			Utilities.writeIntArray(oStream, this.indices);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public int[] getIndices()
	{
		return this.indices;
	}
	
	public int getIndexCount()
	{
		return this.indices.length;
	}
	
	public VerticesAndNormals getVerticesAndNormals() 
	{
		return this.verticesAndNormals;
	}
	
}
