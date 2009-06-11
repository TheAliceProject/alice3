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

//todo: extend NumberFillerInner
/**
 * @author Dennis Cosgrove
 */
public class PortionFillerInner extends org.alice.ide.cascade.fillerinners.InstanceCreationFillerInner {
	public PortionFillerInner() {
		super( org.alice.apis.moveandturn.Portion.class );
	}
	@Override
	public void addFillIns( cascade.Blank blank ) {

		blank.addFillIn( new org.alice.ide.cascade.SimpleExpressionFillIn( new edu.cmu.cs.dennisc.alice.ast.DoubleLiteral( 0.0 ) ) ); 
		blank.addFillIn( new org.alice.ide.cascade.SimpleExpressionFillIn( new edu.cmu.cs.dennisc.alice.ast.DoubleLiteral( 0.1 ) ) ); 
		blank.addFillIn( new org.alice.ide.cascade.SimpleExpressionFillIn( new edu.cmu.cs.dennisc.alice.ast.DoubleLiteral( 0.2 ) ) ); 
		blank.addFillIn( new org.alice.ide.cascade.SimpleExpressionFillIn( new edu.cmu.cs.dennisc.alice.ast.DoubleLiteral( 0.3 ) ) ); 
		blank.addFillIn( new org.alice.ide.cascade.SimpleExpressionFillIn( new edu.cmu.cs.dennisc.alice.ast.DoubleLiteral( 0.4 ) ) ); 
		blank.addFillIn( new org.alice.ide.cascade.SimpleExpressionFillIn( new edu.cmu.cs.dennisc.alice.ast.DoubleLiteral( 0.5 ) ) ); 
		blank.addFillIn( new org.alice.ide.cascade.SimpleExpressionFillIn( new edu.cmu.cs.dennisc.alice.ast.DoubleLiteral( 0.6 ) ) ); 
		blank.addFillIn( new org.alice.ide.cascade.SimpleExpressionFillIn( new edu.cmu.cs.dennisc.alice.ast.DoubleLiteral( 0.7 ) ) ); 
		blank.addFillIn( new org.alice.ide.cascade.SimpleExpressionFillIn( new edu.cmu.cs.dennisc.alice.ast.DoubleLiteral( 0.8 ) ) ); 
		blank.addFillIn( new org.alice.ide.cascade.SimpleExpressionFillIn( new edu.cmu.cs.dennisc.alice.ast.DoubleLiteral( 0.9 ) ) ); 
		blank.addFillIn( new org.alice.ide.cascade.SimpleExpressionFillIn( new edu.cmu.cs.dennisc.alice.ast.DoubleLiteral( 1.0 ) ) ); 
		
//		edu.cmu.cs.dennisc.alice.ast.AbstractType type = this.getType();
//		edu.cmu.cs.dennisc.alice.ast.AbstractConstructor constructor = type.getDeclaredConstructor( Number.class );
//		edu.cmu.cs.dennisc.alice.ast.AbstractParameter parameter = constructor.getParameters().get( 0 );
//		this.addInstanceCreationExpressionFillIn( blank, constructor, parameter, new edu.cmu.cs.dennisc.alice.ast.DoubleLiteral( 0.0 ) );
//		this.addInstanceCreationExpressionFillIn( blank, constructor, parameter, new edu.cmu.cs.dennisc.alice.ast.DoubleLiteral( 0.1 ) );
//		this.addInstanceCreationExpressionFillIn( blank, constructor, parameter, new edu.cmu.cs.dennisc.alice.ast.DoubleLiteral( 0.2 ) );
//		this.addInstanceCreationExpressionFillIn( blank, constructor, parameter, new edu.cmu.cs.dennisc.alice.ast.DoubleLiteral( 0.3 ) );
//		this.addInstanceCreationExpressionFillIn( blank, constructor, parameter, new edu.cmu.cs.dennisc.alice.ast.DoubleLiteral( 0.4 ) );
//		this.addInstanceCreationExpressionFillIn( blank, constructor, parameter, new edu.cmu.cs.dennisc.alice.ast.DoubleLiteral( 0.5 ) );
//		this.addInstanceCreationExpressionFillIn( blank, constructor, parameter, new edu.cmu.cs.dennisc.alice.ast.DoubleLiteral( 0.6 ) );
//		this.addInstanceCreationExpressionFillIn( blank, constructor, parameter, new edu.cmu.cs.dennisc.alice.ast.DoubleLiteral( 0.7 ) );
//		this.addInstanceCreationExpressionFillIn( blank, constructor, parameter, new edu.cmu.cs.dennisc.alice.ast.DoubleLiteral( 0.8 ) );
//		this.addInstanceCreationExpressionFillIn( blank, constructor, parameter, new edu.cmu.cs.dennisc.alice.ast.DoubleLiteral( 0.9 ) );
//		this.addInstanceCreationExpressionFillIn( blank, constructor, parameter, new edu.cmu.cs.dennisc.alice.ast.DoubleLiteral( 1.0 ) );
		blank.addSeparator();
		blank.addFillIn( new org.alice.stageide.cascade.customfillin.CustomPortionFillIn() );
	}
}
