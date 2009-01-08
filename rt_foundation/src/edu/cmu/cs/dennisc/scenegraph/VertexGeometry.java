/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */

package edu.cmu.cs.dennisc.scenegraph;

/**
 * @author Dennis Cosgrove
 */
public abstract class VertexGeometry extends Geometry {
	public class VerticesProperty extends edu.cmu.cs.dennisc.property.CopyableArrayProperty< Vertex > {
		public VerticesProperty( edu.cmu.cs.dennisc.property.InstancePropertyOwner owner, Vertex... vertices ) {
			super( owner, vertices );
		}
		@Override
		protected Vertex[] createArray( int length ) {
			return new Vertex[ length ];
		}
		@Override
		protected Vertex createCopy( Vertex src ) {
			return new Vertex( src );
		}

		@Deprecated
		public void touch() {
			edu.cmu.cs.dennisc.property.PropertyOwner owner = getOwner();
			setValue( owner, getValue( owner ) );
			//todo
			//			edu.cmu.cs.dennisc.property.event.PropertyEvent e = new edu.cmu.cs.dennisc.property.event.PropertyEvent( this, owner, getValue() );
			//			//owner.firePropertyChanging( e );
			//			//m_value = value;
			//			owner.firePropertyChanged( e );
		}
	}

	public final VerticesProperty vertices = new VerticesProperty( this );

	@Override
	protected void updateBoundingBox( edu.cmu.cs.dennisc.math.AxisAlignedBox boundingBox ) {
		edu.cmu.cs.dennisc.scenegraph.bound.BoundUtilities.getBoundingBox( boundingBox, vertices.getValue() );
	}
	@Override
	protected void updateBoundingSphere( edu.cmu.cs.dennisc.math.Sphere boundingSphere ) {
		edu.cmu.cs.dennisc.scenegraph.bound.BoundUtilities.getBoundingSphere( boundingSphere, vertices.getValue() );
	}
	@Override
	protected void updatePlane( edu.cmu.cs.dennisc.math.Vector3 forward, edu.cmu.cs.dennisc.math.Vector3 upGuide, edu.cmu.cs.dennisc.math.Point3 translation ) {
		edu.cmu.cs.dennisc.math.Point3 point0;
		edu.cmu.cs.dennisc.math.Point3 point1;
		edu.cmu.cs.dennisc.math.Vector3f normal;
		edu.cmu.cs.dennisc.scenegraph.Vertex[] vertices = this.vertices.getValue();
		assert vertices.length >= 2;
		point0 = vertices[ 0 ].position;
		point1 = vertices[ 1 ].position;
		normal = vertices[ 0 ].normal;

		forward.set( normal.x, normal.y, normal.z );
		forward.normalize();
		forward.negate();
		
		upGuide.set( point0 );
		upGuide.subtract( point1 );
		upGuide.normalize();
		
		translation.set( point0 );
	}
	@Override
	public void transform( edu.cmu.cs.dennisc.math.AbstractMatrix4x4 trans ) {
		throw new RuntimeException( "todo" );
		//		for( Vertex vertex : vertices.getValue() ) {
		//			vertex.transform( trans );
		//		}
		//		fireVerticesChange();
	}
}
