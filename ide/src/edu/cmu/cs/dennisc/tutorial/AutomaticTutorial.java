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
	private static edu.cmu.cs.dennisc.croquet.Retargeter retargeter;
	public static edu.cmu.cs.dennisc.croquet.Retargeter getRetargeter() {
		return retargeter;
	}
	public static void setRetargeter( edu.cmu.cs.dennisc.croquet.Retargeter retargeter ) {
		AutomaticTutorial.retargeter = retargeter;
	}
	
	public static javax.swing.JLayeredPane getLayeredPane() {
		edu.cmu.cs.dennisc.croquet.Application application = edu.cmu.cs.dennisc.croquet.Application.getSingleton();
		javax.swing.JFrame frame = application.getFrame().getAwtComponent();
		javax.swing.JLayeredPane layeredPane = frame.getLayeredPane();
		final int PAD = 4;
		frame.getJMenuBar().setBorder( javax.swing.BorderFactory.createEmptyBorder(PAD+32,PAD,0,PAD));
		((javax.swing.JComponent)frame.getContentPane()).setBorder( javax.swing.BorderFactory.createEmptyBorder(0,PAD,PAD,PAD));
		return layeredPane; 
	}
	private class AutomaticTutorialStencil extends TutorialStencil {
		public AutomaticTutorialStencil( javax.swing.JLayeredPane layeredPane, edu.cmu.cs.dennisc.croquet.Group[] groups ) {
			super( layeredPane, groups, edu.cmu.cs.dennisc.croquet.ContextManager.getRootContext() );
		}
	}
	private AutomaticTutorialStencil stencil;
	private java.util.ArrayList< edu.cmu.cs.dennisc.croquet.Group > groups; 
	private edu.cmu.cs.dennisc.croquet.RootContext sourceContext;
	public AutomaticTutorial( edu.cmu.cs.dennisc.croquet.Group[] groups ) {
		this.stencil = new AutomaticTutorialStencil( getLayeredPane(), groups );
		this.groups = edu.cmu.cs.dennisc.java.util.Collections.newArrayList( groups );
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
		public AutomaticBooleanStateStep( edu.cmu.cs.dennisc.croquet.BooleanStateContext context, edu.cmu.cs.dennisc.croquet.BooleanStateEdit edit ) {
			super( context.getModel().getClass().getSimpleName(), context.getModel().getClass().getName(), new ModelResolver< edu.cmu.cs.dennisc.croquet.BooleanState >( context ), edit.getNextValue() );
		}
	}
	private class AutomaticInputDialogOperationStep extends InputDialogOpenAndCommitStep {
		private edu.cmu.cs.dennisc.croquet.InputDialogOperationContext< ? > context;
		private edu.cmu.cs.dennisc.croquet.Edit<?> edit;
		public AutomaticInputDialogOperationStep( edu.cmu.cs.dennisc.croquet.InputDialogOperationContext< ? > context, edu.cmu.cs.dennisc.croquet.Edit<?> edit ) {
			super( context.getModel().getClass().getSimpleName(), context.getModel().getClass().getName(), "commit text", new ModelResolver( context ), null, null, null );
			this.context = context;
			this.edit = edit;
		}
		@Override
		protected boolean isEditAcceptable( edu.cmu.cs.dennisc.croquet.Edit< ? > edit ) {
			boolean rv = this.edit.isReplacementAcceptable( edit );
			if( rv ) {
				this.edit.retarget( retargeter, edit );
				sourceContext.retarget( retargeter );
			}
			return rv;
		}
	}
	public void addSteps( edu.cmu.cs.dennisc.croquet.RootContext sourceContext ) {
		this.addMessageStep( "start", "start of tutorial" );
		this.sourceContext = sourceContext;
		for( edu.cmu.cs.dennisc.croquet.HistoryNode node : sourceContext.getChildren() ) {
			if( node instanceof edu.cmu.cs.dennisc.croquet.ModelContext< ? > ) {
				edu.cmu.cs.dennisc.croquet.ModelContext< ? > modelContext = (edu.cmu.cs.dennisc.croquet.ModelContext< ? >)node;
				edu.cmu.cs.dennisc.croquet.Model model = modelContext.getModel();
				edu.cmu.cs.dennisc.croquet.Group group = model.getGroup();
				if( this.groups.contains( group ) ) {
					if( modelContext instanceof edu.cmu.cs.dennisc.croquet.InputDialogOperationContext< ? > ) {
						edu.cmu.cs.dennisc.croquet.InputDialogOperationContext< ? > inputDialogOperationContext = (edu.cmu.cs.dennisc.croquet.InputDialogOperationContext< ? >)modelContext;
						edu.cmu.cs.dennisc.croquet.Edit<?> edit = inputDialogOperationContext.getEdit();
						if( edit != null ) {
							this.stencil.addStep( new AutomaticInputDialogOperationStep( inputDialogOperationContext, edit ) );
						}
					} else if( modelContext instanceof edu.cmu.cs.dennisc.croquet.BooleanStateContext ) {
						edu.cmu.cs.dennisc.croquet.BooleanStateContext booleanStateContext = (edu.cmu.cs.dennisc.croquet.BooleanStateContext)modelContext;
						edu.cmu.cs.dennisc.croquet.Edit<?> edit = booleanStateContext.getEdit();
						if( edit instanceof edu.cmu.cs.dennisc.croquet.BooleanStateEdit ) {
							this.stencil.addStep( new AutomaticBooleanStateStep( booleanStateContext, (edu.cmu.cs.dennisc.croquet.BooleanStateEdit)edit ) );
						}
					}
				} else if( edu.cmu.cs.dennisc.croquet.DragAndDropModel.DRAG_GROUP == group ) {
					if( modelContext instanceof edu.cmu.cs.dennisc.croquet.DragAndDropContext ) {
						edu.cmu.cs.dennisc.croquet.DragAndDropContext dragContext = (edu.cmu.cs.dennisc.croquet.DragAndDropContext)modelContext;
						this.addMessageStep( "drag", "drag" );
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
