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

package org.alice.ide;

/**
 * @author Dennis Cosgrove
 */
public abstract class MainComponent extends org.lgna.croquet.components.BorderPanel {
	private final org.lgna.croquet.State.ValueObserver< org.alice.ide.perspectives.IdePerspective > perspectiveListener = new org.lgna.croquet.State.ValueObserver< org.alice.ide.perspectives.IdePerspective >() {
		public void changing( org.lgna.croquet.State< org.alice.ide.perspectives.IdePerspective > state, org.alice.ide.perspectives.IdePerspective prevValue, org.alice.ide.perspectives.IdePerspective nextValue, boolean isAdjusting ) {
		}
		public void changed( org.lgna.croquet.State< org.alice.ide.perspectives.IdePerspective > state, org.alice.ide.perspectives.IdePerspective prevValue, org.alice.ide.perspectives.IdePerspective nextValue, boolean isAdjusting ) {
			MainComponent.this.handlePerspectiveChanged( prevValue, nextValue );
		}
	};

	public MainComponent() {
		org.alice.ide.croquet.models.typeeditor.DeclarationTabState.getInstance().addAndInvokeValueObserver( new org.lgna.croquet.ListSelectionState.ValueObserver< org.alice.ide.croquet.models.typeeditor.DeclarationComposite >() {
			public void changing( org.lgna.croquet.State< org.alice.ide.croquet.models.typeeditor.DeclarationComposite > state, org.alice.ide.croquet.models.typeeditor.DeclarationComposite prevValue, org.alice.ide.croquet.models.typeeditor.DeclarationComposite nextValue, boolean isAdjusting ) {
			}
			public void changed( org.lgna.croquet.State< org.alice.ide.croquet.models.typeeditor.DeclarationComposite > state, org.alice.ide.croquet.models.typeeditor.DeclarationComposite prevValue, org.alice.ide.croquet.models.typeeditor.DeclarationComposite nextValue, boolean isAdjusting ) {
				refreshAccessibles();
			}
		} );
		this.setBackgroundColor( org.lgna.croquet.components.FolderTabbedPane.DEFAULT_BACKGROUND_COLOR );
		IDE.getActiveInstance().getPerspectiveState().addAndInvokeValueObserver( this.perspectiveListener );
	}
	
	public abstract org.alice.ide.sceneeditor.AbstractSceneEditor getSceneEditor();

	//todo remove
	/*package-private*/ boolean isRespondingToRefreshAccessibles = true;
	public void refreshAccessibles() {
//		this.typeHierarchyView.refresh();
//		if( isRespondingToRefreshAccessibles ) {
//			//edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: reduce visibility of refreshAccessibles" );
//
//			org.lgna.project.ast.AbstractCode code = this.getFocusedCode();
//			org.lgna.project.ast.Accessible accessible = org.alice.ide.croquet.models.ui.AccessibleListSelectionState.getInstance().getSelectedItem();
//
//			java.util.List< org.lgna.project.ast.Accessible > accessibles = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
//			if( this.rootField != null ) {
//				accessibles.add( this.rootField );
//				for( org.lgna.project.ast.AbstractField field : this.getRootTypeDeclaredInAlice().fields ) {
//					if( this.isAccessibleDesired( field ) ) {
//						accessibles.add( field );
//					}
//				}
//			}
//
//			int indexOfLastField = accessibles.size() - 1;
//			if( code instanceof org.lgna.project.ast.CodeDeclaredInAlice ) {
//				org.lgna.project.ast.CodeDeclaredInAlice codeDeclaredInAlice = (org.lgna.project.ast.CodeDeclaredInAlice)code;
//				for( org.lgna.project.ast.ParameterDeclaredInAlice parameter : codeDeclaredInAlice.getParamtersProperty() ) {
//					if( this.isAccessibleDesired( parameter ) ) {
//						accessibles.add( parameter );
//					}
//				}
//				for( org.lgna.project.ast.VariableDeclaredInAlice variable : IDE.getVariables( code ) ) {
//					if( this.isAccessibleDesired( variable ) ) {
//						accessibles.add( variable );
//					}
//				}
//				for( org.lgna.project.ast.ConstantDeclaredInAlice constant : IDE.getConstants( code ) ) {
//					if( this.isAccessibleDesired( constant ) ) {
//						accessibles.add( constant );
//					}
//				}
//			}
//
//			int selectedIndex;
//			if( accessible != null ) {
//				selectedIndex = accessibles.indexOf( accessible );
//			} else {
//				selectedIndex = -1;
//			}
//			if( selectedIndex == -1 ) {
//				if( code != null ) {
//					accessible = this.mapCodeToAccessible.get( code );
//					selectedIndex = accessibles.indexOf( accessible );
//				}
//			}
//			if( selectedIndex == -1 ) {
//				selectedIndex = indexOfLastField;
//			}
//			org.alice.ide.croquet.models.ui.AccessibleListSelectionState.getInstance().setListData( selectedIndex, accessibles );
//		}
	}
	
	private void handlePerspectiveChanged( org.alice.ide.perspectives.IdePerspective prevPerspective, org.alice.ide.perspectives.IdePerspective nextPerspective ) {
		if( prevPerspective != null ) {
			if( prevPerspective != nextPerspective ) {
				prevPerspective.handleDeactivation( this );
			}
		}
		nextPerspective.handleActivation( this );
	}
}
