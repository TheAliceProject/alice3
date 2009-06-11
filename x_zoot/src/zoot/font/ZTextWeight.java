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

package zoot.font;

/**
 * @author Dennis Cosgrove
 */
public enum ZTextWeight implements ZTextAttribute< Float > {
	EXTRA_LIGHT(java.awt.font.TextAttribute.WEIGHT_EXTRA_LIGHT), 
	LIGHT(java.awt.font.TextAttribute.WEIGHT_LIGHT), 
	DEMILIGHT(java.awt.font.TextAttribute.WEIGHT_DEMILIGHT), 
	REGULAR(java.awt.font.TextAttribute.WEIGHT_REGULAR), 
	SEMIBOLD(java.awt.font.TextAttribute.WEIGHT_SEMIBOLD), 
	MEDIUM(java.awt.font.TextAttribute.WEIGHT_MEDIUM), 
	DEMIBOLD(java.awt.font.TextAttribute.WEIGHT_DEMIBOLD), 
	BOLD(java.awt.font.TextAttribute.WEIGHT_BOLD), 
	HEAVY(java.awt.font.TextAttribute.WEIGHT_HEAVY), 
	EXTRABOLD(java.awt.font.TextAttribute.WEIGHT_EXTRABOLD), 
	ULTRABOLD(java.awt.font.TextAttribute.WEIGHT_ULTRABOLD);
	private Float m_value;

	private ZTextWeight( Float value ) {
		m_value = value;
	}
	public java.awt.font.TextAttribute getKey() {
		return java.awt.font.TextAttribute.WEIGHT;
	}
	public Float getValue() {
		return m_value;
	}
}
