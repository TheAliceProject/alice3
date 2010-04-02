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
package org.alice.stageide.sceneeditor.viewmanager;

import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.scenegraph.AbstractCamera;
import edu.cmu.cs.dennisc.scenegraph.AsSeenBy;
import edu.cmu.cs.dennisc.scenegraph.Composite;
import edu.cmu.cs.dennisc.scenegraph.ReferenceFrame;
import edu.cmu.cs.dennisc.scenegraph.SymmetricPerspectiveCamera;
import edu.cmu.cs.dennisc.scenegraph.Transformable;

public class PointOfView {

	private static final String ORIENTATION = ".ORIENTATION";
	private static final String RIGHT = ".RIGHT";
	private static final String UP = ".UP";
	private static final String BACKWARD = ".BACKWARD";
	private static final String POSITION = ".POSITION";
	private static final String X_VALUE = ".X";
	private static final String Y_VALUE = ".Y";
	private static final String Z_VALUE = ".Z";
	private static final String NAME = ".NAME";
	
	private Transformable transformable;
	private AffineMatrix4x4 transform;
	private AbstractCamera camera = null;
	private String viewName;
	
	public PointOfView(String viewName)
	{
		this(viewName, null, null);
	}
	
	public PointOfView(String viewName, AffineMatrix4x4 transform, Composite parent)
	{
		this.viewName = viewName;
		init(transform, parent);
	}
	
	protected void init(AffineMatrix4x4 transform, Composite parent)
	{
		if (transform == null)
		{
			this.transform = new AffineMatrix4x4();
		}
		else
		{
			this.transform = new AffineMatrix4x4(transform);
		}
		this.transformable = new Transformable();
		this.transformable.localTransformation.setValue( this.transform );
		this.transformable.setName( this.viewName );
		this.transformable.setParent( parent );
		SymmetricPerspectiveCamera symmetricPerspectiveCamera = new SymmetricPerspectiveCamera();
		symmetricPerspectiveCamera.farClippingPlaneDistance.setValue( 1000.0 );
		camera = symmetricPerspectiveCamera;
		camera.setParent( this.transformable );
	}
	
	public void initFromProjectProperties(edu.cmu.cs.dennisc.alice.Project.Properties properties, String keyPrefix, String keySuffix)
	{
		double rightX = properties.getDouble( keyPrefix + ORIENTATION+RIGHT+X_VALUE + keySuffix, 1.0d );
		double rightY = properties.getDouble( keyPrefix + ORIENTATION+RIGHT+Y_VALUE + keySuffix, 0.0d );
		double rightZ = properties.getDouble( keyPrefix + ORIENTATION+RIGHT+Z_VALUE + keySuffix, 0.0d );
		Vector3 right = new Vector3(rightX, rightY, rightZ);
		
		double upX = properties.getDouble( keyPrefix + ORIENTATION+UP+X_VALUE + keySuffix, 0.0d );
		double upY = properties.getDouble( keyPrefix + ORIENTATION+UP+Y_VALUE + keySuffix, 0.7071067811865476d );
		double upZ = properties.getDouble( keyPrefix + ORIENTATION+UP+Z_VALUE + keySuffix, -0.7071067811865476d );
		Vector3 up = new Vector3(upX, upY, upZ);
		
		double backwardX = properties.getDouble( keyPrefix + ORIENTATION+BACKWARD+X_VALUE + keySuffix, 0.0d );
		double backwardY = properties.getDouble( keyPrefix + ORIENTATION+BACKWARD+Y_VALUE + keySuffix, 0.7071067811865476d );
		double backwardZ = properties.getDouble( keyPrefix + ORIENTATION+BACKWARD+Z_VALUE + keySuffix, 0.7071067811865476d );
		Vector3 backward = new Vector3(backwardX, backwardY, backwardZ);
		OrthogonalMatrix3x3 orientation = new OrthogonalMatrix3x3( right, up, backward );
		
		double positionX = properties.getDouble( keyPrefix + POSITION+X_VALUE + keySuffix, 0.0d );
		double positionY = properties.getDouble( keyPrefix + POSITION+Y_VALUE + keySuffix, 5.0d );
		double positionZ = properties.getDouble( keyPrefix + POSITION+Z_VALUE + keySuffix, 5.0d );
		Point3 position = new Point3(positionX, positionY, positionZ);

		this.setName( properties.getString( keyPrefix + ORIENTATION+RIGHT+NAME + keySuffix, "DEFAULT_VIEW_NAME") );
		
		this.setTransform( new AffineMatrix4x4(orientation, position) );
	}
	
