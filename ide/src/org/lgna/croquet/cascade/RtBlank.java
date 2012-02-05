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

package org.lgna.croquet.cascade;

import org.lgna.croquet.*;

/**
 * @author Dennis Cosgrove
 */
class RtBlank<B> extends RtNode< CascadeBlank< B >, org.lgna.croquet.cascade.BlankNode< B > > {
	private static boolean isDevoidOfNonSeparators( java.util.List< RtItem > rtItems ) {
		for( RtItem rtItem : rtItems ) {
			if( rtItem instanceof RtSeparator ) {
				//pass
			} else {
				return false;
			}
		}
		return true;
	}
	
	private RtItem[] rtItems;
	private RtItem< B, ?, ?, ? > rtSelectedFillIn;

	public RtBlank( CascadeBlank< B > element ) {
		super( element, BlankNode.createInstance( element ) );
		this.getNode().setRtBlank( this );
	}


	public org.lgna.croquet.cascade.AbstractItemNode getSelectedFillInContext() {
		if( this.rtSelectedFillIn != null ) {
			return this.rtSelectedFillIn.getNode();
		} else {
			return null;
		}
	}

	protected RtItem[] getItemChildren() {
		if( this.rtItems != null ) {
			//pass
		} else {
			java.util.List< RtItem > baseRtItems = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
			for( CascadeBlankChild blankChild : this.getElement().getFilteredChildren( this.getNode() ) ) {
				final int N = blankChild.getItemCount();
				for( int i = 0; i < N; i++ ) {
					CascadeItem item = blankChild.getItemAt( i );
					RtItem rtItem;
					if( item instanceof CascadeMenuModel ) {
						CascadeMenuModel menu = (CascadeMenuModel)item;
						rtItem = new RtMenu< B >( menu, blankChild, i );
					} else if( item instanceof CascadeFillIn ) {
						CascadeFillIn fillIn = (CascadeFillIn)item;
						rtItem = new RtFillIn( fillIn, blankChild, i );
						//					} else if( item instanceof CascadeRoot ) {
						//						CascadeRoot root = (CascadeRoot)item;
						//						rtItem = new RtRoot( root );
					} else if( item instanceof CascadeSeparator ) {
						CascadeSeparator separator = (CascadeSeparator)item;
						rtItem = new RtSeparator( separator, blankChild, i );
					} else if( item instanceof CascadeCancel ) {
						CascadeCancel cancel = (CascadeCancel)item;
						rtItem = new RtCancel( cancel, blankChild, i );
					} else {
						rtItem = null;
					}
					baseRtItems.add( rtItem );
				}
			}

			if( isDevoidOfNonSeparators( baseRtItems ) ) {
				baseRtItems.add( new RtCancel( CascadeUnfilledInCancel.getInstance(), null, -1 ) );
			}

			this.rtItems = edu.cmu.cs.dennisc.java.lang.ArrayUtilities.createArray( baseRtItems, RtItem.class );
			this.updateParentsAndNextSiblings( this.rtItems );
		}
		return this.rtItems;
	}
//	@Override
//	protected RtNode< ? extends Element, ? extends org.lgna.croquet.cascade.CascadeNode< ?, ? > > getNextNode() {
//		return this;
//	}
	@Override
	public RtBlank< ? > getNearestBlank() {
		return this;
	}

	public void setSelectedFillIn( RtItem< B, ?, ?, ? > item ) {
		this.rtSelectedFillIn = item;
		RtNode parent = this.getParent();
		if( parent instanceof RtFillIn< ?, ? > ) {
			RtFillIn< ?, ? > parentFillIn = (RtFillIn< ?, ? >)parent;
			for( RtBlank blank : parentFillIn.getBlankChildren() ) {
				if( blank.rtSelectedFillIn != null ) {
					//pass
				} else {
					return;
				}
			}
			parentFillIn.select();
		}
	}

	private RtFillIn getOneAndOnlyOneFillInIfAppropriate() {
		RtFillIn rv = null;
		RtItem[] children = this.getItemChildren();
		for( RtItem child : children ) {
			if( child instanceof RtFillIn ) {
				if( rv != null ) {
					return null;
				} else {
					rv = (RtFillIn)child;
				}
			} else if( child instanceof RtCancel ) {
				return null;
			} else if( child instanceof RtMenu ) {
				return null;
			} else if( child instanceof RtRoot ) {
				//??
				return null;
			} else if( child instanceof RtSeparator ) {
				//pass
			} else {
				edu.cmu.cs.dennisc.java.util.logging.Logger.severe( "unhandled child", child );
				return null;
			}
		}
		return rv;
	}

	public boolean isFillInAlreadyDetermined() {
		RtFillIn rtFillIn = this.getOneAndOnlyOneFillInIfAppropriate();
		if( rtFillIn != null && rtFillIn.isAutomaticallySelectedWhenSoleOption() ) {
			this.rtSelectedFillIn = rtFillIn;
		}
		return this.rtSelectedFillIn != null;
	}

	public B createValue() {
		if( this.rtSelectedFillIn != null ) {
			return this.rtSelectedFillIn.createValue();
		} else {
			throw new RuntimeException();
		}
	}
}
