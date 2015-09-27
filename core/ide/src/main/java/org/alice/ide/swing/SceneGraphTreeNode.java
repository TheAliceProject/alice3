/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may
 *    "Alice" appear in their name, without prior written permission of
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is
 *    contributed by Electronic Arts Inc. and may be used for personal,
 *    non-commercial, and academic use only. Redistributions of any program
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in
 *    The Alice 3.0 Art Gallery License.
 *
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *******************************************************************************/
package org.alice.ide.swing;

import org.alice.interact.handle.ManipulationHandle3D;

import edu.cmu.cs.dennisc.color.Color4f;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.Matrix3x3;
import edu.cmu.cs.dennisc.scenegraph.Component;
import edu.cmu.cs.dennisc.scenegraph.Composite;
import edu.cmu.cs.dennisc.scenegraph.Element;
import edu.cmu.cs.dennisc.scenegraph.Geometry;
import edu.cmu.cs.dennisc.scenegraph.TexturedAppearance;
import edu.cmu.cs.dennisc.scenegraph.Visual;

/*package-private*/class SceneGraphTreeNode extends BasicTreeNode
{
	//	protected AffineMatrix4x4 localTransform;
	public AffineMatrix4x4 absoluteTransform;
	public StackTraceElement[] stackTrace;

	public int virtualParentHashCode = -1;
	public String virtualParentName = null;

	public float opacity;
	public Matrix3x3 scale;
	public boolean isShowing;

	public int parentHash;
	public String parentName;

	public static SceneGraphTreeNode createSceneGraphTreeStructure( Component sgComponent )
	{
		SceneGraphTreeNode node = new SceneGraphTreeNode( sgComponent );
		if( sgComponent instanceof Composite )
		{
			for( Component c : ( (Composite)sgComponent ).getComponents() )
			{
				node.add( createSceneGraphTreeStructure( c ) );
			}
		}
		if( sgComponent instanceof Visual )
		{
			Visual visual = (Visual)sgComponent;
			for( Geometry geometry : visual.geometries.getValue() )
			{
				node.add( new SceneGraphTreeNode( geometry ) );
			}
		}
		return node;
	}

	@Override
	protected void setData( Object object )
	{
		super.setData( object );
		if( object instanceof Element )
		{
			this.setElementBasedData( (Element)object );
		}
	}

	private void setElementBasedData( Element element )
	{
		this.virtualParentHashCode = -1;
		if( element.containsBonusDataFor( ManipulationHandle3D.DEBUG_PARENT_TRACKER_KEY ) )
		{
			Object obj = element.getBonusDataFor( ManipulationHandle3D.DEBUG_PARENT_TRACKER_KEY );
			if( ( obj != null ) && ( obj instanceof Element ) )
			{
				Element virtualParent = (Element)obj;
				this.virtualParentHashCode = virtualParent.hashCode();
				this.virtualParentName = virtualParent.getName();
				if( this.virtualParentName == null )
				{
					String className = virtualParent.getClass().getName();
					String[] splitClassName = className.split( "\\." );
					if( splitClassName.length > 0 )
					{
						this.virtualParentName = splitClassName[ splitClassName.length - 1 ];
					}
				}
			}
		}
		this.stackTrace = element.getBonusDataFor( Element.DEBUG_CONSTRUCTION_STACK_TRACE_KEY );
		if( element.getName() != null )
		{
			this.name = element.getName() + ":" + this.hashCode;
		}
		if( element instanceof Component )
		{
			Composite parent = ( (Component)element ).getParent();
			if( parent != null )
			{
				this.parentHash = parent.hashCode();
				this.parentName = parent.getName() + ":" + this.parentHash;
			}
			else
			{
				this.parentName = "NO PARENT";
				this.parentHash = -1;
			}
			this.hasExtras = true;
		}
	}

	public SceneGraphTreeNode( Element element )
	{
		super( element );
		this.absoluteTransform = null;
		this.hasExtras = false;
		this.color = null;
		this.scale = null;
		this.opacity = -1;
		if( element instanceof Component )
		{
			Component sgComponent = (Component)element;
			if( ( sgComponent.getRoot() != null ) && ( sgComponent.getParent() != null ) )
			{
				this.absoluteTransform = sgComponent.getAbsoluteTransformation();
			}
			if( sgComponent instanceof Visual )
			{
				Visual visual = (Visual)sgComponent;
				if( visual.frontFacingAppearance.getValue() instanceof TexturedAppearance )
				{
					TexturedAppearance appearance = (TexturedAppearance)visual.frontFacingAppearance.getValue();
					this.color = new Color4f( appearance.diffuseColor.getValue() );
					this.opacity = appearance.opacity.getValue();
				}
				this.scale = new Matrix3x3( visual.scale.getValue() );
				this.isShowing = visual.isShowing.getValue();
			}
			this.hasExtras = true;
		}
	}

	@Override
	public boolean isDifferent( BasicTreeNode other )
	{
		boolean basicDifferent = super.isDifferent( other );
		if( !basicDifferent && ( other instanceof SceneGraphTreeNode ) )
		{
			return this.isSceneGraphDifferent( (SceneGraphTreeNode)other );
		}
		return basicDifferent;
	}

	private boolean isSceneGraphDifferent( SceneGraphTreeNode other )
	{
		if( ( other.absoluteTransform != null ) && ( this.absoluteTransform != null ) )
		{

			if( !other.absoluteTransform.translation.isWithinReasonableEpsilonOf( other.absoluteTransform.translation ) )
			{
				return true;
			}
			if( !other.absoluteTransform.orientation.isWithinReasonableEpsilonOf( other.absoluteTransform.orientation ) )
			{
				return true;
			}
		}
		if( this.hasExtras && other.hasExtras )
		{
			if( other.opacity != this.opacity )
			{
				return true;
			}
			if( other.isShowing != this.isShowing )
			{
				return true;
			}
			if( ( other.color != null ) && !other.color.equals( this.color ) )
			{
				return true;
			}
		}
		return false;
	}

}
