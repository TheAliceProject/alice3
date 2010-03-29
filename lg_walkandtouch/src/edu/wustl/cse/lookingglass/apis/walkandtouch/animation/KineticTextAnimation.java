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

import edu.cmu.cs.dennisc.print.PrintUtilities;


import edu.wustl.cse.lookingglass.apis.walkandtouch.scenegraph.graphics.KineticText;

public class KineticTextAnimation extends org.alice.apis.moveandturn.graphic.animation.OpenUpdateCloseOverlayGraphicAnimation{
	
	KineticText kineticText = null;
	int type;
	
	public KineticTextAnimation(org.alice.apis.moveandturn.Composite composite, double openingDuration, double updatingDuration, double closingDuration, KineticText kineticText) {		
		super(composite, openingDuration, updatingDuration, closingDuration);
		this.kineticText = kineticText;
		this.type = kineticText.type;
	}
	
	@Override
	protected edu.cmu.cs.dennisc.scenegraph.Graphic getSGGraphic() {
		return kineticText;
	}
	
	@Override
	protected void updateStateAndPortion( org.alice.apis.moveandturn.graphic.animation.OpenUpdateCloseOverlayGraphicAnimation.State state, double portion) {
		//todo: reword update method here
	}
	@Override
	protected double update( double deltaSincePrologue, double deltaSinceLastUpdate, edu.cmu.cs.dennisc.animation.AnimationObserver animationObserver ) {
		double closingDuration = this.getClosingDuration();
		double d = this.getUpdatingDuration();
		double p = deltaSincePrologue/d;
		double p2 = (deltaSincePrologue)/(d+closingDuration);
		//kineticText.Alpha = (float)((d-deltaSincePrologue)/d);
		
	
		kineticText.ax = (int)(30*(p));
		kineticText.ay = (int)(30*(p));

		kineticText.size = (int)(kineticText.startSize+(kineticText.endSize-kineticText.startSize)*p);
		double inc = (kineticText.endD - kineticText.startD)/d;
		if(deltaSincePrologue<d){
			kineticText.degree = (int)(kineticText.startD+inc*deltaSincePrologue);
				//kineticText.moveToward(p);
		}
			else {
				//kineticText.moveToward(p2);
				//kineticText.moveToward(closingDuration, deltaSincePrologue-d);
				System.out.println(kineticText.dir);
				if (kineticText.dir==1){	
					kineticText.moveLeft(closingDuration, deltaSincePrologue-d);
				}
				else if (kineticText.dir==2){	
					kineticText.moveRight(closingDuration, deltaSincePrologue-d);
				}
				else if (kineticText.dir==3){	
					kineticText.moveUp(closingDuration, deltaSincePrologue-d);
				}
				else if (kineticText.dir==4){	
					kineticText.moveDown(closingDuration, deltaSincePrologue-d);
				}
				else if (kineticText.dir==23){	
					kineticText.moveRight(closingDuration, deltaSincePrologue-d);
					kineticText.moveUp(closingDuration, deltaSincePrologue-d);

				}
				else if (kineticText.dir==13){	
					kineticText.moveLeft(closingDuration, deltaSincePrologue-d);
					kineticText.moveUp(closingDuration, deltaSincePrologue-d);

				}
				else if (kineticText.dir==24){	
					kineticText.moveRight(closingDuration, deltaSincePrologue-d);
					kineticText.moveDown(closingDuration, deltaSincePrologue-d);

				}
				else if (kineticText.dir==14){	
					kineticText.moveLeft(closingDuration, deltaSincePrologue-d);
					kineticText.moveDown(closingDuration, deltaSincePrologue-d);

				}
				
				
			}
		return super.update(deltaSincePrologue, deltaSinceLastUpdate, animationObserver);
	}

}
