/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may
 *    "Alice" appear in their name, without prior written permission of
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is
 *    contributed by Electronic Arts Inc. and may be used for personal,
 *    non-commercial, and academic use only. Redistributions of any program
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in
 *    The Alice 3.0 Art Gallery License.
 *
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package edu.cmu.cs.dennisc.pathfinding;

/**
 * @author Dennis Cosgrove
 */
public class Cell {
	public static final Cell OUT_OF_BOUNDS_CELL = new Cell( -1, -1 );
	private int m_row;
	private int m_column;

	private boolean m_isOccupied;

	private Cell m_parent = null;

	Cell() {
	}

	Cell( int row, int column ) {
		setRow( row );
		setColumn( column );
	}

	@Override
	public boolean equals( Object other ) {
		if( other instanceof Cell ) {
			Cell otherCell = (Cell)other;
			return isLocatedAt( otherCell.m_row, otherCell.m_column );
		} else {
			return super.equals( other );
		}
	}

	public boolean isLocatedAt( int row, int column ) {
		return ( m_row == row ) && ( m_column == column );
	}

	public int getRow() {
		if( isOutOfBounds() ) {
			throw new RuntimeException();
		}
		return m_row;
	}

	public int getColumn() {
		if( isOutOfBounds() ) {
			throw new RuntimeException();
		}
		return m_column;
	}

	private void setRow( int row ) {
		m_row = row;
	}

	private void setColumn( int column ) {
		m_column = column;
	}

	public boolean isOutOfBounds() {
		return ( m_row < 0 ) || ( m_column < 0 );
	}

	public boolean isOccupied() {
		if( isOutOfBounds() ) {
			throw new RuntimeException();
		}
		return m_isOccupied;
	}

	public void setOccupied( boolean isOccupied ) {
		if( isOutOfBounds() ) {
			throw new RuntimeException();
		}
		m_isOccupied = isOccupied;
	}

	public Cell getParent() {
		if( isOutOfBounds() ) {
			throw new RuntimeException();
		}
		return m_parent;
	}

	public void setParent( Cell parent ) {
		if( isOutOfBounds() ) {
			throw new RuntimeException();
		}
		if( parent == this ) {
			throw new RuntimeException();
		}
		m_parent = parent;
	}

	public int getGToNeighbor( Cell neighbor ) {
		if( isOutOfBounds() ) {
			throw new RuntimeException();
		}
		if( m_row == neighbor.m_row ) {
			return 10;
		} else {
			if( m_column == neighbor.m_column ) {
				return 10;
			} else {
				return 14;
			}
		}
	}

	public int getG() {
		if( isOutOfBounds() ) {
			throw new RuntimeException();
		}
		if( m_parent != null ) {
			return getGToNeighbor( m_parent ) + m_parent.getG();
		} else {
			return 0;
		}
	}

	public int getH( Cell dst ) {
		if( isOutOfBounds() ) {
			throw new RuntimeException();
		}
		if( false ) {
			//dijkstra's
			return 0;
		} else {
			int rowCount = Math.abs( m_row - dst.m_row );
			int columnCount = Math.abs( m_column - dst.m_column );
			if( false ) {
				//manhattan distance
				return ( rowCount + columnCount ) * 10;
			} else {
				int max = Math.max( rowCount, columnCount );
				int min = Math.min( rowCount, columnCount );
				return ( max * 10 ) + ( min * 4 );
			}
		}
	}

	@Override
	public String toString() {
		return "[ " + m_row + ", " + m_column + " ]";
	}
}
