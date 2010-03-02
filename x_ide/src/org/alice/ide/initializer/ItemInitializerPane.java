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
package org.alice.ide.initializer;

/**
 * @author Dennis Cosgrove
 */
public class ItemInitializerPane extends edu.cmu.cs.dennisc.croquet.swing.BorderPane {
	public ItemInitializerPane( edu.cmu.cs.dennisc.alice.ast.ExpressionProperty initializerProperty ) {
		this.add( org.alice.ide.IDE.getSingleton().getCodeFactory().createExpressionPropertyPane( initializerProperty, null ), java.awt.BorderLayout.WEST );
	}
}
//public class ItemInitializerPane extends org.alice.ide.codeeditor.ExpressionPropertyDropDownPane {
//	public ItemInitializerPane( edu.cmu.cs.dennisc.alice.ast.ExpressionProperty initializerProperty ) {
//		super( null, new org.alice.ide.common.ExpressionPropertyPane( org.alice.ide.IDE.getSingleton().getCodeFactory(), initializerProperty ), initializerProperty );
//	}
//}
