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

package edu.cmu.cs.dennisc.scenegraph;

import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import edu.cmu.cs.dennisc.math.EulerAngles;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Vector3f;
import edu.cmu.cs.dennisc.math.property.EulerAnglesProperty;
import edu.cmu.cs.dennisc.math.property.Vector3fProperty;

public class Joint extends Transformable implements ModelJoint
{
	//	private static final java.util.Comparator<Joint> JOINT_ARRAY_COMPARATOR = new java.util.Comparator<Joint>() {
	//		@Override
	//		public int compare( Joint o1, Joint o2 ) {
	//			return o1.jointID.getValue().compareTo( o2.jointID.getValue() );
	//		}
	//	};

	private Joint getJoint( Composite c, String jointID )
	{
		if( c == null )
		{
			return null;
		}
		if( c instanceof Joint )
		{
			Joint j = (Joint)c;
			if( j.jointID.getValue().equals( jointID ) )
			{
				return j;
			}
		}
		for( int i = 0; i < c.getComponentCount(); i++ )
		{
			Component comp = c.getComponentAt( i );
			if( comp instanceof Composite )
			{
				Joint foundJoint = getJoint( (Composite)comp, jointID );
				if( foundJoint != null )
				{
					return foundJoint;
				}
			}
		}
		return null;
	}

	public Joint getJoint( String jointID )
	{
		return getJoint( this, jointID );
	}

	private void getJoints( Composite c, String nameKey, java.util.List<Joint> joints )
	{
		if( c == null )
		{
			return;
		}
		if( c instanceof Joint )
		{
			Joint j = (Joint)c;
			if( j.jointID.getValue().startsWith( nameKey ) )
			{
				joints.add( j );
			}
		}
		for( int i = 0; i < c.getComponentCount(); i++ )
		{
			Component comp = c.getComponentAt( i );
			if( comp instanceof Composite )
			{
				getJoints( (Composite)comp, nameKey, joints );
			}
		}
	}

	public Joint[] getJoints( String nameKey )
	{
		java.util.List<Joint> joints = new java.util.ArrayList<Joint>();
		getJoints( this, nameKey, joints );
		return joints.toArray( new Joint[ joints.size() ] );
	}

	public void setParentVisual( SkeletonVisual parentVisual ) {
		this.parentVisual = parentVisual;
	}

	private SkeletonVisual getParentVisual() {
		if( ( this.parentVisual == null ) && ( this.getParent() instanceof Joint ) ) {
			this.parentVisual = ( (Joint)this.getParent() ).getParentVisual();
		}
		return this.parentVisual;
	}

	private AxisAlignedBox getBoundingBox( Composite c, AxisAlignedBox rv, AffineMatrix4x4 transform, boolean cumulative )
	{
		if( c == null )
		{
			return null;
		}
		if( c instanceof Joint )
		{
			Joint j = (Joint)c;

			//We scale the local bounding box based on the scale of the SkeletonVisual base object
			//We can do this here (in the local space of the joint) because we restrict the scale to be a uniform scale
			AxisAlignedBox scaledBBox = new AxisAlignedBox( j.boundingBox.getValue() );
			SkeletonVisual sv = this.getParentVisual();
			if( sv != null ) {
				scaledBBox.scale( sv.scale.getValue() );
			}

			Point3 localMin = scaledBBox.getMinimum();
			Point3 localMax = scaledBBox.getMaximum();

			Point3 transformedMin = transform.createTransformed( localMin );
			Point3 transformedMax = transform.createTransformed( localMax );
			if( !transformedMin.isNaN() ) {
				rv.union( transformedMin );
			}
			if( !transformedMax.isNaN() ) {
				rv.union( transformedMax );
			}
		}
		if( cumulative ) {
			for( int i = 0; i < c.getComponentCount(); i++ )
			{
				Component comp = c.getComponentAt( i );
				if( comp instanceof Composite )
				{
					AffineMatrix4x4 childTransform = AffineMatrix4x4.createMultiplication( transform, ( (AbstractTransformable)comp ).accessLocalTransformation() );
					getBoundingBox( (Composite)comp, rv, childTransform, cumulative );
				}
			}
		}
		return rv;
	}

	public AxisAlignedBox getBoundingBox( AxisAlignedBox rv, AffineMatrix4x4 transform, boolean cumulative )
	{
		if( rv == null )
		{
			rv = new AxisAlignedBox();
		}
		getBoundingBox( this, rv, transform, cumulative );
		return rv;
	}

	public AxisAlignedBox getBoundingBox( AxisAlignedBox rv, boolean cumulative )
	{
		return getBoundingBox( rv, AffineMatrix4x4.createIdentity(), cumulative );
	}

	@Override
	protected void appendRepr( StringBuilder sb ) {
		super.appendRepr( sb );
		sb.append( " jointId=" + this.jointID.getValue() );
	}

	public final edu.cmu.cs.dennisc.property.StringProperty jointID = new edu.cmu.cs.dennisc.property.StringProperty( this, null );
	public final edu.cmu.cs.dennisc.property.BooleanProperty isFreeInX = new edu.cmu.cs.dennisc.property.BooleanProperty( this, false );
	public final edu.cmu.cs.dennisc.property.BooleanProperty isFreeInY = new edu.cmu.cs.dennisc.property.BooleanProperty( this, false );
	public final edu.cmu.cs.dennisc.property.BooleanProperty isFreeInZ = new edu.cmu.cs.dennisc.property.BooleanProperty( this, false );

	//    public final edu.cmu.cs.dennisc.property.DoubleProperty boundingRadius = new edu.cmu.cs.dennisc.property.DoubleProperty( this, Double.NaN, true);

	public final edu.cmu.cs.dennisc.property.InstanceProperty<AxisAlignedBox> boundingBox = new edu.cmu.cs.dennisc.property.InstanceProperty<AxisAlignedBox>( this, new AxisAlignedBox() );

	public final Vector3fProperty oStiffness = new Vector3fProperty( this, new Vector3f() );
	public final EulerAnglesProperty oBoneOrientation = new EulerAnglesProperty( this, EulerAngles.createIdentity() );
	public final EulerAnglesProperty oPreferedAngles = new EulerAnglesProperty( this, EulerAngles.createIdentity() );
	public final EulerAnglesProperty oLocalRotationAxis = new EulerAnglesProperty( this, EulerAngles.createIdentity() );
	public final Vector3fProperty oMinimumDampRange = new Vector3fProperty( this, new Vector3f() );
	public final Vector3fProperty oMaximumDampRange = new Vector3fProperty( this, new Vector3f() );
	public final Vector3fProperty oMinimumDampStrength = new Vector3fProperty( this, new Vector3f() );
	public final Vector3fProperty oMaximumDampStrength = new Vector3fProperty( this, new Vector3f() );

	private SkeletonVisual parentVisual = null;
}
