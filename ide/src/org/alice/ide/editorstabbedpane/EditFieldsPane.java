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
package org.alice.ide.editorstabbedpane;

/**
* @author Dennis Cosgrove
*/

public class EditFieldsPane extends EditMembersPane< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > {
	private java.util.Set< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > referencedFields;
	private java.util.Set< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > reassignedFields;
	public EditFieldsPane( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice declaringType ) {
		super( declaringType, declaringType.fields );
		this.referencedFields = edu.cmu.cs.dennisc.java.util.Collections.newHashSet();
		this.reassignedFields = edu.cmu.cs.dennisc.java.util.Collections.newHashSet();
		org.alice.ide.IDE ide = org.alice.ide.IDE.getSingleton();
		edu.cmu.cs.dennisc.alice.Project project = ide.getProject();
		if( project != null ) {
			ide.ensureProjectCodeUpToDate();
			edu.cmu.cs.dennisc.alice.ast.AbstractType programType = project.getProgramType();
			for( final edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice field : declaringType.fields ) {
				edu.cmu.cs.dennisc.pattern.IsInstanceCrawler< edu.cmu.cs.dennisc.alice.ast.FieldAccess > crawler = new edu.cmu.cs.dennisc.pattern.IsInstanceCrawler< edu.cmu.cs.dennisc.alice.ast.FieldAccess >( edu.cmu.cs.dennisc.alice.ast.FieldAccess.class ) {
					@Override
					protected boolean isAcceptable( edu.cmu.cs.dennisc.alice.ast.FieldAccess fieldAccess ) {
						return fieldAccess.field.getValue() == field;
					}
				};
				programType.crawl( crawler, true );
				java.util.List< edu.cmu.cs.dennisc.alice.ast.FieldAccess > fieldAccesses = crawler.getList();
				if( fieldAccesses.size() > 0 ) {
					referencedFields.add( field );
					for( edu.cmu.cs.dennisc.alice.ast.FieldAccess fieldAccess : fieldAccesses ) {
						edu.cmu.cs.dennisc.alice.ast.Node parent = fieldAccess.getParent();
						if( parent instanceof edu.cmu.cs.dennisc.alice.ast.AssignmentExpression ) {
							edu.cmu.cs.dennisc.alice.ast.AssignmentExpression assignmentExpression = (edu.cmu.cs.dennisc.alice.ast.AssignmentExpression)parent;
							if( assignmentExpression.leftHandSide.getValue() == fieldAccess ) {
								reassignedFields.add( field );
								break;
							}
						}
					}
				}
			}
		}
	}
	@Override
	protected java.awt.Component createCellRendererComponent( edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice fieldDeclaredInAlice ) {
		return new org.alice.ide.common.FieldDeclarationPane( org.alice.ide.IDE.getSingleton().getTemplatesFactory(), fieldDeclaredInAlice );
	}
	@Override
	protected edu.cmu.cs.dennisc.croquet.AbstractActionOperation createEditOperation( java.util.UUID groupUUID, String name ) {
		return new org.alice.ide.operations.ast.AbstractEditFieldOperation( groupUUID, java.util.UUID.fromString( "f4ff2dde-185f-4de0-8c90-6ab725184dca" ), "Edit..." ) {
			@Override
			protected void perform( edu.cmu.cs.dennisc.croquet.Context context, java.awt.event.ActionEvent e, edu.cmu.cs.dennisc.croquet.KAbstractButton< ? > button ) {
				edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice field = getSelectedItem();
				if( field != null ) {
					this.perform( context, field, referencedFields, reassignedFields );
				} else {
					context.cancel();
				}
			}
		};
	}
	@Override
	protected edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice createMember( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice declaringType ) {
		org.alice.ide.declarationpanes.CreateFieldPane createFieldPane = new org.alice.ide.declarationpanes.CreateFieldPane( declaringType );
		edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice rv = createFieldPane.showInJDialog();
		return rv;
	}
	@Override
	protected boolean isEditItemEnabledFor( edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice e ) {
		return true;
	}
	@Override
	protected boolean isRemoveItemEnabledFor( edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice e ) {
		return referencedFields.contains( e ) == false;
	}
}
//public class EditFieldsPane extends edu.cmu.cs.dennisc.croquet.KInputPane< Boolean > {
//	private javax.swing.JList list;
//	private edu.cmu.cs.dennisc.zoot.list.AbstractEditableListPane< FieldDeclaredInAlice > editableListPane;
//	public EditFieldsPane( final java.util.UUID groupUUID, final edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice declaringType ) {
//		this.list = new javax.swing.JList();
//		this.list.setCellRenderer( new edu.cmu.cs.dennisc.javax.swing.ContentsCachingListCellRenderer< FieldDeclaredInAlice >() {
//			@Override
//			protected java.awt.Component createComponent(FieldDeclaredInAlice fieldDeclaredInAlice) {
//				return new org.alice.ide.common.FieldDeclarationPane( org.alice.ide.IDE.getSingleton().getTemplatesFactory(), fieldDeclaredInAlice );
//			}
//			@Override
//			public void update( java.awt.Component contents, int index, boolean isSelected, boolean cellHasFocus ) {
//				this.setOpaque( isSelected );
//				this.removeAll();
//				this.add( contents );
//			}
//		} );
//		
//		final java.util.Set< FieldDeclaredInAlice > referencedFields = edu.cmu.cs.dennisc.java.util.Collections.newHashSet();
//		final java.util.Set< FieldDeclaredInAlice > reassignedFields = edu.cmu.cs.dennisc.java.util.Collections.newHashSet();
//		org.alice.ide.IDE ide = org.alice.ide.IDE.getSingleton();
//		edu.cmu.cs.dennisc.alice.Project project = ide.getProject();
//		if( project != null ) {
//			ide.ensureProjectCodeUpToDate();
//			edu.cmu.cs.dennisc.alice.ast.AbstractType programType = project.getProgramType();
//			for( final FieldDeclaredInAlice field : declaringType.fields ) {
//				edu.cmu.cs.dennisc.pattern.IsInstanceCrawler< edu.cmu.cs.dennisc.alice.ast.FieldAccess > crawler = new edu.cmu.cs.dennisc.pattern.IsInstanceCrawler< edu.cmu.cs.dennisc.alice.ast.FieldAccess >( edu.cmu.cs.dennisc.alice.ast.FieldAccess.class ) {
//					@Override
//					protected boolean isAcceptable( edu.cmu.cs.dennisc.alice.ast.FieldAccess fieldAccess ) {
//						return fieldAccess.field.getValue() == field;
//					}
//				};
//				programType.crawl( crawler, true );
//				java.util.List< edu.cmu.cs.dennisc.alice.ast.FieldAccess > fieldAccesses = crawler.getList();
//				if( fieldAccesses.size() > 0 ) {
//					referencedFields.add( field );
//					for( edu.cmu.cs.dennisc.alice.ast.FieldAccess fieldAccess : fieldAccesses ) {
//						edu.cmu.cs.dennisc.alice.ast.Node parent = fieldAccess.getParent();
//						if( parent instanceof edu.cmu.cs.dennisc.alice.ast.AssignmentExpression ) {
//							edu.cmu.cs.dennisc.alice.ast.AssignmentExpression assignmentExpression = (edu.cmu.cs.dennisc.alice.ast.AssignmentExpression)parent;
//							if( assignmentExpression.leftHandSide.getValue() == fieldAccess ) {
//								reassignedFields.add( field );
//								break;
//							}
//						}
//					}
//				}
//			}
//		}
//		
//		
//		final edu.cmu.cs.dennisc.javax.swing.ListPropertyListModel< FieldDeclaredInAlice > fieldsListModel = edu.cmu.cs.dennisc.javax.swing.ListPropertyListModel.createInstance( declaringType.fields );
//		this.list.setModel( fieldsListModel );
//
//		
//		
//		class EditableFieldListPane extends edu.cmu.cs.dennisc.zoot.list.AbstractEditableListPane< FieldDeclaredInAlice > {
//			@Override
//			protected edu.cmu.cs.dennisc.zoot.ActionOperation createEditOperation( java.util.UUID groupUUID ) {
//				return new org.alice.ide.operations.ast.AbstractEditFieldOperation( groupUUID, "Edit..." ) {
//					public void perform( edu.cmu.cs.dennisc.zoot.ActionContext actionContext ) {
//						final int index = getSelectedIndex();
//						if( index >= 0 ) {
//							FieldDeclaredInAlice field = getItemAt( index );
//							this.perform( actionContext, field, referencedFields, reassignedFields );
//						} else {
//							actionContext.cancel();
//						}
//					}
//				};
//			}
//
//			public EditableFieldListPane() {
//				super( groupUUID, list );
//			}
//			
//			@Override
//			protected void add( int index, FieldDeclaredInAlice e ) {
//				fieldsListModel.add( index, e );
//			}
//			@Override
//			protected void remove( int index, FieldDeclaredInAlice e ) {
//				fieldsListModel.remove( index, e );
//			}
//			@Override
//			protected void setItemsAt( int index, FieldDeclaredInAlice e0, FieldDeclaredInAlice e1 ) {
//				fieldsListModel.set( index, e0, e1 );
//			}
//			@Override
//			protected boolean isRemoveItemEnabled( int index ) {
//				FieldDeclaredInAlice field = this.getItemAt( index );
//				return referencedFields.contains( field ) == false;
//			}
//			@Override
//			protected boolean isEditItemEnabled( int index ) {
//				return true;
//			}
//			@Override
//			protected FieldDeclaredInAlice createItem() throws Exception {
//				org.alice.ide.declarationpanes.CreateFieldPane createFieldPane = new org.alice.ide.declarationpanes.CreateFieldPane( declaringType );
//				FieldDeclaredInAlice rv = createFieldPane.showInJDialog( org.alice.ide.IDE.getSingleton() );
//				if( rv != null ) {
//					//pass
//				} else {
//					throw new Exception();
//				}
//				return rv;
//			}
//		}
//		this.editableListPane = new EditableFieldListPane();
//		this.editableListPane.setBorder( javax.swing.BorderFactory.createEmptyBorder( 8, 8, 8, 8 ) );
//		this.setLayout( new java.awt.BorderLayout() );
//		this.add( this.editableListPane, java.awt.BorderLayout.CENTER );
//	}
//	@Override
//	protected Boolean getActualInputValue() {
//		return Boolean.TRUE;
//	}
//}
