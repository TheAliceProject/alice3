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

package org.lgna.cheshire.simple.stencil.stepnotes;

/**
 * @author Dennis Cosgrove
 */
public class DropNoteUtilities {
	private DropNoteUtilities() {
		throw new AssertionError();
	}
	public static org.lgna.croquet.triggers.DropTrigger getDropTrigger( StepNote<?> note ) {
		org.lgna.croquet.history.Step<?> step = note.getStep();
		return (org.lgna.croquet.triggers.DropTrigger)step.getTrigger();
	}
	public static org.lgna.croquet.DropSite getDropSite( StepNote<?> note ) {
		org.lgna.croquet.triggers.DropTrigger dropTrigger = getDropTrigger( note );
		return dropTrigger.getDropSite();
	}
	private static boolean EPIC_HACK_isCloseEnough( org.lgna.croquet.Model desiredModel, org.lgna.croquet.Model candidateModel ) {
		assert desiredModel != null;
		if( candidateModel != null ) {
			return desiredModel.getClass() == candidateModel.getClass();
		} else {
			return false;
		}
	}
	private static boolean isWhatWeveBeenWaitingFor( org.lgna.croquet.history.event.Event< ? > event, org.lgna.croquet.Model desiredModel, org.lgna.croquet.DropSite desiredDropSite ) {
		if( event instanceof org.lgna.croquet.history.event.AddStepEvent ) {
			org.lgna.croquet.history.event.AddStepEvent addStepEvent = (org.lgna.croquet.history.event.AddStepEvent)event;
			org.lgna.croquet.history.Step< ? > candidateStep = addStepEvent.getStep();
			org.lgna.croquet.Model candidateModel = candidateStep.getModel();
			if( desiredModel == candidateModel || EPIC_HACK_isCloseEnough( desiredModel, candidateModel ) ) {
				org.lgna.croquet.triggers.Trigger trigger = candidateStep.getTrigger();
				if( trigger instanceof org.lgna.croquet.triggers.DropTrigger ) {
					org.lgna.croquet.triggers.DropTrigger dropTrigger = (org.lgna.croquet.triggers.DropTrigger)trigger;
					return dropTrigger.getDropSite().equals( desiredDropSite );
				} else if( trigger instanceof org.lgna.croquet.triggers.CascadeAutomaticDeterminationTrigger ) {
					edu.cmu.cs.dennisc.java.util.logging.Logger.errln( "todo: handle automatic drop" );
					return true;
				}
			}
//		} else if( event instanceof org.lgna.croquet.history.event.EditCommittedEvent ) {
//			org.lgna.croquet.history.event.EditCommittedEvent editCommittedEvent = (org.lgna.croquet.history.event.EditCommittedEvent)event;
//			org.lgna.croquet.edits.Edit edit = editCommittedEvent.getEdit();
//			return edit.getCompletionStep().getTrigger() instanceof org.lgna.croquet.triggers.DropTrigger;
		}
		return false;
	}
	public static boolean isWhatWeveBeenWaitingFor( org.lgna.croquet.history.event.Event< ? > event, StepNote<?> note ) {
		org.lgna.croquet.history.Step<?> step = note.getStep();
		org.lgna.croquet.Model desiredModel = step.getModel();
		org.lgna.croquet.DropSite desiredDropSite = getDropSite( note );
		return isWhatWeveBeenWaitingFor( event, desiredModel, desiredDropSite );
	}
	public static org.lgna.cheshire.simple.Feature createHole( org.lgna.croquet.history.Step< ? > dropStep ) {
		org.lgna.cheshire.simple.stencil.features.Hole rv = new org.lgna.cheshire.simple.stencil.features.Hole( new org.lgna.cheshire.simple.stencil.resolvers.DropSiteResolver( dropStep ), org.lgna.cheshire.simple.Feature.ConnectionPreference.NORTH_SOUTH );
		rv.setHeightConstraint( 64 );
		return rv;
	}
	public static org.lgna.cheshire.simple.Feature createPreviewHole( org.lgna.croquet.history.Step< ? > dropStep ) {
		return new org.lgna.cheshire.simple.stencil.features.DropPreviewHole( new org.lgna.cheshire.simple.stencil.resolvers.DropSiteResolver( dropStep ), null );
	}
	
	public static org.lgna.croquet.DropRejector createDropRejector( final org.lgna.croquet.DropSite desiredDropSite ) {
		return new org.lgna.croquet.DropRejector() {
			public boolean isRejected( org.lgna.croquet.history.DragStep step ) {
				org.lgna.croquet.DropSite dropSite = step.getCurrentPotentialDropSite();
				boolean areEquivalent = edu.cmu.cs.dennisc.equivalence.EquivalenceUtilities.areEquivalent( desiredDropSite, dropSite );
//				if( areEquivalent ) {
//					//pass
//				} else {
//					edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "rejecting", desiredDropSite.hashCode(), dropSite.hashCode() );
//					edu.cmu.cs.dennisc.java.util.logging.Logger.outln( desiredDropSite, dropSite );
//				}
				return areEquivalent == false;
			}
		};
	}
}
