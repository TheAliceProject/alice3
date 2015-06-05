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
package org.alice.interact.handle;

/**
 * @author David Culyba
 */
public class HandleSet extends java.util.BitSet {
	public static final HandleSet STOOD_UP_GROUND_TRANSLATION_VISUALIZATION = new HandleSet( HandleGroup.STOOD_UP_TRANSLATION, HandleGroup.VISUALIZATION, HandleGroup.X_AND_Z_AXIS );
	public static final HandleSet STOOD_UP_UP_DOWN_TRANSLATION_VISUALIZATION = new HandleSet( HandleGroup.STOOD_UP_TRANSLATION, HandleGroup.VISUALIZATION, HandleGroup.Y_AXIS );
	public static final HandleSet ABSOLUTE_GROUND_TRANSLATION_VISUALIZATION = new HandleSet( HandleGroup.ABSOLUTE_TRANSLATION, HandleGroup.VISUALIZATION, HandleGroup.X_AND_Z_AXIS );
	public static final HandleSet ABSOLUTE_UP_DOWN_TRANSLATION_VISUALIZATION = new HandleSet( HandleGroup.ABSOLUTE_TRANSLATION, HandleGroup.VISUALIZATION, HandleGroup.Y_AXIS );
	public static final HandleSet DEFAULT_INTERACTION = new HandleSet( HandleGroup.DEFAULT, HandleGroup.INTERACTION, HandleGroup.SELECTION );
	public static final HandleSet SELECTION_ONLY = new HandleSet( HandleGroup.SELECTION );
	public static final HandleSet RESIZE_INTERACTION = new HandleSet( HandleGroup.RESIZE, HandleGroup.INTERACTION );
	public static final HandleSet ROTATION_INTERACTION = new HandleSet( HandleGroup.ROTATION, HandleGroup.INTERACTION );
	public static final HandleSet JOINT_ROTATION_INTERACTION = new HandleSet( HandleGroup.ROTATION, HandleGroup.INTERACTION );
	public static final HandleSet TRANSLATION_INTERACTION = new HandleSet( HandleGroup.TRANSLATION, HandleGroup.INTERACTION );
	public static final HandleSet STOOD_UP_TRANSLATION_INTERACTION = new HandleSet( HandleGroup.STOOD_UP_TRANSLATION, HandleGroup.INTERACTION );
	public static final HandleSet ABSOLUTE_TRANSLATION_INTERACTION = new HandleSet( HandleGroup.ABSOLUTE_TRANSLATION, HandleGroup.INTERACTION );
	public static final HandleSet MAIN_ORTHOGRAPHIC_CAMERA_CONTROLS = new HandleSet( HandleGroup.ORTHOGRAPHIC_CAMERA, HandleGroup.MAIN_CAMERA );
	public static final HandleSet MAIN_PERSPECTIVE_CAMERA_CONTROLS = new HandleSet( HandleGroup.PERSPECTIVE_CAMERA, HandleGroup.MAIN_CAMERA );

	public static enum HandleGroup {
		ROTATION,
		TRANSLATION,
		RESIZE,
		DEFAULT,
		LOCAL,
		//		STOOD_UP_Y_AXIS,
		//		STOOD_UP_X_AXIS,
		//		STOOD_UP_Z_AXIS,
		ABSOLUTE,
		VISUALIZATION,
		INTERACTION,
		SELECTION,
		X_AXIS,
		Y_AXIS,
		Z_AXIS,
		RESIZE_AXIS,
		X_AND_Z_AXIS,
		X_AND_Y_AXIS,
		Y_AND_Z_AXIS,
		CAMERA,
		ORTHOGRAPHIC_CAMERA,
		PERSPECTIVE_CAMERA,
		MAIN_CAMERA,
		TOP_LEFT_CAMERA,
		TOP_RIGHT_CAMERA,
		BOTTOM_LEFT_CAMERA,
		BOTTOM_RIGHT_CAMERA,
		JOINT,
		STOOD_UP_ROTATION,
		STOOD_UP_TRANSLATION,
		ABSOLUTE_TRANSLATION
	}

	public static String getStringForSet( HandleSet set ) {
		//		if( set == RESIZE_INTERACTION ) {
		//			java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle( HandleSet.class.getPackage().getName() + ".handle" );
		//			return resourceBundle.getString( "resize" );
		//		} else if( set == ROTATION_INTERACTION ) {
		//			java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle( HandleSet.class.getPackage().getName() + ".handle" );
		//			return resourceBundle.getString( "rotation" );
		//		} else if( set == TRANSLATION_INTERACTION ) {
		//			java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle( HandleSet.class.getPackage().getName() + ".handle" );
		//			return resourceBundle.getString( "translation" );
		//		} else if( set == STOOD_UP_TRANSLATION_INTERACTION ) {
		//			java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle( HandleSet.class.getPackage().getName() + ".handle" );
		//			return resourceBundle.getString( "translation" );
		//		} else if( set == DEFAULT_INTERACTION ) {
		//			java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle( HandleSet.class.getPackage().getName() + ".handle" );
		//			return resourceBundle.getString( "default" );
		//		} else if( set == GROUND_TRANSLATION_VISUALIZATION ) {
		//			java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle( HandleSet.class.getPackage().getName() + ".handle" );
		//			return resourceBundle.getString( "groundTranslation" );
		//		} else
		{

			StringBuilder sb = new StringBuilder();
			for( HandleGroup hg : HandleGroup.class.getEnumConstants() ) {
				if( set.get( hg.ordinal() ) ) {
					sb.append( hg + " " );
				}
			}
			return sb.toString();
		}
	}

	public HandleSet( HandleGroup... groups ) {
		addGroups( groups );
	}

	public void addGroup( HandleGroup group ) {
		this.set( group.ordinal() );
	}

	public void addGroups( HandleGroup... groups ) {
		for( HandleGroup group : groups ) {
			this.set( group.ordinal() );
		}
	}

	//HandleSet intersection is true if all of the bits passed in are set on this
	//An empty set will never match
	@Override
	public boolean intersects( java.util.BitSet set ) {
		if( set != null ) {
			boolean intersection = false;
			for( int i = 0; i < set.length(); i++ ) {
				if( set.get( i ) && this.get( i ) ) {
					intersection = true;
				} else if( set.get( i ) && !this.get( i ) ) {
					return false;
				}
			}
			return intersection;
		} else {
			return false;
		}
	}

	public void addSet( HandleSet set ) {
		this.or( set );
	}

	@Override
	public String toString() {
		return getStringForSet( this );
	}
}
