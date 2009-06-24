/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */
package org.alice.ide.memberseditor;

/**
 * @author Dennis Cosgrove
 */
abstract class MembersTab extends swing.PageAxisPane {
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

	private Iterable< edu.cmu.cs.dennisc.alice.ast.AbstractType > getTypes() {
		return this.types;
	}

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
		for( edu.cmu.cs.dennisc.alice.ast.AbstractType type : this.getTypes() ) {
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

class TabbedPane extends zoot.ZTabbedPane {
	private MembersTab[] membersTabs = { new ProceduresTab(), new FunctionsTab(), new FieldsTab() };
	public TabbedPane() {
		for( MembersTab membersTab : membersTabs ) {
			this.addMembersTab( membersTab );
		}
	}
	private void addMembersTab( MembersTab membersTab ) {
		zoot.ZScrollPane scrollPane = new zoot.ZScrollPane( membersTab );
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
