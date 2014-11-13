/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
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
 */

package edu.cmu.cs.dennisc.render.gl.imp.adapters;

import static javax.media.opengl.GL.GL_BACK;
import static javax.media.opengl.GL.GL_CULL_FACE;
import static javax.media.opengl.GL.GL_GREATER;
import static javax.media.opengl.GL2ES1.GL_ALPHA_TEST;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.property.event.PropertyEvent;
import edu.cmu.cs.dennisc.property.event.PropertyListener;
import edu.cmu.cs.dennisc.render.gl.imp.AdapterFactory;
import edu.cmu.cs.dennisc.render.gl.imp.PickContext;
import edu.cmu.cs.dennisc.render.gl.imp.RenderContext;
import edu.cmu.cs.dennisc.render.gl.imp.adapters.VisualAdapter.RenderType;
import edu.cmu.cs.dennisc.scenegraph.Component;
import edu.cmu.cs.dennisc.scenegraph.Composite;
import edu.cmu.cs.dennisc.scenegraph.Element;
import edu.cmu.cs.dennisc.scenegraph.Joint;
import edu.cmu.cs.dennisc.scenegraph.Mesh;
import edu.cmu.cs.dennisc.scenegraph.SkeletonVisual;
import edu.cmu.cs.dennisc.scenegraph.TexturedAppearance;
import edu.cmu.cs.dennisc.scenegraph.Transformable;
import edu.cmu.cs.dennisc.scenegraph.WeightedMesh;

public class SkeletonVisualAdapter extends edu.cmu.cs.dennisc.render.gl.imp.adapters.VisualAdapter<SkeletonVisual> implements PropertyListener, edu.cmu.cs.dennisc.scenegraph.SkeletonVisualBoundingBoxTracker {

	private static float ALPHA_TEST_THRESHOLD = .5f;

	private boolean skeletonIsDirty = true;
	private Joint currentSkeleton = null;
	protected Map<Integer, TexturedAppearanceAdapter> appearanceIdToAdapterMap = new HashMap<Integer, TexturedAppearanceAdapter>();
	protected Map<Integer, WeightedMeshControl[]> appearanceIdToMeshControllersMap = new HashMap<Integer, WeightedMeshControl[]>();
	protected Map<Integer, MeshAdapter<Mesh>[]> appearanceIdToGeometryAdapaters = new HashMap<Integer, MeshAdapter<Mesh>[]>();
	private boolean isDataDirty = true;

	public SkeletonVisualAdapter()
	{
		super();
	}

	@Override
	public void initialize( SkeletonVisual element )
	{
		if( this.m_element != null ) {
			this.m_element.setTracker( null );
		}
		super.initialize( element );
		element.setTracker( this );
		initializeDataIfNecessary();
	}

	private void initializeDataIfNecessary()
	{
		if( this.isDataDirty )
		{
			initializeData();
		}
	}

	private void initializeData()
	{
		if( this.m_element != null )
		{
			if( this.currentSkeleton != null )
			{
				this.setListeningOnSkeleton( this.currentSkeleton, false );
				System.out.println( "SWITCHING SKELETON FROM " + this.currentSkeleton.hashCode() + " TO " + m_element.skeleton.getValue().hashCode() );
			}
			this.currentSkeleton = m_element.skeleton.getValue();
			if( this.currentSkeleton != null )
			{
				this.setListeningOnSkeleton( this.currentSkeleton, true );
				this.skeletonIsDirty = true;
			}
			if( m_element.renderBackfaces() ) {
				this.m_backFacingAppearanceAdapter = this.m_frontFacingAppearanceAdapter;
			}
			updateAppearanceToGeometryAdapterMap();
			updateAppearanceToMeshControllersMap();
		}
		this.isDataDirty = false;
	}

