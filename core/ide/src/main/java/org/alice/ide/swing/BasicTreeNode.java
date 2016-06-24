/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
 *******************************************************************************/

package org.alice.ide.swing;

import java.awt.Color;

import javax.swing.tree.DefaultMutableTreeNode;

import edu.cmu.cs.dennisc.color.Color4f;

/*package-private*/class BasicTreeNode extends DefaultMutableTreeNode implements Comparable {

	public static enum Difference
	{
		NONE,
		NEW_NODE,
		ATTRIBUTES
	}

	public Difference difference = Difference.NONE;

	public String name;
	public String className;
	private String trimmedClassName;
	public int hashCode;
	public Color4f color = null;

	public boolean hasExtras = false;

	@Override
	public boolean equals( Object obj )
	{
		if( obj instanceof BasicTreeNode )
		{
			return this.hashCode == ( (BasicTreeNode)obj ).hashCode;
		}
		return super.equals( obj );
	}

	protected void setData( Object object )
	{
		this.difference = Difference.NONE;
		this.className = object.getClass().getName();
		this.hashCode = object.hashCode();
		String[] splitClassName = this.className.split( "\\." );
		if( splitClassName.length > 0 )
		{
			this.trimmedClassName = splitClassName[ splitClassName.length - 1 ];
			this.name = this.trimmedClassName + ":" + this.hashCode;
		}
	}

	public BasicTreeNode( Object object )
	{
		super();
		setData( object );
	}

	public boolean hasDifferentChild()
	{
		for( int i = 0; i < this.getChildCount(); i++ )
		{
			BasicTreeNode child = (BasicTreeNode)this.getChildAt( i );
			if( child.isDifferent() )
			{
				return true;
			}
			else
			{
				if( child.hasDifferentChild() )
				{
					return true;
				}
			}
		}
		return false;
	}

	public Color getAWTColor()
	{
		if( this.color == null )
		{
			return null;
		}
		return new Color( (int)( this.color.red * 255 ), (int)( this.color.green * 255 ), (int)( this.color.blue * 255 ) );
	}

	public boolean isDifferent()
	{
		return this.difference != Difference.NONE;
	}

	public void markDifferent( Difference difference )
	{
		this.difference = difference;
	}

	@Override
	public String toString()
	{
		if( ( this.name == null ) || ( this.name.length() == 0 ) )
		{
			return this.trimmedClassName;
		}
		return this.name;
	}

	public BasicTreeNode getMatchingNode( int hashCode )
	{
		if( this.hashCode == hashCode )
		{
			return this;
		}
		else
		{
			for( int i = 0; i < this.getChildCount(); i++ )
			{
				BasicTreeNode child = (BasicTreeNode)this.getChildAt( i );
				BasicTreeNode found = child.getMatchingNode( hashCode );
				if( found != null )
				{
					return found;
				}
			}
			return null;
		}
	}

	public BasicTreeNode getMatchingNode( BasicTreeNode toMatch )
	{
		if( this.compareTo( toMatch ) == 0 )
		{
			return this;
		}
		else
		{
			for( int i = 0; i < this.getChildCount(); i++ )
			{
				BasicTreeNode child = (BasicTreeNode)this.getChildAt( i );
				BasicTreeNode found = child.getMatchingNode( toMatch );
				if( found != null )
				{
					return found;
				}
			}
			return null;
		}
	}

	public boolean isDifferent( BasicTreeNode other )
	{
		if( other.hashCode != this.hashCode )
		{
			return true;
		}
		return false;
	}

	@Override
	public int compareTo( Object o ) {
		if( o instanceof BasicTreeNode )
		{
			BasicTreeNode other = (BasicTreeNode)o;
			if( this.hashCode < other.hashCode )
			{
				return -1;
			}
			else if( this.hashCode == other.hashCode )
			{
				return 0;
			}
			else
			{
				return 1;
			}
		}
		return 0;
	}
}
