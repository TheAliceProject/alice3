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
public abstract class AbstractElement implements Element {
	private static java.util.Map<java.util.UUID, Class<? extends Element>> map;
	static {
		if( edu.cmu.cs.dennisc.java.lang.SystemUtilities.isPropertyTrue( "org.lgna.croquet.Element.isIdCheckDesired" ) ) {
			map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
			edu.cmu.cs.dennisc.java.util.logging.Logger.info( "org.lgna.croquet.Element.isIdCheckDesired==true" );
		}
	}
	
	protected static abstract class IndirectResolver<D extends Element, I extends Element> implements org.lgna.croquet.resolvers.Resolver< D > {
		private final org.lgna.croquet.resolvers.Resolver<I> resolver;
		public IndirectResolver( I indirect ) {
			this.resolver = indirect.getResolver();
		}
		public IndirectResolver( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
			this.resolver = binaryDecoder.decodeBinaryEncodableAndDecodable();
		}
		public final void encode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
			binaryEncoder.encode( this.resolver );
		}
		protected abstract D getDirect( I indirect );
		public final D getResolved() {
			return this.getDirect( this.resolver.getResolved() );
		}
		public final void retarget( org.lgna.croquet.Retargeter retargeter ) {
			this.resolver.retarget( retargeter );
		}
	}

	
	private final java.util.UUID migrationId;
	private org.lgna.croquet.resolvers.Resolver<Element> resolver;
	public AbstractElement( java.util.UUID migrationId ) {
		this.migrationId = migrationId;
		if( map != null ) {
			Class<? extends Element> cls = map.get( migrationId );
			if( cls != null ) {
				if( cls == this.getClass() ) {
					//pass
				} else {
					String clipboardContents = "java.util.UUID.fromString( \"" + migrationId + "\" )";
					edu.cmu.cs.dennisc.java.awt.datatransfer.ClipboardUtilities.setClipboardContents( clipboardContents );
					String message = "WARNING: duplicate migrationId.\n\"" + clipboardContents + "\" has been copied to clipboard.\nRemove all duplicates.";
					Application.getActiveInstance().showMessageDialog( message );
				}
			} else {
				map.put( migrationId, this.getClass() );
			}
		}
	}
	public java.util.UUID getMigrationId() {
		return this.migrationId;
	}
	private boolean isInitialized = false;
	protected void initialize() {
		this.localize();
	}
	public final void initializeIfNecessary() {
		if( this.isInitialized ) {
			//pass
		} else {
			this.initialize();
			this.isInitialized = true;
		}
	}
	public final void relocalize() {
		this.localize();
	}

	protected Class<? extends Element> getClassUsedForLocalization() {
		return this.getClass();
	}
	protected String getSubKeyForLocalization() {
		return null;
	}
	private static String findLocalizedText( Class<? extends Element> cls, String subKey ) {
		if( cls != null ) {
			String bundleName = cls.getPackage().getName() + ".croquet";
			try {
				java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle( bundleName, javax.swing.JComponent.getDefaultLocale() );
				String key = cls.getSimpleName();
				
				//todo?
				//if( cls.isMemberClass() ) {
				//}
				
				
				if( subKey != null ) {
					StringBuilder sb = new StringBuilder();
					sb.append( key );
					sb.append( "." );
					sb.append( subKey );
					key = sb.toString();
				}
				String rv = resourceBundle.getString( key );
				return rv;
			} catch( java.util.MissingResourceException mre ) {
				if( cls == AbstractElement.class ) {
					return null;
				} else {
					return findLocalizedText( (Class<? extends Element>)cls.getSuperclass(), subKey );
				}
			}
		} else {
			return null;
		}
	}

	protected final String findLocalizedText( String subKey ) {
		String inherantSubKey = this.getSubKeyForLocalization();
		String actualSubKey;
		if( inherantSubKey != null ) {
			if( subKey != null ) {
				actualSubKey = inherantSubKey + "." + subKey;
			} else {
				actualSubKey = inherantSubKey;
			}
		} else {
			actualSubKey = subKey;
		}
		return findLocalizedText( this.getClassUsedForLocalization(), actualSubKey );
	}
	protected String findDefaultLocalizedText() {
		return this.findLocalizedText( null );
	}

	private static int getField( Class<?> cls, String fieldName, int nullValue ) {
		if( fieldName != null ) {
			try {
				java.lang.reflect.Field field = cls.getField( fieldName );
				Object value = field.get( null );
				if( value instanceof Integer ) {
					return (Integer)value;
				}
			} catch( NoSuchFieldException nsfe ) {
				nsfe.printStackTrace();
			} catch( IllegalAccessException iae ) {
				iae.printStackTrace();
			}
		}
		return nullValue;
	}
	private static int getKeyCode( String vkFieldName ) {
		return getField( java.awt.event.KeyEvent.class, vkFieldName, NULL_MNEMONIC );
	}
	private static int getModifierMask( String modifierText ) {
		if( "PLATFORM_ACCELERATOR_MASK".equals( modifierText ) ) {
			return edu.cmu.cs.dennisc.java.awt.event.InputEventUtilities.getAcceleratorMask();
		} else {
			return getField( java.awt.event.InputEvent.class, modifierText, NULL_ACCELERATOR_MASK );
		}
	}
	
	protected int getLocalizedMnemonicKey() {
		return getKeyCode( this.findLocalizedText( "mnemonic" ) );
	}
	private static final int NULL_MNEMONIC = 0;
	private static final int NULL_ACCELERATOR_MASK = 0;
	protected static javax.swing.KeyStroke getKeyStroke( String acceleratorText ) {
		if( acceleratorText != null ) {
			String[] array = acceleratorText.split(",");
			if( array.length > 0 ) {
				int keyCode = getKeyCode( array[ 0 ] );
				if( keyCode != NULL_MNEMONIC ) {
					int modifierMask;
					if( java.awt.event.KeyEvent.VK_F1 <= keyCode && keyCode <= java.awt.event.KeyEvent.VK_F24 ) {
						modifierMask = 0;
					} else {
						modifierMask = edu.cmu.cs.dennisc.java.awt.event.InputEventUtilities.getAcceleratorMask();
					}
					if( array.length > 1 ) {
						String[] modifierTexts = array[ 1 ].split( "\\|" );
						for( String modifierText : modifierTexts ) {
							int modifier = getModifierMask( modifierText );
							if( modifier != NULL_ACCELERATOR_MASK ) {
								modifierMask |= modifier;
							} else {
								//todo?
							}
						}
					}
					return javax.swing.KeyStroke.getKeyStroke( keyCode, modifierMask );
				}
				
			}
		}
		return null;
	}
	protected javax.swing.KeyStroke getLocalizedAcceleratorKeyStroke() {
		return getKeyStroke( this.findLocalizedText( "accelerator" ) );
	}
	
	protected abstract void localize();
	
	protected <M extends Element> org.lgna.croquet.resolvers.Resolver< M > createResolver() {
		return new org.lgna.croquet.resolvers.SingletonResolver( this );
	}
	public <M extends Element> org.lgna.croquet.resolvers.Resolver< M > getResolver() {
		if( this.resolver != null ) {
			//pass
		} else {
			this.resolver = this.createResolver();
		}
		return (org.lgna.croquet.resolvers.Resolver< M >)this.resolver;
	}
	
	protected void appendRepr( StringBuilder sb ) {
	}
	protected String createRepr() {
		StringBuilder sb = new StringBuilder();
		sb.append( this.getClass().getSimpleName() );
		sb.append( "[" );
		this.appendRepr( sb );
		sb.append( "]" );
		return sb.toString();
	}
	
	public void appendUserRepr( StringBuilder sb ) {
		sb.append( "todo: override appendUserString\n" );
		sb.append( this );
		sb.append( "\n" );
		sb.append( this.getClass().getName() );
		sb.append( "\n" );
	}
	@Override
	public final String toString() {
		return this.createRepr();
	}
}
