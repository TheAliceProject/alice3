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
package org.lgna.ik.poser.anchors;

/**
 * @author Dennis Cosgrove
 */
public class Anchors {
	private final java.util.List<org.lgna.ik.poser.anchors.events.AnchorListener> anchorListeners = edu.cmu.cs.dennisc.java.util.Lists.newCopyOnWriteArrayList();
	private org.lgna.story.resources.JointId leftArm;
	private org.lgna.story.resources.JointId rightArm;
	private org.lgna.story.resources.JointId leftLeg;
	private org.lgna.story.resources.JointId rightLeg;

	public void addAnchorListener( org.lgna.ik.poser.anchors.events.AnchorListener anchorListener ) {
		this.anchorListeners.add( anchorListener );
	}

	public void removeAnchorListener( org.lgna.ik.poser.anchors.events.AnchorListener anchorListener ) {
		this.anchorListeners.remove( anchorListener );
	}

	public org.lgna.story.resources.JointId getLeftArm() {
		return this.leftArm;
	}

	public org.lgna.story.resources.JointId getRightArm() {
		return this.rightArm;
	}

	public org.lgna.story.resources.JointId getLeftLeg() {
		return this.leftLeg;
	}

	public org.lgna.story.resources.JointId getRightLeg() {
		return this.rightLeg;
	}

	public void setLeftArm( org.lgna.story.resources.JointId leftArm ) {
		if( this.leftArm != leftArm ) {
			org.lgna.ik.poser.anchors.events.AnchorEvent e = new org.lgna.ik.poser.anchors.events.AnchorEvent( this.leftArm, leftArm );
			this.leftArm = leftArm;
			for( org.lgna.ik.poser.anchors.events.AnchorListener anchorListener : this.anchorListeners ) {
				anchorListener.leftArmChanged( e );
			}
		}
	}

	public void setRightArm( org.lgna.story.resources.JointId rightArm ) {
		if( this.rightArm != rightArm ) {
			org.lgna.ik.poser.anchors.events.AnchorEvent e = new org.lgna.ik.poser.anchors.events.AnchorEvent( this.rightArm, rightArm );
			this.rightArm = rightArm;
			for( org.lgna.ik.poser.anchors.events.AnchorListener anchorListener : this.anchorListeners ) {
				anchorListener.rightArmChanged( e );
			}
		}
	}

	public void setLeftLeg( org.lgna.story.resources.JointId leftLeg ) {
		if( this.leftLeg != leftLeg ) {
			org.lgna.ik.poser.anchors.events.AnchorEvent e = new org.lgna.ik.poser.anchors.events.AnchorEvent( this.leftLeg, leftLeg );
			this.leftLeg = leftLeg;
			for( org.lgna.ik.poser.anchors.events.AnchorListener anchorListener : this.anchorListeners ) {
				anchorListener.leftLegChanged( e );
			}
		}
	}

	public void setRightLeg( org.lgna.story.resources.JointId rightLeg ) {
		if( this.rightLeg != rightLeg ) {
			org.lgna.ik.poser.anchors.events.AnchorEvent e = new org.lgna.ik.poser.anchors.events.AnchorEvent( this.rightLeg, rightLeg );
			this.rightLeg = rightLeg;
			for( org.lgna.ik.poser.anchors.events.AnchorListener anchorListener : this.anchorListeners ) {
				anchorListener.rightLegChanged( e );
			}
		}
	}
}
