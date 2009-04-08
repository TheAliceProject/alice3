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
package org.alice.ide.cascade;

/**
 * @author Dennis Cosgrove
 */
public class SimpleExpressionFillIn< E extends edu.cmu.cs.dennisc.alice.ast.Expression > extends cascade.SimpleFillIn< E > {
	public SimpleExpressionFillIn( E model ) {
		super( model );
	}
	protected org.alice.ide.IDE getIDE() {
		return org.alice.ide.IDE.getSingleton();
	}
	@Override
	protected javax.swing.JComponent createMenuProxy() {
		javax.swing.JComponent rv;
		org.alice.ide.common.Factory factory = getIDE().getPreviewFactory();
		edu.cmu.cs.dennisc.alice.ast.Expression expression = this.getModel();
//		if( expression instanceof edu.cmu.cs.dennisc.alice.ast.FieldAccess ) {
//			rv = new org.alice.ide.common.FieldAccessPane( factory, (edu.cmu.cs.dennisc.alice.ast.FieldAccess)expression );
//		} else {
			rv = (javax.swing.JComponent)factory.createExpressionPane( expression );
//		}
		return rv;
	}
}