	private void releaseMappedAdapter( ElementAdapter<? extends Element> adapter )
	{
		if( ( adapter != this.m_backFacingAppearanceAdapter ) && ( adapter != this.m_frontFacingAppearanceAdapter ) )
		{
			boolean matchesGeometry = false;
			if( m_geometryAdapters != null )
			{
				for( GeometryAdapter<? extends edu.cmu.cs.dennisc.scenegraph.Geometry> geometryAdapter : this.m_geometryAdapters )
				{
					if( adapter == geometryAdapter )
					{
						matchesGeometry = true;
						break;
					}
				}
			}
			if( !matchesGeometry )
			{
				if( adapter.m_element != null )
				{
					adapter.handleReleased();
				}
			}
		}
	}

	@Override
	public void handleReleased()
	{
		super.handleReleased();
		for( Entry<Integer, TexturedAppearanceAdapter> appearanceEntry : this.appearanceIdToAdapterMap.entrySet() )
		{
			releaseMappedAdapter( appearanceEntry.getValue() );
		}
		for( Entry<Integer, MeshAdapter<Mesh>[]> meshEntry : this.appearanceIdToGeometryAdapaters.entrySet() )
		{
			for( int i = 0; i < meshEntry.getValue().length; i++ )
			{
				releaseMappedAdapter( meshEntry.getValue()[ i ] );
			}
		}
		this.appearanceIdToAdapterMap.clear();
		this.appearanceIdToGeometryAdapaters.clear();
		this.appearanceIdToMeshControllersMap.clear();
	}

	@Override
	protected void pickGeometry( PickContext pc, boolean isSubElementActuallyRequired )
	{

		//TODO: Enable gl.glEnable( GL_TEXTURE_2D ) alpha test picking 

		initializeDataIfNecessary();
		if( this.skeletonIsDirty )
		{
			this.processWeightedMesh();
		}
		int i = 0;
		for( Entry<Integer, TexturedAppearanceAdapter> appearanceEntry : this.appearanceIdToAdapterMap.entrySet() )
		{
			WeightedMeshControl[] weightedMeshControls = appearanceIdToMeshControllersMap.get( appearanceEntry.getKey() );
			if( weightedMeshControls != null ) {
				for( WeightedMeshControl wmc : weightedMeshControls )
				{
					pc.gl.glPushName( i++ );
					if( !wmc.weightedMesh.cullBackfaces.getValue() )
					{
						pc.gl.glDisable( GL_CULL_FACE );
					}
					else
					{
						pc.gl.glEnable( GL_CULL_FACE );
						pc.gl.glCullFace( GL_BACK );
					}
					if( wmc.weightedMesh.useAlphaTest.getValue() )
					{
						pc.gl.glEnable( GL_ALPHA_TEST );
						pc.gl.glAlphaFunc( GL_GREATER, ALPHA_TEST_THRESHOLD );
					}
					else
					{
						pc.gl.glDisable( GL_ALPHA_TEST );
					}
					wmc.pickGeometry( pc, isSubElementActuallyRequired );
					pc.gl.glEnable( GL_CULL_FACE );
					pc.gl.glDisable( GL_ALPHA_TEST );
					pc.gl.glPopName();
				}
			}
			MeshAdapter<Mesh>[] meshAdapters = this.appearanceIdToGeometryAdapaters.get( appearanceEntry.getKey() );
			if( meshAdapters != null )
			{
				for( MeshAdapter<Mesh> ma : meshAdapters )
				{
					pc.gl.glPushName( i++ );
					if( !( (Mesh)ma.m_element ).cullBackfaces.getValue() )
					{
						pc.gl.glDisable( GL_CULL_FACE );
					}
					else
					{
						pc.gl.glEnable( GL_CULL_FACE );
						pc.gl.glCullFace( GL_BACK );
					}
					if( ( (Mesh)ma.m_element ).useAlphaTest.getValue() )
					{
						pc.gl.glEnable( GL_ALPHA_TEST );
						pc.gl.glAlphaFunc( GL_GREATER, ALPHA_TEST_THRESHOLD );
					}
					else
					{
						pc.gl.glDisable( GL_ALPHA_TEST );
					}
					ma.pickGeometry( pc, isSubElementActuallyRequired );
					pc.gl.glEnable( GL_CULL_FACE );
					pc.gl.glDisable( GL_ALPHA_TEST );
					pc.gl.glPopName();
				}
			}
		}
	}

