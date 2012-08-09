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

package org.alice.ide.declarationseditor;

import org.alice.stageide.StageIDE;

/**
 * @author Dennis Cosgrove
 */
public class DeclarationTabState extends org.lgna.croquet.TabSelectionState< DeclarationComposite > {
	private static class SingletonHolder {
		private static DeclarationTabState instance = new DeclarationTabState();
	}
	public static DeclarationTabState getInstance() {
		return SingletonHolder.instance;
	}
	
	private final edu.cmu.cs.dennisc.property.event.ListPropertyListener< org.lgna.project.ast.UserMethod > methodsListener = new edu.cmu.cs.dennisc.property.event.ListPropertyListener< org.lgna.project.ast.UserMethod >() {
		public void adding( edu.cmu.cs.dennisc.property.event.AddListPropertyEvent< org.lgna.project.ast.UserMethod > e ) {
		}
		public void added( edu.cmu.cs.dennisc.property.event.AddListPropertyEvent< org.lgna.project.ast.UserMethod > e ) {
			DeclarationTabState.this.refresh();
		}
		public void clearing( edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent< org.lgna.project.ast.UserMethod > e ) {
		}
		public void cleared( edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent< org.lgna.project.ast.UserMethod > e ) {
			DeclarationTabState.this.refresh();
		}
		public void removing( edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent< org.lgna.project.ast.UserMethod > e ) {
		}
		public void removed( edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent< org.lgna.project.ast.UserMethod > e ) {
			DeclarationTabState.this.refresh();
		}
		public void setting( edu.cmu.cs.dennisc.property.event.SetListPropertyEvent< org.lgna.project.ast.UserMethod > e ) {
		}
		public void set( edu.cmu.cs.dennisc.property.event.SetListPropertyEvent< org.lgna.project.ast.UserMethod > e ) {
			DeclarationTabState.this.refresh();
		}
	};
	private final org.lgna.croquet.State.ValueListener< org.lgna.project.ast.NamedUserType > typeListener = new org.lgna.croquet.State.ValueListener< org.lgna.project.ast.NamedUserType >() {
		public void changing( org.lgna.croquet.State< org.lgna.project.ast.NamedUserType > state, org.lgna.project.ast.NamedUserType prevValue, org.lgna.project.ast.NamedUserType nextValue, boolean isAdjusting ) {
		}
		public void changed( org.lgna.croquet.State< org.lgna.project.ast.NamedUserType > state, org.lgna.project.ast.NamedUserType prevValue, org.lgna.project.ast.NamedUserType nextValue, boolean isAdjusting ) {
			DeclarationTabState.this.handleTypeChanged( prevValue, nextValue );
		}
	};
	private final org.lgna.croquet.State.ValueListener<Boolean> isEmphasizingClassesListener = new org.lgna.croquet.State.ValueListener<Boolean>() {
		public void changing( org.lgna.croquet.State< Boolean > state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
		}
		public void changed( org.lgna.croquet.State< Boolean > state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
			DeclarationTabState.this.refresh();
		}
	};
	
	private final org.lgna.croquet.State.ValueListener< org.alice.ide.ProjectDocument > projectListener = new org.lgna.croquet.State.ValueListener< org.alice.ide.ProjectDocument >() {
		public void changing( org.lgna.croquet.State< org.alice.ide.ProjectDocument > state, org.alice.ide.ProjectDocument prevValue, org.alice.ide.ProjectDocument nextValue, boolean isAdjusting ) {
		}
		public void changed( org.lgna.croquet.State< org.alice.ide.ProjectDocument > state, org.alice.ide.ProjectDocument prevValue, org.alice.ide.ProjectDocument nextValue, boolean isAdjusting ) {
			DeclarationTabState.this.clear();
		}
	};
	
	private org.lgna.project.ast.NamedUserType type;
	private DeclarationTabState() {
		super( org.alice.ide.IDE.DOCUMENT_UI_GROUP, java.util.UUID.fromString( "7b3f95a0-c188-43bf-9089-21ec77c99a69" ), org.alice.ide.croquet.codecs.typeeditor.DeclarationCompositeCodec.SINGLETON );
		TypeState.getInstance().addAndInvokeValueListener( this.typeListener );
		org.alice.ide.croquet.models.ui.preferences.IsEmphasizingClassesState.getInstance().addValueListener( this.isEmphasizingClassesListener );
		org.alice.ide.project.ProjectDocumentState.getInstance().addValueListener( this.projectListener );
	}

