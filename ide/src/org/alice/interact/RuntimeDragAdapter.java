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

import java.awt.event.MouseEvent;

import org.alice.interact.ModifierMask.ModifierKey;
import org.alice.interact.condition.ManipulatorConditionSet;
import org.alice.interact.condition.MouseDragCondition;
import org.alice.interact.condition.PickCondition;
import org.alice.interact.manipulator.HandlelessObjectRotateDragManipulator;
import org.alice.interact.manipulator.ObjectTranslateDragManipulator;
import org.alice.interact.manipulator.ObjectUpDownDragManipulator;

/**
 * @author David Culyba
 */
public class RuntimeDragAdapter extends AbstractDragAdapter {

	
	@Override
	protected void setUpControls()
	{
		ManipulatorConditionSet mouseTranslateObject = new ManipulatorConditionSet( new ObjectTranslateDragManipulator() );
		MouseDragCondition moveableObject = new MouseDragCondition( java.awt.event.MouseEvent.BUTTON1, new PickCondition( PickHint.MOVEABLE_OBJECTS), new ModifierMask( ModifierMask.NO_MODIFIERS_DOWN ));
		mouseTranslateObject.addCondition( moveableObject );
		this.manipulators.add( mouseTranslateObject );
		
		ManipulatorConditionSet mouseUpDownTranslateObject = new ManipulatorConditionSet( new ObjectUpDownDragManipulator() );
		MouseDragCondition moveableObjectWithShift = new MouseDragCondition( java.awt.event.MouseEvent.BUTTON1, new PickCondition( PickHint.MOVEABLE_OBJECTS), new ModifierMask( ModifierKey.SHIFT ));
		mouseUpDownTranslateObject.addCondition( moveableObjectWithShift );
		this.manipulators.add( mouseUpDownTranslateObject );
		
		ManipulatorConditionSet mouseRotateObjectLeftRight = new ManipulatorConditionSet( new HandlelessObjectRotateDragManipulator(MovementDirection.UP) );
		MouseDragCondition moveableObjectWithCtrl = new MouseDragCondition( java.awt.event.MouseEvent.BUTTON1, new PickCondition( PickHint.MOVEABLE_OBJECTS), new ModifierMask( ModifierKey.CONTROL ));
		mouseRotateObjectLeftRight.addCondition( moveableObjectWithCtrl );
		this.manipulators.add( mouseRotateObjectLeftRight );
		
		for (int i=0; i<this.manipulators.size(); i++)
		{
			this.manipulators.get( i ).getManipulator().setDragAdapter( this );
		}
	}
	
	
	@Override
	public void mouseMoved( java.awt.event.MouseEvent e ) {
		//Overridden to prevent picking every frame since there is no need for rollover events
		this.currentInputState.setMouseLocation( e.getPoint() );
		this.handleStateChange();
	}
	
	@Override
	public void mouseEntered( MouseEvent e ) {
		//Overridden to do nothing
	}
}
