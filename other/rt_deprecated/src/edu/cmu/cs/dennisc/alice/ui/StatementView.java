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
package edu.cmu.cs.dennisc.alice.ui;

/**
 * @author Dennis Cosgrove
 */
public abstract class StatementView extends edu.cmu.cs.dennisc.alan.DragSourceView {
	private Spacer m_preSpacer = new Spacer();
	private Spacer m_postSpacer = new Spacer();
	
	public StatementView() {
		setLayoutManager( new edu.cmu.cs.dennisc.alan.VerticalFlowLayoutManager() );
	}
	
	public Spacer getPreSpacer() {
		return m_preSpacer;
	}
	public Spacer getPostSpacer() {
		return m_postSpacer;
	}
	@Override
	protected void setChildrenHighlighted( boolean isHighlighted, java.awt.event.MouseEvent mouseEvent ) {
	}
	
	@Override
	protected void paint( java.awt.Graphics2D g2, float x, float y, float width, float height ) {
		float preSpacerHeight = m_preSpacer.getCurrentHeight();
		float postSpacerHeight = m_postSpacer.getCurrentHeight();
		
		float actualHeight = height - preSpacerHeight - postSpacerHeight;
		if( actualHeight != height ) {
			g2.translate( 0.0f, preSpacerHeight );
			g2.scale( 1.0f, actualHeight/height );
		}
		super.paint( g2, x, y, width, height );
		if( actualHeight != height ) {
			g2.scale( 1.0f, height/actualHeight );
			g2.translate( 0.0f, -preSpacerHeight );
		}
//		g2.setColor( java.awt.Color.BLACK );
//		g2.drawRect( (int)x, (int)y, (int)width, (int)height );
	}
}
