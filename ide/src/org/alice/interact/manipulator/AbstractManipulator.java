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

package org.alice.interact.manipulator;

import java.util.List;
import java.util.UUID;
import java.util.Vector;

import org.alice.interact.AbstractDragAdapter;
import org.alice.interact.InputState;
import org.alice.interact.event.ManipulationEvent;
import org.alice.interact.handle.HandleSet;
import org.alice.interact.operations.PredeterminedSetLocalTransformationActionOperation;

import edu.cmu.cs.dennisc.alice.Project;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.print.PrintUtilities;
import edu.cmu.cs.dennisc.zoot.ZManager;


//adding comment for testing tags

/**
 * @author David Culyba
 */
public abstract class AbstractManipulator {
	
	protected edu.cmu.cs.dennisc.scenegraph.Transformable manipulatedTransformable = null;
	protected AffineMatrix4x4 originalTransformation = null;
	private boolean hasStarted = false;
	protected AbstractDragAdapter dragAdapter;
	protected boolean hasDoneUpdate = false;

	protected List< ManipulationEvent > manipulationEvents = new Vector< ManipulationEvent >();
	
	public void setManipulatedTransformable( edu.cmu.cs.dennisc.scenegraph.Transformable manipulatedTransformable)
	{
		this.manipulatedTransformable = manipulatedTransformable;
	}
	
	public edu.cmu.cs.dennisc.scenegraph.Transformable getManipulatedTransformable()
	{
		return this.manipulatedTransformable;
	}
	
	public boolean hasStarted()
	{
		return this.hasStarted;
	}
	
	public boolean hasUpdated()
	{
		return this.hasDoneUpdate;
	}
	
	protected void setHasUpdated(boolean hasUpdated)
	{
		this.hasDoneUpdate = hasUpdated;
	}
	
	public void setDragAdapter( AbstractDragAdapter dragAdapter )
	{
		this.dragAdapter = dragAdapter;
	}
	
	protected abstract HandleSet getHandleSetToEnable();
	
	protected void initializeEventMessages()
	{
		this.manipulationEvents.clear();
	}
	
	public List< ManipulationEvent > getManipulationEvents()
	{
		this.initializeEventMessages();
		return this.manipulationEvents;
	}
	
	public void triggerAllDeactivateEvents()
	{
		for (int i=0; i<this.manipulationEvents.size(); i++)
		{
			this.dragAdapter.triggerManipulationEvent( this.manipulationEvents.get( i ), false );
		}
	}
	
	public boolean startManipulator( InputState startInput )
	{
		this.hasStarted = doStartManipulator( startInput );
		setHasUpdated(false);
		if (this.hasStarted)
		{
			undoRedoBeginManipulation();
			HandleSet setToShow = this.getHandleSetToEnable();
			if (setToShow != null && this.dragAdapter != null)
			{
//				System.out.println("Push on start:");
				this.dragAdapter.pushHandleSet( setToShow );
			}
		}
		return this.hasStarted;
	}
	
	public void dataUpdateManipulator(InputState currentInput, InputState previousInput  )
	{
		if (this.hasStarted)
		{
			doDataUpdateManipulator( currentInput, previousInput );
			setHasUpdated(true);
		}
	}
	
	public void timeUpdateManipulator( double dTime, InputState currentInput )
	{
		if (this.hasStarted)
		{
			doTimeUpdateManipulator( dTime, currentInput );
			setHasUpdated(true);
		}
	}
	
	public void clickManipulator( InputState clickInput, InputState previousInput )
	{
		if ( startManipulator( clickInput ) )
		{
			doClickManipulator(clickInput, previousInput);
			doEndManipulator( clickInput, previousInput );
			if (isUndoable())
			{
				undoRedoEndManipulation();
			}
			if (this.hasStarted)
			{
				this.hasStarted = false;
				HandleSet setToShow = this.getHandleSetToEnable();
				if (setToShow != null && this.dragAdapter != null)
				{
					this.dragAdapter.popHandleSet();
				}
			}
			triggerAllDeactivateEvents();
		}
	}
	
	public void endManipulator(InputState endInput, InputState previousInput  )
	{
		doEndManipulator( endInput, previousInput );
		if (isUndoable())
		{
			undoRedoEndManipulation();
		}
		else if (this.getManipulatedTransformable() != null)
		{
			AffineMatrix4x4 newTransformation = this.getManipulatedTransformable().getLocalTransformation();
			if (!newTransformation.equals( originalTransformation ) && originalTransformation != null)
			{
				PrintUtilities.println("Skipping undoable action for a manipulation that actually changed the transformation.");
			}
		}
		if (this.hasStarted)
		{
			this.hasStarted = false;
			HandleSet setToShow = this.getHandleSetToEnable();
			if (setToShow != null && this.dragAdapter != null)
			{
//				System.out.println("Pop on manip end:");
				this.dragAdapter.popHandleSet();
			}
		}
		SnapUtilities.hideMovementSnapVisualization();
		triggerAllDeactivateEvents();
	}
	
	public abstract String getUndoRedoDescription();
	
	@Override
	public String toString()
	{
		return this.getClass().toString() + ":"+this.hashCode();
	}
	
	public void undoRedoBeginManipulation()
	{
		if (this.getManipulatedTransformable() != null)
		{
			this.originalTransformation = this.getManipulatedTransformable().getLocalTransformation();
		}
	}
	
	public void undoRedoEndManipulation()
	{
		if (this.getManipulatedTransformable() != null)
		{
			AffineMatrix4x4 newTransformation = this.getManipulatedTransformable().getLocalTransformation();
			
			if (newTransformation.equals( originalTransformation ))
			{
				PrintUtilities.println("Adding an undoable action for a manipulation that didn't actually change the transformation.");
			}
			edu.cmu.cs.dennisc.animation.Animator animator;
			if( this.dragAdapter != null ) {
				animator = this.dragAdapter.getAnimator();
			} else {
				animator = null;
			}
			if (originalTransformation == null || newTransformation == null)
			{
				System.out.println("boom");
			}
			PredeterminedSetLocalTransformationActionOperation undoOperation = new PredeterminedSetLocalTransformationActionOperation(Project.GROUP, false, animator, this.getManipulatedTransformable(), originalTransformation, newTransformation, getUndoRedoDescription());
			undoOperation.fire();
		}
	}
	
	public boolean isUndoable()
	{
		return this.hasUpdated();
	}
	
	public abstract boolean doStartManipulator( InputState startInput );
	
	public abstract void doDataUpdateManipulator( InputState currentInput, InputState previousInput );
	
	public abstract void doTimeUpdateManipulator( double dTime, InputState currentInput );
	
	public abstract void doEndManipulator( InputState endInput, InputState previousInput  );
	
	public abstract void doClickManipulator( InputState endInput, InputState previousInput );

}
