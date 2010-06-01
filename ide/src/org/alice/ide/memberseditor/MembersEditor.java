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
package org.alice.ide.memberseditor;

/**
 * @author Dennis Cosgrove
 */
abstract class OrganizedByTypeMembersContentPanel extends MembersContentPanel {
	@Override
	protected void handleFieldSelection( edu.cmu.cs.dennisc.alice.ast.AbstractField field, java.util.List< edu.cmu.cs.dennisc.alice.ast.AbstractType > types ) {
		this.removeAllComponents();
		boolean isNonConsumedTypeDeclaredInJavaAlreadyEncountered = false;

		if( types.size() > 0 ) {
			boolean isSeparatorDesired = types.get( 0 ) instanceof edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice;
			for( edu.cmu.cs.dennisc.alice.ast.AbstractType type : types ) {
				boolean isFirstNonConsumedTypeEncounteredInJava = false;
				if( type instanceof edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava ) {
					if( isSeparatorDesired ) {
						this.addComponent( new edu.cmu.cs.dennisc.croquet.HorizontalSeparator() );
						this.addComponent( edu.cmu.cs.dennisc.croquet.BoxUtilities.createVerticalSliver( 16 ) );
						isSeparatorDesired = false;
					}
					if( isNonConsumedTypeDeclaredInJavaAlreadyEncountered ) {
						//pass
					} else {
						if( type.isConsumptionBySubClassDesired() ) {
							//pass
						} else {
							isFirstNonConsumedTypeEncounteredInJava = true;
							isNonConsumedTypeDeclaredInJavaAlreadyEncountered = true;
						}
					}
				}
				if( type.isConsumptionBySubClassDesired() ) {
					//pass
				} else {
					if( org.alice.ide.IDE.getSingleton().isEmphasizingClasses() || type instanceof edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice || isFirstNonConsumedTypeEncounteredInJava ) {
						this.addComponent( MembersEditor.getComponentFor( this.getClass(), type ) );
					}
				}
				this.addComponent( this.getTypeMembersPane( type ) );
			}
		}
		this.revalidateAndRepaint();
	}
	private java.util.Map< edu.cmu.cs.dennisc.alice.ast.AbstractType, AbstractTypeMembersPane > mapTypeToPane = new java.util.HashMap< edu.cmu.cs.dennisc.alice.ast.AbstractType, AbstractTypeMembersPane >();
	protected abstract AbstractTypeMembersPane createTypeMembersPane( edu.cmu.cs.dennisc.alice.ast.AbstractType type );
	protected AbstractTypeMembersPane getTypeMembersPane( edu.cmu.cs.dennisc.alice.ast.AbstractType type ) {
		AbstractTypeMembersPane rv = this.mapTypeToPane.get( type );
		if( rv != null ) {
			//todo?
			rv.refresh();
		} else {
			rv = this.createTypeMembersPane( type );
			this.mapTypeToPane.put( type, rv );
		}
		return rv;
	}
}

abstract class MethodsContentPanel extends OrganizedByTypeMembersContentPanel {
}

class ProceduresContentPanel extends MethodsContentPanel {
	public ProceduresContentPanel() {
		this.setBackgroundColor( org.alice.ide.IDE.getSingleton().getProcedureColor() );
	}
	@Override
	protected AbstractTypeMembersPane createTypeMembersPane( edu.cmu.cs.dennisc.alice.ast.AbstractType type ) {
		return new TypeProceduresPane( type );
	}
}

class FunctionsContentPanel extends MethodsContentPanel {
	public FunctionsContentPanel() {
		this.setBackgroundColor( org.alice.ide.IDE.getSingleton().getFunctionColor() );
	}
	@Override
	protected AbstractTypeMembersPane createTypeMembersPane( edu.cmu.cs.dennisc.alice.ast.AbstractType type ) {
		return new TypeFunctionsPane( type );
	}
}

class FieldsContentPanel extends OrganizedByTypeMembersContentPanel {
	public FieldsContentPanel() {
		this.setBackgroundColor( org.alice.ide.IDE.getSingleton().getFieldColor() );
	}
	@Override
	protected AbstractTypeMembersPane createTypeMembersPane( edu.cmu.cs.dennisc.alice.ast.AbstractType type ) {
		return new TypeFieldsPane( type );
	}
}

