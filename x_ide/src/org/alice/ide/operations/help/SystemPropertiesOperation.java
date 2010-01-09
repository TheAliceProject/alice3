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
package org.alice.ide.operations.help;


/**
 * @author Dennis Cosgrove
 */
public class SystemPropertiesOperation extends org.alice.ide.operations.InconsequentialActionOperation {
	public SystemPropertiesOperation() {
		this.putValue( javax.swing.Action.NAME, "Display System Properties..." );
	}
	
	
	@Override
	protected void performInternal( edu.cmu.cs.dennisc.zoot.ActionContext actionContext ) {
		edu.cmu.cs.dennisc.croquet.swing.FormPane formPane = new edu.cmu.cs.dennisc.croquet.swing.FormPane( 8, 2 ) {
			private java.awt.Component[] createComponentRowForSystemProperty( String name ) {
				return edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( createColumn0Label( name+":" ), edu.cmu.cs.dennisc.zoot.ZLabel.acquire( System.getProperty( name ) ) );
			}
			@Override
			protected java.util.List<java.awt.Component[]> addComponentRows(java.util.List<java.awt.Component[]> rv) {
				rv.add( createComponentRowForSystemProperty( "java.version" ) );
				rv.add( createComponentRowForSystemProperty( "os.name" ) );
				rv.add( createComponentRowForSystemProperty( "os.arch" ) );
				rv.add( createComponentRowForSystemProperty( "os.version" ) );
				return rv;
			}
		};
		edu.cmu.cs.dennisc.croquet.swing.PageAxisPane pane = new edu.cmu.cs.dennisc.croquet.swing.PageAxisPane(
			formPane,
			javax.swing.Box.createVerticalStrut( 16 ),
			new javax.swing.JButton( "Show All Properties..." ),
			javax.swing.Box.createVerticalStrut( 8 )
		);
		javax.swing.JOptionPane.showMessageDialog( this.getIDE(), pane, "System Properties", javax.swing.JOptionPane.INFORMATION_MESSAGE ); 
	}
}
