/**
 * Copyright (c) 2006-2012, Carnegie Mellon University. All rights reserved.
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
package org.alice.ide.ast.type.merge.croquet;

/**
 * @author Dennis Cosgrove
 */
public final class DifferentImplementationCardOwner extends org.lgna.croquet.CardOwnerComposite {
	public static class Builder {
		private final DifferentImplementation<?> differentImplementation;
		private org.lgna.croquet.Composite<?> neitherCard;
		private org.lgna.croquet.Composite<?> replaceCard;
		private org.lgna.croquet.Composite<?> keepCard;
		private org.lgna.croquet.Composite<?> renameCard;

		public Builder( DifferentImplementation<?> differentImplementation ) {
			this.differentImplementation = differentImplementation;
		}

		public Builder neither( org.lgna.croquet.Composite<?> neitherCard ) {
			this.neitherCard = neitherCard;
			return this;
		}

		public Builder replace( org.lgna.croquet.Composite<?> replaceCard ) {
			this.replaceCard = replaceCard;
			return this;
		}

		public Builder keep( org.lgna.croquet.Composite<?> keepCard ) {
			this.keepCard = keepCard;
			return this;
		}

		public Builder rename( org.lgna.croquet.Composite<?> renameCard ) {
			this.renameCard = renameCard;
			return this;
		}

		public DifferentImplementationCardOwner build() {
			return new DifferentImplementationCardOwner( this.differentImplementation, this.neitherCard, this.replaceCard, this.keepCard, this.renameCard );
		}
	}

	private final DifferentImplementation<?> differentImplementation;
	private final org.lgna.croquet.Composite<?> neitherCard;
	private final org.lgna.croquet.Composite<?> replaceCard;
	private final org.lgna.croquet.Composite<?> keepCard;
	private final org.lgna.croquet.Composite<?> renameCard;

	private final org.lgna.croquet.State.ValueListener<Boolean> valueListener = new org.lgna.croquet.State.ValueListener<Boolean>() {
		public void changing( org.lgna.croquet.State<Boolean> state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
		}

		public void changed( org.lgna.croquet.State<Boolean> state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
			updateCardAndUpdateIsMemberDesiredStatesWhileAtIt();
		}
	};

	private DifferentImplementationCardOwner( DifferentImplementation<?> differentImplementation, org.lgna.croquet.Composite<?> neitherCard, org.lgna.croquet.Composite<?> replaceCard, org.lgna.croquet.Composite<?> keepCard, org.lgna.croquet.Composite<?> renameCard ) {
		super( java.util.UUID.fromString( "e49e92f7-1eac-4571-bbef-b53970b11b3d" ) );
		this.differentImplementation = differentImplementation;
		this.neitherCard = neitherCard;
		this.replaceCard = replaceCard;
		this.keepCard = keepCard;
		this.renameCard = renameCard;

		if( this.neitherCard != null ) {
			this.addCard( this.neitherCard );
		}
		if( this.replaceCard != null ) {
			this.addCard( this.replaceCard );
		}
		if( this.keepCard != null ) {
			this.addCard( this.keepCard );
		}
		if( this.renameCard != null ) {
			this.addCard( this.renameCard );
		}

		this.differentImplementation.getImportHub().getIsDesiredState().addValueListener( this.valueListener );
		this.differentImplementation.getProjectHub().getIsDesiredState().addAndInvokeValueListener( this.valueListener );
	}

	//private static final String REQUIRES_RENAME = " <em>(requires rename)</em>";
	//private static final String WOULD_REQUIRE_RENAME = " <em>(would require rename)</em>";

	private void updateCardAndUpdateIsMemberDesiredStatesWhileAtIt() {
		boolean isAddDesired = this.differentImplementation.getImportHub().getIsDesiredState().getValue();
		boolean isKeepDesired = this.differentImplementation.getProjectHub().getIsDesiredState().getValue();
		String prefix;
		org.lgna.croquet.Composite<?> card;
		if( isKeepDesired ) {
			prefix = "add ";
			if( isAddDesired ) {
				card = this.renameCard;
			} else {
				card = this.keepCard;
			}
		} else {
			if( isAddDesired ) {
				card = this.replaceCard;
				prefix = "replace ";
			} else {
				card = this.neitherCard;
				prefix = "replace/add ";
			}
		}

		//String addPostfix;
		//		if( isKeepDesired ) {
		//			if( isAddDesired ) {
		//				addPostfix = REQUIRES_RENAME;
		//			} else {
		//				addPostfix = WOULD_REQUIRE_RENAME;
		//			}
		//		} else {
		//			addPostfix = "";
		//		}
		//		String keepPostfix;
		//		if( isAddDesired ) {
		//			if( isKeepDesired ) {
		//				keepPostfix = REQUIRES_RENAME;
		//			} else {
		//				keepPostfix = WOULD_REQUIRE_RENAME;
		//			}
		//		} else {
		//			keepPostfix = "";
		//		}

		this.showCard( card );
		this.differentImplementation.getImportHub().getIsDesiredState().setPrependText( prefix );
		//		this.differentImplementation.getIsKeepDesiredState().setTextForBothTrueAndFalseBasedOnMemberName( "keep ", keepPostfix );
	}
}
