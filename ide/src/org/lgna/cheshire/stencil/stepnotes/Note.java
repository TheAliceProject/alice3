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

package org.lgna.cheshire.stencil.stepnotes;

/**
 * @author Dennis Cosgrove
 */
public abstract class Note<S extends org.lgna.croquet.steps.Step<?>> extends org.lgna.cheshire.stencil.Note {
	private final S step;
	public Note( S step ) {
		this.step = step;
		this.addFeatures( this.step );
	}
	protected abstract void addFeatures( S step );
	public S getStep() {
		return this.step;
	}
	@Override
	protected String getText() {
		org.lgna.croquet.steps.Transaction transaction = this.step.getParent();
		org.lgna.croquet.edits.Edit< ? > edit = transaction.getEdit();
		org.lgna.croquet.UserInformation userInformation = org.lgna.cheshire.stencil.StencilsPresentation.getInstance().getUserInformation();
		return this.step.getTutorialNoteText( edit, userInformation );
	}
	@Override
	public boolean isWhatWeveBeenWaitingFor( org.lgna.cheshire.events.Event event ) {
		if( event instanceof org.lgna.cheshire.events.StepAddedEvent ) {
			org.lgna.cheshire.events.StepAddedEvent stepAddedEvent = (org.lgna.cheshire.events.StepAddedEvent)event;
			if( this.getStep().getModel() == stepAddedEvent.getStep().getModel() ) {
				return true;
			} else {
				//todo
				if( stepAddedEvent.getStep().getModel() != null ) {
					try {
						if( this.getStep().getModel().getClass() == stepAddedEvent.getStep().getModel().getClass() ) {;
							edu.cmu.cs.dennisc.print.PrintUtilities.println( "isWhatWeveBeenWaitingFor", this.getStep().getModel() == stepAddedEvent.getStep().getModel(), this.getStep().getModel(), stepAddedEvent.getStep().getModel() );
							return true;
						} else {
							return false;
						}
					} catch( NullPointerException npe ) {
						return false;
					}
				} else {
					return false;
				}
			}
		} else {
			return false;
		}
	}
}
