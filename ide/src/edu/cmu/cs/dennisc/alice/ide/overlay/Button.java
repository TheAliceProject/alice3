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
package edu.cmu.cs.dennisc.alice.ide.overlay;

/**
 * @author Dennis Cosgrove
 */
public class Button extends edu.cmu.cs.dennisc.lookingglass.overlay.AbstractButton {
	public Button(edu.cmu.cs.dennisc.alice.ide.Operation operation) {
		this( operation, new edu.cmu.cs.dennisc.lookingglass.overlay.DefaultButtonRenderer( (String)operation.getActionForConfiguringSwingComponents().getValue( javax.swing.Action.NAME ) ) );
	}
	public Button(edu.cmu.cs.dennisc.alice.ide.Operation operation, edu.cmu.cs.dennisc.lookingglass.overlay.Renderer renderer ) {
		this.addActionListener( operation.getActionForConfiguringSwingComponents() );
		this.setRenderer( renderer );
	}
	@Override
	public void setHighlighted( boolean isHighlighted ) {
		super.setHighlighted( isHighlighted );
		float opacity;
		if( isHighlighted ) {
			opacity = 1.0f;
		} else {
			opacity = 0.5f;
		}
		this.setOpacity( opacity );
	}
}
