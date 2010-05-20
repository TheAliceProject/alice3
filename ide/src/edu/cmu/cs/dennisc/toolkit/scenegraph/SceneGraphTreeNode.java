package edu.cmu.cs.dennisc.toolkit.scenegraph;

import java.awt.Color;

import javax.swing.tree.DefaultMutableTreeNode;

import org.alice.interact.handle.ManipulationHandle3D;

import edu.cmu.cs.dennisc.color.Color4f;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.Matrix3x3;
import edu.cmu.cs.dennisc.scenegraph.AsSeenBy;
import edu.cmu.cs.dennisc.scenegraph.Component;
import edu.cmu.cs.dennisc.scenegraph.Composite;
import edu.cmu.cs.dennisc.scenegraph.Element;
import edu.cmu.cs.dennisc.scenegraph.Geometry;
import edu.cmu.cs.dennisc.scenegraph.SingleAppearance;
import edu.cmu.cs.dennisc.scenegraph.Visual;

public class SceneGraphTreeNode extends DefaultMutableTreeNode implements Comparable
{
	public enum Difference
	{
		NONE,
		NEW_NODE,
		ATTRIBUTES
	}
	
	public Difference difference = Difference.NONE;
	
	public String name;
	public String className;
	private String trimmedClassName;
//	protected AffineMatrix4x4 localTransform;
	public AffineMatrix4x4 absoluteTransform;
	public StackTraceElement[] stackTrace;
	
	public int virtualParentHashCode = -1;
	public String virtualParentName = null;
	
	public boolean hasExtras;
	public Color4f color;
	public float opacity;
	public Matrix3x3 scale;
	public boolean isShowing;
	
	public int hashCode;
	
	public static SceneGraphTreeNode createSceneGraphTreeStructure( Component sgComponent )
	{
		SceneGraphTreeNode node = new SceneGraphTreeNode( sgComponent );
		if ( sgComponent instanceof Composite )
		{
			for ( Component c : ((Composite)sgComponent).getComponents() )
			{
				node.add( createSceneGraphTreeStructure(c) );
			}
		}
		if (sgComponent instanceof Visual)
		{
			Visual visual = (Visual)sgComponent;
			for (Geometry geometry : visual.geometries.getValue())
			{
				node.add( new SceneGraphTreeNode(geometry));
			}
		}
		return node;
	}
	
	@Override
	public boolean equals(Object obj) 
	{
		if (obj instanceof SceneGraphTreeNode)
		{
			return this.hashCode == ((SceneGraphTreeNode)obj).hashCode;	
		}
		return super.equals(obj);
	}
	
	private void setElementBasedData(Element element)
	{
		this.virtualParentHashCode = -1;
		if (element.containsBonusDataFor(ManipulationHandle3D.VIRTUAL_PARENT_KEY))
		{
			Object obj = element.getBonusDataFor(ManipulationHandle3D.VIRTUAL_PARENT_KEY);
			if (obj != null && obj instanceof Element)
			{
				Element virtualParent = (Element)obj;
				this.virtualParentHashCode = virtualParent.hashCode();
				this.virtualParentName = virtualParent.getName();
				if (this.virtualParentName == null)
				{
					String className = virtualParent.getClass().getName();
					String[] splitClassName = className.split("\\.");
					if (splitClassName.length > 0)
					{
						this.virtualParentName = splitClassName[splitClassName.length-1];
					}
				}
			}
		}
		if (element.containsBonusDataFor(Element.DEBUG_STACK_TRACK_PROPERTY_NAME))
		{
			this.stackTrace = (StackTraceElement[])element.getBonusDataFor(Element.DEBUG_STACK_TRACK_PROPERTY_NAME);
		}
		else
		{
			this.stackTrace = null;
		}
		this.name = element.getName();
		this.difference = Difference.NONE;
		this.className = element.getClass().getName();
		this.hashCode = element.hashCode();
		String[] splitClassName = this.className.split("\\.");
		if (splitClassName.length > 0)
		{
			this.trimmedClassName = splitClassName[splitClassName.length-1];
		}
	}

	public SceneGraphTreeNode( Geometry geometry )
	{
		super(); 
		setElementBasedData(geometry);
		this.absoluteTransform = null;
		this.hasExtras = false;
		this.color = null;
		this.scale = null;
		this.opacity = -1;
	}
	
