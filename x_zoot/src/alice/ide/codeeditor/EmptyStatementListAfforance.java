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
package alice.ide.codeeditor;

/**
 * @author Dennis Cosgrove
 */
public class EmptyStatementListAfforance extends alice.ide.ast.StatementLikeSubstance {
	public EmptyStatementListAfforance() {
		super( edu.cmu.cs.dennisc.alice.ast.Statement.class );
		//this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 8, 16, 8, 48 ) );
		alice.ide.ast.Label label = new alice.ide.ast.Label( "drop statement here" );
		label.setFont( label.getFont().deriveFont( java.awt.Font.ITALIC ) );
		this.add( label );
		//this.setBackground( edu.cmu.cs.dennisc.awt.ColorUtilities.createGray( 230 ) );
		this.setBackground( new java.awt.Color( 63, 63, 63, 63 ) );
	}
	@Override
	protected boolean isActuallyPotentiallyActive() {
		return false;
	}
	@Override
	protected boolean isActuallyPotentiallySelectable() {
		return false;
	}
	@Override
	protected boolean isKnurlDesired() {
		return false;
	}
}
