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
package org.alice.ide.memberseditor;

/**
 * @author Dennis Cosgrove
 */
abstract class OrganizedByTypeMembersContentPanel extends MembersContentPanel {
	@Override
	protected void refresh( java.util.List< edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> > types ) {
		this.removeAllComponents();
		boolean isNonConsumedTypeDeclaredInJavaAlreadyEncountered = false;

		if( types.size() > 0 ) {
			boolean isSeparatorDesired = types.get( 0 ) instanceof edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice;
			for( edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> type : types ) {
				boolean isFirstNonConsumedTypeEncounteredInJava = false;
				if( type instanceof edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava ) {
					if( isSeparatorDesired ) {
						this.addComponent( new edu.cmu.cs.dennisc.croquet.HorizontalSeparator() );
						this.addComponent( edu.cmu.cs.dennisc.croquet.BoxUtilities.createVerticalSliver( 16 ) );
						isSeparatorDesired = false;
					}
					if( isNonConsumedTypeDeclaredInJavaAlreadyEncountered ) {
						//pass
					} else {
						if( type.isConsumptionBySubClassDesired() ) {
							//pass
						} else {
							isFirstNonConsumedTypeEncounteredInJava = true;
							isNonConsumedTypeDeclaredInJavaAlreadyEncountered = true;
						}
					}
				}
				if( type.isConsumptionBySubClassDesired() ) {
					//pass
				} else {
					if( /*org.alice.ide.IDE.getSingleton().isEmphasizingClasses() ||*/ type instanceof edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice || isFirstNonConsumedTypeEncounteredInJava ) {
						this.addComponent( MembersEditor.getComponentFor( this.getClass(), type ) );
					}
				}
				this.addComponent( this.getTypeMembersPane( type ) );
			}
		}
		this.revalidateAndRepaint();
	}

	private java.util.Map< edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?>, AbstractTypeMembersPane > mapTypeToPane = new java.util.HashMap< edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?>, AbstractTypeMembersPane >();
	protected abstract AbstractTypeMembersPane createTypeMembersPane( edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> type );
	protected AbstractTypeMembersPane getTypeMembersPane( edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> type ) {
		AbstractTypeMembersPane rv = this.mapTypeToPane.get( type );
		if( rv != null ) {
			//todo?
			rv.refresh();
		} else {
			rv = this.createTypeMembersPane( type );
			this.mapTypeToPane.put( type, rv );
		}
		return rv;
	}
}
