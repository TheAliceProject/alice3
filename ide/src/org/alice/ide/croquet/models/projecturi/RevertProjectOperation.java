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
package org.alice.ide.croquet.models.projecturi;

/**
 * @author Dennis Cosgrove
 */
public class RevertProjectOperation extends UriActionOperation {
	private static class SingletonHolder {
		private static RevertProjectOperation instance = new RevertProjectOperation();
	}

	public static RevertProjectOperation getInstance() {
		return SingletonHolder.instance;
	}

	private RevertProjectOperation() {
		super( java.util.UUID.fromString( "e1c3b3d7-dc4b-491c-8958-9a98710d5d1a" ) );
	}

	@Override
	protected final void perform( org.lgna.croquet.history.Transaction transaction, org.lgna.croquet.triggers.Trigger trigger ) {
		org.lgna.croquet.history.CompletionStep<?> step = transaction.createAndSetCompletionStep( this, trigger );
		org.alice.ide.ProjectApplication application = org.alice.ide.ProjectApplication.getActiveInstance();
		org.lgna.croquet.YesNoCancelOption yesNoCancelOption = application.showYesNoCancelConfirmDialog( "WARNING: revert restores your project to the last saved version.\nWould you like to continue with revert?", "Revert?", org.lgna.croquet.MessageType.WARNING );
		if( yesNoCancelOption == org.lgna.croquet.YesNoCancelOption.YES ) {
			java.net.URI uri = application.getUri();
			if( uri != null ) {
				application.loadProjectFrom( uri );
				step.finish();
			} else {
				application.showMessageDialog( "todo: revert uri == null" );
				step.cancel();
			}
			//			context.commitAndInvokeDo( new edu.cmu.cs.dennisc.zoot.AbstractEdit() {
			//				@Override
			//				public void doOrRedo( boolean isDo ) {
			//					getIDE().revert();
			//				}
			//				@Override
			//				public void undo() {
			//					throw new AssertionError();
			//				}
			//				@Override
			//				public boolean canUndo() {
			//					return false;
			//				}
			//				@Override
			//				protected StringBuffer updatePresentation( StringBuffer rv, java.util.Locale locale ) {
			//					rv.append( "revert" );
			//					return rv;
			//				}
			//			} );
		} else {
			step.cancel();
		}
	}
}
