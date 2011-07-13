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

import org.lgna.croquet.edits.Edit;
import org.lgna.croquet.resolvers.RuntimeResolver;
/**
 * @author Dennis Cosgrove
 */
public abstract class Model extends Element implements RuntimeResolver< Model > {
	private static final int NULL_MNEMONIC = 0;
	private static final int NULL_ACCELERATOR_MASK = 0;
	public Model( java.util.UUID id ) {
		super( id );
	}

	public final Model getResolved() {
		return this;
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

	protected abstract void localize();
	
	public abstract org.lgna.croquet.history.Step<?> fire( org.lgna.croquet.triggers.Trigger trigger );

	private static String findLocalizedText( Class<? extends Model> cls, Class<? extends Model> clsRoot, String subKey ) {
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
			if( cls == clsRoot ) {
				return null;
			} else {
				return findLocalizedText( (Class<? extends Model>)cls.getSuperclass(), clsRoot, subKey );
			}
		}
	}
	private static String getLocalizedText( Class<? extends Model> cls, String subKey ) {
		return findLocalizedText( cls, cls, subKey );
	}

	protected Class<? extends Model> getClassUsedForLocalization() {
		return this.getClass();
	}
	protected final String getLocalizedText( String subKey ) {
		return getLocalizedText( this.getClassUsedForLocalization(), subKey );
	}
	protected final String findLocalizedText( String subKey, Class<? extends Model> clsRoot ) {
		return findLocalizedText( this.getClassUsedForLocalization(), clsRoot, subKey );
	}
	
