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
package org.alice.ide;

import org.alice.ide.ast.AbstractArgumentListPropertyPane;
import org.alice.ide.codeeditor.ConstantPane;
import org.alice.ide.codeeditor.DefaultListPropertyPane;
import org.alice.ide.codeeditor.DefaultNodeListPropertyPane;
import org.alice.ide.codeeditor.ExpressionListPropertyPane;
import org.alice.ide.codeeditor.ExpressionPropertyPane;
import org.alice.ide.codeeditor.InstancePropertyPane;
import org.alice.ide.codeeditor.NodePropertyPane;
import org.alice.ide.codeeditor.StatementListPropertyPane;
import org.alice.ide.codeeditor.TypedDeclarationPane;
import org.alice.ide.codeeditor.VariablePane;

/**
 * @author Dennis Cosgrove
 */
public class Factory {
	public javax.swing.JComponent create( java.awt.Container container, edu.cmu.cs.dennisc.alice.ast.AbstractType type ) {
		return new org.alice.ide.ast.TypePane( type );
	}
}
