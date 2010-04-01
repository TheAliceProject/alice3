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
package org.alice.interact;

import sun.security.x509.DistributionPoint;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.print.PrintUtilities;

/**
 * @author David Culyba
 */
public abstract class AffineMatrix4x4TargetBasedAnimation extends TargetBasedFrameObserver< AffineMatrix4x4 > {

	private edu.cmu.cs.dennisc.math.UnitQuaternion m_qBuffer;
	private edu.cmu.cs.dennisc.math.AffineMatrix4x4 m_mBuffer;
	
	private static final double CUSTOM_SPEED = 5.0d;
	
	private boolean shouldAnimate = false;
	
	public AffineMatrix4x4TargetBasedAnimation( AffineMatrix4x4 currentValue )
	{
		this(currentValue, currentValue, CUSTOM_SPEED);
	}
	
	public AffineMatrix4x4TargetBasedAnimation( AffineMatrix4x4 currentValue, double speed )
	{
		this(currentValue, currentValue, speed);
	}
	
	public AffineMatrix4x4TargetBasedAnimation( AffineMatrix4x4 currentValue, AffineMatrix4x4 targetValue )
	{
		this(currentValue, targetValue, CUSTOM_SPEED);
	}
	
	public AffineMatrix4x4TargetBasedAnimation( AffineMatrix4x4 currentValue, AffineMatrix4x4 targetValue, double speed )
	{
		super(currentValue, targetValue, speed);
		m_qBuffer = edu.cmu.cs.dennisc.math.UnitQuaternion.createNaN();
		m_mBuffer = edu.cmu.cs.dennisc.math.AffineMatrix4x4.createNaN();
		if (getDistanceToDone() > MIN_DISTANCE_TO_DONE)
		{
			this.shouldAnimate = true;
		}
		else
		{
			this.shouldAnimate = false;
		}
	}

	@Override
	protected double getDistanceToDone() {
		edu.cmu.cs.dennisc.math.UnitQuaternion currentQ = this.currentValue.orientation.createUnitQuaternion();
		edu.cmu.cs.dennisc.math.UnitQuaternion targetQ = this.targetValue.orientation.createUnitQuaternion();
		
		double xDif = currentQ.x - targetQ.x;
		double yDif = currentQ.y - targetQ.y;
		double zDif = currentQ.z - targetQ.z;
		double wDif = currentQ.w - targetQ.w;
		
		double angleDist = Math.sqrt( xDif*xDif + yDif*yDif + zDif*zDif + wDif*wDif );
		double translationDist = Point3.calculateDistanceBetween( this.currentValue.translation, this.targetValue.translation );
		
		return angleDist + translationDist;
	}

//	@Override
//	public void update( double current ) {
//		if (this.shouldAnimate)
//		{
//			super.update( current );
//		}
//		if ()
//	}
	
	@Override
	protected AffineMatrix4x4 interpolate( AffineMatrix4x4 v0, AffineMatrix4x4 v1, double deltaSinceLastUpdate ) 
	{
		float portion = (float)(deltaSinceLastUpdate*this.speed);
		portion = Math.min( portion, 1.0f );
		portion = Math.max( -1.0f, portion );
		m_mBuffer.translation.setToInterpolation( v0.translation, v1.translation, portion );
		m_qBuffer.setToInterpolation( v0.orientation.createUnitQuaternion(), v1.orientation.createUnitQuaternion(), portion );
		AffineMatrix4x4 toReturn = new AffineMatrix4x4(m_mBuffer);
		toReturn.orientation.setValue( m_qBuffer );
		toReturn.orientation.normalizeColumns();
		return toReturn;
	}

	@Override
	public boolean isDone() {
		return ( this.getDistanceToDone() < .000001);
	}

	@Override
	protected AffineMatrix4x4 newE( AffineMatrix4x4 other ) {
		return new AffineMatrix4x4(other);
	}
}