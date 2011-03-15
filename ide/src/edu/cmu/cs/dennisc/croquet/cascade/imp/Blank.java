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

package edu.cmu.cs.dennisc.croquet.cascade.imp;

/**
 * @author Dennis Cosgrove
 */
public class Blank extends Node {
	private final edu.cmu.cs.dennisc.croquet.CascadeBlankContext context;
	public Blank( edu.cmu.cs.dennisc.croquet.CascadeBlankContext context ) {
		this.context = context;
	}
	@Override
	protected edu.cmu.cs.dennisc.croquet.CascadeBlankContext getContext() {
		return this.context;
	}
//	public CascadingRoot getCascadingRoot() {
//		CascadingRoot rv;
//		if( this.cascadingRoot != null ) {
//			rv = this.cascadingRoot;
//		} else {
//			if( this.cascadingRootResolver != null ) {
//				rv = this.cascadingRootResolver.getResolved(); 
//				if( this.cascadingRootResolver instanceof RetargetableResolver< ? > ) {
//					//pass
//				} else {
//					this.cascadingRoot = rv;
//				}
//			} else {
//				rv = null;
//			}
//		}
//		return rv;
//	}
//	public void setCascadingRoot( CascadingRoot cascadingRoot ) {
//		this.cascadingRoot = cascadingRoot;
//		if( this.cascadingRoot != null ) {
//			this.cascadingRootResolver = this.cascadingRoot.getCodableResolver();
//		} else {
//			this.cascadingRootResolver = null;
//		}
//	}
//
//	public String getTitle() {
//		return this.title;
//	}
//	public void setTitle(String title) {
//		this.title = title;
//	}
//		
//	@Override
//	protected void addPrefixChildren() {
//		if( this.title != null ) {
//			this.addFillIn( new SeparatorFillIn( this.title ) );
//			this.addFillIn( new SeparatorFillIn() );
//		}
//	}
//	
//	
//	public FillIn<?> getFillInAt( int index ) {
//		return (FillIn<?>)getChildren().get( index );
//	}
//	public FillIn<?> getParentFillIn() {
//		return (FillIn<?>)getParent();
//	}
//	
//	public void addFillIn( FillIn<?> fillIn ) {
//		super.addChild( fillIn );
//	}
//	
//	@Override
//	protected Blank getNearestBlank() {
//		return this;
//	}
//	@Override
//	protected Node getNextNode() {
//		return this;
//	}
//	public FillIn<?> getSelectedFillIn() {
//		if( this.selectedFillIn != null ) {
//			//pass
//		} else {
//			if( this.getNonSeparatorFillInCount() == 1 ) {
//				FillIn<?> fillIn = this.getFirstNonSeparatorFillIn();
//				if( fillIn instanceof AutoCompleteFillIn< ? > ) {
//					return fillIn;
//				}
//			}
//		}
// 		return this.selectedFillIn;
//	}
//	public void setSelectedFillIn( FillIn<?> fillIn ) {
//		this.selectedFillIn = fillIn;
//		Node parent = this.getParent();
//		if( parent instanceof FillIn<?> ) {
//			FillIn<?> parentFillIn = (FillIn<?>)parent;
//			for( Node child : parent.getChildren() ) {
//				Blank blank = (Blank)child;
//				if( blank.selectedFillIn != null ) {
//					//pass
//				} else {
//					return;
//				}
//			}
//			parentFillIn.select();
//		}
//	}
//	
//	private static boolean isEmptySeparator( Node node ) {
//		 if( node instanceof SeparatorFillIn ) {
//			 SeparatorFillIn separatorFillIn = (SeparatorFillIn)node;
//			 return separatorFillIn.getName() == null && separatorFillIn.getIcon() == null;
//		 } else {
//			 return false;
//		 }
//	}
//	@Override
//	protected void cleanUp() {
//		 java.util.ListIterator< Node > listIterator = this.children.listIterator();
//		 boolean isSeparatorAcceptable = false;
//		 while( listIterator.hasNext() ) {
//			 Node node = listIterator.next();
//			 if( isEmptySeparator( node ) ) {
//				 if( isSeparatorAcceptable ) {
//					//pass 
//				 } else {
//					 listIterator.remove();
//				 }
//				 isSeparatorAcceptable = false;
//			 } else {
//				 isSeparatorAcceptable = true;
//			 }
//		 }
//		 
//		 //remove separators at the end (should be a maximum of only 1)
//		 final int N = this.children.size();
//		 for( int i=0; i<N; i++ ) {
//			 int index = N-i-1;
//			 if( isEmptySeparator( this.children.get( index ) ) ) {
//				 this.children.remove( index );
//			 } else {
//				 break;
//			 }
//		 }
//	}
//
//	public void addSeparator() {
//		this.addFillIn( new SeparatorFillIn() );
//	}
//	
//	protected java.util.List< edu.cmu.cs.dennisc.croquet.Model > getModels() {
//		java.util.List< edu.cmu.cs.dennisc.croquet.Model > rv = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
//		for( Node child : this.getNextNode().getChildren() ) {
////			if( child instanceof Blank ) {
////				edu.cmu.cs.dennisc.print.PrintUtilities.println( "blank", child );
////				child = child.getChildren().get( 0 );
////			}
//			
//			rv.add( ((FillIn)child).getCroquetModel() );
//		}	
//		return rv;
//	}
//	
////	public void addSeparator() {
////		this.addSeparator( null );
////	}
////	public void addSeparator( String text ) {
////		this.addFillIn( new SeparatorFillIn( text ) );
////	}
//
//	public int getNonSeparatorFillInCount() {
//		int rv = 0;
//		for( Node child : this.getChildren() ) {
//			if( child instanceof SeparatorFillIn ) {
//				//pass
//			} else {
//				rv += 1;
//			}
//		}
//		return rv;
//	}
//	public FillIn<?> getFirstNonSeparatorFillIn() {
//		for( Node child : this.getChildren() ) {
//			if( child instanceof SeparatorFillIn ) {
//				//pass
//			} else {
//				return (FillIn<?>)child;
//			}
//		}
//		return null;
//	}
}