	@Override
	public edu.cmu.cs.dennisc.math.AxisAlignedBox getAxisAlignedMinimumBoundingBox( edu.cmu.cs.dennisc.math.AxisAlignedBox rv ) {
		initializeDataIfNecessary();
		if( this.skeletonIsDirty )
		{
			this.processWeightedMesh();
		}
		int i = 0;
		for( Entry<Integer, TexturedAppearanceAdapter> appearanceEntry : this.appearanceIdToAdapterMap.entrySet() )
		{
			WeightedMeshControl[] weightedMeshControls = appearanceIdToMeshControllersMap.get( appearanceEntry.getKey() );
			if( weightedMeshControls != null ) {
				for( WeightedMeshControl wmc : weightedMeshControls )
				{
					edu.cmu.cs.dennisc.math.AxisAlignedBox b = new edu.cmu.cs.dennisc.math.AxisAlignedBox();
					edu.cmu.cs.dennisc.scenegraph.bound.BoundUtilities.getBoundingBox( b, wmc.vertexBuffer );
					rv.union( b );
				}
			}
			MeshAdapter<Mesh>[] meshAdapters = this.appearanceIdToGeometryAdapaters.get( appearanceEntry.getKey() );
			if( meshAdapters != null )
			{
				for( MeshAdapter<Mesh> ma : meshAdapters )
				{
					edu.cmu.cs.dennisc.math.AxisAlignedBox b = ma.m_element.getAxisAlignedMinimumBoundingBox();
					rv.union( b );
				}
			}
		}
		return rv;
	}

	@Override
	protected void renderGeometry( RenderContext rc, edu.cmu.cs.dennisc.render.gl.imp.adapters.VisualAdapter.RenderType renderType )
	{
		initializeDataIfNecessary();
		if( this.skeletonIsDirty )
		{
			this.processWeightedMesh();
		}
		for( Entry<Integer, TexturedAppearanceAdapter> appearanceEntry : this.appearanceIdToAdapterMap.entrySet() )
		{
			appearanceEntry.getValue().setTexturePipelineState( rc );

			WeightedMeshControl[] weightedMeshControls = appearanceIdToMeshControllersMap.get( appearanceEntry.getKey() );
			if( weightedMeshControls != null ) {
				for( WeightedMeshControl wmc : weightedMeshControls )
				{
					if( !wmc.weightedMesh.cullBackfaces.getValue() )
					{
						rc.gl.glDisable( GL_CULL_FACE );
					}
					else
					{
						rc.gl.glEnable( GL_CULL_FACE );
						rc.gl.glCullFace( GL_BACK );
					}
					if( wmc.weightedMesh.useAlphaTest.getValue() )
					{
						rc.gl.glEnable( GL_ALPHA_TEST );
						rc.gl.glAlphaFunc( GL_GREATER, ALPHA_TEST_THRESHOLD );
					}
					else
					{
						rc.gl.glDisable( GL_ALPHA_TEST );
					}
					wmc.renderGeometry( rc );
					rc.gl.glEnable( GL_CULL_FACE );
					rc.gl.glDisable( GL_ALPHA_TEST );
				}
			}
			MeshAdapter<Mesh>[] meshAdapters = this.appearanceIdToGeometryAdapaters.get( appearanceEntry.getKey() );
			if( meshAdapters != null )
			{
				for( MeshAdapter<Mesh> ma : meshAdapters )
				{
					if( !( (Mesh)ma.m_element ).cullBackfaces.getValue() )
					{
						rc.gl.glDisable( GL_CULL_FACE );
					}
					else
					{
						rc.gl.glEnable( GL_CULL_FACE );
						rc.gl.glCullFace( GL_BACK );
					}
					if( ( (Mesh)ma.m_element ).useAlphaTest.getValue() )
					{
						rc.gl.glEnable( GL_ALPHA_TEST );
						rc.gl.glAlphaFunc( GL_GREATER, ALPHA_TEST_THRESHOLD );
					}
					else
					{
						rc.gl.glDisable( GL_ALPHA_TEST );
					}
					ma.render( rc, renderType );
					rc.gl.glEnable( GL_CULL_FACE );
					rc.gl.glDisable( GL_ALPHA_TEST );
				}
			}

		}
	}

