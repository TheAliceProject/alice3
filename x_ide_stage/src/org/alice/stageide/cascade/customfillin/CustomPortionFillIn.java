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
package org.alice.stageide.cascade.customfillin;


/**
 * @author Dennis Cosgrove
 */
public class CustomPortionFillIn extends org.alice.ide.cascade.customfillin.CustomFillIn< org.alice.apis.moveandturn.Portion > {
	@Override
	protected String getMenuProxyText() {
		return "Custom Portion...";
	}
	@Override
	protected org.alice.ide.cascade.customfillin.CustomPane createCustomPane() {
		return new CustomPortionPane();
	}
	@Override
	protected edu.cmu.cs.dennisc.alice.ast.Expression createExpression( org.alice.apis.moveandturn.Portion value ) {
		edu.cmu.cs.dennisc.alice.ast.AbstractType type = edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.alice.apis.moveandturn.Portion.class );
		edu.cmu.cs.dennisc.alice.ast.AbstractConstructor constructor = type.getDeclaredConstructor( Number.class );
		edu.cmu.cs.dennisc.alice.ast.AbstractParameter parameter = constructor.getParameters().get( 0 );
		return new edu.cmu.cs.dennisc.alice.ast.InstanceCreation( constructor,
				new edu.cmu.cs.dennisc.alice.ast.Argument(
						parameter, 
						new edu.cmu.cs.dennisc.alice.ast.DoubleLiteral( value.getValue() )
				)
		);
	}
}