	protected static String getDefaultLocalizedText( Class<? extends Model> cls ) {
		return getLocalizedText( cls, null );
	}
	protected /*final*/ String getDefaultLocalizedText() {
		return getDefaultLocalizedText( this.getClassUsedForLocalization() );
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
	
	protected static int getLocalizedMnemonicKey( Class<? extends Model> cls ) {
		return getKeyCode( getLocalizedText( cls, "mnemonic" ) );
	}
	protected int getLocalizedMnemonicKey() {
		return getLocalizedMnemonicKey( this.getClass() );
	}
	protected static javax.swing.KeyStroke getLocalizedAcceleratorKeyStroke( Class<? extends Model> cls ) {
		String acceleratorText = getLocalizedText( cls, "accelerator" );
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
		return getLocalizedAcceleratorKeyStroke( this.getClass() );
	}
	
	private boolean isEnabled = true;
	public boolean isEnabled() {
		return this.isEnabled;
	}
	public void setEnabled( boolean isEnabled ) {
		if( this.isEnabled != isEnabled ) {
			this.isEnabled = isEnabled;
			synchronized( this.components ) {
				for( org.lgna.croquet.components.JComponent<?> component : this.components ) {
					component.getAwtComponent().setEnabled( this.isEnabled );
				}
			}
		}
	}

	private String toolTipText = null;
	public String getToolTipText() {
		return this.toolTipText;
	}
	public void setToolTipText( String toolTipText ) {
		if( edu.cmu.cs.dennisc.equivalence.EquivalenceUtilities.areEquivalent( this.toolTipText, toolTipText ) ) {
			//pass
		} else {
			this.toolTipText = toolTipText;
			synchronized( this.components ) {
				for( org.lgna.croquet.components.JComponent<?> component : this.components ) {
					component.setToolTipText( this.toolTipText );
				}
			}
		}
	}

	private java.util.List< org.lgna.croquet.components.JComponent<?> > components = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
	public void addComponent( org.lgna.croquet.components.JComponent<?> component ) {
		if( this.components.size() == 0 ) {
			Manager.registerModel( this );
		}
		synchronized( this.components ) {
			this.components.add( component );
		}
		component.getAwtComponent().setEnabled( this.isEnabled );
		component.setToolTipText( this.toolTipText );
	}
	public void removeComponent( org.lgna.croquet.components.JComponent<?> component ) {
		synchronized( this.components ) {
			this.components.remove( component );
		}
		if( this.components.size() == 0 ) {
			Manager.unregisterModel( this );
		} else {
			//edu.cmu.cs.dennisc.print.PrintUtilities.println( "removeComponent", this.components.size(), this );
		}
	}

	protected void repaintAllComponents() {
		synchronized( this.components ) {
			for( org.lgna.croquet.components.JComponent<?> component : this.components ) {
				component.repaint();
			}
		}
	}
	protected void revalidateAndRepaintAllComponents() {
		synchronized( this.components ) {
			for( org.lgna.croquet.components.JComponent<?> component : this.components ) {
				component.revalidateAndRepaint();
			}
		}
	}
	
	/*package-private*/ Iterable< org.lgna.croquet.components.JComponent<?> > getComponents() {
		return this.components;
	}
	
	private org.lgna.croquet.components.JComponent< ? > firstComponentHint;
	
	@Deprecated
	public <J extends org.lgna.croquet.components.JComponent< ? > > J getFirstComponent( Class<J> cls, boolean isVisibleAcceptable ) {
		if( this.firstComponentHint != null ) {
			return cls.cast( this.firstComponentHint );
		} else {
//			edu.cmu.cs.dennisc.print.PrintUtilities.println( "getFirstComponent:", this );
//			edu.cmu.cs.dennisc.print.PrintUtilities.println( "count:", this.components.size() );
			for( org.lgna.croquet.components.JComponent< ? > component : this.components ) {
				if( cls.isAssignableFrom( component.getClass() ) ) {
					if( component.getAwtComponent().isShowing() ) {
//						edu.cmu.cs.dennisc.print.PrintUtilities.println( "isShowing:", component.getAwtComponent().getClass() );
						return cls.cast( component );
					} else {
						//pass
					}
				}
			}
			if( isVisibleAcceptable ) {
				for( org.lgna.croquet.components.JComponent< ? > component : this.components ) {
					if( cls.isAssignableFrom( component.getClass() ) ) {
						if( component.getAwtComponent().isVisible() ) {
//							edu.cmu.cs.dennisc.print.PrintUtilities.println( "isShowing:", component.getAwtComponent().getClass() );
							return cls.cast( component );
						} else {
							//pass
						}
					}
				}
			}
		}
		return null;
	}
//	public JComponent getFirstNotNecessarilyShowingComponent() {
//		for( JComponent< ? > component : this.components ) {
//			if( component.getAwtComponent().isVisible() ) {
//				return component;
//			} else {
//				//pass
//			}
//		}
//		return null;
//	}
	@Deprecated
	public<J extends org.lgna.croquet.components.JComponent< ? > > J getFirstComponent( Class<J> cls ) {
		return getFirstComponent( cls, false );
	}
	@Deprecated
	public org.lgna.croquet.components.JComponent< ? > getFirstComponent( boolean isVisibleAcceptable ) {
		return getFirstComponent( org.lgna.croquet.components.JComponent.class, isVisibleAcceptable );
	}
	@Deprecated
	public org.lgna.croquet.components.JComponent< ? > getFirstComponent() {
		return getFirstComponent( false );
	}
	@Deprecated
	public void setFirstComponentHint( org.lgna.croquet.components.JComponent< ? > firstComponentHint ) {
//		Thread.dumpStack();
		assert this.components.contains( firstComponentHint );
		if( this.firstComponentHint != firstComponentHint ) {
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "setFirstComponentHint", this.firstComponentHint, "->", firstComponentHint );
			this.firstComponentHint = firstComponentHint;
		}
	}
	
	protected StringBuilder updateTutorialStepText( StringBuilder rv, org.lgna.croquet.history.Step< ? > step, Edit< ? > edit, UserInformation userInformation ) {
		rv.append( "text: " );
		rv.append( this );
		return rv;
	}

	public final String getTutorialNoteText( org.lgna.croquet.history.Step< ? > step, Edit< ? > edit, UserInformation userInformation ) {
		StringBuilder sb = new StringBuilder();
		this.updateTutorialStepText( sb, step, edit, userInformation );
		return sb.toString();
	}
}
