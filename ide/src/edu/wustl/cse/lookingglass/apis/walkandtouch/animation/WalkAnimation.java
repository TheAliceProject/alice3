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

import edu.cmu.cs.dennisc.animation.AnimationObserver;
import edu.wustl.cse.lookingglass.apis.walkandtouch.Amount;
import edu.wustl.cse.lookingglass.apis.walkandtouch.Person;

/**
 * @author Caitlin Kelleher
 */
public class WalkAnimation extends AbstractWalkAnimation {
	protected double distance;
	public WalkAnimation( Person subject, double distance, double stepSpeed, boolean swingArms, Amount stepAmount, Amount bounceAmount, Amount armSwingAmount ) {
		super( subject, stepSpeed, swingArms, stepAmount, bounceAmount, armSwingAmount );
		this.distance = distance;
	}
	double currentPos = 0.0;
	double stepLength = -1.0;
	
	double numberOfSteps = -1.0;
	double timePerStep = -1.0;
	
	boolean done = false;
	
	protected double getActualStepLength() {
		if (stepLength == -1) {
			stepLength = this.getStepLength();
			if (stepLength == 0.0) stepLength = 1.0;
		}
		
		if (numberOfSteps == -1) {
			numberOfSteps = java.lang.Math.round(distance/stepLength);
		}
		
		return distance/numberOfSteps;
		
	}
	
	protected double getTotalTime() {
		getActualStepLength();
		return numberOfSteps/stepSpeed;
	}
	
	public double getTimeRemaining( double deltaSincePrologue ) {
		return (getTotalTime() - deltaSincePrologue);
	}
	
	@Override
	public void prologue() {
		super.prologue();
		currentPos = 0.0;
	}
	
	
	@Override
	protected double update( double deltaSincePrologue, double deltaSinceLastUpdate, AnimationObserver observer ) {
		double timeRemaining = this.getTimeRemaining(deltaSincePrologue);
		
		
		if (timeRemaining > 0) {
			
			done = false;		
			timePerStep = 1.0/stepSpeed;
			
			int stepNumber = (int)java.lang.Math.ceil( deltaSincePrologue  * (1.0/timePerStep)) - 1;
			if (stepNumber == -1) stepNumber = 0;
			if (stepNumber == numberOfSteps) {
				stepNumber -= 1;
			} 
			double portionOfStep = (deltaSincePrologue - (stepNumber * timePerStep)) / timePerStep;	
			
			if (portionOfStep > 1.0) portionOfStep = 1.0;
						
			boolean lastStep = false;
			if (stepNumber == numberOfSteps-1) {lastStep = true;}
				
			if ((stepNumber % 2) == 0) {
				this.stepRight(portionOfStep, lastStep);
			} else {
				this.stepLeft(portionOfStep, lastStep);
			}
			
			double portion = deltaSincePrologue/getTotalTime();
			double targetDistance = distance * portion;
					
			if (targetDistance - currentPos > 0) {
			
//				WalkAnimation.this.subject.getTransformableValue().moveRightNow(org.alice.apis.moveandturn.Direction.FORWARD, targetDistance - currentPos);
				subject.moveRightNow(org.alice.apis.moveandturn.MoveDirection.FORWARD, targetDistance - currentPos);
				currentPos += (targetDistance-currentPos);
				
				adjustHeight();
			}
		}
		return timeRemaining;
	}
		
}
