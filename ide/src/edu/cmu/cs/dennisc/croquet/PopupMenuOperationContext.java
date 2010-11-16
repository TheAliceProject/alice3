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
public class PopupMenuOperationContext extends OperationContext<PopupMenuOperation> {
	/*package-private*/ PopupMenuOperationContext( PopupMenuOperation popupMenuOperation, java.util.EventObject e, ViewController< ?,? > viewController ) {
		super( popupMenuOperation, e, viewController );
	}
	public PopupMenuOperationContext( PopupMenuOperation popupMenuOperation ) {
		this( popupMenuOperation, null, null );
	}
	public PopupMenuOperationContext( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		super( binaryDecoder );
	}
	
	public static class RetargetableMenuModelInitializationEvent extends ModelEvent< PopupMenuOperationContext > {
		private RetargetingData retargetingData;
		public RetargetableMenuModelInitializationEvent( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
			super( binaryDecoder );
		}
		private RetargetableMenuModelInitializationEvent( RetargetingData data ) {
			this.retargetingData = data;
		}
		
		public RetargetingData getRetargetingData() {
			return this.retargetingData;
		}
//		@Override
//		public void retarget( edu.cmu.cs.dennisc.croquet.Retargeter retargeter ) {
//			super.retarget( retargeter );
//			this.retargetingData = retargeter.retarget( this.retargetingData );
//		}
		@Override
		protected void decodeInternal( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
			super.decodeInternal( binaryDecoder );
			this.retargetingData = binaryDecoder.decodeBinaryEncodableAndDecodable();
		}
		@Override
		protected void encodeInternal( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
			super.encodeInternal( binaryEncoder );
			binaryEncoder.encode( this.retargetingData );
		}
		@Override
		public State getState() {
			return null;
		}
	}

	public static class MenuResizedEvent extends ModelEvent< PopupMenuOperationContext > {
		private java.awt.event.ComponentEvent componentEvent;
		public MenuResizedEvent( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
			super( binaryDecoder );
		}
		private MenuResizedEvent( java.awt.event.ComponentEvent componentEvent ) {
			this.componentEvent = componentEvent;
		}
		public java.awt.event.ComponentEvent getComponentEvent() {
			return this.componentEvent;
		}
		@Override
		public State getState() {
			return null;
		}
	}
	
	public static class MenuSelectionEvent extends ModelEvent< PopupMenuOperationContext > {
		private javax.swing.event.ChangeEvent changeEvent;
		private Model[] models;
		private CodableResolver< Model >[] modelResolvers;

		public MenuSelectionEvent( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
			super( binaryDecoder );
		}
		private MenuSelectionEvent( javax.swing.event.ChangeEvent changeEvent, java.util.List< Model > models ) {
			this.changeEvent = changeEvent;
			final int N = models.size();
			
			this.models = new Model[ N ];
			this.modelResolvers = new CodableResolver[ N ];
			
			for( int i=0; i<N; i++ ) {
				this.models[ i ] = models.get( i );
				this.modelResolvers[ i ] = this.models[ i ].getCodableResolver();
			}
		}
		public MenuSelectionEvent( java.util.List< Model > models ) {
			this( null, models );
		}
		
		@Override
		public void retarget( edu.cmu.cs.dennisc.croquet.Retargeter retargeter ) {
			super.retarget( retargeter );
			final int N = this.models.length;
			for( int i=0; i<N; i++ ) {
//				this.models[ i ] = retargeter.retarget( this.models[ i ] );
//				if( this.models[ i ] != null ) {
//					this.modelResolvers[ i ] = this.models[ i ].getCodableResolver();
//				} else {
				if( this.modelResolvers[ i ] instanceof RetargetableResolver ) {
					this.models[ i ] = null;
					((RetargetableResolver)this.modelResolvers[ i ]).retarget( retargeter );
				}
//				}
			}
		}
		public javax.swing.event.ChangeEvent getChangeEvent() {
			return this.changeEvent;
		}
		
		public int getModelCount() {
			return this.models.length;
		}
		public <M extends Model> M getModelAt( int i ) {
			Model rv;
			if( this.models[ i ] != null ) {
				rv = this.models[ i ];
			} else {
				if( this.modelResolvers[ i ] != null ) {
					rv = this.modelResolvers[ i ].getResolved(); 
					if( this.modelResolvers[ i ] instanceof RetargetableResolver< ? > ) {
						//pass
					} else {
						this.models[ i ] = rv;
					}
				} else {
					rv = null;
				}
			}
			return (M)rv;
		}
		
		public <M extends Model> M getLastModel() {
			final int N = this.getModelCount();
			if( N > 0 ) {
				return this.getModelAt( N-1 );
			} else {
				return null;
			}
		}
		
		@Override
		protected void decodeInternal( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
			super.decodeInternal( binaryDecoder );
			this.modelResolvers = binaryDecoder.decodeBinaryEncodableAndDecodableArray( CodableResolver.class );
			this.models = new Model[ this.modelResolvers.length ];
		}
		@Override
		protected void encodeInternal( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
			super.encodeInternal( binaryEncoder );
			binaryEncoder.encode( this.modelResolvers );
		}
		@Override
		public State getState() {
			return null;
		}
		@Override
		protected StringBuilder appendRepr( StringBuilder rv ) {
			super.appendRepr( rv );
			for( CodableResolver< Model > modelResolver : this.modelResolvers ) {
				rv.append( " " );
				rv.append( modelResolver.getResolved() );
			}
			return rv;
		}
	}
	
	/*package-private*/ void handleRetargetableMenuModelInitialization( RetargetingData retargetingData ) {
		this.addChild( new RetargetableMenuModelInitializationEvent( retargetingData ) );
	}
	/*package-private*/ void handleMenuSelectionChanged( javax.swing.event.ChangeEvent e, java.util.List< Model > models ) {
		this.addChild( new MenuSelectionEvent( e, models ) );
	}
	/*package-private*/ void handleResized( java.awt.event.ComponentEvent e ) {
		this.addChild( new MenuResizedEvent( e ) );
	}
}
