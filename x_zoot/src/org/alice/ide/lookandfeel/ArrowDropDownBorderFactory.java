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
package org.alice.ide.lookandfeel;

/**
 * @author Dennis Cosgrove
 */
public class ArrowDropDownBorderFactory implements DropDownBorderFactory {
	public static final int AFFORDANCE_SIZE = 9; 
	private static final int INSET = 2; 
	private static final int LEFT = INSET; 
	private static final int TOP = INSET; 
	private static final int RIGHT = INSET + AFFORDANCE_SIZE; 
	private static final int BOTTOM = INSET;
	public javax.swing.border.Border createBorder( java.lang.Object context, java.awt.Component component ) {
		return javax.swing.BorderFactory.createEmptyBorder( TOP, LEFT, BOTTOM, RIGHT );
	}
}
