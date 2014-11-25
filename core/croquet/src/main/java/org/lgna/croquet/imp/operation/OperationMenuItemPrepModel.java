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
package org.lgna.croquet.imp.operation;

/**
 * @author Dennis Cosgrove
 */
/*package-private*/class OperationMenuItemPrepModel extends org.lgna.croquet.StandardMenuItemPrepModel {
	public OperationMenuItemPrepModel( org.lgna.croquet.Operation operation ) {
		super( java.util.UUID.fromString( "652a76ce-4c05-4c31-901c-ff14548e50aa" ) );
		assert operation != null;
		this.operation = operation;
	}

	@Override
	public Iterable<? extends org.lgna.croquet.Model> getChildren() {
		return edu.cmu.cs.dennisc.java.util.Lists.newArrayList( this.operation );
	}

	@Override
	protected void localize() {
	}

	public org.lgna.croquet.Operation getOperation() {
		return this.operation;
	}

	@Override
	public boolean isEnabled() {
		return this.operation.isEnabled();
	}

	@Override
	public void setEnabled( boolean isEnabled ) {
		this.operation.setEnabled( isEnabled );
	}

	@Override
	public org.lgna.croquet.views.MenuItem createMenuItemAndAddTo( org.lgna.croquet.views.MenuItemContainer menuItemContainer ) {
		org.lgna.croquet.views.MenuItem menuItem = new org.lgna.croquet.views.MenuItem( this.getOperation() );
		menuItemContainer.addMenuItem( menuItem );
		return menuItem;
	}

	@Override
	protected void appendRepr( StringBuilder sb ) {
		super.appendRepr( sb );
		sb.append( "operation=" );
		sb.append( this.getOperation() );
	}

	private final org.lgna.croquet.Operation operation;
}
