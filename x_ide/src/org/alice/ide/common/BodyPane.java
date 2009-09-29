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
package org.alice.ide.common;

public class BodyPane extends org.alice.ide.common.StatementLikeSubstance {
	public BodyPane( java.awt.Component statementListComponent ) {
		super( edu.cmu.cs.dennisc.alice.ast.DoInOrder.class, javax.swing.BoxLayout.PAGE_AXIS );
		this.setLayout( new java.awt.BorderLayout() );
		this.setOpaque( false );
		this.add( edu.cmu.cs.dennisc.zoot.ZLabel.acquire( "do in order" ), java.awt.BorderLayout.NORTH );
		this.add( statementListComponent, java.awt.BorderLayout.CENTER );
		this.add( javax.swing.Box.createHorizontalStrut( 8 ), java.awt.BorderLayout.WEST );
	}
	@Override
	protected boolean isKnurlDesired() {
		return false;
	}
}
