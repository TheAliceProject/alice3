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
package org.lgna.ik.poser;

import java.util.List;

import org.alice.interact.InputState;
import org.alice.interact.manipulator.ObjectUpDownDragManipulator;
import org.alice.interact.manipulator.OmniDirectionalDragManipulator;
import org.lgna.ik.poser.controllers.PoserEvent;
import org.lgna.ik.poser.jselection.JointSelectionSphere;
import org.lgna.story.SThing;
import org.lgna.story.implementation.EntityImp;

import edu.cmu.cs.dennisc.java.util.Lists;

/**
 * @author Matt May
 */
public class PoserSphereManipulator {
	private OmniDirectionalPoserSphereManipulator omni = new OmniDirectionalPoserSphereManipulator();
	private UpDownPoserSphereManipulator uppy = new UpDownPoserSphereManipulator();

	List<PoserSphereManipulatorListener> listeners = Lists.newArrayList();

	public void addListener( PoserSphereManipulatorListener listener ) {
		listeners.add( listener );
	}

	private void fireAllStart( JointSelectionSphere sphere ) {
		fireFinish( sphere );
	}

	private void fireFinish( JointSelectionSphere sphere ) {
		for( PoserSphereManipulatorListener listener : listeners ) {
			listener.fireStart( new PoserEvent( sphere ) );
		}
	}

	public OmniDirectionalPoserSphereManipulator getOmni() {
		return this.omni;
	}

	public UpDownPoserSphereManipulator getUppy() {
		return this.uppy;
	}

	public class OmniDirectionalPoserSphereManipulator extends OmniDirectionalDragManipulator {

		@Override
		public boolean doStartManipulator( InputState startInput ) {
			boolean rv = super.doStartManipulator( startInput );
			if( manipulatedTransformable != null ) {
				SThing abstractionFromSgElement = EntityImp.getAbstractionFromSgElement( manipulatedTransformable );
				if( abstractionFromSgElement instanceof JointSelectionSphere ) {
					JointSelectionSphere sphere = (JointSelectionSphere)abstractionFromSgElement;
					fireStart( sphere );
				}
			}
			return rv;
		}

		private void fireStart( JointSelectionSphere sphere ) {
			fireAllStart( sphere );
		}

	}

	public class UpDownPoserSphereManipulator extends ObjectUpDownDragManipulator {

		@Override
		public boolean doStartManipulator( InputState startInput ) {
			boolean rv = super.doStartManipulator( startInput );
			if( manipulatedTransformable != null ) {
				SThing abstractionFromSgElement = EntityImp.getAbstractionFromSgElement( manipulatedTransformable );
				if( abstractionFromSgElement instanceof JointSelectionSphere ) {
					JointSelectionSphere sphere = (JointSelectionSphere)abstractionFromSgElement;
					fireStart( sphere );
				}
			}
			return rv;
		}

		private void fireStart( JointSelectionSphere sphere ) {
			fireAllStart( sphere );
		}
	}
}
