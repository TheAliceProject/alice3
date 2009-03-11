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

abstract class MembersTab extends javax.swing.JPanel {
	public MembersTab() {
		this.setBackground( org.alice.ide.lookandfeel.PaintUtilities.getColorFor( this.getKey() ) );
	}
	protected org.alice.ide.IDE getIDE() {
		return org.alice.ide.IDE.getSingleton();
	}
	protected abstract String getKey();
	public final String getTitle() {
		return org.alice.ide.lookandfeel.PaintUtilities.getTextFor( this.getKey() );
	}
	
}

abstract class MethodsTab extends MembersTab {
}

class ProceduresTab extends MethodsTab {
	@Override
	protected String getKey() {
		return "procedures";
	}
	
}
class FunctionsTab extends MethodsTab {
	@Override
	protected String getKey() {
		return "functions";
	}
}
class FieldsTab extends MembersTab {
	@Override
	protected String getKey() {
		return "fields";
	}
}

/**
 * @author Dennis Cosgrove
 */
public class MembersEditor extends org.alice.ide.Editor<edu.cmu.cs.dennisc.alice.ast.AbstractType> {
	private zoot.ZTabbedPane tabbedPane = new zoot.ZTabbedPane( null );
	public MembersEditor() {
		this.addTab( new ProceduresTab() );
		this.addTab( new FunctionsTab() );
		this.addTab( new FieldsTab() );
		this.setLayout( new edu.cmu.cs.dennisc.awt.ExpandAllToBoundsLayoutManager() );
		this.add( this.tabbedPane );
	}
	private void addTab( MembersTab membersTab ) {
		this.tabbedPane.addTab( membersTab.getTitle(), membersTab );
		
	}
	@Override
	public java.awt.Dimension getPreferredSize() {
		return new java.awt.Dimension( 100, 100 );
	}
}
