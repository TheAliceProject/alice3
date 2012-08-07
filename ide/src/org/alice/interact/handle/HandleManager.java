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
package org.alice.interact.handle;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.alice.interact.PickHint;
import org.alice.interact.PickUtilities;
import org.alice.interact.event.ManipulationEvent;
import org.alice.interact.event.ManipulationEventCriteria;
import org.alice.interact.event.ManipulationListener;

import edu.cmu.cs.dennisc.animation.Animator;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.scenegraph.AbstractTransformable;

/**
 * @author David Culyba
 */
public class HandleManager implements ManipulationListener{
	private Stack< HandleSet > handleSetStack = new Stack< HandleSet >();
	private List< ManipulationHandle > handles = new ArrayList<ManipulationHandle>();
	
	private Point3 cameraPosition;
	
	
	public void addHandle( ManipulationHandle handle )
	{
		handles.add( handle );
		handle.setHandleManager( this );
		handle.setCameraPosition(this.cameraPosition);
	}
	
	public HandleSet getCurrentHandleSet()
	{
		if (this.handleSetStack.empty())
		{
			return null;
		}
		else
		{
			return this.handleSetStack.peek();
		}
	}
	
	public void updateCameraPosition(Point3 position)
	{
		if (position == null)
		{
			this.cameraPosition = null;
		}
		else if (this.cameraPosition == null)
		{
			this.cameraPosition = new Point3(position);
		}
		else 
		{
			this.cameraPosition.set(position);
		}
		for (ManipulationHandle handle : this.handles)
		{
			handle.setCameraPosition(this.cameraPosition);
		}
		
	}
	
	private void updateHandlesBasedOnHandleSet()
	{
		for (ManipulationHandle handle : this.handles)
		{
			if (!handle.isAlwaysVisible())
			{
				if (handle.isMemberOf( this.getCurrentHandleSet() ))
				{
					handle.setHandleVisible( true );
				}
				else
				{
					handle.setHandleVisible( false );
				}
			}
		}
	}
	
	public void setHandleSet( HandleSet handleSet )
	{
		if (!this.handleSetStack.empty())
		{
			this.handleSetStack.pop();
		}
		this.handleSetStack.push( handleSet );
		this.updateHandlesBasedOnHandleSet();
	}
	
	public void pushNewHandleSet( HandleSet handleSet )
	{
		this.handleSetStack.push( handleSet );
//		System.out.println("Pushed "+handleSet+":"+handleSet.hashCode()+", and size is now "+this.handleSetStack.size());
		this.updateHandlesBasedOnHandleSet();
	}
	
	public HandleSet popHandleSet()
	{
		if (this.handleSetStack.empty())
		{
			System.err.println("TRYING TO POP AN EMPTY HANDLE STACK!!!");
			Thread.dumpStack();
			return null;
		}
		HandleSet popped = this.handleSetStack.pop();
//		System.out.println("popped "+popped+" and current handle set is "+this.handleSetStack.peek());
		this.updateHandlesBasedOnHandleSet();
		return popped;
	}
	
	public static boolean canHaveHandles(AbstractTransformable object) {
		PickHint objectPickHint = PickUtilities.getPickType(object);
		if ( (objectPickHint.intersects( PickHint.PickType.RESIZABLE.pickHint()) ||
				objectPickHint.intersects( PickHint.PickType.MOVEABLE.pickHint()) ||
				objectPickHint.intersects( PickHint.PickType.TURNABLE.pickHint()) ||
				objectPickHint.intersects( PickHint.PickType.SELECTABLE.pickHint()))  &&
				!(objectPickHint.intersects(PickHint.PickType.SUN.pickHint())))
		{
			return true;
		}
		return false;
	}
	
	private static boolean canHaveHandle(AbstractTransformable selectedObject, ManipulationHandle handle) 
	{
		if (handle instanceof ManipulationHandleIndirection) {
			handle = ((ManipulationHandleIndirection)handle).getCurrentHandle();
		}
		PickHint objectPickHint = PickUtilities.getPickType(selectedObject);
		if (handle instanceof LinearTranslateHandle) {
			return (objectPickHint.intersects(PickHint.PickType.MOVEABLE.pickHint()));
		}
		else if (handle instanceof RotationRingHandle) {
			boolean doJointsMatch = objectPickHint.intersects(PickHint.PickType.JOINT.pickHint()) == handle.isMemberOf( HandleSet.HandleGroup.JOINT);
			return doJointsMatch && objectPickHint.intersects(PickHint.PickType.TURNABLE.pickHint());
		}
		else if (handle instanceof LinearScaleHandle) {
			LinearScaleHandle scaleHandle = (LinearScaleHandle)handle;
			if (objectPickHint.intersects(PickHint.PickType.RESIZABLE.pickHint())) {
				org.lgna.story.implementation.EntityImp entityImp = PickUtilities.getEntityImpFromPickedObject(selectedObject);
				if (entityImp instanceof org.lgna.story.implementation.ModelImp) {
					org.lgna.story.implementation.ModelImp modelImp = (org.lgna.story.implementation.ModelImp)entityImp;
					org.lgna.story.implementation.ModelImp.Resizer[] resizers = modelImp.getResizers();
					for (org.lgna.story.implementation.ModelImp.Resizer r : resizers) {
						if (r == scaleHandle.getResizer()) {
							return true;
						}
					}
				}
				return false;
			}
			else {
				return false;
			}
		}
		return true;
	}
	
