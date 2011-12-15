package edu.cmu.cs.dennisc.scenegraph;

import java.util.LinkedList;
import java.util.List;

public class TextureMeshAssociation {
	private TexturedAppearance textureAppearance;
	private List<WeightedMesh> meshes;
	
	public TextureMeshAssociation(TexturedAppearance textureAppearance)
	{
		this.textureAppearance = textureAppearance;
		this.meshes = new LinkedList<WeightedMesh>();
	}
	
	public void addMesh(WeightedMesh mesh)
	{
		this.meshes.add(mesh);
	}
	
	public TexturedAppearance getAppearance()
	{
		return this.textureAppearance;
	}
	
	public WeightedMesh getMesh(int index)
	{
		return this.meshes.get(index);
	}
	
	public List<WeightedMesh> getMeshes()
	{
		return this.meshes;
	}
	
	public int meshCount()
	{
		return this.meshes.size();
	}
	
	public void release()
	{
		if (this.textureAppearance != null)
		{
			this.textureAppearance.release();
		}
		for( Mesh mesh : meshes ) {
			mesh.release();
        }
	}
}
