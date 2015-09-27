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
package org.alice.interact;

import java.util.List;

import org.alice.interact.handle.ManipulationHandle3D;
import org.lgna.story.SThing;
import org.lgna.story.implementation.EntityImp;

import edu.cmu.cs.dennisc.scenegraph.Component;
import edu.cmu.cs.dennisc.scenegraph.OrthographicCamera;
import edu.cmu.cs.dennisc.scenegraph.SymmetricPerspectiveCamera;

/**
 * @author dculyba
 * 
 */
public class PickUtilities {

	public static Component getFirstClassFromComponent( Component object ) {
		if( object == null ) {
			return null;
		}
		if( object instanceof ManipulationHandle3D ) {
			return object;
		}
		if( EntityImp.getInstance( object ) != null ) {
			return object;
		}
		return getFirstClassFromComponent( object.getParent() );
	}

	public static PickHint getPickType( edu.cmu.cs.dennisc.render.PickResult pickObject ) {
		boolean isNull = ( pickObject == null ) || ( pickObject.getGeometry() == null ) || ( pickObject.getVisual() == null );
		if( isNull ) {
			return PickHint.PickType.NOTHING.pickHint();
		} else {
			PickHint pickType = getPickType( pickObject.getVisual() );
			return pickType;
		}
	}

	private static <E> E getParentOfClass( edu.cmu.cs.dennisc.scenegraph.Component object, Class<E> cls ) {
		if( object != null ) {
			if( cls.isAssignableFrom( object.getClass() ) ) {
				return cls.cast( object );
			} else {
				return getParentOfClass( object.getParent(), cls );
			}
		} else {
			return null;
		}
	}

	public static EntityImp getEntityImpFromPickedObject( edu.cmu.cs.dennisc.scenegraph.Component pickedObject ) {
		if( pickedObject != null ) {
			edu.cmu.cs.dennisc.scenegraph.Component mainComponent = PickUtilities.getFirstClassFromComponent( pickedObject );
			EntityImp entityImplementation = EntityImp.getInstance( mainComponent );
			return entityImplementation;
		} else {
			return null;
		}
	}

	public static SThing getEntityFromPickedObject( edu.cmu.cs.dennisc.scenegraph.Component pickedObject ) {
		if( pickedObject != null ) {
			edu.cmu.cs.dennisc.scenegraph.Component mainComponent = PickUtilities.getFirstClassFromComponent( pickedObject );
			EntityImp entityImplementation = EntityImp.getInstance( mainComponent );
			if( entityImplementation != null ) {
				return entityImplementation.getAbstraction();
			}
		}
		return null;
	}

	public static PickHint getPickHintForEntity( SThing entity ) {
		List<PickHint.PickType> pickTypes = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
		if( entity != null ) {
			if( entity instanceof org.lgna.story.STurnable ) {
				pickTypes.add( PickHint.PickType.TURNABLE );
			}
			if( entity instanceof org.lgna.story.SMovableTurnable ) {
				pickTypes.add( PickHint.PickType.MOVEABLE );
			}
			if( entity instanceof org.lgna.story.Resizable ) {
				pickTypes.add( PickHint.PickType.RESIZABLE );
			}
			if( ( entity instanceof org.lgna.story.SCamera ) || ( entity instanceof org.lgna.story.SModel ) || ( entity instanceof org.lgna.story.SMarker ) ) {
				pickTypes.add( PickHint.PickType.SELECTABLE );
			}
			if( entity instanceof org.lgna.story.SMovableTurnable ) {
				pickTypes.add( PickHint.PickType.VIEWABLE );
			}
			if( entity instanceof org.lgna.story.CameraMarker ) {
				pickTypes.add( PickHint.PickType.CAMERA_MARKER );
			}
			if( entity instanceof org.lgna.story.SThingMarker ) {
				pickTypes.add( PickHint.PickType.OBJECT_MARKER );
			}
			if( entity instanceof org.lgna.story.SSun ) {
				pickTypes.add( PickHint.PickType.SUN );
			}
			if( entity instanceof org.lgna.story.SJoint ) {
				pickTypes.add( PickHint.PickType.JOINT );
			}
		}
		if( pickTypes.size() == 0 ) {
			return new PickHint();
		} else {
			return new PickHint( pickTypes.toArray( new PickHint.PickType[ pickTypes.size() ] ) );
		}
	}

	public static PickHint getPickTypeForImp( EntityImp imp ) {
		if( imp != null ) {
			return getPickHintForEntity( imp.getAbstraction() );
		} else {
			return getPickHintForEntity( (SThing)null );
		}
	}

	public static PickHint getPickType( edu.cmu.cs.dennisc.scenegraph.Component pickedObject ) {
		PickHint returnHint = null;
		if( pickedObject != null ) {
			SThing entity = getEntityFromPickedObject( pickedObject );
			returnHint = getPickHintForEntity( entity );

			if( pickedObject instanceof edu.cmu.cs.dennisc.scenegraph.Composite ) {
				edu.cmu.cs.dennisc.scenegraph.AbstractCamera camera = null;
				for( edu.cmu.cs.dennisc.scenegraph.Component c : ( (edu.cmu.cs.dennisc.scenegraph.Composite)pickedObject ).getComponents() ) {
					if( c instanceof edu.cmu.cs.dennisc.scenegraph.AbstractCamera ) {
						camera = (edu.cmu.cs.dennisc.scenegraph.AbstractCamera)c;
					}
				}
				if( camera != null ) {
					if( camera instanceof SymmetricPerspectiveCamera ) {
						returnHint.addPickType( PickHint.PickType.PERSPECTIVE_CAMERA );
					} else if( camera instanceof OrthographicCamera ) {
						returnHint.addPickType( PickHint.PickType.ORTHOGRAPHIC_CAMERA );
					}
				} else {
					ManipulationHandle3D handle3D = getParentOfClass( pickedObject, ManipulationHandle3D.class );
					if( handle3D != null ) {
						returnHint.addPickType( PickHint.PickType.THREE_D_HANDLE );
					}
				}
			}
		}
		if( ( returnHint == null ) || returnHint.isEmpty() ) {
			return PickHint.PickType.NOTHING.pickHint();
		} else {
			return returnHint;
		}
	}
}
