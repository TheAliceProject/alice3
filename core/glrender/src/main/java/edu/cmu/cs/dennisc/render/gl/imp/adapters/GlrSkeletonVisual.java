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

package edu.cmu.cs.dennisc.render.gl.imp.adapters;

import static com.jogamp.opengl.GL.GL_BACK;
import static com.jogamp.opengl.GL.GL_CULL_FACE;
import static com.jogamp.opengl.GL.GL_GREATER;
import static com.jogamp.opengl.GL2ES1.GL_ALPHA_TEST;

import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import edu.cmu.cs.dennisc.java.util.BufferUtilities;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.property.event.PropertyEvent;
import edu.cmu.cs.dennisc.property.event.PropertyListener;
import edu.cmu.cs.dennisc.render.gl.imp.PickContext;
import edu.cmu.cs.dennisc.render.gl.imp.RenderContext;
import edu.cmu.cs.dennisc.scenegraph.Component;
import edu.cmu.cs.dennisc.scenegraph.Composite;
import edu.cmu.cs.dennisc.scenegraph.Element;
import edu.cmu.cs.dennisc.scenegraph.InverseAbsoluteTransformationWeightsPair;
import edu.cmu.cs.dennisc.scenegraph.Joint;
import edu.cmu.cs.dennisc.scenegraph.Mesh;
import edu.cmu.cs.dennisc.scenegraph.SkeletonVisual;
import edu.cmu.cs.dennisc.scenegraph.TexturedAppearance;
import edu.cmu.cs.dennisc.scenegraph.Transformable;
import edu.cmu.cs.dennisc.scenegraph.WeightedMesh;

public class GlrSkeletonVisual extends edu.cmu.cs.dennisc.render.gl.imp.adapters.GlrVisual<SkeletonVisual> implements PropertyListener, edu.cmu.cs.dennisc.scenegraph.SkeletonVisualBoundingBoxTracker {
	public static class WeightedMeshControl {
		protected WeightedMesh weightedMesh;

		protected DoubleBuffer vertexBuffer;
		protected FloatBuffer normalBuffer;
		protected FloatBuffer textCoordBuffer;
		protected IntBuffer indexBuffer;

		private AffineMatrix4x4[] affineMatrices;
		private float[] weights;
		private boolean needsInitialization = true;

		//note: no one seems to pay any attention to this. --dennisc
		private boolean geometryIsChanged = true;

		public void initialize( WeightedMesh weightedMesh ) {
			this.weightedMesh = weightedMesh;
			internalInitialize();
		}

		public boolean isGeometryChanged() {
			return geometryIsChanged;
		}

		public void setIsGeometryChanged( boolean isGeometryChanged ) {
			geometryIsChanged = isGeometryChanged;
		}

		private void internalInitialize() {
			if( this.weightedMesh != null ) {
				this.textCoordBuffer = this.weightedMesh.textCoordBuffer.getValue();
				this.indexBuffer = this.weightedMesh.indexBuffer.getValue();

				this.normalBuffer = BufferUtilities.copyFloatBuffer( this.weightedMesh.normalBuffer.getValue() );
				this.vertexBuffer = BufferUtilities.copyDoubleBuffer( this.weightedMesh.vertexBuffer.getValue() );
				int nVertexCount = this.vertexBuffer.limit() / 3;

				this.affineMatrices = new AffineMatrix4x4[ nVertexCount ];
				this.weights = new float[ nVertexCount ];
				for( int i = 0; i < nVertexCount; i++ ) {
					this.affineMatrices[ i ] = new AffineMatrix4x4();
					this.weights[ i ] = 0f;
				}
				needsInitialization = false;
			}
		}

		public void preProcess() {
			for( int i = 0; i < this.affineMatrices.length; i++ ) {
				this.affineMatrices[ i ].setZero();
				this.weights[ i ] = 0f;
			}
		}