	@Override
	protected boolean isActuallyShowing() {
		initializeDataIfNecessary();
		if( super.isActuallyShowing() )
		{
			return true;
		}
		if( m_isShowing && ( appearanceIdToMeshControllersMap != null ) && ( appearanceIdToMeshControllersMap.size() > 0 ) )
		{
			if( m_frontFacingAppearanceAdapter != null ) {
				if( m_frontFacingAppearanceAdapter.isActuallyShowing() ) {
					return true;
				}
			}
			if( m_backFacingAppearanceAdapter != null ) {
				if( m_backFacingAppearanceAdapter.isActuallyShowing() ) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	protected boolean hasOpaque() {
		initializeDataIfNecessary();
		if( super.hasOpaque() )
		{
			return true;
		}
		//Check the base adapter to see if it's set to be alpha (through a sub 1 opacity setting)
		//If it's alpha, return false
		if( m_frontFacingAppearanceAdapter != null ) {
			if( m_frontFacingAppearanceAdapter.isAllAlphaBlended() ) {
				return false;
			}
		}
		if( m_backFacingAppearanceAdapter != null ) {
			if( m_backFacingAppearanceAdapter.isAllAlphaBlended() ) {
				return false;
			}
		}
		//Check to see if there are non-alpha textures or none "all" alpha values
		if( ( appearanceIdToMeshControllersMap != null ) && ( appearanceIdToMeshControllersMap.size() > 0 ) )
		{
			if( m_frontFacingAppearanceAdapter != null ) {
				if( !m_frontFacingAppearanceAdapter.isAlphaBlended() ) {
					return true;
				}
			}
			if( m_backFacingAppearanceAdapter != null ) {
				if( !m_backFacingAppearanceAdapter.isAlphaBlended() ) {
					return true;
				}
			}
			for( Entry<Integer, WeightedMeshControl[]> controlEntry : this.appearanceIdToMeshControllersMap.entrySet() )
			{
				TexturedAppearanceAdapter ta = appearanceIdToAdapterMap.get( controlEntry.getKey() );
				if( ( ta != null ) && ta.isActuallyShowing() && !ta.isAlphaBlended() )
				{
					return true;
				}
			}
		}
		return false;
	}

	@Override
	protected boolean isAlphaBlended()
	{
		initializeDataIfNecessary();
		if( super.isAlphaBlended() )
		{
			return true;
		}

		if( ( appearanceIdToMeshControllersMap != null ) && ( appearanceIdToMeshControllersMap.size() > 0 ) )
		{
			if( m_frontFacingAppearanceAdapter != null ) {
				if( m_frontFacingAppearanceAdapter.isAlphaBlended() ) {
					return true;
				}
			}
			if( m_backFacingAppearanceAdapter != null ) {
				if( m_backFacingAppearanceAdapter.isAlphaBlended() ) {
					return true;
				}
			}
			for( Entry<Integer, WeightedMeshControl[]> controlEntry : this.appearanceIdToMeshControllersMap.entrySet() )
			{
				TexturedAppearanceAdapter ta = appearanceIdToAdapterMap.get( controlEntry.getKey() );
				if( ( ta != null ) && ta.isActuallyShowing() && ta.isAlphaBlended() )
				{
					return true;
				}
			}
		}
		return false;
	}

	//PropertyListener methods for listening to changes on skeleton transforms
	@Override
	public void propertyChanging( PropertyEvent e )
	{
		//Do Nothing
	}

	private void setListeningOnSkeleton( Composite c, boolean shouldListen )
	{
		if( c == null )
		{
			return;
		}
		if( c instanceof Joint )
		{
			if( shouldListen )
			{
				( (Joint)c ).localTransformation.addPropertyListener( this );
			}
			else
			{
				( (Joint)c ).localTransformation.removePropertyListener( this );
			}
		}
		for( int i = 0; i < c.getComponentCount(); i++ )
		{
			if( c.getComponentAt( i ) instanceof Composite )
			{
				setListeningOnSkeleton( (Composite)c.getComponentAt( i ), shouldListen );
			}
		}
	}

	@Override
	public void propertyChanged( PropertyEvent e )
	{
		handleSkeletonTransformationChange();
	}

	private void handleNewSkeleton() {
		this.isDataDirty = true;
	}

	private void handleSkeletonTransformationChange()
	{
		this.skeletonIsDirty = true;
	}

	public void processWeightedMesh()
	{
		if( this.currentSkeleton != null )
		{
			synchronized( appearanceIdToMeshControllersMap ) {
				for( Entry<Integer, WeightedMeshControl[]> controlEntry : this.appearanceIdToMeshControllersMap.entrySet() )
				{
					for( WeightedMeshControl wmc : controlEntry.getValue() )
					{
						wmc.preProcess();
					}
				}
				AffineMatrix4x4 oTransformationPre = new AffineMatrix4x4();
				edu.cmu.cs.dennisc.math.Matrix3x3 inverseScale = new edu.cmu.cs.dennisc.math.Matrix3x3( m_element.scale.getValue() );
				inverseScale.invert();
				synchronized( this.currentSkeleton ) {
					processWeightedMesh( this.currentSkeleton, oTransformationPre, inverseScale );
				}
				for( Entry<Integer, WeightedMeshControl[]> controlEntry : this.appearanceIdToMeshControllersMap.entrySet() )
				{
					for( WeightedMeshControl wmc : controlEntry.getValue() )
					{
						wmc.postProcess();
					}
				}
			}
		}
		this.skeletonIsDirty = false;
	}

	private void processWeightedMesh( Composite currentNode, AffineMatrix4x4 oTransformationPre, edu.cmu.cs.dennisc.math.Matrix3x3 inverseScale )
	{
		if( currentNode == null )
		{
			return;
		}
		AffineMatrix4x4 oTransformationPost = oTransformationPre;
		if( currentNode instanceof Transformable )
		{
			oTransformationPost = AffineMatrix4x4.createMultiplication( oTransformationPre, ( (Transformable)currentNode ).localTransformation.getValue() );

			AffineMatrix4x4 unscaledTransform = new AffineMatrix4x4( oTransformationPost );
			unscaledTransform.translation.x *= inverseScale.right.x;
			unscaledTransform.translation.y *= inverseScale.up.y;
			unscaledTransform.translation.z *= inverseScale.backward.z;

			if( currentNode instanceof Joint )
			{
				for( Entry<Integer, WeightedMeshControl[]> controlEntry : this.appearanceIdToMeshControllersMap.entrySet() )
				{
					for( WeightedMeshControl wmc : controlEntry.getValue() )
					{
						wmc.process( (Joint)currentNode, unscaledTransform );
					}
				}
			}
		}
		for( int i = 0; i < currentNode.getComponentCount(); i++ )
		{
			Component comp = currentNode.getComponentAt( i );
			if( comp instanceof Composite )
			{
				Composite jointChild = (Composite)comp;
				processWeightedMesh( jointChild, oTransformationPost, inverseScale );
			}
		}
	}

	protected void updateAppearanceIdToAdapterMap() {
		synchronized( appearanceIdToAdapterMap ) {
			List<ElementAdapter<? extends Element>> oldAdapters = new ArrayList<ElementAdapter<? extends Element>>();
			for( Entry<Integer, TexturedAppearanceAdapter> appearanceEntry : this.appearanceIdToAdapterMap.entrySet() )
			{
				if( !oldAdapters.contains( appearanceEntry.getValue() ) )
				{
					oldAdapters.add( appearanceEntry.getValue() );
				}
			}
			appearanceIdToAdapterMap.clear();
			List<ElementAdapter<? extends Element>> newAdapters = new ArrayList<ElementAdapter<? extends Element>>();
			for( TexturedAppearance ta : this.m_element.textures.getValue() )
			{
				TexturedAppearanceAdapter newAdapter = AdapterFactory.getAdapterFor( ta );
				appearanceIdToAdapterMap.put( ta.textureId.getValue(), newAdapter );
				if( !newAdapters.contains( newAdapter ) )
				{
					newAdapters.add( newAdapter );
				}
			}
			for( ElementAdapter<? extends Element> oldAdapter : oldAdapters )
			{
				if( !newAdapters.contains( oldAdapter ) )
				{
					releaseMappedAdapter( oldAdapter );
				}
			}
		}
	}

	protected void updateAppearanceToGeometryAdapterMap() {
		synchronized( appearanceIdToGeometryAdapaters ) {
			appearanceIdToGeometryAdapaters.clear();
			for( TexturedAppearance ta : this.m_element.textures.getValue() )
			{
				List<MeshAdapter<Mesh>> meshAdapters = new LinkedList<MeshAdapter<Mesh>>();
				for( GeometryAdapter adapter : this.m_geometryAdapters )
				{
					if( adapter instanceof MeshAdapter<?> )
					{
						MeshAdapter<Mesh> ma = (MeshAdapter<Mesh>)adapter;
						if( ma.m_element.textureId.getValue() == ta.textureId.getValue() )
						{
							meshAdapters.add( ma );
						}
					}
				}
				appearanceIdToGeometryAdapaters.put( ta.textureId.getValue(), meshAdapters.toArray( new MeshAdapter[ meshAdapters.size() ] ) );
			}
		}
	}

	protected void updateAppearanceToMeshControllersMap() {
		synchronized( appearanceIdToMeshControllersMap ) {
			appearanceIdToMeshControllersMap.clear();
			for( TexturedAppearance ta : this.m_element.textures.getValue() )
			{
				List<WeightedMeshControl> controls = new LinkedList<WeightedMeshControl>();
				for( WeightedMesh weightedMesh : this.m_element.weightedMeshes.getValue() )
				{
					if( weightedMesh.textureId.getValue() == ta.textureId.getValue() )
					{
						WeightedMeshControl control = new WeightedMeshControl();
						control.initialize( weightedMesh );
						controls.add( control );
					}
				}
				appearanceIdToMeshControllersMap.put( ta.textureId.getValue(), controls.toArray( new WeightedMeshControl[ controls.size() ] ) );
			}
		}
	}

	@Override
	protected void updateGeometryAdapters() {
		super.updateGeometryAdapters();
		updateAppearanceToGeometryAdapterMap();
	}

	@Override
	protected void propertyChanged( edu.cmu.cs.dennisc.property.InstanceProperty<?> property ) {
		if( property == m_element.skeleton )
		{
			handleNewSkeleton();
		}
		else if( property == m_element.weightedMeshes )
		{
			updateAppearanceToMeshControllersMap();
		}
		else if( property == m_element.textures ) {
			updateAppearanceIdToAdapterMap();
		}
		else if( property == m_element.baseBoundingBox ) {
			//pass
		}
		else {
			super.propertyChanged( property );
		}
	}
}
