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
package org.alice.stageide.cascade.fillerinners;

/**
 * @author Dennis Cosgrove
 */
public class VolumeLevelFillerInner extends org.alice.ide.cascade.fillerinners.AbstractNumberFillerInner {
	public VolumeLevelFillerInner() {
		super( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.alice.apis.moveandturn.VolumeLevel.class ), edu.cmu.cs.dennisc.alice.ast.DoubleLiteral.class );
	}
	@Override
	public void addFillIns( edu.cmu.cs.dennisc.cascade.Blank blank ) {
		this.addExpressionFillIn( blank, 0.0 );
		this.addExpressionFillIn( blank, 0.25 );
		this.addExpressionFillIn( blank, 0.5 );
		this.addExpressionFillIn( blank, 1.0 );
		this.addExpressionFillIn( blank, 2.0 );
		blank.addSeparator();
		blank.addFillIn( new org.alice.stageide.cascade.customfillin.CustomVolumeLevelFillIn() );
	}
}