		public void process( edu.cmu.cs.dennisc.scenegraph.Joint joint, AffineMatrix4x4 oTransformation ) {
			InverseAbsoluteTransformationWeightsPair iatwp = this.weightedMesh.weightInfo.getValue().getMap().get( joint.jointID.getValue() );
			if( iatwp != null ) {
				//        	System.out.println("\n  Processing mesh "+this.weightedMesh.hashCode());
				//        	System.out.println("  On Joint "+joint.hashCode());
				//        	System.out.println("  Weight Info "+this.weightedMesh.weightInfo.getValue().hashCode());
				//        	System.out.println("  iatwp "+iatwp.hashCode());
				AffineMatrix4x4 oDelta = AffineMatrix4x4.createMultiplication( oTransformation, iatwp.getInverseAbsoluteTransformation() );
				iatwp.reset();
				while( !iatwp.isDone() ) {
					int vertexIndex = iatwp.getIndex();
					float weight = iatwp.getWeight();
					AffineMatrix4x4 transform = AffineMatrix4x4.createMultiplication( oDelta, weight );
					this.affineMatrices[ vertexIndex ].add( transform );
					this.weights[ vertexIndex ] += weight;
					iatwp.advance();
				}
			}
		}

		public void postProcess() {
			for( int i = 0; i < this.affineMatrices.length; i++ ) {
				float weight = this.weights[ i ];
				if( ( 0.999f < weight ) && ( weight < 1.001f ) ) {
					//pass
				} else if( weight != 0 ) {
					AffineMatrix4x4 am = this.affineMatrices[ i ];
					this.affineMatrices[ i ].multiply( 1.0 / weight );
				}
			}
			this.transformBuffers( this.affineMatrices, this.vertexBuffer, this.normalBuffer, this.weightedMesh.vertexBuffer.getValue(), this.weightedMesh.normalBuffer.getValue() );
			setIsGeometryChanged( true );
		}

		private void transformBuffers( AffineMatrix4x4[] voAffineMatrices, DoubleBuffer vertices, FloatBuffer normals, DoubleBuffer verticesSrc, FloatBuffer normalsSrc ) {
			double[] vertexSrc = new double[ 3 ];
			float[] normalSrc = new float[ 3 ];
			double[] vertexDst = new double[ 3 ];
			float[] normalDst = new float[ 3 ];
			vertices.rewind();
			normals.rewind();
			verticesSrc.rewind();
			normalsSrc.rewind();

			for( AffineMatrix4x4 voAffineMatrice : voAffineMatrices ) {
				vertexSrc[ 0 ] = verticesSrc.get();
				vertexSrc[ 1 ] = verticesSrc.get();
				vertexSrc[ 2 ] = verticesSrc.get();
				voAffineMatrice.transformVertex( vertexDst, 0, vertexSrc, 0 );
				vertices.put( vertexDst );

				normalSrc[ 0 ] = normalsSrc.get();
				normalSrc[ 1 ] = normalsSrc.get();
				normalSrc[ 2 ] = normalsSrc.get();
				voAffineMatrice.transformNormal( normalDst, 0, normalSrc, 0 );
				normals.put( normalDst );
			}
		}

		public void renderGeometry( RenderContext rc ) {
			if( this.needsInitialization ) {
				this.internalInitialize();
			}
			GlrMesh.renderMesh( rc, vertexBuffer, normalBuffer, textCoordBuffer, indexBuffer );
		}

		public void pickGeometry( PickContext pc, boolean isSubElementRequired ) {
			if( this.needsInitialization ) {
				this.internalInitialize();
			}
			GlrMesh.pickMesh( pc, vertexBuffer, indexBuffer );
		}
	}

	private static float ALPHA_TEST_THRESHOLD = .5f;

