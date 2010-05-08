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

import org.alice.app.openprojectpane.TabContentPane;

/**
 * @author Dennis Cosgrove
 */
abstract class MembersTab extends edu.cmu.cs.dennisc.croquet.PageAxisPanel {
	private java.util.UUID individualId;
	public MembersTab( java.util.UUID individualId ) {
		this.individualId = individualId;
		this.setOpaque( true );
	}
	protected org.alice.ide.IDE getIDE() {
		return org.alice.ide.IDE.getSingleton();
	}
	protected abstract String getKey();
	public final String getTitle() {
		java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle( "org.alice.ide.memberseditor.TabTitles", javax.swing.JComponent.getDefaultLocale() );
		return resourceBundle.getString( this.getKey() );
	}

	private java.util.List< edu.cmu.cs.dennisc.alice.ast.AbstractType > types = new java.util.LinkedList< edu.cmu.cs.dennisc.alice.ast.AbstractType >();

//	private Iterable< edu.cmu.cs.dennisc.alice.ast.AbstractType > getTypes() {
//		return this.types;
//	}

	private edu.cmu.cs.dennisc.croquet.TabIsSelectedOperation tabIsSelectedOperation;
	public edu.cmu.cs.dennisc.croquet.TabIsSelectedOperation getTabIsSelectedOperation() {
		if( this.tabIsSelectedOperation != null ) {
			//pass
		} else {
			this.tabIsSelectedOperation = new edu.cmu.cs.dennisc.croquet.TabIsSelectedOperation(
					edu.cmu.cs.dennisc.zoot.ZManager.UNKNOWN_GROUP, 
					this.individualId, 
					false, 
					this.getTitle() ) {
				@Override
				protected edu.cmu.cs.dennisc.croquet.Component<?> createSingletonView() {
//					edu.cmu.cs.dennisc.croquet.ScrollPane scrollPane = new edu.cmu.cs.dennisc.croquet.ScrollPane( MembersTab.this );
//					scrollPane.setBorder( javax.swing.BorderFactory.createEmptyBorder() );
//					scrollPane.setBackgroundColor( MembersTab.this.getBackgroundColor() );
//					scrollPane.getJComponent().getVerticalScrollBar().setUnitIncrement( 12 );
//					return scrollPane;
					return MembersTab.this;
				}
				@Override
				protected boolean isCloseAffordanceDesired() {
					return false;
				}
			};
		}
		return this.tabIsSelectedOperation;
	}
	
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
				if( this.getIDE().isEmphasizingClasses() || type instanceof edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice || isFirstNonConsumedTypeEncounteredInJava ) {
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
		this.revalidateAndRepaint();
	}
}

abstract class MethodsTab extends MembersTab {
	public MethodsTab( java.util.UUID individualId ) {
		super( individualId );
	}
}

class ProceduresTab extends MethodsTab {
	public ProceduresTab() {
		super( java.util.UUID.fromString( "2731d704-1f80-444e-a610-e6e5866c0b9a" ) );
		this.setBackgroundColor( getIDE().getProcedureColor() );
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

class FunctionsTab extends MethodsTab {
	public FunctionsTab() {
		super( java.util.UUID.fromString( "0f5d1f93-fc67-4109-9aff-0e7b232f201c" ) );
		this.setBackgroundColor( getIDE().getFunctionColor() );
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

class FieldsTab extends MembersTab {
	public FieldsTab() {
		super( java.util.UUID.fromString( "6cb9c5a1-dc60-48e7-9a52-534009a093b8" ) );  
		this.setBackgroundColor( getIDE().getFieldColor() );
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
public class MembersEditor extends org.alice.ide.Editor< edu.cmu.cs.dennisc.alice.ast.AbstractType > {
	private MembersTab[] membersTabs = { new ProceduresTab(), new FunctionsTab(), new FieldsTab() };
	private edu.cmu.cs.dennisc.croquet.TabSelectionOperation tabSelectionOperation = new edu.cmu.cs.dennisc.croquet.TabSelectionOperation( edu.cmu.cs.dennisc.zoot.ZManager.UNKNOWN_GROUP,  java.util.UUID.fromString( "d8348dfa-35df-441d-b233-0e1bd9ffd68f" ) );
	private org.alice.ide.IDE.FieldSelectionObserver fieldSelectionObserver = new org.alice.ide.IDE.FieldSelectionObserver() {
		public void fieldSelectionChanging( edu.cmu.cs.dennisc.alice.ast.AbstractField previousField, edu.cmu.cs.dennisc.alice.ast.AbstractField nextField ) {
		}
		public void fieldSelectionChanged( edu.cmu.cs.dennisc.alice.ast.AbstractField previousField, edu.cmu.cs.dennisc.alice.ast.AbstractField nextField ) {
			MembersEditor.this.handleFieldSelection( nextField );
		}
	};

	private void handleFieldSelection( edu.cmu.cs.dennisc.alice.ast.AbstractField field ) {
		for( MembersTab membersTab : membersTabs ) {
			membersTab.handleFieldSelection( field );
		}
	}
	private void refresh() {
		for( MembersTab membersTab : membersTabs ) {
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
			rv = new org.alice.ide.common.TypeComponent( type );
			map.put( cls, type, rv );
		}
		return rv;
	}
	
	public MembersEditor() {
		for( MembersTab membersTab : membersTabs ) {
			this.tabSelectionOperation.addTabIsSelectedOperation( membersTab.getTabIsSelectedOperation() );
		}
		this.addComponent( this.tabSelectionOperation.getSingletonTabbedPane() );
	}
	@Override
	protected java.awt.LayoutManager createLayoutManager( javax.swing.JPanel jPanel ) {
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: investigate ExpandAllToBoundsLayoutManager" );
		return new edu.cmu.cs.dennisc.java.awt.ExpandAllToBoundsLayoutManager();
	}

	public void setEmphasizingClasses( boolean isEmphasizingClasses ) {
		this.refresh();
	}
	public void setOmittingThisFieldAccesses( boolean isOmittingThisFieldAccesses ) {
		this.refresh();
	}
	@Override
	protected void adding() {
		super.adding();
		this.getIDE().addFieldSelectionObserver( this.fieldSelectionObserver );
		this.handleFieldSelection( getIDE().getFieldSelection() );
	}
	@Override
	protected void removed() {
		this.getIDE().removeFieldSelectionObserver( this.fieldSelectionObserver );
		super.removed();
	}
	
}
