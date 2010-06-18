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
/*package-private*/ abstract class AbstractDialogCloseStep<M extends edu.cmu.cs.dennisc.croquet.AbstractDialogOperation> extends WaitingStep<M> {
	public AbstractDialogCloseStep( String title, String text, Feature feature, final edu.cmu.cs.dennisc.croquet.Resolver<M> dialogOperationResolver ) {
		super( title, text, feature, dialogOperationResolver );
	}
	
	@Override
	protected final boolean isAlreadyInTheDesiredState() {
		edu.cmu.cs.dennisc.croquet.Dialog dialog = this.getModel().getActiveDialog();
		if( dialog != null ) {
			java.awt.Rectangle dialogLocalBounds = dialog.getLocalBounds();
			boolean isIntersecting = false;
			for( Note note : this.getNotes() ) {
				java.awt.Rectangle bounds = note.getBounds( dialog );
				if( bounds.intersects( dialogLocalBounds ) ) {
					isIntersecting = true;
					break;
				}
			}
			if( isIntersecting ) {
				if( this.getNoteCount() == 1 ) {
					Note note0 = this.getNoteAt( 0 );
					note0.setLocation( dialog.getWidth()+100, dialog.getHeight()/2, dialog );					
				}
			}
		}
		return this.getModel().getActiveDialog() == null;
	}
	@Override
	protected final void complete( edu.cmu.cs.dennisc.croquet.ModelContext< ? > context ) {
		edu.cmu.cs.dennisc.croquet.Dialog dialog = this.getModel().getActiveDialog();
		if( dialog != null ) {
			dialog.setVisible( false );
		}
	}
}
