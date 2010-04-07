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
abstract class MembersTab extends edu.cmu.cs.dennisc.javax.swing.components.JPageAxisPane {
	public MembersTab() {
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

	public void handleFieldSelection( org.alice.ide.event.FieldSelectionEvent e ) {
		this.types.clear();
		edu.cmu.cs.dennisc.alice.ast.AbstractField field = e.getNextValue();
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
		this.removeAll();
		
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
					this.add( MembersEditor.getComponentFor( this.getClass(), type ) );
				}
			}
			this.add( this.getTypeMembersPane( type ) );
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
		this.revalidate();
		this.repaint();
	}
}

abstract class MethodsTab extends MembersTab {
}

class ProceduresTab extends MethodsTab {
	public ProceduresTab() {
		this.setBackground( getIDE().getProcedureColor() );
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
		this.setBackground( getIDE().getFunctionColor() );
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
		this.setBackground( getIDE().getFieldColor() );
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

class TabbedPane extends edu.cmu.cs.dennisc.zoot.ZTabbedPane {
	private MembersTab[] membersTabs = { new ProceduresTab(), new FunctionsTab(), new FieldsTab() };
	public TabbedPane() {
		for( MembersTab membersTab : membersTabs ) {
			this.addMembersTab( membersTab );
		}
	}
	private void addMembersTab( MembersTab membersTab ) {
		edu.cmu.cs.dennisc.javax.swing.components.JScrollPane scrollPane = new edu.cmu.cs.dennisc.javax.swing.components.JScrollPane( membersTab );
		scrollPane.setBorder( javax.swing.BorderFactory.createEmptyBorder() );
		scrollPane.setBackground( membersTab.getBackground() );
		scrollPane.getVerticalScrollBar().setUnitIncrement( 12 );
		this.addTab( membersTab.getTitle(), scrollPane );
	}
	public void handleFieldSelection( org.alice.ide.event.FieldSelectionEvent e ) {
		for( MembersTab membersTab : membersTabs ) {
			membersTab.handleFieldSelection( e );
		}
	}
	public void refresh() {
		for( MembersTab membersTab : membersTabs ) {
			membersTab.refresh();
		}
	}
}

/**
 * @author Dennis Cosgrove
 */
public class MembersEditor extends org.alice.ide.Editor< edu.cmu.cs.dennisc.alice.ast.AbstractType > implements org.alice.ide.event.IDEListener {
	private TabbedPane tabbedPane = new TabbedPane();

	//private static java.util.Map< edu.cmu.cs.dennisc.alice.ast.AbstractType, java.awt.Component > map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	private static edu.cmu.cs.dennisc.map.MapToMap< Class< ? >, edu.cmu.cs.dennisc.alice.ast.AbstractType, java.awt.Component > map = edu.cmu.cs.dennisc.map.MapToMap.newInstance();
	public static java.awt.Component getComponentFor( Class< ? > cls, edu.cmu.cs.dennisc.alice.ast.AbstractType type ) {
		java.awt.Component rv = map.get( cls, type );
		if( rv != null ) {
			//pass
		} else {
			rv = new org.alice.ide.common.TypeComponent( type );
			map.put( cls, type, rv );
		}
		return rv;
	}
	
	public MembersEditor() {
		this.setLayout( new edu.cmu.cs.dennisc.awt.ExpandAllToBoundsLayoutManager() );
		this.add( this.tabbedPane );
	}

	public void fieldSelectionChanging( org.alice.ide.event.FieldSelectionEvent e ) {
	}
	public void fieldSelectionChanged( org.alice.ide.event.FieldSelectionEvent e ) {
		this.tabbedPane.handleFieldSelection( e );
	}

	public void localeChanging( org.alice.ide.event.LocaleEvent e ) {
	}
	public void localeChanged( org.alice.ide.event.LocaleEvent e ) {
	}

	public void focusedCodeChanging( org.alice.ide.event.FocusedCodeChangeEvent e ) {
	}
	public void focusedCodeChanged( org.alice.ide.event.FocusedCodeChangeEvent e ) {
	}

	public void projectOpening( org.alice.ide.event.ProjectOpenEvent e ) {
	}
	public void projectOpened( org.alice.ide.event.ProjectOpenEvent e ) {
	}

	public void transientSelectionChanging( org.alice.ide.event.TransientSelectionEvent e ) {
	}
	public void transientSelectionChanged( org.alice.ide.event.TransientSelectionEvent e ) {
	}

	public void setEmphasizingClasses( boolean isEmphasizingClasses ) {
		this.tabbedPane.refresh();
	}
	public void setOmittingThisFieldAccesses( boolean isOmittingThisFieldAccesses ) {
		this.tabbedPane.refresh();
	}
}
