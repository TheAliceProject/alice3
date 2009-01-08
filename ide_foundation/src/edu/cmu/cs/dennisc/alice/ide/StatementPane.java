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
package edu.cmu.cs.dennisc.alice.ide;

/**
 * @author Dennis Cosgrove
 */
public class StatementPane extends javax.swing.JPanel {
	private edu.cmu.cs.dennisc.alice.ast.Statement m_statement;
	public StatementPane( edu.cmu.cs.dennisc.alice.ast.Statement statement ) {
		refresh();
	}
	private String getFormatString() {
//		IDE ide = IDE.getSingleton();
//		String text = ide.getHeaderFormatString( m_statement );
		return "while( <property:expression> ) {\n\t<property:body>\n}";
		//return "int {{property:constant}}={{property:count}};\nfor(int {{property:variable}}=0; {{property:variable}}<{{property:constant}}; {{property:variable}}++ ) {";
	}
	private java.util.ArrayList< javax.swing.JComponent > getComponents( String formatString ) {
		java.util.ArrayList< javax.swing.JComponent > rv = new java.util.ArrayList< javax.swing.JComponent >();
		java.util.regex.Pattern pattern = java.util.regex.Pattern.compile( "<property:.*>" );
		java.util.regex.Matcher matcher = pattern.matcher( formatString );
		int i = 0;
		while( matcher.find() ) {
			String labelText = formatString.substring( 0, matcher.start() );
			if( labelText.length() > 0 ) {
				rv.add( new javax.swing.JLabel( labelText ) );
			}
			String buttonText = formatString.substring( matcher.start(), matcher.end() );
			rv.add( new javax.swing.JButton( buttonText ) );
			i = matcher.end();
		}
		String labelText = formatString.substring( i );
		if( labelText.length() > 0 ) {
			rv.add( new javax.swing.JLabel( labelText ) );
		}
		return rv;
	}
	
	private void refresh() {
		this.setLayout( new java.awt.FlowLayout() );
		removeAll();
		String formatString = getFormatString();
		
		java.util.regex.Pattern pattern = java.util.regex.Pattern.compile( "\n\t<property:body>\n" );
		java.util.regex.Matcher matcher = pattern.matcher( formatString );
		matcher.find();
		String headerFormatString = formatString.substring( 0, matcher.start() ); 
		String tailFormatString = formatString.substring( matcher.end() ); 

		java.util.ArrayList< javax.swing.JComponent > headerComponents = getComponents( headerFormatString );
		for( javax.swing.JComponent component : headerComponents ) {
			this.add( component );
		}
		java.util.ArrayList< javax.swing.JComponent > tailComponents = getComponents( tailFormatString );
		for( javax.swing.JComponent component : tailComponents ) {
			this.add( component );
		}
		
	}
	
	public static void main( String[] args ) {
		
		StatementPane statementPane = new StatementPane( new edu.cmu.cs.dennisc.alice.ast.WhileLoop() );
		javax.swing.JFrame frame = new javax.swing.JFrame();
		frame.getContentPane().add( statementPane );
		frame.setSize(  640, 480 );
		frame.setVisible( true );
	}
}
