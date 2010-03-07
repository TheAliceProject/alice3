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

import org.alice.stageide.choosers.AngleChooser;


/**
 * @author Dennis Cosgrove
 */
public class CustomAngleFillIn extends org.alice.ide.cascade.customfillin.CustomFillIn< edu.cmu.cs.dennisc.alice.ast.Expression, org.alice.apis.moveandturn.Angle > {
	@Override
	protected String getMenuProxyText() {
		return "Other Angle...";
	}
	@Override
	protected org.alice.ide.choosers.ValueChooser createCustomPane() {
		return new AngleChooser();
	}
	@Override
	protected edu.cmu.cs.dennisc.alice.ast.Expression createExpression( org.alice.apis.moveandturn.Angle value ) {
		edu.cmu.cs.dennisc.alice.ast.DoubleLiteral doubleLiteral = new edu.cmu.cs.dennisc.alice.ast.DoubleLiteral( value.doubleValue() );
		final boolean IS_LITERAL_DESIRED = true;
		if( IS_LITERAL_DESIRED ) {
			return doubleLiteral;
		} else {
			return org.alice.ide.ast.NodeUtilities.createInstanceCreation( org.alice.apis.moveandturn.AngleInRevolutions.class, new Class<?>[] { Number.class }, doubleLiteral );
		}
	}
}