	public void setSelectedObject(AbstractTransformable selectedObject)
	{
//		PrintUtilities.println("Setting handle selected object to "+selectedObject);
		for (ManipulationHandle handle : this.handles)
		{
			if (canHaveHandle(selectedObject, handle)) {
				handle.setSelectedObject( selectedObject );
			}
			else {
				handle.setSelectedObject(null);
			}
				
		}
	}
	
	
	public static boolean isSelectable(AbstractTransformable object)
	{
		return canHaveHandles(object);
	}
	
	public AbstractTransformable getSelectedObject()
	{
		if (this.handles.size() == 0)
		{
			return null;
		}
		AbstractTransformable selected = this.handles.get(0).getManipulatedObject();
		for (ManipulationHandle handle : this.handles)
		{
			if (handle.getManipulatedObject() != selected && !(handle instanceof ManipulationHandle2D))
			{
				edu.cmu.cs.dennisc.print.PrintUtilities.println("Handle "+handle+" selected ("+handle.getManipulatedObject()+", does not equal "+selected);
			}
		}
		return selected;
		
	}
	
	public void setHandlesShowing(boolean showing)
	{
//		PrintUtilities.println("Setting handle showing to: "+showing);
		for (ManipulationHandle handle : this.handles)
		{
//			PrintUtilities.println("Setting handle "+handle+", which is visible? "+handle.isHandleVisible());
			handle.setVisualsShowing(showing);
		}
	}
	
	public void setAnimator(Animator animator)
	{
		for (ManipulationHandle handle : this.handles)
		{
			handle.setAnimator( animator );
		}
	}

	public void setHandlesVisible(boolean visible)
	{
		for (ManipulationHandle handle : this.handles)
		{
			handle.setHandleVisible(visible);
		}
	}
	
	public boolean isHandleVisible(ManipulationHandle handle)
	{
		if (handle.isAlwaysVisible())
		{
			return true;
		}
		else return handle.isMemberOf( this.getCurrentHandleSet() );
	}
	
	private List<ManipulationHandle> getSiblings(ManipulationHandle handle)
	{
		ArrayList<ManipulationHandle> siblings = new ArrayList< ManipulationHandle >();
		HandleSet toCheck;
		if (handle.isAlwaysVisible())
		{
			toCheck = handle.getHandleSet();
		}
		else
		{
			if (handle.isMemberOf( this.getCurrentHandleSet() ))
			{
				toCheck = this.getCurrentHandleSet();
			}
			else
			{
				toCheck = new HandleSet();
			}
		}
		for (ManipulationHandle otherHandle : this.handles)
		{
			if (otherHandle != handle)
			{
				if (handle.isAlwaysVisible())
				{
					//Handles that are always visible are siblings if their memberships are equal
					if (otherHandle.getHandleSet().equals( toCheck ))
					{
						siblings.add( otherHandle );
					}
				}
				else if (otherHandle.isMemberOf( toCheck ))
				{
					siblings.add( otherHandle );
				}
			}
		}
		return siblings;
	}
	
	public boolean isASiblingActive(ManipulationHandle handle)
	{
		List<ManipulationHandle> siblings = this.getSiblings( handle );
		for (ManipulationHandle sibling : siblings)
		{
			if (sibling.getHandleStateCopy().isActive())
			{
				return true;
			}
		}
		return false;
	}
	
	public void setHandleRollover(ManipulationHandle handle, boolean isRollover)
	{
		handle.setHandleRollover( isRollover );
	}
	
	public void activate( ManipulationEvent event ) {
		
	}

	public void deactivate( ManipulationEvent event ) {
		// TODO Auto-generated method stub
		
	}

	public void addCondition( ManipulationEventCriteria condition ) {
		// TODO Auto-generated method stub
		
	}

	public boolean matches( ManipulationEvent event ) {
		// TODO Auto-generated method stub
		return false;
	}

	public void removeCondition( ManipulationEventCriteria condition ) {
		// TODO Auto-generated method stub
		
	}

	
}
