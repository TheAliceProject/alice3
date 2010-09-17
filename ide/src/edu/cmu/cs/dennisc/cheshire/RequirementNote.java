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
package edu.cmu.cs.dennisc.cheshire;

/**
 * @author Dennis Cosgrove
 */
public class RequirementNote extends RetargetableNote /* implements ParentContextCriterion */ {
	private java.util.List< ProgressRequirement > progressRequirements = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
	private int unfulfilledRequirementIndex;
	
	private java.util.List< edu.cmu.cs.dennisc.croquet.HistoryNode > nodes = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
	private int checkIndex = -1;
	public RequirementNote( ProgressRequirement... requirements ) {
		edu.cmu.cs.dennisc.java.util.Collections.addAll( this.progressRequirements, requirements );
		for( int i=0; i<this.progressRequirements.size(); i++ ) {
			this.nodes.add( null );
		}
	}
//	public void setCheckIndex( int checkIndex ) {
//		this.checkIndex = checkIndex;
//	}
	public void addRequirement( ProgressRequirement requirement ) {
		this.progressRequirements.add( requirement );
		this.nodes.add( null );
	}
	@Override
	public void reset() {
		super.reset();
		this.unfulfilledRequirementIndex = 0;
		for( int i=0; i<this.nodes.size(); i++ ) {
			this.nodes.set( i, null );
		}
		for( ProgressRequirement progressRequirement : this.progressRequirements ) {
			progressRequirement.reset();
		}
	}
	
	public ParentContextCriterion getAcceptedContextAt( final int checkIndex ) {
		return new ParentContextCriterion() {
			private int calculateActualIndex() {
				final int N = RequirementNote.this.progressRequirements.size();
				int actualIndex = checkIndex;
				if( actualIndex < 0 ) {
					actualIndex += N;
				}
				return actualIndex;
			}
			public boolean isAcceptableParentContext( edu.cmu.cs.dennisc.croquet.ModelContext< ? > parentContext ) {
				boolean rv;
				int actualIndex = this.calculateActualIndex();
				edu.cmu.cs.dennisc.croquet.HistoryNode checkNode = RequirementNote.this.nodes.get( actualIndex );
				if( checkNode != null ) {
					if( checkNode instanceof edu.cmu.cs.dennisc.croquet.ModelContext ) {
						rv = checkNode == parentContext;
					} else {
						rv = checkNode.getParent() == parentContext;
					}
				} else {
					throw new NullPointerException();
				}
				return rv;
			}
			@Override
			public String toString() {
				int actualIndex = this.calculateActualIndex();
				StringBuilder sb = new StringBuilder();
				sb.append( RequirementNote.this );
				sb.append( "\n\t\tindex: " );
				sb.append( actualIndex );
				sb.append( "\n\t\tnode: " );
				sb.append(  RequirementNote.this.nodes.get( actualIndex ) );
				return sb.toString();
			}
		};
	}
	public ParentContextCriterion getLastAcceptedContext() {
		return this.getAcceptedContextAt( -1 );
	}
	
	@Override
	public edu.cmu.cs.dennisc.croquet.ReplacementAcceptability getReplacementAcceptability() {
		edu.cmu.cs.dennisc.croquet.ReplacementAcceptability rv = null;
		for( ProgressRequirement progressRequirement : this.progressRequirements ) {
			edu.cmu.cs.dennisc.croquet.ReplacementAcceptability replacementAcceptability = progressRequirement.getReplacementAcceptability();
			if( replacementAcceptability != null ) {
				//last one wins
				rv = replacementAcceptability;
			}
		}
		return rv;
	}
	
	protected void handleCancel() {
		this.reset();
	}
	