/**
 * @author Dennis Cosgrove
 */
public class MembersEditor extends edu.cmu.cs.dennisc.croquet.BorderPanel {
	private static abstract class MemberTab extends edu.cmu.cs.dennisc.croquet.PredeterminedTab {
		private static String getTitle( String key ) {
			java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle( "org.alice.ide.memberseditor.TabTitles", javax.swing.JComponent.getDefaultLocale() );
			return resourceBundle.getString( key );
		}
		public MemberTab( java.util.UUID individualId, String key ) {
			super( individualId, getTitle( key ) );
		}
		@Override
		public edu.cmu.cs.dennisc.croquet.ScrollPane createScrollPane() {
			edu.cmu.cs.dennisc.croquet.ScrollPane rv = super.createScrollPane();
			rv.getAwtComponent().getVerticalScrollBar().setUnitIncrement( 12 );
			return rv;
		}
	}

	private MemberTab proceduresTab = new MemberTab( java.util.UUID.fromString( "2731d704-1f80-444e-a610-e6e5866c0b9a" ), "procedure" ) {
		@Override
		protected edu.cmu.cs.dennisc.croquet.JComponent< ? > createMainComponent() {
			return new ProceduresContentPanel();
		}
	};
	private MemberTab functionsTab = new MemberTab( java.util.UUID.fromString( "0f5d1f93-fc67-4109-9aff-0e7b232f201c" ), "function" ) {
		@Override
		protected edu.cmu.cs.dennisc.croquet.JComponent< ? > createMainComponent() {
			return new FunctionsContentPanel();
		}
	};
	private MemberTab fieldsTab = new MemberTab( java.util.UUID.fromString( "6cb9c5a1-dc60-48e7-9a52-534009a093b8" ), "field" ) {
		@Override
		protected edu.cmu.cs.dennisc.croquet.JComponent< ? > createMainComponent() {
			return new FieldsContentPanel();
		}
	};

	public edu.cmu.cs.dennisc.croquet.PredeterminedTab getProceduresTab() {
		return this.proceduresTab;
	}
	public edu.cmu.cs.dennisc.croquet.PredeterminedTab getFunctionsTab() {
		return this.functionsTab;
	}
	public edu.cmu.cs.dennisc.croquet.PredeterminedTab getFieldsTab() {
		return this.fieldsTab;
	}

	private edu.cmu.cs.dennisc.croquet.TabSelectionOperation tabbedPaneSelectionState = new edu.cmu.cs.dennisc.croquet.TabSelectionOperation( 
			org.alice.ide.IDE.IDE_GROUP, 
			java.util.UUID.fromString( "d8348dfa-35df-441d-b233-0e1bd9ffd68f" ),
			this.proceduresTab, this.functionsTab, this.fieldsTab );


	public edu.cmu.cs.dennisc.croquet.TabSelectionOperation getTabbedPaneSelectionState() {
		return this.tabbedPaneSelectionState;
	}
//	private edu.cmu.cs.dennisc.croquet.ItemSelectionState.ValueObserver< edu.cmu.cs.dennisc.alice.ast.AbstractField > fieldSelectionObserver = new edu.cmu.cs.dennisc.croquet.ItemSelectionState.ValueObserver< edu.cmu.cs.dennisc.alice.ast.AbstractField >() {
//		public void changed( edu.cmu.cs.dennisc.alice.ast.AbstractField nextValue ) {
//			MembersEditor.this.handleFieldSelection( nextValue );
//		}
//	};
//	private void handleFieldSelection( edu.cmu.cs.dennisc.alice.ast.AbstractField field ) {
//		final int N = this.tabbedPaneSelectionState.getItemCount();
//		for( int i=0; i<N; i++ ) {
//			MembersContentPanel membersTab = (MembersContentPanel)this.tabbedPaneSelectionState.getItemAt( i ).getMainComponent();
//			membersTab.handleFieldSelection( field );
//		}
//	}
//	private void refresh() {
//		final int N = this.tabbedPaneSelectionState.getItemCount();
//		for( int i=0; i<N; i++ ) {
//			MembersContentPanel membersTab = (MembersContentPanel)this.tabbedPaneSelectionState.getItemAt( i ).getMainComponent();
//			membersTab.refresh();
//		}
//	}

