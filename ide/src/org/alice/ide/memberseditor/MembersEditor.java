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
abstract class MembersContentPanel extends edu.cmu.cs.dennisc.croquet.PageAxisPanel {
	public MembersContentPanel() {
		this.setOpaque( true );
	}
	public final String getTitle() {
		java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle( "org.alice.ide.memberseditor.TabTitles", javax.swing.JComponent.getDefaultLocale() );
		return resourceBundle.getString( this.getKey() );
	}
	protected abstract String getKey();

	private java.util.List< edu.cmu.cs.dennisc.alice.ast.AbstractType > types = new java.util.LinkedList< edu.cmu.cs.dennisc.alice.ast.AbstractType >();

	public void handleFieldSelection( edu.cmu.cs.dennisc.alice.ast.AbstractField field ) {
		this.types.clear();
		if( field != null ) {
			edu.cmu.cs.dennisc.alice.ast.AbstractType type = field.getValueType();
			while( type != null ) {
				this.types.add( type );
				if( type.isFollowToSuperClassDesired() ) {
					type = type.getSuperType();
				} else {
					break;
				}
			}
		}
		this.refresh();
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

	protected void refresh() {
		this.removeAllComponents();
		boolean isNonConsumedTypeDeclaredInJavaAlreadyEncountered = false;

		for( edu.cmu.cs.dennisc.alice.ast.AbstractType type : this.types ) {
			boolean isFirstNonConsumedTypeEncounteredInJava = false;
			if( type instanceof edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava ) {
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
		//		java.util.List< AbstractTypeMembersPane > panes = new java.util.LinkedList< AbstractTypeMembersPane >();
		//		for( edu.cmu.cs.dennisc.alice.ast.AbstractType type : this.getTypes() ) {
		//			panes.add( this.getTypeMembersPane( type ) );
		//		}
		//		for( AbstractTypeMembersPane pane : panes ) {
		//			for( java.awt.Component component : pane.getComponents() ) {
		//				this.add( component );
		//			}
		//		}
		//		this.addComponent( edu.cmu.cs.dennisc.croquet.BoxUtilities.createGlue() );
		this.revalidateAndRepaint();
	}
}

abstract class MethodsContentPanel extends MembersContentPanel {
}

class ProceduresContentPanel extends MethodsContentPanel {
	public ProceduresContentPanel() {
		this.setBackgroundColor( org.alice.ide.IDE.getSingleton().getProcedureColor() );
	}
	@Override
	protected AbstractTypeMembersPane createTypeMembersPane( edu.cmu.cs.dennisc.alice.ast.AbstractType type ) {
		return new TypeProceduresPane( type );
	}
	@Override
	protected String getKey() {
		return "procedure";
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
	@Override
	protected String getKey() {
		return "function";
	}
}

class FieldsContentPanel extends MembersContentPanel {
	public FieldsContentPanel() {
		this.setBackgroundColor( org.alice.ide.IDE.getSingleton().getFieldColor() );
	}
	@Override
	protected AbstractTypeMembersPane createTypeMembersPane( edu.cmu.cs.dennisc.alice.ast.AbstractType type ) {
		return new TypeFieldsPane( type );
	}
	@Override
	protected String getKey() {
		return "field";
	}
}

/**
 * @author Dennis Cosgrove
 */
public class MembersEditor extends edu.cmu.cs.dennisc.croquet.BorderPanel {
	private static abstract class MemberTabStateOperation extends edu.cmu.cs.dennisc.croquet.TabStateOperation {
		public MemberTabStateOperation( java.util.UUID individualId, boolean initialState ) {
			super( org.alice.app.ProjectApplication.IDE_GROUP, individualId, initialState );
			MembersContentPanel memberTab = (MembersContentPanel)this.getSingletonView();
			String trueAndFalseText = memberTab.getTitle();
			this.setTrueText( trueAndFalseText );
			this.setFalseText( trueAndFalseText );
		}
		@Override
		protected final boolean isCloseAffordanceDesired() {
			return false;
		}
	}

	private MemberTabStateOperation proceduresTabStateOperation = new MemberTabStateOperation( java.util.UUID.fromString( "2731d704-1f80-444e-a610-e6e5866c0b9a" ), true ) {
		@Override
		protected edu.cmu.cs.dennisc.croquet.JComponent< ? > createSingletonView() {
			return new ProceduresContentPanel();
		}
	};
	private MemberTabStateOperation functionsTabStateOperation = new MemberTabStateOperation( java.util.UUID.fromString( "0f5d1f93-fc67-4109-9aff-0e7b232f201c" ), false ) {
		@Override
		protected edu.cmu.cs.dennisc.croquet.JComponent< ? > createSingletonView() {
			return new FunctionsContentPanel();
		}
	};
	private MemberTabStateOperation fieldsTabStateOperation = new MemberTabStateOperation( java.util.UUID.fromString( "6cb9c5a1-dc60-48e7-9a52-534009a093b8" ), false ) {
		@Override
		protected edu.cmu.cs.dennisc.croquet.JComponent< ? > createSingletonView() {
			return new FieldsContentPanel();
		}
	};

	public edu.cmu.cs.dennisc.croquet.TabStateOperation getProceduresTabStateOperation() {
		return this.proceduresTabStateOperation;
	}
	public edu.cmu.cs.dennisc.croquet.TabStateOperation getFunctionsTabStateOperation() {
		return this.functionsTabStateOperation;
	}
	public edu.cmu.cs.dennisc.croquet.TabStateOperation getFieldsTabStateOperation() {
		return this.fieldsTabStateOperation;
	}

	private edu.cmu.cs.dennisc.croquet.ItemSelectionOperation.ValueObserver< edu.cmu.cs.dennisc.alice.ast.AbstractField > fieldSelectionObserver = new edu.cmu.cs.dennisc.croquet.ItemSelectionOperation.ValueObserver< edu.cmu.cs.dennisc.alice.ast.AbstractField >() {
		public void changed( edu.cmu.cs.dennisc.alice.ast.AbstractField nextValue ) {
			MembersEditor.this.handleFieldSelection( nextValue );
		}
	};

	private edu.cmu.cs.dennisc.croquet.TabSelectionOperation tabbedPaneSelectionOperation = new edu.cmu.cs.dennisc.croquet.TabSelectionOperation( org.alice.ide.IDE.IDE_GROUP, java.util.UUID.fromString( "d8348dfa-35df-441d-b233-0e1bd9ffd68f" ),
			this.fieldsTabStateOperation, this.functionsTabStateOperation, this.proceduresTabStateOperation );

	private void handleFieldSelection( edu.cmu.cs.dennisc.alice.ast.AbstractField field ) {
		for( edu.cmu.cs.dennisc.croquet.TabStateOperation tabStateOperation : this.tabbedPaneSelectionOperation.getTabStateOperations() ) {
			MembersContentPanel membersTab = (MembersContentPanel)tabStateOperation.getSingletonView();
			membersTab.handleFieldSelection( field );
		}
	}
	private void refresh() {
		for( edu.cmu.cs.dennisc.croquet.TabStateOperation tabStateOperation : this.tabbedPaneSelectionOperation.getTabStateOperations() ) {
			MembersContentPanel membersTab = (MembersContentPanel)tabStateOperation.getSingletonView();
			membersTab.refresh();
		}
	}

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

		edu.cmu.cs.dennisc.croquet.AbstractTabbedPane tabbedPane = this.tabbedPaneSelectionOperation.createToolPaletteTabbedPane();
		//= this.tabbedPaneSelectionOperation.createFolderTabbedPane();
		this.addComponent( instancePanel, edu.cmu.cs.dennisc.croquet.BorderPanel.Constraint.NORTH );
		this.addComponent( tabbedPane, Constraint.CENTER );
		tabbedPane.scaleFont( 1.5f );
		
		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 0, 4, 0, 4 ) );
		this.setOpaque( true );
		
		this.tabbedPaneSelectionOperation.addAndInvokeSelectionObserver( new edu.cmu.cs.dennisc.croquet.TabSelectionOperation.SelectionObserver() {
			public void selected( edu.cmu.cs.dennisc.croquet.TabStateOperation next ) {
				MembersEditor.this.setBackgroundColor( next.getSingletonView().getBackgroundColor() );
				MembersEditor.this.repaint();
			}
		} );
		
	}
	public void setEmphasizingClasses( boolean isEmphasizingClasses ) {
		this.refresh();
	}
	public void setOmittingThisFieldAccesses( boolean isOmittingThisFieldAccesses ) {
		this.refresh();
	}
	@Override
	protected void handleAddedTo( edu.cmu.cs.dennisc.croquet.Component< ? > parent ) {
		super.handleAddedTo( parent );

		org.alice.ide.IDE.getSingleton().getFieldSelectionState().addAndInvokeValueObserver( this.fieldSelectionObserver );
	}
	@Override
	protected void handleRemovedFrom( edu.cmu.cs.dennisc.croquet.Component< ? > parent ) {
		org.alice.ide.IDE.getSingleton().getFieldSelectionState().removeValueObserver( this.fieldSelectionObserver );
		super.handleRemovedFrom( parent );
	}
}