	@Override
	protected void fireChanged( org.alice.ide.declarationseditor.DeclarationComposite prevValue, org.alice.ide.declarationseditor.DeclarationComposite nextValue, boolean isAdjusting ) {
		super.fireChanged( prevValue, nextValue, isAdjusting );
		if( org.alice.ide.croquet.models.ui.preferences.IsEmphasizingClassesState.getInstance().getValue() ) {
			//pass
		} else {
			if( nextValue != null ) {
				org.lgna.project.ast.AbstractType< ?,?,? > type = nextValue.getType();
				if( type instanceof org.lgna.project.ast.NamedUserType ) {
					org.lgna.project.ast.NamedUserType namedUserType = (org.lgna.project.ast.NamedUserType)type;
					TypeState.getInstance().setValueTransactionlessly( namedUserType );
				}
			}
		}
	}
	@Override
	protected void handleMissingItem( org.alice.ide.declarationseditor.DeclarationComposite missingItem ) {
		this.addItem( missingItem );
		if( org.alice.ide.croquet.models.ui.preferences.IsEmphasizingClassesState.getInstance().getValue() ) {
			if( missingItem != null ) {
				org.lgna.project.ast.NamedUserType missingItemType = (org.lgna.project.ast.NamedUserType)missingItem.getType();
				if( missingItemType != TypeState.getInstance().getValue() ) {
					TypeState.getInstance().setValue( missingItemType );
				}
			}
		}
	}
	private void refresh() {
		if( org.alice.ide.croquet.models.ui.preferences.IsEmphasizingClassesState.getInstance().getValue() ) {
			this.pushAtomic();
			try {
				java.util.List< DeclarationComposite > items = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
				if( this.type != null ) {
					items.add( DeclarationComposite.getInstance( this.type ) );
					final boolean isInitializeEventListenersDesired = false;
					for( org.lgna.project.ast.UserMethod method : this.type.methods ) {
						if( method.isPublicAccess() || ( isInitializeEventListenersDesired && StageIDE.INITIALIZE_EVENT_LISTENERS_METHOD_NAME.equals( method.getName() ) ) ) {
							if( method.getManagementLevel() == org.lgna.project.ast.ManagementLevel.NONE ) {
								items.add( DeclarationComposite.getInstance( method ) );
							}
						}
					}
					DeclarationComposite selection = DeclarationComposite.getInstance( this.type );
					int index;
					if( selection != null ) {
						index = this.indexOf( selection );
						index = Math.max( index, 0 );
					} else {
						index = this.getItemCount()-1;
						//index = -1;
					}
					this.setListData( index, items );
				} else {
					this.setSelectedIndex( -1 );
				}
			} finally {
				this.popAtomic();
			}
		} else {
			boolean isTypeRemovalNecessary = false;
			DeclarationComposite[] items = this.toArray();
			for( DeclarationComposite item : items ) {
				if( item.getDeclaration() instanceof org.lgna.project.ast.AbstractType< ?,?,? > ) {
					isTypeRemovalNecessary = true;
				}
			}
			if( isTypeRemovalNecessary ) {
				DeclarationComposite selectedItem = this.getSelectedItem();
				int selectionIndex = -1;
				java.util.List< DeclarationComposite > nextItems = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
				for( DeclarationComposite item : items ) {
					if( item.getDeclaration() instanceof org.lgna.project.ast.AbstractCode ) {
						if( item == selectedItem ) {
							selectionIndex = nextItems.size();
						}
						nextItems.add( item );
					}
				}
				this.setListData( selectionIndex, nextItems );
			}
		}
	}
	private void handleTypeChanged( org.lgna.project.ast.NamedUserType prevType, org.lgna.project.ast.NamedUserType nextType ) {
		if( this.type != nextType ) {
			if( this.type != null ) {
				this.type.methods.removeListPropertyListener( this.methodsListener );
			}
			this.type = nextType;
			this.refresh();
			if( this.type != null ) {
				this.type.methods.addListPropertyListener( this.methodsListener );
			}
		}
	}
	public org.lgna.croquet.ActionOperation getItemSelectionOperation( org.lgna.project.ast.AbstractDeclaration declaration ) {
		org.lgna.croquet.ActionOperation rv = super.getItemSelectionOperation( DeclarationComposite.getInstance( declaration ) );
		if( declaration instanceof org.lgna.project.ast.AbstractConstructor ) {
			rv.setName( "Edit Constructor" );
		} else {
			rv.setName( "Edit" );
		}
		return rv;
	}	
	public void handleAstChangeThatCouldBeOfInterest() {
		org.alice.ide.declarationseditor.DeclarationComposite declarationComposite = this.getValue();
		if( declarationComposite != null ) {
			org.lgna.croquet.components.View view = declarationComposite.getView();
			if( view instanceof org.alice.ide.declarationseditor.code.components.CodeDeclarationView ) {
				org.alice.ide.declarationseditor.code.components.CodeDeclarationView codeDeclarationView = (org.alice.ide.declarationseditor.code.components.CodeDeclarationView)view;
				org.alice.ide.codedrop.CodePanelWithDropReceptor codePanelWithDropReceptor = codeDeclarationView.getCodePanelWithDropReceptor();
				if( codePanelWithDropReceptor instanceof org.alice.ide.codeeditor.CodeEditor ) {
					org.alice.ide.codeeditor.CodeEditor codeEditor = (org.alice.ide.codeeditor.CodeEditor)codePanelWithDropReceptor;
					codeEditor.handleAstChangeThatCouldBeOfInterest();
				}
			}
		}
	}
}