	//private static java.util.Map< edu.cmu.cs.dennisc.alice.ast.AbstractType, java.awt.Component > map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	private static edu.cmu.cs.dennisc.map.MapToMap< Class< ? >, edu.cmu.cs.dennisc.alice.ast.AbstractType, edu.cmu.cs.dennisc.croquet.Component< ? > > map = edu.cmu.cs.dennisc.map.MapToMap.newInstance();

	public static edu.cmu.cs.dennisc.croquet.Component< ? > getComponentFor( Class< ? > cls, edu.cmu.cs.dennisc.alice.ast.AbstractType type ) {
		edu.cmu.cs.dennisc.croquet.Component< ? > rv = map.get( cls, type );
		if( rv != null ) {
			//pass
		} else {
			rv = org.alice.ide.common.TypeComponent.createInstance( type );
			map.put( cls, type, rv );
		}
		return rv;
	}

	public MembersEditor() {
		final float FONT_SCALAR = 1.4f;
		edu.cmu.cs.dennisc.croquet.ComboBox< edu.cmu.cs.dennisc.alice.ast.AbstractField > comboBox = org.alice.ide.IDE.getSingleton().getFieldSelectionState().createComboBox();
		comboBox.scaleFont( FONT_SCALAR );
		//comboBox.changeFont( edu.cmu.cs.dennisc.java.awt.font.TextWeight.BOLD );
		
		comboBox.setRenderer( new edu.cmu.cs.dennisc.javax.swing.renderers.ListCellRenderer< edu.cmu.cs.dennisc.alice.ast.AbstractField >() {
			@Override
			protected javax.swing.JLabel getListCellRendererComponent( javax.swing.JLabel rv, javax.swing.JList list, edu.cmu.cs.dennisc.alice.ast.AbstractField value, int index, boolean isSelected, boolean cellHasFocus ) {
				if( value != null ) {
					rv.setText( value.getName() );
				}
				return rv;
			}
		} );
		edu.cmu.cs.dennisc.croquet.Label label = new edu.cmu.cs.dennisc.croquet.Label( "instance:" );
		label.scaleFont( FONT_SCALAR );
		edu.cmu.cs.dennisc.croquet.LineAxisPanel instancePanel = new edu.cmu.cs.dennisc.croquet.LineAxisPanel( label, comboBox );

		this.tabbedPaneSelectionState.setValue( this.proceduresTab );
		edu.cmu.cs.dennisc.croquet.AbstractTabbedPane tabbedPane = this.tabbedPaneSelectionState.createDefaultToolPaletteTabbedPane();
		this.addComponent( instancePanel, edu.cmu.cs.dennisc.croquet.BorderPanel.Constraint.NORTH );
		this.addComponent( tabbedPane, Constraint.CENTER );
		tabbedPane.scaleFont( 1.5f );
		
		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 0, 4, 0, 4 ) );
		this.setOpaque( true );
		
		this.tabbedPaneSelectionState.addAndInvokeValueObserver( new edu.cmu.cs.dennisc.croquet.ItemSelectionState.ValueObserver< edu.cmu.cs.dennisc.croquet.PredeterminedTab >() {
			public void changed(edu.cmu.cs.dennisc.croquet.PredeterminedTab nextValue) {
				if( nextValue != null ) {
					MembersEditor.this.setBackgroundColor( nextValue.getMainComponent().getBackgroundColor() );
				}
				MembersEditor.this.repaint();
			}
		} );
		
	}
//	public void setEmphasizingClasses( boolean isEmphasizingClasses ) {
//		this.refresh();
//	}
//	public void setOmittingThisFieldAccesses( boolean isOmittingThisFieldAccesses ) {
//		this.refresh();
//	}
//	@Override
//	protected void handleAddedTo( edu.cmu.cs.dennisc.croquet.Component< ? > parent ) {
//		super.handleAddedTo( parent );
//
//		org.alice.ide.IDE.getSingleton().getFieldSelectionState().addAndInvokeValueObserver( this.fieldSelectionObserver );
//	}
//	@Override
//	protected void handleRemovedFrom( edu.cmu.cs.dennisc.croquet.Component< ? > parent ) {
//		org.alice.ide.IDE.getSingleton().getFieldSelectionState().removeValueObserver( this.fieldSelectionObserver );
//		super.handleRemovedFrom( parent );
//	}
}