	@Override
	public void initialize( SkeletonVisual element )
	{
		if( this.owner != null ) {
			this.owner.setTracker( null );
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
		if( this.owner != null )
		{
			if( this.currentSkeleton != null )
			{
				this.setListeningOnSkeleton( this.currentSkeleton, false );
				System.out.println( "SWITCHING SKELETON FROM " + this.currentSkeleton.hashCode() + " TO " + owner.skeleton.getValue().hashCode() );
			}
			this.currentSkeleton = owner.skeleton.getValue();
			if( this.currentSkeleton != null )
			{
				this.setListeningOnSkeleton( this.currentSkeleton, true );
				this.skeletonIsDirty = true;
			}
			if( owner.renderBackfaces() ) {
				this.glrBackFacingAppearance = this.glrFrontFacingAppearance;
			}
			updateAppearanceToGeometryAdapterMap();
			updateAppearanceToMeshControllersMap();
		}
		this.isDataDirty = false;
	}

	private void releaseMappedAdapter( GlrElement<? extends Element> adapter )
	{
		if( ( adapter != this.glrBackFacingAppearance ) && ( adapter != this.glrFrontFacingAppearance ) )
		{
			boolean matchesGeometry = false;
			if( this.glrGeometries != null )
			{
				for( GlrGeometry<? extends edu.cmu.cs.dennisc.scenegraph.Geometry> geometryAdapter : this.glrGeometries )
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
				if( adapter.owner != null )
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
		for( java.util.Map.Entry<Integer, GlrTexturedAppearance> appearanceEntry : this.appearanceIdToAdapterMap.entrySet() )
		{
			releaseMappedAdapter( appearanceEntry.getValue() );
		}
		for( java.util.Map.Entry<Integer, GlrMesh<Mesh>[]> meshEntry : this.appearanceIdToGeometryAdapaters.entrySet() )
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
		for( java.util.Map.Entry<Integer, GlrTexturedAppearance> appearanceEntry : this.appearanceIdToAdapterMap.entrySet() )
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
			GlrMesh<Mesh>[] meshAdapters = this.appearanceIdToGeometryAdapaters.get( appearanceEntry.getKey() );
			if( meshAdapters != null )
			{
				for( GlrMesh<Mesh> ma : meshAdapters )
				{
					pc.gl.glPushName( i++ );
					if( !( (Mesh)ma.owner ).cullBackfaces.getValue() )
					{
						pc.gl.glDisable( GL_CULL_FACE );
					}
					else
					{
						pc.gl.glEnable( GL_CULL_FACE );
						pc.gl.glCullFace( GL_BACK );
					}
					if( ( (Mesh)ma.owner ).useAlphaTest.getValue() )
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
		for( java.util.Map.Entry<Integer, GlrTexturedAppearance> appearanceEntry : this.appearanceIdToAdapterMap.entrySet() )
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
			GlrMesh<Mesh>[] meshAdapters = this.appearanceIdToGeometryAdapaters.get( appearanceEntry.getKey() );
			if( meshAdapters != null )
			{
				for( GlrMesh<Mesh> ma : meshAdapters )
				{
					edu.cmu.cs.dennisc.math.AxisAlignedBox b = ma.owner.getAxisAlignedMinimumBoundingBox();
					rv.union( b );
				}
			}
		}
		return rv;
	}

	@Override
	protected void renderGeometry( RenderContext rc, edu.cmu.cs.dennisc.render.gl.imp.adapters.GlrVisual.RenderType renderType )
	{
		initializeDataIfNecessary();
		if( this.skeletonIsDirty )
		{
			this.processWeightedMesh();
		}

		boolean canRenderAlpha = ( renderType == edu.cmu.cs.dennisc.render.gl.imp.adapters.GlrVisual.RenderType.ALL ) || ( renderType == edu.cmu.cs.dennisc.render.gl.imp.adapters.GlrVisual.RenderType.ALPHA_BLENDED );
		boolean canRenderOpaque = ( renderType == edu.cmu.cs.dennisc.render.gl.imp.adapters.GlrVisual.RenderType.ALL ) || ( renderType == edu.cmu.cs.dennisc.render.gl.imp.adapters.GlrVisual.RenderType.OPAQUE );
		for( java.util.Map.Entry<Integer, GlrTexturedAppearance> appearanceEntry : this.appearanceIdToAdapterMap.entrySet() )
		{
			if( renderType == RenderType.SILHOUETTE ) {
				//pass
			} else {
				appearanceEntry.getValue().setTexturePipelineState( rc );
			}

			boolean textureIsAlphaBlend = appearanceEntry.getValue().isAlphaBlended();

			WeightedMeshControl[] weightedMeshControls = appearanceIdToMeshControllersMap.get( appearanceEntry.getKey() );
			if( weightedMeshControls != null ) {
				for( WeightedMeshControl wmc : weightedMeshControls )
				{
					boolean meshIsAlpha = textureIsAlphaBlend && !wmc.weightedMesh.useAlphaTest.getValue();
					if( ( meshIsAlpha && canRenderAlpha ) || ( !meshIsAlpha && canRenderOpaque ) )
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
			}
			GlrMesh<Mesh>[] meshAdapters = this.appearanceIdToGeometryAdapaters.get( appearanceEntry.getKey() );
			if( meshAdapters != null )
			{
				for( GlrMesh<Mesh> ma : meshAdapters )
				{
					boolean meshIsAlpha = textureIsAlphaBlend && !( (Mesh)ma.owner ).useAlphaTest.getValue();
					if( ( meshIsAlpha && canRenderAlpha ) || ( !meshIsAlpha && canRenderOpaque ) )
					{
						if( !( (Mesh)ma.owner ).cullBackfaces.getValue() )
						{
							rc.gl.glDisable( GL_CULL_FACE );
						}
						else
						{
							rc.gl.glEnable( GL_CULL_FACE );
							rc.gl.glCullFace( GL_BACK );
						}
						if( ( (Mesh)ma.owner ).useAlphaTest.getValue() )
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
	}

	@Override
	protected boolean isActuallyShowing() {
		initializeDataIfNecessary();
		if( super.isActuallyShowing() )
		{
			return true;
		}
		if( this.isShowing && ( appearanceIdToMeshControllersMap != null ) && ( appearanceIdToMeshControllersMap.size() > 0 ) )
		{
			if( this.glrFrontFacingAppearance != null ) {
				if( this.glrFrontFacingAppearance.isActuallyShowing() ) {
					return true;
				}
			}
			if( this.glrBackFacingAppearance != null ) {
				if( this.glrBackFacingAppearance.isActuallyShowing() ) {
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
		if( this.glrFrontFacingAppearance != null ) {
			if( this.glrFrontFacingAppearance.isAllAlphaBlended() ) {
				return false;
			}
		}
		if( this.glrBackFacingAppearance != null ) {
			if( this.glrBackFacingAppearance.isAllAlphaBlended() ) {
				return false;
			}
		}
		//Check to see if there are non-alpha textures or none "all" alpha values
		if( ( appearanceIdToMeshControllersMap != null ) && ( appearanceIdToMeshControllersMap.size() > 0 ) )
		{
			if( this.glrFrontFacingAppearance != null ) {
				if( !this.glrFrontFacingAppearance.isAlphaBlended() ) {
					return true;
				}
			}
			if( this.glrBackFacingAppearance != null ) {
				if( !this.glrBackFacingAppearance.isAlphaBlended() ) {
					return true;
				}
			}
			for( java.util.Map.Entry<Integer, WeightedMeshControl[]> controlEntry : this.appearanceIdToMeshControllersMap.entrySet() )
			{
				GlrTexturedAppearance ta = appearanceIdToAdapterMap.get( controlEntry.getKey() );
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
			if( this.glrFrontFacingAppearance != null ) {
				if( this.glrFrontFacingAppearance.isAlphaBlended() ) {
					return true;
				}
			}
			if( this.glrBackFacingAppearance != null ) {
				if( this.glrBackFacingAppearance.isAlphaBlended() ) {
					return true;
				}
			}
			for( java.util.Map.Entry<Integer, WeightedMeshControl[]> controlEntry : this.appearanceIdToMeshControllersMap.entrySet() )
			{
				GlrTexturedAppearance ta = appearanceIdToAdapterMap.get( controlEntry.getKey() );
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
				for( java.util.Map.Entry<Integer, WeightedMeshControl[]> controlEntry : this.appearanceIdToMeshControllersMap.entrySet() )
				{
					for( WeightedMeshControl wmc : controlEntry.getValue() )
					{
						wmc.preProcess();
					}
				}
				AffineMatrix4x4 oTransformationPre = new AffineMatrix4x4();
				edu.cmu.cs.dennisc.math.Matrix3x3 inverseScale = new edu.cmu.cs.dennisc.math.Matrix3x3( owner.scale.getValue() );
				inverseScale.invert();
				synchronized( this.currentSkeleton ) {
					processWeightedMesh( this.currentSkeleton, oTransformationPre, inverseScale );
				}
				for( java.util.Map.Entry<Integer, WeightedMeshControl[]> controlEntry : this.appearanceIdToMeshControllersMap.entrySet() )
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
				for( java.util.Map.Entry<Integer, WeightedMeshControl[]> controlEntry : this.appearanceIdToMeshControllersMap.entrySet() )
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
			List<GlrElement<? extends Element>> oldAdapters = new ArrayList<GlrElement<? extends Element>>();
			for( java.util.Map.Entry<Integer, GlrTexturedAppearance> appearanceEntry : this.appearanceIdToAdapterMap.entrySet() )
			{
				if( !oldAdapters.contains( appearanceEntry.getValue() ) )
				{
					oldAdapters.add( appearanceEntry.getValue() );
				}
			}
			appearanceIdToAdapterMap.clear();
			List<GlrElement<? extends Element>> newAdapters = new ArrayList<GlrElement<? extends Element>>();
			for( TexturedAppearance ta : this.owner.textures.getValue() )
			{
				GlrTexturedAppearance newAdapter = AdapterFactory.getAdapterFor( ta );
				appearanceIdToAdapterMap.put( ta.textureId.getValue(), newAdapter );
				if( !newAdapters.contains( newAdapter ) )
				{
					newAdapters.add( newAdapter );
				}
			}
			for( GlrElement<? extends Element> oldAdapter : oldAdapters )
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
			for( TexturedAppearance ta : this.owner.textures.getValue() )
			{
				List<GlrMesh<?>> meshAdapters = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
				for( GlrGeometry<?> adapter : this.glrGeometries )
				{
					if( adapter instanceof GlrMesh<?> )
					{
						GlrMesh<?> ma = (GlrMesh<?>)adapter;
						if( ma.owner.textureId.getValue() == ta.textureId.getValue() )
						{
							meshAdapters.add( ma );
						}
					}
				}
				appearanceIdToGeometryAdapaters.put( ta.textureId.getValue(), meshAdapters.toArray( new GlrMesh[ meshAdapters.size() ] ) );
			}
		}
	}

	protected void updateAppearanceToMeshControllersMap() {
		synchronized( appearanceIdToMeshControllersMap ) {
			appearanceIdToMeshControllersMap.clear();
			for( TexturedAppearance ta : this.owner.textures.getValue() )
			{
				List<WeightedMeshControl> controls = new LinkedList<WeightedMeshControl>();
				for( WeightedMesh weightedMesh : this.owner.weightedMeshes.getValue() )
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
		if( property == owner.skeleton ) {
			handleNewSkeleton();
		} else if( property == owner.weightedMeshes ) {
			updateAppearanceToMeshControllersMap();
		} else if( property == owner.textures ) {
			updateAppearanceIdToAdapterMap();
		} else if( property == owner.baseBoundingBox ) {
			//pass
		} else {
			super.propertyChanged( property );
		}
	}

	private boolean skeletonIsDirty = true;
	private Joint currentSkeleton = null;
	private java.util.Map<Integer, GlrTexturedAppearance> appearanceIdToAdapterMap = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
	protected java.util.Map<Integer, WeightedMeshControl[]> appearanceIdToMeshControllersMap = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
	private java.util.Map<Integer, GlrMesh<Mesh>[]> appearanceIdToGeometryAdapaters = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
	private boolean isDataDirty = true;
}
