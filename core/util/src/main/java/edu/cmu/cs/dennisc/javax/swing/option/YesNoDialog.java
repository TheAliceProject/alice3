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
package edu.cmu.cs.dennisc.javax.swing.option;

/**
 * @author Dennis Cosgrove
 */
public class YesNoDialog extends OptionDialog {
	public static class Builder {
		public Builder( String message ) {
			this.message = message;
		}

		public Builder( java.awt.Component message ) {
			this.message = message;
		}

		public Builder parentComponent( java.awt.Component parentComponent ) {
			this.parentComponent = parentComponent;
			return this;
		}

		public Builder title( String title ) {
			this.title = title;
			return this;
		}

		public Builder messageType( MessageType messageType ) {
			this.messageType = messageType;
			return this;
		}

		public Builder icon( javax.swing.Icon icon ) {
			this.icon = icon;
			return this;
		}

		private YesNoDialog build() {
			return new YesNoDialog( this );
		}

		public YesNoResult buildAndShow() {
			return this.build().show();
		}

		private java.awt.Component parentComponent;
		private Object message;
		private String title;
		private MessageType messageType = MessageType.QUESTION;
		private javax.swing.Icon icon;
	}

	private YesNoDialog( Builder builder ) {
		super( builder.parentComponent );
		this.message = builder.message;
		this.title = builder.title;
		this.messageType = builder.messageType;
		this.icon = builder.icon;
	}

	public YesNoResult show() {
		return YesNoResult.getInstance( javax.swing.JOptionPane.showConfirmDialog( this.getParentComponent(), this.message, this.title, javax.swing.JOptionPane.YES_NO_OPTION, this.messageType.getInternal(), this.icon ) );
	}

	private final Object message;
	private final String title;
	private final MessageType messageType;
	private final javax.swing.Icon icon;
}
