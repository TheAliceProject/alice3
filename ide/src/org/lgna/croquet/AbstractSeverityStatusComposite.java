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

package org.lgna.croquet;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractSeverityStatusComposite<V extends org.lgna.croquet.components.View<?, ?>> extends AbstractComposite<V> {
	public static final Status IS_GOOD_TO_GO_STATUS = null;

	public static abstract class Status extends AbstractInternalStringValue {
		private Status( java.util.UUID id, Key key ) {
			super( id, key );
		}

		public abstract boolean isGoodToGo();
	}

	public static final class WarningStatus extends Status {
		private WarningStatus( Key key ) {
			super( java.util.UUID.fromString( "a1375dce-1d5f-4717-87a1-7d9759a12862" ), key );
		}

		@Override
		public boolean isGoodToGo() {
			return true;
		}
	}

	public static final class ErrorStatus extends Status {
		private ErrorStatus( Key key ) {
			super( java.util.UUID.fromString( "e966c721-1a6e-478d-a22f-92725d68552e" ), key );
		}

		@Override
		public boolean isGoodToGo() {
			return false;
		}
	}

	protected WarningStatus createWarningStatus( Key key ) {
		WarningStatus rv = new WarningStatus( key );
		this.registerStringValue( rv );
		return rv;
	}

	protected ErrorStatus createErrorStatus( Key key ) {
		ErrorStatus rv = new ErrorStatus( key );
		this.registerStringValue( rv );
		return rv;
	}

	public AbstractSeverityStatusComposite( java.util.UUID id ) {
		super( id );
	}
}
