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
package edu.cmu.cs.dennisc.tutorial;

/**
 * @author Dennis Cosgrove
 */
/*package-private*/ abstract class WaitingOnCompleteStep<M extends edu.cmu.cs.dennisc.croquet.Model> extends WaitingStep<M> {
	private boolean isExactMatchRequired;
	public WaitingOnCompleteStep( String title, String text, edu.cmu.cs.dennisc.croquet.RuntimeResolver< ? extends edu.cmu.cs.dennisc.croquet.TrackableShape > trackableShapeResolver, Feature.ConnectionPreference connectionPreference, edu.cmu.cs.dennisc.croquet.RuntimeResolver< M > modelResolver, boolean isDiscriminatingAboutComplete ) {
		super( title, text, new Hole( trackableShapeResolver, connectionPreference ), modelResolver );
		this.isExactMatchRequired = isDiscriminatingAboutComplete;
	}
	protected abstract boolean isInTheDesiredState(edu.cmu.cs.dennisc.croquet.Edit<?> edit);
	private boolean isAcceptable( edu.cmu.cs.dennisc.croquet.AbstractCompleteEvent completeEvent ) {
		edu.cmu.cs.dennisc.croquet.Model eventModel = completeEvent.getParent().getModel();
		if( this.isExactMatchRequired ) {
			return this.getModel() == eventModel;
		} else {
			if( this.getModel() != null ) {
				return this.getModel().getGroup() == eventModel.getGroup();
			} else {
				return false;
			}
		}
	}
	@Override
	public boolean isWhatWeveBeenWaitingFor( edu.cmu.cs.dennisc.croquet.HistoryNode child ) {
		if( child instanceof edu.cmu.cs.dennisc.croquet.AbstractCompleteEvent ) {
			edu.cmu.cs.dennisc.croquet.AbstractCompleteEvent completeEvent = (edu.cmu.cs.dennisc.croquet.AbstractCompleteEvent)child;
			if( this.isAcceptable( completeEvent ) ) {
				edu.cmu.cs.dennisc.croquet.Edit<?> edit;
				if (child instanceof edu.cmu.cs.dennisc.croquet.CommitEvent) {
					edu.cmu.cs.dennisc.croquet.CommitEvent commitEvent = (edu.cmu.cs.dennisc.croquet.CommitEvent) child;
					edit = commitEvent.getEdit();
				} else {
					edit = null;
				}
				boolean rv = this.isInTheDesiredState(edit);
				if( rv ) {
					SoundCache.SUCCESS.startIfNotAlreadyActive();
				} else {
					SoundCache.FAILURE.startIfNotAlreadyActive();
				}
				return rv;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
}
