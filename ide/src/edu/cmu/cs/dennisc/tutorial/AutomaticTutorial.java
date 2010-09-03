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
public class AutomaticTutorial {
	private TutorialStencil stencil;
	private edu.cmu.cs.dennisc.croquet.Retargeter retargeter;
	private java.util.ArrayList< edu.cmu.cs.dennisc.croquet.Group > groups; 
	public AutomaticTutorial( edu.cmu.cs.dennisc.croquet.Group[] groups ) {
		this.stencil = TutorialStencil.createInstance( groups );
		this.groups = edu.cmu.cs.dennisc.java.util.Collections.newArrayList( groups );
	}
	public void setRetargeter( edu.cmu.cs.dennisc.croquet.Retargeter retargeter ) {
		this.retargeter = retargeter;
	}
	private void addMessageStep( String title, String text ) {
		this.stencil.addStep( new MessageStep( title, text ) );
	}
	private static class ModelResolver< M extends edu.cmu.cs.dennisc.croquet.Model > implements edu.cmu.cs.dennisc.croquet.RuntimeResolver< M > {
		private edu.cmu.cs.dennisc.croquet.ModelContext< M > modelContext;
		public ModelResolver( edu.cmu.cs.dennisc.croquet.ModelContext< M > modelContext ) {
			this.modelContext = modelContext;
		}
		public M getResolved() {
			return this.modelContext.getModel();
		}
	}
	private class AutomaticBooleanStateStep extends BooleanStateStep { 
		public AutomaticBooleanStateStep( edu.cmu.cs.dennisc.croquet.BooleanStateContext booleanStateContext, edu.cmu.cs.dennisc.croquet.BooleanStateEdit edit ) {
			super( booleanStateContext.getModel().getClass().getSimpleName(), booleanStateContext.getModel().getClass().getName(), new ModelResolver< edu.cmu.cs.dennisc.croquet.BooleanState >( booleanStateContext ), edit.getNextValue() );
		}
		@Override
		protected void complete() {
		}
	}
	public void addSteps( edu.cmu.cs.dennisc.croquet.RootContext rootContext ) {
		this.addMessageStep( "start", "start of tutorial" );
		for( edu.cmu.cs.dennisc.croquet.HistoryNode node : rootContext.getChildren() ) {
			if( node instanceof edu.cmu.cs.dennisc.croquet.ModelContext< ? > ) {
				edu.cmu.cs.dennisc.croquet.ModelContext< ? > modelContext = (edu.cmu.cs.dennisc.croquet.ModelContext< ? >)node;
				edu.cmu.cs.dennisc.croquet.Model model = modelContext.getModel();
				if( this.groups.contains( model.getGroup() ) ) {
					if( modelContext instanceof edu.cmu.cs.dennisc.croquet.InputDialogOperationContext< ? > ) {
						edu.cmu.cs.dennisc.croquet.InputDialogOperationContext< ? > inputDialogOperationContext = (edu.cmu.cs.dennisc.croquet.InputDialogOperationContext< ? >)modelContext;
						edu.cmu.cs.dennisc.croquet.InputDialogOperation< ? > inputDialogOperation = inputDialogOperationContext.getModel();
						assert inputDialogOperation != null;
						edu.cmu.cs.dennisc.print.PrintUtilities.println( "inputDialogOperation:", inputDialogOperation );
						//tutorial.addInputDialogOpenAndCommitStep( "title", "openText", "commitText", inputDialogOperation, tutorial.createToDoCompletorValidator() );
					} else if( modelContext instanceof edu.cmu.cs.dennisc.croquet.BooleanStateContext ) {
						edu.cmu.cs.dennisc.croquet.BooleanStateContext booleanStateContext = (edu.cmu.cs.dennisc.croquet.BooleanStateContext)modelContext;
						edu.cmu.cs.dennisc.croquet.BooleanStateEdit edit = booleanStateContext.getEdit();
						if( edit != null ) {
							edu.cmu.cs.dennisc.croquet.BooleanState booleanState = booleanStateContext.getModel();
//							//booleanState = org.alice.ide.croquet.models.ui.IsSceneEditorExpandedState.getInstance();
//							if( booleanState instanceof org.alice.ide.croquet.models.ui.debug.IsInteractionTreeShowingState ) {
//								//pass
//							} else {
								this.stencil.addStep( new AutomaticBooleanStateStep( booleanStateContext, edit ) );
//							}
						}
					}
				}
			}
		}
		this.addMessageStep( "end", "end of tutorial" );
	}
	public void setSelectedIndex( int index ) {
		this.stencil.setSelectedIndex( index );
	}
	public void setVisible( boolean isVisible ) {
		if( isVisible ) {
			this.stencil.addToLayeredPane();
		} else {
			this.stencil.removeFromLayeredPane();
		}
	}
}
