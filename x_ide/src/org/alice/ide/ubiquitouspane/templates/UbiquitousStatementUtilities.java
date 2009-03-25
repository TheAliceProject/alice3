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
package org.alice.ide.ubiquitouspane.templates;

/**
 * @author Dennis Cosgrove
 */
public class UbiquitousStatementUtilities {
	public static void adorn( UbiquitousStatementTemplate ubiquitousStatementTemplate, edu.cmu.cs.dennisc.alice.ast.Statement incompleteStatement ) {
		javax.swing.JComponent component = ubiquitousStatementTemplate.getJComponent();
		Class< ? extends edu.cmu.cs.dennisc.alice.ast.Statement > cls = ubiquitousStatementTemplate.getStatementCls();
		String text = edu.cmu.cs.dennisc.util.ResourceBundleUtilities.getStringFromSimpleNames( cls, "org.alice.ide.ubiquitouspane.Templates" );
		//text = "label: " + text;
		zoot.ZLabel label = new zoot.ZLabel();
		label.setText( text );
		component.add( label );
		component.setToolTipText( "" );
		
		org.alice.ide.IDE ide = org.alice.ide.IDE.getSingleton();
		javax.swing.JComponent incompleteStatementPane = ide.getTemplatesFactory().createStatementPane( incompleteStatement );
		ide.addToConcealedBin( incompleteStatementPane );

		ubiquitousStatementTemplate.setToolTip( new zoot.ZToolTip( incompleteStatementPane ) );
	}
}