	@Override
	public final boolean isWhatWeveBeenWaitingFor( edu.cmu.cs.dennisc.croquet.HistoryNode child ) throws CancelException {
		if( child instanceof edu.cmu.cs.dennisc.croquet.AbstractDialogOperationContext.WindowOpenedEvent ) {
			edu.cmu.cs.dennisc.croquet.AbstractDialogOperationContext.WindowOpenedEvent windowOpenedEvent = (edu.cmu.cs.dennisc.croquet.AbstractDialogOperationContext.WindowOpenedEvent)child;
			edu.cmu.cs.dennisc.croquet.AbstractDialogOperationContext<?> dialogOperationContext = windowOpenedEvent.getParent();
			edu.cmu.cs.dennisc.croquet.Dialog dialog = dialogOperationContext.getModel().getActiveDialog();
			if( dialog != null ) {
				java.awt.Rectangle dialogLocalBounds = dialog.getLocalBounds();
				java.awt.Rectangle bounds = this.getBounds( dialog );
				if( bounds.intersects( dialogLocalBounds ) ) {
					this.setLocation( dialog.getWidth()+100, dialog.getHeight()/2, dialog );
				}
			}
		} else if( child instanceof edu.cmu.cs.dennisc.croquet.PopupMenuOperationContext.MenuResizedEvent ) {
			edu.cmu.cs.dennisc.croquet.PopupMenuOperationContext.MenuResizedEvent menuResizedEvent = (edu.cmu.cs.dennisc.croquet.PopupMenuOperationContext.MenuResizedEvent)child;
			java.awt.event.ComponentEvent e = menuResizedEvent.getComponentEvent();
			if( e != null ) {
				java.awt.Component awtComponent = e.getComponent();
				edu.cmu.cs.dennisc.croquet.Component< ? > popUp = edu.cmu.cs.dennisc.croquet.Component.lookup( awtComponent );
				if( popUp != null ) {
					java.awt.Rectangle dialogLocalBounds = popUp.getLocalBounds();
					java.awt.Rectangle bounds = this.getBounds( popUp );
					if( bounds.intersects( dialogLocalBounds ) ) {
						this.setLocation( popUp.getWidth()+100, popUp.getHeight()/2, popUp );
					}
				}
			}
		}
		
//		if( child instanceof edu.cmu.cs.dennisc.croquet.CancelEvent ) {
//			throw new CancelException( "cancel event" );
//		}

		
//			System.err.println( "isWhatWeveBeenWaitingFor? " + child );
//			edu.cmu.cs.dennisc.print.PrintUtilities.println( "isWhatWeveBeenWaitingFor", child );
//			edu.cmu.cs.dennisc.print.PrintUtilities.println( "isWhatWeveBeenWaitingFor", this.unfulfilledRequirementIndex );
		final int N = this.progressRequirements.size();
		while( this.unfulfilledRequirementIndex<N ) {
			ProgressRequirement requirement = this.progressRequirements.get( this.unfulfilledRequirementIndex );
//				System.err.println( "checking requirement at index: " + this.unfulfilledRequirementIndex + "; length= " + N );
//				System.err.println( requirement );
			if( requirement.isWhatWereLookingFor( child ) ) {
//					System.err.println( "SUCCESS" );
				this.nodes.set( this.unfulfilledRequirementIndex, child );
				this.unfulfilledRequirementIndex += 1;
			} else {
				break;
			}
		}
//			edu.cmu.cs.dennisc.print.PrintUtilities.println( "isWhatWeveBeenWaitingFor", this.unfulfilledRequirementIndex );
//			System.err.println( this.unfulfilledRequirementIndex == N );
		return this.unfulfilledRequirementIndex == N;
	}
//	public boolean isAcceptableParentContext( edu.cmu.cs.dennisc.croquet.ModelContext< ? > parentContext ) {
//		boolean rv;
//		final int N = this.requirements.size();
//		int actualIndex = this.checkIndex;
//		if( actualIndex < 0 ) {
//			actualIndex += N;
//		}
//		edu.cmu.cs.dennisc.croquet.HistoryNode checkNode = this.nodes.get( actualIndex );
//		if( checkNode != null ) {
//			if( checkNode instanceof edu.cmu.cs.dennisc.croquet.ModelContext ) {
//				rv = checkNode == parentContext;
//			} else {
//				rv = checkNode.getParent() == parentContext;
//			}
//		} else {
//			throw new NullPointerException();
//		}
//		
////		if( rv ) {
////			//pass
////		} else {
////			for( int i=0; i<N; i++ ) {
////				edu.cmu.cs.dennisc.croquet.HistoryNode node = this.nodes.get( i );
////				edu.cmu.cs.dennisc.print.PrintUtilities.println( "isAcceptableParentContext:", i, N, node != null ? node.hashCode() : 0, node );
////			}
////			edu.cmu.cs.dennisc.print.PrintUtilities.println( "isAcceptableParentContext: parentCx", parentContext.hashCode(), parentContext );
////			edu.cmu.cs.dennisc.print.PrintUtilities.println( "isAcceptableParentContext: value", checkNode == parentContext );
////		}
//		return rv;
//	}
}
