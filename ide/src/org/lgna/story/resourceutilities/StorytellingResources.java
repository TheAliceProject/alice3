package org.lgna.story.resourceutilities;

import java.io.File;
import java.util.List;

import org.alice.ide.ResourcePathManager;

public class StorytellingResources {
	private static class SingletonHolder {
		private static StorytellingResources instance = new StorytellingResources();
	}
	public static StorytellingResources getInstance() {
		return SingletonHolder.instance;
	}
	
	private final ModelResourceTree galleryTree;
	
	private StorytellingResources(){
		List<File> resourcePaths = ResourcePathManager.getPaths(ResourcePathManager.MODEL_RESOURCE_KEY);
		List<Class<?>> modelResourceClasses = ModelResourceUtilities.getAndLoadModelResourceClasses(resourcePaths);
		this.galleryTree = new ModelResourceTree(modelResourceClasses);
	}
	
	public List< org.lgna.project.ast.UserType<?> > getTopLevelGalleryTypes() {
		List<ModelResourceTreeNode> rootNodes = this.galleryTree.getRootNodes();
		List< org.lgna.project.ast.UserType<?> > rootTypes = edu.cmu.cs.dennisc.java.util.Collections.newArrayList();
		for (ModelResourceTreeNode node : rootNodes)
		{
			rootTypes.add(node.getValue());
		}
		return rootTypes;
	}
	
	public ModelResourceTreeNode getGalleryResourceTreeNodeForJavaType( org.lgna.project.ast.AbstractType< ?, ?, ? > type ) 
	{
		return this.galleryTree.getGalleryResourceTreeNodeForJavaType(type);
	}
	
	public ModelResourceTreeNode getGalleryResourceTreeNodeForUserType( org.lgna.project.ast.AbstractType< ?, ?, ? > type ) 
	{
		return this.galleryTree.getGalleryResourceTreeNodeForUserType(type);
	}
	
	public ModelResourceTreeNode getGalleryTree()
	{
		return galleryTree.getTree();
	}
	

}
