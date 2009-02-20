/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * THE ALICE 3.0 ART GALLERY LICENSE.
 * 
 * The Alice 3.0 gallery of The Sims 2 art assets and animations is provided by 
 * Electronic Arts Inc. pursuant to this license.  
 * Copyright (c) 2004 Electronic Arts Inc. All rights reserved.
 * 
 * Redistribution and use of the The Sims 2 art assets, animations, and other 
 * materials ("The Sims 2 Assets"), without modification, are permitted solely 
 * with programs written with Alice 3.0 for personal, non-commercial, and 
 * academic use only, provided that the following conditions are met:
 * 
 * 1. Redistributions of any program source code that uses The Sims 2 Assets 
 *    must retain the above copyright notice, this list of conditions and the 
 *    following disclaimer.
 * 2. Redistributions of any program in binary form that uses The Sims 2 Assets 
 *    must reproduce the above copyright notice, this list of conditions and the 
 *    following disclaimer in the documentation and/or other materials provided 
 *    with the distribution.
 * 3. Neither the name of the Electronic Arts Inc. nor any of its trademarks, 
 *    including the trademark THE SIMS 2, may be used to endorse or promote 
 *    programs or products derived from Alice 3.0 without specific prior written
 *    permission from Electronic Arts Inc.
 * 4. All promotional materials mentioning features or use of Alice 3.0 must 
 *    display the following acknowledgement:
 *     
 *    "This program/product includes art and animations developed by Electronic Arts Inc."
 *
 * THE SIMS 2 ASSETS ARE PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS 
 * "AS IS" WITHOUT WARRANTY OF ANY KIND.  ANY AND ALL EXPRESS, STATUTORY OR 
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF 
 * MERCHANTABILITY,  FITNESS FOR A PARTICULAR PURPOSE, TITLE, AND 
 * NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR 
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; 
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR 
 * OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO THE USE OF OR OTHER DEALINGS 
 * WITH THE SIMS 2 ASSETS, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 */
package edu.cmu.cs.dennisc.nebulous;

/**
 * @author Dennis Cosgrove
 */
public class Person extends Model {
	static {
		edu.cmu.cs.dennisc.lookingglass.opengl.AdapterFactory.register( Person.class, PersonAdapter.class );
	}
	public Person( Object o ) throws LicenseRejectedException {
		initialize( o );
	}
	private native void initialize( Object o );
	public native void setOutfit( Object o );
	public native void setSkinTone( Object o );
	public native void setFitnessLevel( Object o );
	public native void setEyeColor( Object o );
	public native void setHair( Object o );
	

//	private native void getLocalTransformationForBodyPartNamed( double[] result, String name );
//	private native void setLocalTransformationForBodyPartNamed( String name, double[] value );
//	//todo: remove
//	public edu.cmu.cs.dennisc.math.AffineMatrix4x4 getLocalTransformationForBodyPartNamed( edu.cmu.cs.dennisc.math.AffineMatrix4x4 rv, String name ) {
//		assert name != null;
//		assert name.length() > 0;
//		assert name.equals( "<unnamed>" ) == false;
//		assert rv != null;
//
//		double[] buffer = new double[ 12 ];
//		getLocalTransformationForBodyPartNamed( buffer, name );
//		
//		rv.orientation.right.set   ( buffer[ 0 ], buffer[ 1 ], buffer[ 2 ] );
//		rv.orientation.up.set      ( buffer[ 3 ], buffer[ 4 ], buffer[ 5 ] );
//		rv.orientation.backward.set( buffer[ 6 ], buffer[ 7 ], buffer[ 8 ] );
//
//		rv.translation.set( buffer[ 9 ], buffer[ 10 ], buffer[ 11 ] );
//		
//		assert rv.isNaN() == false;
//		return rv;
//	}
//	public edu.cmu.cs.dennisc.math.AffineMatrix4x4 getLocalTransformationForBodyPartNamed( String name ) {
//		return getLocalTransformationForBodyPartNamed( edu.cmu.cs.dennisc.math.AffineMatrix4x4.createNaN(), name );
//	}
//	public void setLocalTransformationForBodyPartNamed( String name, edu.cmu.cs.dennisc.math.AffineMatrix4x4 m ) {
//		assert name != null;
//		assert name.length() > 0;
//		assert name.equals( "<unnamed>" ) == false;
//		assert m != null;
//		assert m.isNaN() == false;
//		double[] buffer = {
//				m.orientation.right.x,
//				m.orientation.right.y,
//				m.orientation.right.z,
//
//				m.orientation.up.x,
//				m.orientation.up.y,
//				m.orientation.up.z,
//
//				m.orientation.backward.x,
//				m.orientation.backward.y,
//				m.orientation.backward.z,
//
//				m.translation.x,
//				m.translation.y,
//				m.translation.z
//		};
//		setLocalTransformationForBodyPartNamed( name, buffer );
//	}
//	
//	private native void getBoneAxis( double[] result, String name );
//	public edu.cmu.cs.dennisc.math.Vector3 getBoneAxis( edu.cmu.cs.dennisc.math.Vector3 rv, String name ) {
//		assert name != null;
//		assert name.length() > 0;
//		assert name.equals( "<unnamed>" ) == false;
//		assert rv != null;
//
//		double[] buffer = new double[ 3 ];
//		getBoneAxis( buffer, name );
//		
//		rv.set( buffer[ 0 ], buffer[ 1 ], buffer[ 2 ] );
//		
//		assert rv.isNaN() == false;
//		return rv;
//	}
//	public edu.cmu.cs.dennisc.math.Vector3 getBoneAxis( String name ) {
//		return getBoneAxis( edu.cmu.cs.dennisc.math.Vector3.createNaN(), name );
//	}

}
