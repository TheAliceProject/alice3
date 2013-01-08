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

import java.util.ArrayList;

import org.lgna.ik.poser.JointSelectionSphere;
import org.lgna.ik.poser.PoserControllerAdapter;
import org.lgna.story.AddMouseClickOnObjectListener;
import org.lgna.story.Color;
import org.lgna.story.ImplementationAccessor;
import org.lgna.story.SBiped;
import org.lgna.story.SCamera;
import org.lgna.story.SGround;
import org.lgna.story.SJoint;
import org.lgna.story.SScene;
import org.lgna.story.SSphere;
import org.lgna.story.SSun;
import org.lgna.story.SpatialRelation;
import org.lgna.story.event.MouseClickOnObjectEvent;
import org.lgna.story.event.MouseClickOnObjectListener;
import org.lgna.story.implementation.JointImp;
import org.lgna.story.resources.JointId;

import edu.cmu.cs.dennisc.java.lang.ArrayUtilities;
import edu.cmu.cs.dennisc.java.util.Collections;

/**
 * @author Matt May
 */
public class PoserScene extends SScene {
	private final SSun sun = new SSun();
	private final SGround snow = new SGround();
	private final SCamera camera;
	public final SBiped ogre;
	public SSphere anchor = new SSphere();
	public SSphere ee = new SSphere();
	private ArrayList<JointSelectionSphere> jssArr;// = Collections.newArrayList();
	private ArrayList<JointId> anchorPoints = Collections.newArrayList();
	private PoserControllerAdapter adapter;

	//	private State<JointSelectionSphere> selectedEndJointSphere = new State<PoserScene.JointSelectionSphere>();

	public PoserScene( SCamera camera, SBiped ogre ) {
		this.camera = camera;
		this.ogre = ogre;
		this.jssArr = Collections.newArrayList(
				createJSS( ogre.getRightAnkle() ),
				createJSS( ogre.getRightKnee() ),
				createJSS( ogre.getRightHip() ),
				createJSS( ogre.getRightWrist() ),
				createJSS( ogre.getRightElbow() ),
				createJSS( ogre.getRightClavicle() ),
				createJSS( ogre.getRightWrist() ),
				createJSS( ogre.getLeftAnkle() ),
				createJSS( ogre.getLeftKnee() ),
				createJSS( ogre.getLeftHip() ),
				createJSS( ogre.getLeftWrist() ),
				createJSS( ogre.getLeftElbow() ),
				createJSS( ogre.getLeftClavicle() ),
				createJSS( ogre.getLeftWrist() )
				);
		ArrayList<SJoint> sJointList = Collections.newArrayList( ogre.getRightClavicle(), ogre.getLeftClavicle(), ogre.getRightHip(), ogre.getLeftHip() );
		for( SJoint joint : sJointList ) {
			anchorPoints.add( ( (JointImp)ImplementationAccessor.getImplementation( joint ) ).getJointId() );
		}
	}

	private JointSelectionSphere createJSS( SJoint joint ) {
		return new JointSelectionSphere( (JointImp)ImplementationAccessor.getImplementation( joint ) );
	}

	private void performGeneratedSetup() {
		this.snow.setVehicle( this );
		this.sun.setVehicle( this );
		this.camera.setVehicle( this );
		this.ogre.setVehicle( this );
		anchor.setVehicle( this );
		ee.setVehicle( this );

		anchor.setRadius( .15 );
		anchor.setPaint( Color.GREEN );
		anchor.setOpacity( 0.5 );

		ee.setRadius( .1 );
		ee.setPaint( Color.BLUE );

		this.ogre.place( SpatialRelation.ABOVE, this.snow );
		this.snow.setPaint( SGround.SurfaceAppearance.SNOW );

		//		target.setPositionRelativeToVehicle(new Position(1, 0, 0));

		//camera vantage point taken care of by camera navigator
		//this.camera.moveAndOrientToAGoodVantagePointOf( this.ogre );
		performInitializeEvents();
	}

	private void performCustomSetup() {
		//if you want the skeleton visualization to be co-located
		//		this.ogre.setOpacity( 0.25 );

		//		org.lgna.story.implementation.JointedModelImp impl = ImplementationAccessor.getImplementation( this.ogre );
		//		impl.showVisualization();
	}

	private void performInitializeEvents() {
		this.addDefaultModelManipulation();
		this.addMouseClickOnObjectListener( new MouseClickOnObjectListener() {

			public void mouseClicked( MouseClickOnObjectEvent e ) {
				if( e.getModelAtMouseLocation() instanceof JointSelectionSphere ) {
					JointSelectionSphere jss = (JointSelectionSphere)e.getModelAtMouseLocation();
					jss.setVehicle( PoserScene.this );
					JointImp end = jss.getJoint();
					JointImp anchor2 = (JointImp)ImplementationAccessor.getImplementation( ogre.getJoint( getAnchorForEndJoint( end ) ) );
					IKMagicWand.moveChainToPointInSceneSpace( anchor2, end, ImplementationAccessor.getImplementation( jss ).getAbsoluteTransformation().translation );
					jss.setVehicle( end.getAbstraction() );
					jss.moveAndOrientTo( end.getAbstraction() );
				}
			}

		}, AddMouseClickOnObjectListener.setOfVisuals( ArrayUtilities.createArray( jssArr, JointSelectionSphere.class ) ) );
	}

	private JointId getAnchorForEndJoint( JointImp joint ) {
		if( isAParentOfB( adapter.getAnchorJointID(), joint.getJointId() ) ) {
			return adapter.getAnchorJointID();
		}
		return IKMagicWand.getDefaultAnchorForBipedEndJoint( joint.getJointId() );
	}

	private boolean isAParentOfB( JointId parent, JointId joint ) {
		if( parent.equals( joint ) ) {
			return true;
		}
		JointId rv = joint.getParent();
		if( rv != null ) {
			return isAParentOfB( parent, rv );
		}
		return false;
	}

	private JointId findParent( JointId jointId ) {
		if( anchorPoints.contains( jointId ) ) {
			return jointId.getParent();
		}
		return findParent( jointId.getParent() );
	}

	@Override
	protected void handleActiveChanged( Boolean isActive, Integer activeCount ) {
		if( isActive ) {
			if( activeCount == 1 ) {
				this.performGeneratedSetup();
				this.performCustomSetup();
			} else {
				this.restoreStateAndEventListeners();
			}
		} else {
			this.restoreStateAndEventListeners();
		}
	}

	public ArrayList<JointSelectionSphere> getJointSelectionSheres() {
		return this.jssArr;
	}

	public void setAdapter( PoserControllerAdapter adapter ) {
		this.adapter = adapter;
	}
}
