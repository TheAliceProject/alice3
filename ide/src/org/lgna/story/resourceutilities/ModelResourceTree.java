package org.lgna.story.resourceutilities;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.alice.ide.ResourcePathManager;
import org.lgna.project.ast.JavaField;
import org.lgna.project.ast.NamedUserConstructor;
import org.lgna.project.ast.NamedUserType;
import org.lgna.project.ast.UserField;
import org.lgna.project.ast.UserMethod;
import org.lgna.project.ast.UserPackage;
import org.lgna.story.resources.ModelResource;


public class ModelResourceTree {
	
	private Map<Class<?>, Class<?>> resourceClassToModelClassMap = new HashMap<Class<?>, Class<?>>();
	private Map<Object, ModelResourceTreeNode> resourceClassToNodeMap = new HashMap<Object, ModelResourceTreeNode>();
	
	private final ModelResourceTreeNode galleryTree;
	
	public ModelResourceTree(List<Class<?>> classes)
	{
		this.galleryTree = this.createClassTree(classes);
		
	}
	
	public ModelResourceTreeNode getTree()
	{
		return this.galleryTree;
	}
	
	public List<ModelResourceTreeNode> getRootNodes()
	{
		LinkedList<ModelResourceTreeNode> rootNodes = new LinkedList<ModelResourceTreeNode>();
		if (this.galleryTree != null)
		{
			for (int i=0; i<this.galleryTree.getChildCount(); i++)
			{
				rootNodes.add((ModelResourceTreeNode)this.galleryTree.getChildAt(i));
			}
		}
		return rootNodes;
	}
	
	public ModelResourceTreeNode getGalleryResourceTreeNodeForUserType( org.lgna.project.ast.AbstractType< ?, ?, ? > type ) 
	{
		return this.galleryTree.getDescendantOfUserType(type);
	}
	
	public ModelResourceTreeNode getGalleryResourceTreeNodeForJavaType( org.lgna.project.ast.AbstractType< ?, ?, ? > type ) 
	{
		return this.galleryTree.getDescendantOfJavaType(type);
	}
	
	public List<? extends ModelResourceTreeNode> getGalleryResourceChildrenForJavaType( org.lgna.project.ast.AbstractType< ?, ?, ? > type ) 
	{
		ModelResourceTreeNode node = this.galleryTree.getDescendantOfJavaType(type);
		if (node != null)
		{
			return node.childrenList();
		}
		return new LinkedList<ModelResourceTreeNode>();
	}
	
	//The Stack<Class<?>> classes is a stack of classes representing the hierarchy of the classes, with the parent class at the top of the stack
	private ModelResourceTreeNode addNodes(ModelResourceTreeNode root, Stack<Class<?>> classes)
	{
		Class<?> rootClass = null;
		ModelResourceTreeNode currentNode = root;
		while (!classes.isEmpty())
		{
			Class<?> currentClass = classes.pop();
			//The root class is the one at the top of the stack, so grab it the first time around
			if (rootClass == null)
			{
				rootClass = currentClass;
			}
			ModelResourceTreeNode parentNode = currentNode;
			ModelResourceTreeNode classNode = null;
			if (resourceClassToNodeMap.containsKey(currentClass))
			{
				classNode = resourceClassToNodeMap.get(currentClass);
			}
			//Build a new ModelResourceTreeNode for the current class
			if (classNode == null)
			{
				NamedUserType aliceType = null;
				String aliceClassName = ModelResourceUtilities.getAliceClassName(currentClass);
				UserPackage packageName = ModelResourceUtilities.getAlicePackage(currentClass, rootClass);
				UserMethod[] methods = {};
				UserField[] fields = {};
				if (parentNode == null || parentNode.getUserType() == null)
				{
					Class<?> parentClass = ModelResourceUtilities.getModelClassForResourceClass(currentClass);
					ConstructorParameterPair parentConstructorAndParameter = ModelResourceUtilities.getConstructorAndParameterForJavaClass(parentClass);
					NamedUserConstructor constructor = ModelResourceUtilities.createConstructorForResourceClass(currentClass, parentConstructorAndParameter);
					NamedUserConstructor[] constructors = {constructor};
					aliceType = new NamedUserType(aliceClassName, packageName, parentClass, constructors, methods, fields);
				}
				else
				{
					NamedUserType parentType = parentNode.getUserType();
					ConstructorParameterPair parentConstructorAndParameter = ModelResourceUtilities.getConstructorAndParameterForAliceClass(parentType);
					NamedUserConstructor constructor = ModelResourceUtilities.createConstructorForResourceClass(currentClass, parentConstructorAndParameter);
					NamedUserConstructor[] constructors = {constructor};
					aliceType = new NamedUserType(aliceClassName, packageName, parentType, constructors, methods, fields);
				}
				classNode = new ModelResourceTreeNode(aliceType, currentClass);
				resourceClassToNodeMap.put(currentClass, classNode);
				if (root == null) //if the root node passed in is null, assign it to be the node from the first class we process
				{
					root = classNode;
				}
				Field[] resourceConstants = currentClass.getFields();
				if (resourceConstants.length != 0)
				{
					for (Field f : resourceConstants)
					{
						String fieldClassName = ModelResourceUtilities.getClassNameFromName(f.getName())+aliceClassName;
						NamedUserType parentType = classNode.getUserType();
						ConstructorParameterPair parentConstructorAndParameter = ModelResourceUtilities.getConstructorAndParameterForAliceClass(parentType);
						NamedUserConstructor constructor = ModelResourceUtilities.createConstructorForResourceField(f, parentConstructorAndParameter);
						NamedUserConstructor[] constructors = {constructor};
						NamedUserType fieldType = new NamedUserType(fieldClassName, packageName, parentType, constructors, methods, fields);
						ModelResourceTreeNode fieldNode = new ModelResourceTreeNode(fieldType, currentClass);
						try
						{
							ModelResource resource = (ModelResource)f.get(null);
							fieldNode.setModelResourceInstance(resource);
							JavaField javaField = JavaField.getInstance(f);
							fieldNode.setJavaField(javaField);
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
						fieldNode.setParent(classNode);
						resourceClassToNodeMap.put(f, fieldNode);
					}
				}
				
			}
			classNode.setParent(parentNode);
			currentNode = classNode;
		}
		return root;
	}
	
	private  ModelResourceTreeNode createClassTree(List<Class<?>> classes)
	{
		ModelResourceTreeNode topNode = new ModelResourceTreeNode(null, null);
		for (Class<?> cls : classes)
		{
			Class<?> currentClass = cls;
			Stack<Class<?>> classStack = new Stack<Class<?>>();			
			Class<?>[] interfaces = null;
			while (currentClass != null)
			{
				classStack.push(currentClass);
				Class<?> modelClass = ModelResourceUtilities.getModelClassForResourceClass(currentClass);
				if (modelClass != null)
				{
					if (!resourceClassToModelClassMap.containsKey(currentClass))
					{
						resourceClassToModelClassMap.put(currentClass, modelClass);
					}
					break;
				}
				
				interfaces = currentClass.getInterfaces();
				if (interfaces != null && interfaces.length > 0)
				{
					currentClass = interfaces[0];
				}
				else
				{
					currentClass = null;
				}
			}
			addNodes(topNode, classStack);
		}
		topNode.printTree();
		return topNode;
	}

}
