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

package org.alice.apis.moveandturn;


/**
 * @author Dennis Cosgrove
 */
public class PickHintUtilities {
	private static java.util.Set< Class< ? > > groundClses = new java.util.HashSet< Class<?> >(); 
	static {
		String[] clsNames = {
				"org.alice.apis.moveandturn.gallery.environments.Ground",
				"org.alice.apis.moveandturn.gallery.environments.grounds.DirtGround",
				"org.alice.apis.moveandturn.gallery.environments.grounds.GrassyGround",
				"org.alice.apis.moveandturn.gallery.environments.grounds.MoonSurface",
				"org.alice.apis.moveandturn.gallery.environments.grounds.SandyGround",
				"org.alice.apis.moveandturn.gallery.environments.grounds.SeaSurface",
				"org.alice.apis.moveandturn.gallery.environments.grounds.SnowyGround",
		};
		for( String clsName : clsNames ) {
			try {
				Class<?> cls = edu.cmu.cs.dennisc.java.lang.ClassUtilities.forName( clsName );
				groundClses.add( cls );
			} catch( ClassNotFoundException cnfe ) {
				edu.cmu.cs.dennisc.print.PrintUtilities.println( "WARNING: could not find class:", clsName );
			}
		}
	}
	private PickHintUtilities() {
		throw new AssertionError();
	}

	private static boolean isGround( org.alice.apis.moveandturn.AbstractModel model ) {
		for( Class< ? > cls : PickHintUtilities.groundClses ) {
			if( cls.isAssignableFrom( model.getClass() ) ) {
				return true;
			}
		}
		return false;
	}

	public static Transformable setPickHint( Transformable rv ) {
		edu.cmu.cs.dennisc.scenegraph.Transformable sgTransformable = rv.getSGTransformable();
		if( rv instanceof org.alice.apis.moveandturn.AbstractModel ) {
			org.alice.apis.moveandturn.AbstractModel model = (org.alice.apis.moveandturn.AbstractModel)rv;
			if( PickHintUtilities.isGround( model ) ) {
				sgTransformable.putBonusDataFor( org.alice.interact.PickHint.PICK_HINT_KEY, org.alice.interact.PickHint.GROUND );
			} else {
				sgTransformable.putBonusDataFor( org.alice.interact.PickHint.PICK_HINT_KEY, org.alice.interact.PickHint.MOVEABLE_OBJECTS );
				edu.cmu.cs.dennisc.math.AxisAlignedBox box = model.getAxisAlignedMinimumBoundingBox();
				edu.cmu.cs.dennisc.math.Matrix3x3 scale = model.getOriginalScale();
				if( scale != null && scale.isNaN() == false && scale.isIdentity() == false ) {
					edu.cmu.cs.dennisc.math.Matrix3x3 scaleInv = new edu.cmu.cs.dennisc.math.Matrix3x3( scale );
					scaleInv.invert();
					box.scale( scaleInv );
				}
				sgTransformable.putBonusDataFor( org.alice.interact.GlobalDragAdapter.BOUNDING_BOX_KEY, box );
			}
		} else if( rv instanceof org.alice.apis.moveandturn.Light ) {
			sgTransformable.putBonusDataFor( org.alice.interact.PickHint.PICK_HINT_KEY, org.alice.interact.PickHint.LIGHT );
		} else if( rv instanceof org.alice.apis.moveandturn.SymmetricPerspectiveCamera ) {
			sgTransformable.putBonusDataFor( org.alice.interact.PickHint.PICK_HINT_KEY, org.alice.interact.PickHint.PERSPECTIVE_CAMERA );
		} else if( rv instanceof org.alice.apis.moveandturn.OrthographicCamera ) {
			sgTransformable.putBonusDataFor( org.alice.interact.PickHint.PICK_HINT_KEY, org.alice.interact.PickHint.ORTHOGRAPHIC_CAMERA );
		}
		
		return rv;
	}
}
