/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
 *******************************************************************************/

package org.alice.interact;

import java.util.LinkedList;
import java.util.List;

import org.alice.interact.HandleSupportingDragAdapter.ObjectType;
import org.alice.interact.condition.ManipulatorConditionSet;
import org.alice.interact.handle.HandleSet;

public final class InteractionGroup {

	public static class PossibleObjects {

		public PossibleObjects( ObjectType... possibleObjects ) {
			this.possibleObjects = possibleObjects;
		}

		public boolean containsType( ObjectType type ) {
			for( ObjectType t : this.possibleObjects ) {
				if( ( t == type ) || ( t == ObjectType.ANY ) ) {
					return true;
				}
			}
			return false;
		}

		private final ObjectType[] possibleObjects;
	}

	public static class InteractionInfo {
		public InteractionInfo( PossibleObjects possibleObjects, HandleSet handleSet, ManipulatorConditionSet manipulator, PickHint.PickType... acceptableTypes ) {
			this.possibleObjects = possibleObjects;
			this.handleSet = handleSet;
			this.manipulator = manipulator;
			this.pickHint = new PickHint( acceptableTypes );
		}

		public HandleSet getHandleSet() {
			return this.handleSet;
		}

		public boolean canUseIteractionGroup( PickHint pickType ) {
			return this.pickHint.intersects( pickType );
		}

		@Override
		public String toString() {
			return HandleSet.getStringForSet( this.handleSet );
		}

		private final HandleSet handleSet;
		private final ManipulatorConditionSet manipulator;
		private final PickHint pickHint;
		private final PossibleObjects possibleObjects;
	}

	public InteractionGroup() {
	}

	public InteractionGroup( InteractionInfo... interactionInfos ) {
		for( InteractionInfo info : interactionInfos ) {
			groups.add( info );
		}
	}

	public void addInteractionInfo( InteractionInfo info ) {
		groups.add( info );
	}

	public void addInteractionInfo( PossibleObjects possibleObjects, HandleSet handleSet, ManipulatorConditionSet manipulator, PickHint.PickType... acceptableTypes ) {
		groups.add( new InteractionInfo( possibleObjects, handleSet, manipulator, acceptableTypes ) );
	}

	public InteractionInfo getMatchingInfo( ObjectType objectType ) {
		for( InteractionInfo e : groups ) {
			if( e.possibleObjects.containsType( objectType ) ) {
				return e;
			}
		}
		return null;

	}

	public void enabledManipulators( boolean enabled ) {
		for( InteractionInfo e : groups ) {
			e.manipulator.setEnabled( enabled );
		}
	}

	private List<InteractionInfo> groups = new LinkedList<InteractionInfo>();
}
