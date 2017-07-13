package edu.cmu.cs.dennisc.nebulous.javabased;

import java.util.LinkedList;
import java.util.List;

public class TextureGroup
{
	private int id;
	private String name;
	
	private List<Texture> textures = new LinkedList<Texture>();
	private List<String> textureNames;
	
	private Texture activeTexture;
	
	private boolean isMain = false;
	private boolean isSecondary = false;
	private boolean usesAlphaBlend = false;
	
	public TextureGroup(int id, String name)
	{
		this.id = id;
		this.name = name;
		this.textureNames = null;
	}
	
	public TextureGroup(int id, String name, List<String> textureNames)
	{
		this.id = id;
		this.name = name;
		this.textureNames = textureNames;
	}
	
	private boolean matches(String toMatch, String toCheck)
	{
		if (toMatch.equalsIgnoreCase(toCheck))
		{
			return true;
		}
		return false;
	}
	
	private void addMatchingTexture(String toMatch, List<Texture> textures)
	{
		for (Texture t : textures)
		{
			if (matches(toMatch, t.getName()))
			{
				this.textures.add(t);
			}
		}
	}
	
	public void setTextures(List<Texture> textures)
	{
		if (this.textureNames == null)
		{
			this.addMatchingTexture(this.name, textures);
		}
		else
		{
			for (String textureName : this.textureNames)
			{
				this.addMatchingTexture(textureName, textures);
			}
		}
		if (this.textures.size() > 0)
		{
			this.activeTexture = this.textures.get(0);
		}
	}
	
	public int getID()
	{
		return this.id;
	}
	
	public Texture getActiveTexture()
	{
		return this.activeTexture;
	}
	
	public void setActiveTexture(int index)
	{
		this.activeTexture = this.textures.get(index);
	}
	
	public int getTextureCount()
	{
		return this.textures.size();
	}
	
	public Texture getTexture(int index)
	{
		return this.textures.get(index);
	}
	
	public void setIsMain(boolean isMain) {
		this.isMain = isMain;
	}
	
	public boolean isMain() {
		return this.isMain;
	}
	
	public void setIsSecondary(boolean isSecondary) {
		this.isSecondary = isSecondary;
	}
	
	public boolean isSecondary() {
		return this.isSecondary;
	}
	
	public void setUsesAlphaBlend(boolean usesAlphaBlend) {
		this.usesAlphaBlend = usesAlphaBlend;
	}
	
	public boolean usesAlphaBlend() {
		return this.usesAlphaBlend;
	}
	
}
