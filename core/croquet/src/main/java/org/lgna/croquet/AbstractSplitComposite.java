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
package org.lgna.croquet;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractSplitComposite<SP extends org.lgna.croquet.views.AbstractSplitPane<?>> extends AbstractComposite<SP> {
	public AbstractSplitComposite( java.util.UUID id ) {
		super( id );
	}

	public abstract Composite<?> getLeadingComposite();

	public abstract Composite<?> getTrailingComposite();

	@Override
	protected final org.lgna.croquet.views.ScrollPane createScrollPaneIfDesired() {
		return null;
	}

	@Override
	public final boolean contains( org.lgna.croquet.Model model ) {
		if( super.contains( model ) ) {
			return true;
		} else {
			Composite<?> leadingComposite = this.getLeadingComposite();
			if( leadingComposite != null ) {
				if( leadingComposite.contains( model ) ) {
					return true;
				}
			}
			Composite<?> trailingComposite = this.getTrailingComposite();
			if( trailingComposite != null ) {
				if( trailingComposite.contains( model ) ) {
					return true;
				}
			}
			return false;
		}
	}

	@Override
	public void releaseView() {
		Composite<?> leadingComposite = this.getLeadingComposite();
		if( leadingComposite != null ) {
			leadingComposite.releaseView();
		}
		Composite<?> trailingComposite = this.getTrailingComposite();
		if( trailingComposite != null ) {
			trailingComposite.releaseView();
		}
		super.releaseView();
	}

	@Override
	public void handlePreActivation() {
		super.handlePreActivation();
		Composite<?> leadingComposite = this.getLeadingComposite();
		if( leadingComposite != null ) {
			leadingComposite.handlePreActivation();
		}
		Composite<?> trailingComposite = this.getTrailingComposite();
		if( trailingComposite != null ) {
			trailingComposite.handlePreActivation();
		}
	}

	@Override
	public void handlePostDeactivation() {
		Composite<?> leadingComposite = this.getLeadingComposite();
		if( leadingComposite != null ) {
			leadingComposite.handlePostDeactivation();
		}
		Composite<?> trailingComposite = this.getTrailingComposite();
		if( trailingComposite != null ) {
			trailingComposite.handlePostDeactivation();
		}
		super.handlePostDeactivation();
	}

	protected abstract SP createHorizontalSplitPane();

	protected abstract SP createVerticalSplitPane();
}
