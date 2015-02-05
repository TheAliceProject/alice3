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
package org.lgna.ik.poser.scene;

import java.awt.event.MouseEvent;
import java.util.List;

import org.alice.interact.handle.ManipulationHandle3D;
import org.lgna.ik.poser.PoserSphereManipulatorListener;
import org.lgna.ik.poser.controllers.PoserEvent;
import org.lgna.ik.poser.jselection.JointSelectionSphere;
import org.lgna.story.EmployeesOnly;
import org.lgna.story.SMovableTurnable;
import org.lgna.story.SSphere;
import org.lgna.story.implementation.CameraImp;
import org.lgna.story.implementation.EntityImp;
import org.lgna.story.implementation.SceneImp;

import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Ray;
import edu.cmu.cs.dennisc.render.OnscreenRenderTarget;
import edu.cmu.cs.dennisc.scenegraph.Composite;
import edu.cmu.cs.dennisc.scenegraph.Joint;
import edu.cmu.cs.dennisc.scenegraph.Transformable;

/**
 * @author Matt May
 */
public class PoserPicturePlaneInteraction extends PicturePlaneInteraction {

	private static final double MIN_SELECTION_DISTANCE = 50;
	private final AbstractPoserScene<?> scene;
	private final CameraImp camera;
	private final List<PoserSphereManipulatorListener> listeners = edu.cmu.cs.dennisc.java.util.Lists.newCopyOnWriteArrayList();
	private JointSelectionSphere selected;
	private JointSelectionSphere anchor;
	private Joint joint;
	private boolean started = false;

	public PoserPicturePlaneInteraction( OnscreenRenderTarget<?> renderTarget, AbstractPoserScene scene ) {
		super( renderTarget, ( (CameraImp)( (SceneImp)EmployeesOnly.getImplementation( scene ) ).findFirstCamera() ).getSgCamera() );
		this.scene = scene;
		SceneImp sceneImp = (SceneImp)EmployeesOnly.getImplementation( scene );
		this.camera = sceneImp.findFirstCamera();

		final boolean IS_DEBUG_DESIRED = true;
		if( IS_DEBUG_DESIRED ) {
			OnscreenRenderTarget onscreenPicturePlane = getOnscreenPicturePlane();
			onscreenPicturePlane.addRenderTargetListener( new DebugOverlay( new OverlayFunction() {

				@Override
				public java.awt.Color getColorForXY( int x, int y ) {
					JointSelectionSphere jointSelectionSphere = calculateJointSelectionSphereAtPixel( x, y );
					if( jointSelectionSphere != null ) {
						return java.awt.Color.RED;
					} else {
						return java.awt.Color.BLUE;
					}
				}
			} ) );
		}
	}

	private JointSelectionSphere calculateJointSelectionSphereAtPixel( int x, int y ) {
		Ray rayAtPixel = this.getOnscreenPicturePlane().getRayAtPixel( x, y );
		double closest = Double.MAX_VALUE;//Integer.MAX_VALUE;
		JointSelectionSphere selected = null;
		for( JointSelectionSphere sphere : scene.getJointSelectionSpheres() ) {
			double rayLength = getSphereRayIntersection( rayAtPixel, sphere );
			if( Double.isNaN( rayLength ) == false ) {
				if( ( rayLength > 0 ) && ( rayLength < closest ) ) {
					//System.out.println( "selected(m): " + sphere );
					//System.out.println( "rayLength: " + rayLength );
					selected = sphere;
					closest = rayLength;
				}
			}
		}
		return selected;
	}

