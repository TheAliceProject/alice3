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
package edu.cmu.cs.dennisc.alan;

/**
 * @author Dennis Cosgrove
 */
public class HorizontalFlowLayoutManager implements LayoutManager {
	public enum Direction {
		LEFT_TO_RIGHT,
		RIGHT_TO_LEFT;
	}
	private int m_indent;
//	private Direction m_direction = Direction.LEFT_TO_RIGHT;
	public HorizontalFlowLayoutManager() {
		this( 0 );
	}
	public HorizontalFlowLayoutManager( int indent ) {
		m_indent = indent;
	}
	
//	public Direction getDirection() {
//		return m_direction;
//	}
//	public void setDirection( Direction direction ) {
//		m_direction = direction;
//	}
	
	public int getIndent() {
		return m_indent;
	}
	public void setIndent( int indent ) {
		m_indent = indent;
	}

	public void layout( Iterable< View > children ) {
//		if( m_direction == Direction.LEFT_TO_RIGHT ) {
			float xPrev = m_indent;
			for( View child : children ) {
				child.updateLocalBounds();
				child.setTranslation( xPrev - child.m_localBounds.x, 0 );
				xPrev = child.getXMax( true );
				xPrev += 8;
			}
//		} else {
//			throw new RuntimeException( "todo" );
//		}
	}
}
