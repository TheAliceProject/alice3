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
import java.awt.event.MouseListener;
import java.util.ArrayList;

import org.lgna.ik.poser.JointSelectionSphere;
import org.lgna.story.ImplementationAccessor;
import org.lgna.story.implementation.CameraImp;
import org.lgna.story.implementation.EntityImp;
import org.lgna.story.implementation.SceneImp;

import edu.cmu.cs.dennisc.java.util.Collections;
import edu.cmu.cs.dennisc.math.Point3;

/**
 * @author Matt May
 */
public class JointSelectionAdapter {

	private PoserScene scene;
	private CameraImp camera;
	private static final double MIN_SELECTION_DISTANCE = 50;

	public JointSelectionAdapter( PoserScene poserScene ) {
		this.scene = poserScene;
		this.camera = ( (SceneImp)ImplementationAccessor.getImplementation( scene ) ).findFirstCamera();
	}

	private MouseListener listener = new MouseListener() {

		public void mouseReleased( MouseEvent e ) {
		}

		public void mousePressed( MouseEvent e ) {
			ArrayList<Point> sphereLocations = Collections.newArrayList();
			Point location = e.getComponent().getLocation();
			double minDist = MIN_SELECTION_DISTANCE;
			JointSelectionSphere[] arr = scene.getJointSelectionSheres().toArray( new JointSelectionSphere[ 0 ] );
			JointSelectionSphere selected = null;
			for( JointSelectionSphere sphere : arr ) {
				EntityImp implementation = ImplementationAccessor.getImplementation( sphere );
				Point3 point = implementation.getAbsoluteTransformation().translation;
				sphereLocations.add( implementation.transformToAwt( point, camera ) );
			}
			for( int i = 0; i != arr.length; ++i ) {
				double dist = Point.distance( sphereLocations.get( i ).x, sphereLocations.get( i ).y, location.x, location.y );
				if( dist < minDist ) {
					selected = arr[ i ];
					minDist = dist;
				}
			}
			if( selected != null ) {
				scene.jointSelected( selected, e );
			}
		}

		public void mouseExited( MouseEvent e ) {
		}

		public void mouseEntered( MouseEvent e ) {
		}

		public void mouseClicked( MouseEvent e ) {
		}
	};
}
