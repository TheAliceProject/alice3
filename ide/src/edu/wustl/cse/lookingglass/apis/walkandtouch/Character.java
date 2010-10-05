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
package edu.wustl.cse.lookingglass.apis.walkandtouch;

import edu.cmu.cs.dennisc.alice.annotations.*;


public class Character extends PolygonalModel {
	
	
//	public void showTitle( String text, Double duration, edu.cmu.cs.dennisc.font.Font font, edu.cmu.cs.dennisc.color.ColorF4 textColor, edu.cmu.cs.dennisc.color.ColorF4 bubbleFillColor, edu.cmu.cs.dennisc.color.ColorF4 bubbleOutlineColor ) {
//		displayBubble( new TitleBubble( null, text, font.getAWTFont(), textColor.getAWTColor(), bubbleFillColor.getAWTColor(), bubbleOutlineColor.getAWTColor() ), duration );
//	}

	
	//public BetterStandUpAnimation(edu.wustl.cse.ckelleher.apis.walkandtouch.PolygonalModel subject, boolean scootForward, double duration) {
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void standUp(boolean scootForward, Number duration) {
		perform(new edu.wustl.cse.lookingglass.apis.walkandtouch.animation.BetterStandUpAnimation(this, scootForward, duration.doubleValue()));
	}
	
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void standUp(boolean scootForward) {
		standUp(scootForward, 1.0);
	}
	
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void standUp() {
		standUp(true);
	}
	
//	public LookDirectionAnimation(edu.wustl.cse.ckelleher.apis.walkandtouch.PolygonalModel subject, org.alice.apis.moveandturn.MoveDirection lookDirection, double amount, double duration) {
	
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void look(LookDirection lookDirection, Number amount, Number duration) {
		perform(new edu.wustl.cse.lookingglass.apis.walkandtouch.animation.LookDirectionAnimation(this, lookDirection, amount.doubleValue(), duration.doubleValue()));
	}
	
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void look(LookDirection lookDirection, Number amount) {
		look(lookDirection, amount, 1.0);
	}
	
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void look(LookDirection lookDirection) {
		look(lookDirection, 0.15);
	}
	
//	public LookAtAnimation(edu.wustl.cse.ckelleher.apis.walkandtouch.PolygonalModel subject, edu.wustl.cse.ckelleher.apis.walkandtouch.PolygonalModel target, boolean onlyAffectYaw, double duration) {
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void lookAt(org.alice.apis.moveandturn.Transformable target, Boolean onlyAffectYaw, Number duration) {
		perform(new edu.wustl.cse.lookingglass.apis.walkandtouch.animation.LookAtAnimation(this, target, onlyAffectYaw, duration.doubleValue()));
	}
	
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void lookAt(org.alice.apis.moveandturn.Transformable target, Boolean onlyAffectYaw) {
		lookAt(target, onlyAffectYaw, 1.0);
	}
	
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void lookAt(org.alice.apis.moveandturn.Transformable target) {
		lookAt(target, false);
	}

}
