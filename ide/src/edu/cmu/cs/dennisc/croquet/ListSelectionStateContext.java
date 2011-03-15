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
package edu.cmu.cs.dennisc.croquet;

/**
 * @author Dennis Cosgrove
 */
public class ListSelectionStateContext< T > extends AbstractModelContext<ListSelectionState<T>> {
	public static class PopupMenuWillBecomeVisibleEvent<T> extends ModelEvent< ListSelectionStateContext< T > > {
		private javax.swing.event.PopupMenuEvent popupMenuEvent;
		public PopupMenuWillBecomeVisibleEvent( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
			super( binaryDecoder );
		}
		private PopupMenuWillBecomeVisibleEvent( javax.swing.event.PopupMenuEvent popupMenuEvent ) {
			this.popupMenuEvent = popupMenuEvent;
		}
		@Override
		protected void decodeInternal( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		}
		@Override
		protected void encodeInternal( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
		}
		@Override
		public State getState() {
			return null;
		}
	}
	//private int prevIndex;
	//private T prevItem;
	//private int nextIndex;
	//private T nextItem;
	/*package-private*/ ListSelectionStateContext( ListSelectionState< T > itemSelectionState, java.util.EventObject e, ViewController< ?,? > viewController/*, int prevIndex, T prevItem, int nextIndex, T nextItem*/ ) {
		super( itemSelectionState, e, viewController );
		//this.prevIndex = prevIndex;
		//this.prevItem = prevItem;
		//this.nextIndex = nextIndex;
		//this.nextItem = nextItem;
	}
	public ListSelectionStateContext( ListSelectionState< T > itemSelectionState ) {
		this( itemSelectionState, null, null );
	}
	public ListSelectionStateContext( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		super( binaryDecoder );
	}
	/*package-private*/ void handlePopupMenuWillBecomeVisibleEvent( javax.swing.event.PopupMenuEvent e ) {
		this.addChild( new PopupMenuWillBecomeVisibleEvent( e ) );
	}
	@Override
	protected StringBuilder appendRepr( StringBuilder rv ) {
		super.appendRepr( rv );
		rv.append( " [" );
		rv.append( this.getModel().getSelectedItem() );
		rv.append( "]" );
		return rv;
	}
}