	public SceneGraphTreeNode( Component sgComponent )
	{
		super();
		setElementBasedData(sgComponent);
		if (sgComponent.getRoot() != null && sgComponent.getParent() != null)
		{
//			Component root = sgComponent.getRoot();
//			Component parent = sgComponent.getParent();
//			this.localTransform = sgComponent.getTransformation(AsSeenBy.PARENT);
			this.absoluteTransform = sgComponent.getAbsoluteTransformation();
		}
		
		this.hasExtras = false;
		this.color = null;
		this.scale = null;
		this.opacity = -1;
		if (sgComponent instanceof Visual)
		{
			Visual visual = (Visual)sgComponent;
			if (visual.frontFacingAppearance.getValue() instanceof SingleAppearance)
			{	
				SingleAppearance appearance = (SingleAppearance)visual.frontFacingAppearance.getValue();
				this.color = new Color4f(appearance.diffuseColor.getValue());
				this.opacity = appearance.opacity.getValue();
			}
			this.scale = new Matrix3x3(visual.scale.getValue());
			this.isShowing = visual.isShowing.getValue();
			this.hasExtras = true;
		}
		
	}
	
	public boolean hasDifferentChild()
	{
		for (int i=0; i<this.getChildCount(); i++)
		{
			SceneGraphTreeNode child = (SceneGraphTreeNode)this.getChildAt(i);
			if (child.isDifferent())
			{
				return true;
			}
			else
			{
				if (child.hasDifferentChild())
				{
					return true;
				}
			}
		}
		return false;
	}
	
	public Color getAWTColor()
	{
		return new Color((int)(this.color.red*255), (int)(this.color.green*255), (int)(this.color.blue*255));
	}
	
	public boolean isDifferent()
	{
		return this.difference != Difference.NONE;
	}
	
	public void markDifferent(Difference difference)
	{
		this.difference = difference;
	}
	
	@Override
	public String toString() 
	{
		if (this.name == null || this.name.length() == 0)
		{
			return this.trimmedClassName;
		}
		return this.name;
	}
	
	public SceneGraphTreeNode getMatchingNode( int hashCode )
	{
		if (this.hashCode == hashCode)
		{
			return this;
		}
		else
		{
			for (int i=0; i<this.getChildCount(); i++)
			{
				SceneGraphTreeNode child = (SceneGraphTreeNode)this.getChildAt(i);
				SceneGraphTreeNode found = child.getMatchingNode(hashCode);
				if (found != null)
				{
					return found;
				}
			}
			return null;
		}
	}
	
	public SceneGraphTreeNode getMatchingNode( SceneGraphTreeNode toMatch )
	{
		if (this.compareTo(toMatch) == 0)
		{
			return this;
		}
		else
		{
			for (int i=0; i<this.getChildCount(); i++)
			{
				SceneGraphTreeNode child = (SceneGraphTreeNode)this.getChildAt(i);
				SceneGraphTreeNode found = child.getMatchingNode(toMatch);
				if (found != null)
				{
					return found;
				}
			}
			return null;
		}
	}
	
	public boolean isDifferent(SceneGraphTreeNode other)
	{
		if (other.hashCode != this.hashCode)
		{
			return true;
		}
		if (other.absoluteTransform != null && this.absoluteTransform != null)
		{
			
			if (!other.absoluteTransform.translation.isWithinReasonableEpsilonOf(other.absoluteTransform.translation))
			{
				return true;
			}
			if (!other.absoluteTransform.orientation.isWithinReasonableEpsilonOf(other.absoluteTransform.orientation))
			{
				return true;
			}
		}
		if (this.hasExtras && other.hasExtras)
		{
			if (other.opacity != this.opacity)
			{
				return true;
			}
			if (other.isShowing != this.isShowing)
			{
				return true;
			}
			if (!other.color.equals(this.color))
			{
				return true;
			}
		}
		return false;
	}
	
	public int compareTo(Object o) {
		if (o instanceof SceneGraphTreeNode)
		{
			SceneGraphTreeNode other = (SceneGraphTreeNode)o;
			if (this.hashCode < other.hashCode)
			{
				return -1;
			}
			else if (this.hashCode == other.hashCode)
			{
				return 0;
			}
			else
			{
				return 1;
			}
		}
		return 0;
	}
	
	
}