	public void writeToProjectProperties(edu.cmu.cs.dennisc.alice.Project.Properties properties, String keyPrefix, String keySuffix)
	{
		
		if (this.transformable != null)
		{
			AffineMatrix4x4 transform = this.transformable.getAbsoluteTransformation();
			properties.putString( keyPrefix + ORIENTATION+RIGHT+NAME + keySuffix, this.viewName);
			
			properties.putDouble( keyPrefix + ORIENTATION+RIGHT+X_VALUE + keySuffix, transform.orientation.right.x);
			properties.putDouble( keyPrefix + ORIENTATION+RIGHT+Y_VALUE + keySuffix, transform.orientation.right.y);
			properties.putDouble( keyPrefix + ORIENTATION+RIGHT+Z_VALUE + keySuffix, transform.orientation.right.z );
			
			properties.putDouble( keyPrefix + ORIENTATION+UP+X_VALUE + keySuffix, transform.orientation.up.x);
			properties.putDouble( keyPrefix + ORIENTATION+UP+Y_VALUE + keySuffix, transform.orientation.up.y);
			properties.putDouble( keyPrefix + ORIENTATION+UP+Z_VALUE + keySuffix, transform.orientation.up.z );
			
			properties.putDouble( keyPrefix + ORIENTATION+BACKWARD+X_VALUE + keySuffix, transform.orientation.backward.x);
			properties.putDouble( keyPrefix + ORIENTATION+BACKWARD+Y_VALUE + keySuffix, transform.orientation.backward.y);
			properties.putDouble( keyPrefix + ORIENTATION+BACKWARD+Z_VALUE + keySuffix, transform.orientation.backward.z );
			
			properties.putDouble( keyPrefix + POSITION+X_VALUE + keySuffix, transform.translation.x );
			properties.putDouble( keyPrefix + POSITION+Y_VALUE + keySuffix, transform.translation.y );
			properties.putDouble( keyPrefix + POSITION+Z_VALUE + keySuffix, transform.translation.z );
		}
	}
	
	public String getName()
	{
		return this.viewName;
	}
	
	public void setName(String name)
	{
		this.viewName = name;
		if (this.transformable != null)
		{
			this.transformable.setName( name );
		}
	}
	
	public AffineMatrix4x4 getTransform() {
		return this.transformable.getAbsoluteTransformation();
	}

	public void setTransform(AffineMatrix4x4 transform) 
	{
		this.transform.set( transform );
		if (this.transformable.getRoot() != null)
		{
			this.transformable.setTransformation( transform, AsSeenBy.SCENE );
		}
	}

	public Transformable getTransformable()
	{
		return this.transformable;
	}
	
	public void setParent(Composite parent)
	{
		this.transformable.setParent( parent );
		if (this.transformable.getRoot() != null)
		{
			this.transformable.setTransformation( this.transform, AsSeenBy.SCENE );
		}
	}
	
	public AbstractCamera getCamera()
	{
		return this.camera;
	}

	
//	@Override
//	public String toString() {
//		String s = "";
//		s += "[["+this.transform.orientation.right.x+", "+this.transform.orientation.right.y+", "+this.transform.orientation.right.z+"]\n"+
//		"["+this.transform.orientation.up.x+", "+this.transform.orientation.up.y+", "+this.transform.orientation.up.z+"]\n"+
//		"["+this.transform.orientation.backward.x+", "+this.transform.orientation.backward.y+", "+this.transform.orientation.backward.z+"]]\n";
//		s += "<"+this.transform.translation.x+", "+this.transform.translation.y+", "+this.transform.translation.z+">";
//		return s;
//	}
//	
	
}
