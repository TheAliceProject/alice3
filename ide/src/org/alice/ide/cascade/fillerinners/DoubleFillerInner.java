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
package org.alice.ide.cascade.fillerinners;

/**
 * @author Dennis Cosgrove
 */
public class DoubleFillerInner extends AbstractNumberFillerInner {
	public DoubleFillerInner() {
		super( Double.class );
	}

	@Override
	public void appendItems( java.util.List<org.lgna.croquet.CascadeBlankChild> items, org.lgna.project.annotations.ValueDetails<?> details, boolean isTop, org.lgna.project.ast.Expression prevExpression ) {
		super.appendItems( items, details, isTop, prevExpression );
		double[] literals;
		if( details instanceof org.lgna.project.annotations.NumberValueDetails ) {
			literals = ( (org.lgna.project.annotations.NumberValueDetails)details ).getLiterals();
		} else {
			literals = new double[] { 0.25, 0.5, 1.0, 2.0, 10.0 };
		}
		for( double d : literals ) {
			items.add( org.alice.ide.croquet.models.cascade.literals.DoubleLiteralFillIn.getInstance( d ) );
		}
		if( isTop && ( prevExpression != null ) ) {
			items.add( org.lgna.croquet.CascadeLineSeparator.getInstance() );
			items.add( org.alice.ide.croquet.models.cascade.number.RandomCascadeMenu.getInstance() );
			items.add( org.lgna.croquet.CascadeLineSeparator.getInstance() );
			items.add( org.alice.ide.croquet.models.cascade.number.MathCascadeMenu.getInstance() );
		}
		items.add( org.lgna.croquet.CascadeLineSeparator.getInstance() );
		org.alice.ide.IDE ide = org.alice.ide.IDE.getActiveInstance();
		org.alice.ide.ApiConfigurationManager apiConfigurationManager = ide.getApiConfigurationManager();
		org.lgna.croquet.CascadeItem item = apiConfigurationManager.getCustomFillInFor( details );
		if( item != null ) {
			items.add( item );
		} else {
			items.add( org.alice.ide.custom.DoubleCustomExpressionCreatorComposite.getInstance().getValueCreator().getFillIn() );
		}
	}
}