	@Override
	protected Transformable pick( MouseEvent e ) {
		ManipulationHandle3D handle = checkIfHandleSelected( e );
		if( handle != null ) {
			joint = (Joint)handle.getManipulatedObject();
			return handle;
		}
		JointSelectionSphere selected = this.calculateJointSelectionSphereAtPixel( e.getX(), e.getY() );
		if( selected != null ) {
			//System.out.println( "selectedFinal: " + selected.getJoint() );
			Composite sgComposite = EmployeesOnly.getImplementation( selected ).getSgComposite();
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

	private double getSphereRayIntersection( Ray ray, SSphere sSphere ) {
		EntityImp sphere = EmployeesOnly.getImplementation( sSphere );
		Point3 center = sphere.getTransformation( camera ).translation;

		final boolean IS_USING_MATH_CLASSES = true;
		if( IS_USING_MATH_CLASSES ) {
			edu.cmu.cs.dennisc.math.immutable.MRay mRayInCameraSpace = ray.createImmutable();
			edu.cmu.cs.dennisc.math.immutable.MSphere mSphereInCameraSpace = new edu.cmu.cs.dennisc.math.immutable.MSphere( center.createImmutable(), sSphere.getRadius() );
			return mSphereInCameraSpace.intersect( mRayInCameraSpace );
		} else {
			//this formula comes from ccs.neu.edu
			//		center.x = -1 * center.x;
			//		center.y = -1 * center.y;
			//		center.z = -1 * center.z;
			double radius = sSphere.getRadius();//1;
			double dx = ray.getDirection().x - ray.getOrigin().x;
			double dy = ray.getDirection().y - ray.getOrigin().y;
			double dz = ray.getDirection().z - ray.getOrigin().z;
			double a = ( dx * dx ) + ( dy * dy ) + ( dz * dz );
			double b = ( 2 * dx * ( ray.getOrigin().x - center.x ) ) + ( 2 * dy * ( ray.getOrigin().y - center.y ) ) + ( 2 * dz * ( ray.getOrigin().z - center.z ) );
			double c = ( ( center.x * center.x ) + ( center.y * center.y ) + ( center.z * center.z ) + ( ray.getOrigin().x * ray.getOrigin().x ) + ( ray.getOrigin().y * ray.getOrigin().y ) + ( ray.getOrigin().z * ray.getOrigin().z )
					+ ( -2 * ( ( center.x * ray.getOrigin().x ) + ( center.y * ray.getOrigin().y ) + ( center.z * ray.getOrigin().z ) ) ) ) - ( radius * radius );
			double t = ( -b - Math.sqrt( ( b * b ) - ( 4 * a * c ) ) ) / ( 2 * a );

			double intersectionX = ray.getOrigin().x + ( t * dx );
			double intersectionY = ray.getOrigin().y + ( t * dy );
			double intersectionZ = ray.getOrigin().z + ( t * dz );

			if( Double.isNaN( t ) ) {
				//			System.out.println( "Fail(NaN): " + sSphere );
				return -1;
			} else if( t < 0 ) {
				//			System.out.println( "Fail(Neg): " + sSphere );
				return -1;
			}
			double length = Math.sqrt( ( intersectionX * intersectionX ) + ( intersectionY * intersectionY ) + ( intersectionZ * intersectionZ ) );
			System.out.println( "======" );
			System.out.println( "t: " + t );
			System.out.println( sSphere );
			//		System.out.println( "( " + intersectionX + ", " + intersectionY + ", " + intersectionZ + " )" );
			System.out.println( "len:" + length );
			System.out.println( "======" );
			return length;
		}
	}

	private JointSelectionSphere pickJoint( JointSelectionSphere one, JointSelectionSphere two, double distOne, double distTwo ) {
		double oneCameraDistance = one.getDistanceTo( (SMovableTurnable)camera.getAbstraction() );
		double twoCameraDistance = two.getDistanceTo( (SMovableTurnable)camera.getAbstraction() );
		double cameraDelta = oneCameraDistance - twoCameraDistance;
		if( Math.abs( cameraDelta ) > .1 ) {
			System.out.println( "short" );
			return oneCameraDistance < twoCameraDistance ? one : two;
		} else {
			System.out.println( cameraDelta );
		}
		System.out.println( "=============" );
		System.out.println( one.getJoint() );
		System.out.println( "oneC:  " + oneCameraDistance );
		System.out.println( "dist1: " + distOne );
		System.out.println( "twoC:  " + twoCameraDistance );
		System.out.println( "dist2: " + distTwo );
		System.out.println( two.getJoint() );
		System.out.println( "=============" );
		if( ( oneCameraDistance < twoCameraDistance ) ) {
			if( distOne < distTwo ) {
				System.out.println( "a1" );
				return one;
			} else if( ( distTwo * 2 ) < distOne ) {
				System.out.println( "a2" );
				return two;
			} else {
				System.out.println( "a3" );
				return one;
			}
		} else if( twoCameraDistance < oneCameraDistance ) {
			if( distTwo < distOne ) {
				System.out.println( "b1" );
				return two;
			} else if( ( distOne * 2 ) < distTwo ) {
				System.out.println( "b2" );
				return one;
			} else {
				System.out.println( "b3" );
				return two;
			}
		} else {
			if( distOne < distTwo ) {
				System.out.println( "c1" );
				return one;
			} else {
				System.out.println( "c2" );
				return two;
			}
		}
	}

	private ManipulationHandle3D checkIfHandleSelected( MouseEvent e ) {
		SceneImp implementation = EmployeesOnly.getImplementation( scene );
		edu.cmu.cs.dennisc.render.RenderTarget rt = implementation.getProgram().getOnscreenRenderTarget();
		edu.cmu.cs.dennisc.render.PickResult pickResult = rt.getSynchronousPicker().pickFrontMost( e.getX(), e.getY(), edu.cmu.cs.dennisc.render.PickSubElementPolicy.NOT_REQUIRED );
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
		if( joint != null ) {
			JointSelectionSphere[] arr = (JointSelectionSphere[])scene.getJointSelectionSpheres().toArray( new JointSelectionSphere[ 0 ] );
			for( JointSelectionSphere sphere : arr ) {
				if( sphere.getJoint().getSgComposite() == joint ) {
					selected = sphere;
				}
			}
		}
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
