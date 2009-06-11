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
package cascade;

/**
 * @author Dennis Cosgrove
 */
public class CancelFillIn extends FillIn< Object > {
	private zoot.ZLabel label = zoot.ZLabel.acquire();
	public CancelFillIn( String text ) {
		this.label.setText( text );
	}
	@Override
	protected void addChildren() {
	}
	@Override
	public final Object getValue() {
		throw new CancelException( this.label.getText() );
	}
	@Override
	public final Object getTransientValue() {
		return null;
	}
	@Override
	protected javax.swing.JComponent createMenuProxy() {
		return this.label;
	}
}
