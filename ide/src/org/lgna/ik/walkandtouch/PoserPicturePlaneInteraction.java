/**
 * Copyright (c) 2006-2012, Carnegie Mellon University. All rights reserved.
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
package org.lgna.ik.walkandtouch;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import org.alice.interact.handle.ManipulationHandle3D;
import org.lgna.ik.poser.JointSelectionSphere;
import org.lgna.ik.poser.PoserEvent;
import org.lgna.ik.poser.PoserSphereManipulatorListener;
import org.lgna.story.ImplementationAccessor;
import org.lgna.story.implementation.CameraImp;
import org.lgna.story.implementation.EntityImp;
import org.lgna.story.implementation.SceneImp;

import edu.cmu.cs.dennisc.java.util.Collections;
import edu.cmu.cs.dennisc.lookingglass.LookingGlass;
import edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass;
import edu.cmu.cs.dennisc.lookingglass.PickSubElementPolicy;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.scenegraph.Composite;
import edu.cmu.cs.dennisc.scenegraph.Transformable;
import examples.math.pictureplane.PicturePlaneInteraction;

/**
 * @author Matt May
 */
public class PoserPicturePlaneInteraction extends PicturePlaneInteraction {

	private static final double MIN_SELECTION_DISTANCE = 50;
	private final PoserScene scene;
	private final CameraImp camera;
	private final List<PoserSphereManipulatorListener> listeners = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList();
	private JointSelectionSphere selected;
	private JointSelectionSphere anchor;

	public PoserPicturePlaneInteraction( PoserScene scene, OnscreenLookingGlass lookingGlass ) {
		super( lookingGlass, ( (CameraImp)( (SceneImp)ImplementationAccessor.getImplementation( scene ) ).findFirstCamera() ).getSgCamera() );
		this.scene = scene;
		SceneImp sceneImp = (SceneImp)ImplementationAccessor.getImplementation( scene );
		this.camera = sceneImp.findFirstCamera();
	}

	@Override
	protected Transformable pick( MouseEvent e ) {
		ManipulationHandle3D handle = checkIfHandleSelected( e );
		if( handle != null ) {
			return handle;
		}
		ArrayList<Point> sphereLocations = Collections.newArrayList();
		double minDist = MIN_SELECTION_DISTANCE;
		JointSelectionSphere[] arr = scene.getJointSelectionSheres().toArray( new JointSelectionSphere[ 0 ] );
		JointSelectionSphere selected = null;
		SceneImp sceneImp = ImplementationAccessor.getImplementation( scene );
		for( JointSelectionSphere sphere : arr ) {
			EntityImp implementation = ImplementationAccessor.getImplementation( sphere );
			Point3 point = implementation.getAbsoluteTransformation().translation;
			sphereLocations.add( sceneImp.transformToAwt( point, camera ) );
		}
		for( int i = 0; i != arr.length; ++i ) {
			double dist = Point.distance( sphereLocations.get( i ).x, sphereLocations.get( i ).y, e.getPoint().x, e.getPoint().y );
			if( dist < minDist ) {
				selected = arr[ i ];
				minDist = dist;
			}
		}
		if( selected != null ) {
			Composite sgComposite = ImplementationAccessor.getImplementation( selected ).getSgComposite();
			if( javax.swing.SwingUtilities.isLeftMouseButton( e ) ) {
				this.selected = selected;
			} else if( javax.swing.SwingUtilities.isRightMouseButton( e ) ) {

				this.anchor = selected;
			}
			return (Transformable)sgComposite;
		} else {
			return null;
		}
	}

	private ManipulationHandle3D checkIfHandleSelected( MouseEvent e ) {
		SceneImp implementation = ImplementationAccessor.getImplementation( scene );
		LookingGlass lg = implementation.getProgram().getOnscreenLookingGlass();
		edu.cmu.cs.dennisc.lookingglass.PickResult pickResult = lg.getPicker().pickFrontMost( e.getX(), e.getY(), PickSubElementPolicy.NOT_REQUIRED );
		if( ( pickResult != null ) && ( pickResult.getVisual() != null ) ) {
			Composite composite = pickResult.getVisual().getParent();
			if( composite != null ) {
				if( composite.getParent() instanceof ManipulationHandle3D ) {
					return (ManipulationHandle3D)composite.getParent();
				}
			}
		}
		return null;
	}

	public void addListener( PoserSphereManipulatorListener sphereDragListener ) {
		this.listeners.add( sphereDragListener );
	}

	private void fireMousePressed( MouseEvent e ) {
		if( selected != null ) {
			for( PoserSphereManipulatorListener listener : listeners ) {
				listener.fireStart( new PoserEvent( selected ) );
			}
		} else if( anchor != null ) {
			for( PoserSphereManipulatorListener listener : listeners ) {
				listener.fireAnchorUpdate( new PoserEvent( anchor ) );
			}
		}
	}

	private void fireMouseReleased( MouseEvent e ) {
		if( selected != null ) {
			for( PoserSphereManipulatorListener listener : listeners ) {
				listener.fireFinish( new PoserEvent( selected ) );
			}
			selected = null;
		}
		anchor = null;

	}

	@Override
	protected void handleMousePressed( MouseEvent e ) {
		super.handleMousePressed( e );
		fireMousePressed( e );
	}

	@Override
	protected void handleMouseReleased( MouseEvent e ) {
		super.handleMouseReleased( e );
		fireMouseReleased( e );
	}

	@Override
	protected void handleMouseDragged( MouseEvent e ) {
		if( selected != null ) {
			super.handleMouseDragged( e );
			fireMousePressed( e );
		}
	}

	protected void handleStateChange() {
	}

}
