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
package edu.cmu.cs.dennisc.cheshire;

/**
 * @author Dennis Cosgrove
 */
class IsAcceptableSuccessfulCompletionOf extends IsChildOfAndInstanceOf< edu.cmu.cs.dennisc.croquet.SuccessfulCompletionEvent > {
	private edu.cmu.cs.dennisc.croquet.SuccessfulCompletionEvent originalSuccessfulCompletionEvent;
	public IsAcceptableSuccessfulCompletionOf( ParentContextCriterion parentContextCriterion, edu.cmu.cs.dennisc.croquet.SuccessfulCompletionEvent originalSuccessfulCompletionEvent ) {
		super( parentContextCriterion, edu.cmu.cs.dennisc.croquet.SuccessfulCompletionEvent.class );
		this.originalSuccessfulCompletionEvent = originalSuccessfulCompletionEvent;
	}
	@Override
	protected boolean isSpecificallyWhatWereLookingFor( edu.cmu.cs.dennisc.croquet.SuccessfulCompletionEvent successfulCompletionEvent ) throws CancelException {
		boolean rv = super.isSpecificallyWhatWereLookingFor( successfulCompletionEvent );
		if( rv ) {
			if( successfulCompletionEvent instanceof edu.cmu.cs.dennisc.croquet.FinishEvent ) {
				rv = this.originalSuccessfulCompletionEvent instanceof edu.cmu.cs.dennisc.croquet.FinishEvent;
				if( rv ) {
					this.setReplacementAcceptability( edu.cmu.cs.dennisc.croquet.ReplacementAcceptability.PERFECT_MATCH );
				} else {
					//todo?
				}
			} else {
				edu.cmu.cs.dennisc.croquet.Edit< ? > potentialReplacementEdit = successfulCompletionEvent.getEdit();
				if( this.originalSuccessfulCompletionEvent != null ) {
					edu.cmu.cs.dennisc.croquet.Edit< ? > originalEdit = this.originalSuccessfulCompletionEvent.getEdit();
					if( originalEdit != null ) {
						edu.cmu.cs.dennisc.croquet.ReplacementAcceptability replacementAcceptability = originalEdit.getReplacementAcceptability( potentialReplacementEdit, ConstructionGuide.getInstance().getUserInformation() );
						this.setReplacementAcceptability( replacementAcceptability );
						if( replacementAcceptability.isAcceptable() ) {
							edu.cmu.cs.dennisc.croquet.Retargeter retargeter = ConstructionGuide.getInstance().getRetargeter();
							originalEdit.addKeyValuePairs( retargeter, potentialReplacementEdit );
							ConstructionGuide.getInstance().retargetOriginalContext( retargeter );
						} else {
							edu.cmu.cs.dennisc.croquet.Application.getSingleton().showMessageDialog( replacementAcceptability.getDeviationDescription(), "edit does not pass muster" );
							throw new CancelException( "unacceptable: replacement edit does not pass muster." );
						}
					} else {
						throw new CancelException( "unacceptable: replacement edit is null." );
					}
				} else {
					if( potentialReplacementEdit != null ) {
						throw new CancelException( "unacceptable: original edit is null." );
					} else {
						//pass
					}
				}
			}
		} else {
			System.err.println( "IsAcceptableSuccessfulCompletionOf did not pass super" );
		}
		return rv;
	}
}
