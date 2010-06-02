/*
 * Copyright (c) 2008-2010, Washington University in St. Louis. All rights reserved.
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
 * 3. Products derived from the software may not be called "Looking Glass", nor 
 *    may "Looking Glass" appear in their name, without prior written permission
 *    of Washington University in St. Louis.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software 
 *    developed by Washington University in St. Louis"
 *
 * 5. The gallery of art assets and animations provided with this software is 
 *    contributed by Electronic Arts Inc. and may be used for personal, 
 *    non-commercial, and academic use only. Redistributions of any program 
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in 
 *    The Alice 3.0 Art Gallery License.
 *
 * DISCLAIMER:
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.  ANY AND ALL 
 * EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A PARTICULAR PURPOSE, 
 * TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHORS, 
 * COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, 
 * INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO 
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */
package edu.wustl.cse.lookingglass.apis.walkandtouch.animation;

import org.alice.apis.moveandturn.AsSeenBy;
import org.alice.apis.moveandturn.HowMuch;

import edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3;
import edu.cmu.cs.dennisc.math.UnitQuaternion;
import edu.cmu.cs.dennisc.math.Vector3;


/**
 * @author caitlink
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class StraightenAnimation extends edu.cmu.cs.dennisc.animation.DurationBasedAnimation {
	edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel subject = null;
	java.util.Vector<edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3> bodyPartInitialOrientations = null;
	java.util.Vector<edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel> bodyParts = null;
	edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 normalOrientation;	
	
	OrthogonalMatrix3x3 subjectInitialOrientation;
	OrthogonalMatrix3x3 subjectFinalOrientation;
	
	public StraightenAnimation(edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel subject, double duration) {
		super(duration);
		this.subject = subject;
	}
	
	protected void adjustHeight() {
		double distanceAboveGround = 0.0;
		
		if (subject != null) {
			distanceAboveGround = subject.getAxisAlignedMinimumBoundingBox(org.alice.apis.moveandturn.AsSeenBy.SCENE).getCenterOfBottomFace().y;
			subject.moveRightNow(org.alice.apis.moveandturn.MoveDirection.DOWN, distanceAboveGround, org.alice.apis.moveandturn.AsSeenBy.SCENE );
		}
	}
	
	@Override
	public void prologue(  ) {
		bodyPartInitialOrientations = new java.util.Vector<edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3>();
		bodyParts = new java.util.Vector<edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel>();
			
		normalOrientation = OrthogonalMatrix3x3.createIdentity();
		
		if (subject != null) {
//			addBodyPart(subject); // we want to straighten top level object too.
			findChildren(subject);				
			
			// top level object wants to be upright but facing the same way it was facing before
			subjectInitialOrientation = subject.getOrientation(AsSeenBy.SCENE);
			subjectFinalOrientation = OrthogonalMatrix3x3.createFromStandUp(subjectInitialOrientation);
		}		
	}
	
	@Override
	protected void setPortion( double portion ) {
		for (int i = 0; i < bodyPartInitialOrientations.size(); i++) {
			setOrientation(bodyParts.elementAt(i), bodyPartInitialOrientations.elementAt(i), normalOrientation, portion);
		}	
		
//		UnitQuaternion interpQuat = UnitQuaternion.createInterpolation(subjectInitialOrientation.createUnitQuaternion(), subjectFinalOrientation.createUnitQuaternion(), portion);
//		if (subject != null) {
//			subject.setOrientationRightNow( interpQuat.createOrthogonalMatrix3x3(), org.alice.apis.moveandturn.AsSeenBy.SCENE );
//		}
	}
	
	@Override
	protected void epilogue() {}
	
	private void findChildren(edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel model) {
		
		java.util.List<edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel> list = model.findAllMatches( HowMuch.DESCENDANT_PARTS_ONLY, edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel.class);
		
		for (edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel listItem: list) {
			addBodyPart(listItem);
		}
	}
	
	private void addBodyPart(edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel partToAdd) {
		bodyPartInitialOrientations.addElement(partToAdd.getOrientationAsAxes(org.alice.apis.moveandturn.AsSeenBy.PARENT));
		bodyParts.addElement(partToAdd);
	}
	
	protected void setOrientation(edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel part, edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 initialOrient, edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 finalOrient, double portion){
		//System.out.println(portion);
		
		UnitQuaternion interpQuat = UnitQuaternion.createInterpolation(initialOrient.createUnitQuaternion(), finalOrient.createUnitQuaternion(), portion);
		if (part != null) {

//			PrintUtilities.println("part ", part, " ", interpQuat.createOrthogonalMatrix3x3() );
			part.setOrientationRightNow( interpQuat.createOrthogonalMatrix3x3(), org.alice.apis.moveandturn.AsSeenBy.PARENT );
		} 
	}

}
