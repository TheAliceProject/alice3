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
public abstract class CascadeBlank< B > extends AbstractModel {
	private java.util.List< AbstractCascadeFillIn< ? extends B,?,?,? > > ownees;
	public CascadeBlank( java.util.UUID id ) {
		super( Application.CASCADE_GROUP, id );
	}

	protected abstract java.util.List< AbstractCascadeFillIn > updateChildren( java.util.List< AbstractCascadeFillIn > rv, CascadeBlankContext<B> context );
	public final Iterable< AbstractCascadeFillIn > getChildren( CascadeBlankContext<B> context ) {
		java.util.List< AbstractCascadeFillIn > rv = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
		this.updateChildren( rv, context );
		return rv;
	}
	
//	public Iterable< AbstractCascadeFillIn< ? extends B,?,?,? > > getOwnees() {
//		if( this.ownees != null ) {
//			//pass
//		} else {
//			this.ownees = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
//			this.addFillIns();
//		}
//		return this.ownees;
//	}

//	protected abstract void addFillIns();
//	public void addFillIn( CascadeFillIn< ? extends B,? > fillIn ) {
//		this.ownees.add( fillIn );
//	}
//	public void addMenu( CascadeMenu< ? extends B > menu ) {
//		this.ownees.add( menu );
//	}
//	public void addSeparator() {
//		this.addSeparator( CascadeLineSeparator.getInstance() );
//	}
//	public void addSeparator( CascadeSeparator separator ) {
//		//note: we drop generic information since separators are never selected 
//		this.ownees.add( (AbstractCascadeFillIn< ? extends B,?,?,? >)separator );
//	}
//	public void addCancel( CascadeCancel< ? extends B > cancel ) {
//		this.ownees.add( cancel );
//	}
	@Override
	protected void localize() {
	}
	@Override
	public boolean isAlreadyInState( edu.cmu.cs.dennisc.croquet.Edit< ? > edit ) {
		return false;
	}
}
