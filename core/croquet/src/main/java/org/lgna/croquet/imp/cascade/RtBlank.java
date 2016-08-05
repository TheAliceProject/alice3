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

package org.lgna.croquet.imp.cascade;

import org.lgna.croquet.AbstractCascadeMenuModel;
import org.lgna.croquet.CascadeBlank;
import org.lgna.croquet.CascadeBlankChild;
import org.lgna.croquet.CascadeCancel;
import org.lgna.croquet.CascadeFillIn;
import org.lgna.croquet.CascadeItem;
import org.lgna.croquet.CascadeSeparator;
import org.lgna.croquet.CascadeUnfilledInCancel;

/**
 * @author Dennis Cosgrove
 */
class RtBlank<B> extends RtNode<CascadeBlank<B>, org.lgna.croquet.imp.cascade.BlankNode<B>> {
	public static class ItemChildrenAndComboOffsetsPair {
		private final RtItem[] rtItems;
		private final java.util.List<Integer> comboOffsets;

		public ItemChildrenAndComboOffsetsPair( java.util.List<RtItem> baseRtItems, java.util.List<Integer> comboOffsets ) {
			this.rtItems = edu.cmu.cs.dennisc.java.lang.ArrayUtilities.createArray( baseRtItems, RtItem.class );
			if( comboOffsets.size() > 0 ) {
				this.comboOffsets = comboOffsets;
			} else {
				this.comboOffsets = null;
			}
		}

		public RtItem[] getItemChildren() {
			return this.rtItems;
		}

		public boolean isComboOffset( int index ) {
			return this.comboOffsets != null ? this.comboOffsets.contains( index ) : false;
		}
	}

	private ItemChildrenAndComboOffsetsPair itemChildrenAndComboOffsetsPair;

	private boolean isAutomaticallyDetermined;
	private RtItem<B, ?, ?, ?> rtSelectedFillIn;

	public RtBlank( CascadeBlank<B> element ) {
		super( element, BlankNode.createInstance( element ) );
		this.getNode().setRtBlank( this );
	}

	@Override
	public RtBlank<?> getNearestBlank() {
		return this;
	}

	public boolean isAutomaticallyDetermined() {
		this.getItemChildrenAndComboOffsets();
		return this.isAutomaticallyDetermined;
	}

	public org.lgna.croquet.imp.cascade.AbstractItemNode getSelectedFillInNode() {
		if( this.rtSelectedFillIn != null ) {
			return this.rtSelectedFillIn.getNode();
		} else {
			return null;
		}
	}

	private RtFillIn getOneAndOnlyOneFillInIfAppropriate() {
		RtFillIn rv = null;
		RtItem[] children = this.getItemChildrenAndComboOffsets().rtItems;
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

	protected ItemChildrenAndComboOffsetsPair getItemChildrenAndComboOffsets() {
		if( this.itemChildrenAndComboOffsetsPair != null ) {
			//pass
		} else {
			java.util.List<Integer> comboOffsets = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
			java.util.List<RtItem> baseRtItems = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
			for( CascadeBlankChild blankChild : this.getElement().getFilteredChildren( this.getNode() ) ) {
				final int N = blankChild.getItemCount();
				switch( N ) {
				case 1:
					break;
				case 2:
					comboOffsets.add( baseRtItems.size() );
					break;
				default:
					edu.cmu.cs.dennisc.java.util.logging.Logger.severe( N, blankChild );
				}
				for( int i = 0; i < N; i++ ) {
					CascadeItem item = blankChild.getItemAt( i );
					RtItem rtItem;
					if( item instanceof AbstractCascadeMenuModel ) {
						AbstractCascadeMenuModel menu = (AbstractCascadeMenuModel)item;
						rtItem = new RtMenu( menu, blankChild, i );
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

			boolean isDevoidOfNonSeparators = true;
			for( RtItem rtItem : baseRtItems ) {
				if( rtItem instanceof RtSeparator ) {
					//pass
				} else {
					isDevoidOfNonSeparators = false;
				}
			}
			if( isDevoidOfNonSeparators ) {
				baseRtItems.add( new RtCancel( CascadeUnfilledInCancel.getInstance(), null, -1 ) );
			}

			this.itemChildrenAndComboOffsetsPair = new ItemChildrenAndComboOffsetsPair( baseRtItems, comboOffsets );

			this.updateParentsAndNextSiblings( this.itemChildrenAndComboOffsetsPair.rtItems );

			RtFillIn rtFillIn = this.getOneAndOnlyOneFillInIfAppropriate();
			if( ( rtFillIn != null ) && rtFillIn.isAutomaticallySelectedWhenSoleOption() ) {
				this.rtSelectedFillIn = rtFillIn;
				this.isAutomaticallyDetermined = true;
			} else {
				this.isAutomaticallyDetermined = false;
			}
		}
		return this.itemChildrenAndComboOffsetsPair;
	}

	public void setSelectedFillIn( RtItem<B, ?, ?, ?> item ) {
		this.rtSelectedFillIn = item;
		RtNode parent = this.getParent();
		if( parent instanceof RtFillIn<?, ?> ) {
			RtFillIn<?, ?> parentFillIn = (RtFillIn<?, ?>)parent;
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

	public B createValue( org.lgna.croquet.history.TransactionHistory transactionHistory ) {
		if( this.rtSelectedFillIn != null ) {
			return this.rtSelectedFillIn.createValue( transactionHistory );
		} else {
			throw new RuntimeException();
		}
	}
}
