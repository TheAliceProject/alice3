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

package test;

class EnumConstantFillIn<T extends Enum< T >> extends org.lgna.croquet.CascadeFillIn< T, Void > {
	private static java.util.Map< Object, EnumConstantFillIn > map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();

	public static synchronized <T extends Enum< T >> EnumConstantFillIn< T > getInstance( T value ) {
		EnumConstantFillIn rv = map.get( value );
		if( rv != null ) {
			// pass
		} else {
			rv = new EnumConstantFillIn( value );
			map.put( value, rv );
		}
		return rv;
	}

	private T value;

	private EnumConstantFillIn( T value ) {
		super( java.util.UUID.fromString( "0128c434-7a3d-4e32-b56e-ff3a233c0af8" ) );
		this.value = value;
	}

	@Override
	protected javax.swing.JComponent createMenuItemIconProxy( org.lgna.croquet.cascade.ItemNode< ? super T,Void > step ) {
		return null;
	}

	@Override
	public javax.swing.Icon getMenuItemIcon( org.lgna.croquet.cascade.ItemNode< ? super T,Void > step ) {
		return null;
	}

	@Override
	public String getMenuItemText( org.lgna.croquet.cascade.ItemNode< ? super T,Void > step ) {
		return this.value.name();
	}

	@Override
	public T createValue( org.lgna.croquet.cascade.ItemNode< ? super T,Void > step ) {
		return this.value;
	}

	@Override
	public T getTransientValue( org.lgna.croquet.cascade.ItemNode< ? super T,Void > step ) {
		return this.value;
	}
}

class EnumBlank<T extends Enum< T >> extends org.lgna.croquet.CascadeBlank< T > {
	private final Class< T > cls;

	public EnumBlank( Class< T > cls ) {
		super( java.util.UUID.fromString( "6598e548-592b-420f-8619-16abcd9bfc99" ) );
		this.cls = cls;
	}

	@Override
	protected java.util.List< org.lgna.croquet.CascadeBlankChild > updateChildren( java.util.List< org.lgna.croquet.CascadeBlankChild > rv, org.lgna.croquet.cascade.BlankNode< T > blankNode ) {
		for( T value : this.cls.getEnumConstants() ) {
			rv.add( EnumConstantFillIn.getInstance( value ) );
		}
		return rv;
	}
}

class IntegerLiteralFillIn extends org.lgna.croquet.CascadeFillIn< Integer, Void > {
	private static java.util.Map< Integer, IntegerLiteralFillIn > map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();

	public static synchronized IntegerLiteralFillIn getInstance( Integer value ) {
		IntegerLiteralFillIn rv = map.get( value );
		if( rv != null ) {
			// pass
		} else {
			rv = new IntegerLiteralFillIn( value );
			map.put( value, rv );
		}
		return rv;
	}

	private Integer value;

	private IntegerLiteralFillIn( Integer value ) {
		super( java.util.UUID.fromString( "8ac33874-dd22-4440-b768-234d10d41cad" ) );
		this.value = value;
	}

	@Override
	protected javax.swing.JComponent createMenuItemIconProxy( org.lgna.croquet.cascade.ItemNode< ? super Integer,Void > step ) {
		return null;
	}

	@Override
	public javax.swing.Icon getMenuItemIcon( org.lgna.croquet.cascade.ItemNode< ? super Integer,Void > step ) {
		return null;
	}
	@Override
	public String getMenuItemText( org.lgna.croquet.cascade.ItemNode< ? super Integer,Void > step ) {
		return Integer.toString( this.value );
	}

	@Override
	public Integer createValue( org.lgna.croquet.cascade.ItemNode< ? super Integer,Void > step ) {
		return this.value;
	}

	@Override
	public Integer getTransientValue( org.lgna.croquet.cascade.ItemNode< ? super Integer,Void > step ) {
		return this.value;
	}
}

class CustomIntegerFillIn extends org.lgna.croquet.CascadeFillIn< Integer, Void > {
	private static class SingletonHolder {
		private static CustomIntegerFillIn instance = new CustomIntegerFillIn();
	}

	public static CustomIntegerFillIn getInstance() {
		return SingletonHolder.instance;
	}

	private CustomIntegerFillIn() {
		super( java.util.UUID.fromString( "abfa96df-32be-4a94-8f5d-030f173b77e9" ) );
	}
	@Override
	protected javax.swing.JComponent createMenuItemIconProxy( org.lgna.croquet.cascade.ItemNode< ? super Integer,Void > step ) {
		return null;
	}

	@Override
	public javax.swing.Icon getMenuItemIcon( org.lgna.croquet.cascade.ItemNode< ? super Integer,Void > step ) {
		return null;
	}

	@Override
	public String getMenuItemText( org.lgna.croquet.cascade.ItemNode< ? super Integer,Void > step ) {
		return "custom integer...";
	}

	@Override
	public Integer createValue( org.lgna.croquet.cascade.ItemNode< ? super Integer,Void > step ) {
		return 42;
	}

	@Override
	public Integer getTransientValue( org.lgna.croquet.cascade.ItemNode< ? super Integer,Void > step ) {
		return null;
	}
}

class IntegerBlank extends org.lgna.croquet.CascadeBlank< Integer > {
	private static class SingletonHolder {
		private static IntegerBlank instance = new IntegerBlank();
	}

	public static IntegerBlank getInstance() {
		return SingletonHolder.instance;
	}

	private IntegerBlank() {
		super( java.util.UUID.fromString( "7705a77c-b5a9-4955-966d-0350bac1ade9" ) );
	}

	@Override
	protected java.util.List< org.lgna.croquet.CascadeBlankChild> updateChildren( java.util.List< org.lgna.croquet.CascadeBlankChild> rv, org.lgna.croquet.cascade.BlankNode< Integer > blankNode ) {
		for( Integer value : new int[] { 1, 2, 3, 4, 5 } ) {
			rv.add( IntegerLiteralFillIn.getInstance( value ) );
		}
		rv.add( org.lgna.croquet.CascadeLineSeparator.getInstance() );
		rv.add( CustomIntegerFillIn.getInstance() );
		return rv;
	}
}

enum ZodiacSigns {
	ARIES, TAURUS, GEMINI, CANCER, LEO, VIRGO, LIBRA, SCORPIO, SAGITARIUS, CAPRICORN, AQUARIUS, PISCES
}

class MyCascade extends org.lgna.croquet.Cascade< Object > {
	private static class SingletonHolder {
		private static MyCascade instance = new MyCascade();
	}

	public static MyCascade getInstance() {
		return SingletonHolder.instance;
	}

	private MyCascade() {
		super( null, java.util.UUID.fromString( "2c0ba898-1f06-48ff-bc15-65f6f350484b" ), Object.class, new org.lgna.croquet.CascadeBlank[] { new EnumBlank( ZodiacSigns.class ), IntegerBlank.getInstance(), IntegerBlank.getInstance(),
				new EnumBlank( ZodiacSigns.class ), IntegerBlank.getInstance() } );
	}

//	@Override
//	protected void localize() {
//		super.localize();
//		this.getPopupPrepModel().setName( "test cascade" );
//	}

	@Override
	protected org.lgna.croquet.edits.Edit createEdit( org.lgna.croquet.history.CascadeCompletionStep< Object > step, final java.lang.Object[] values ) {
		return new org.lgna.croquet.edits.Edit< org.lgna.croquet.Cascade<Object> >( null ) {
			@Override
			protected void doOrRedoInternal( boolean isDo ) {
				edu.cmu.cs.dennisc.print.PrintUtilities.println( values );
			}
			@Override
			protected void undoInternal() {
			}
			@Override
			protected StringBuilder updatePresentation( StringBuilder rv, java.util.Locale locale ) {
				return null;
			}
		};
	}
}

abstract class CustomState< T > extends org.lgna.croquet.ItemState< T > {
	private class CustomBlank extends org.lgna.croquet.CascadeBlank<T> {
		public CustomBlank() {
			super( java.util.UUID.fromString( "3fa6c08f-550d-4d80-b4a9-71c35c0fd186" ) );
		}
		@Override
		protected java.util.List< org.lgna.croquet.CascadeBlankChild > updateChildren( java.util.List< org.lgna.croquet.CascadeBlankChild> rv, org.lgna.croquet.cascade.BlankNode< T > blankNode ) {
			return CustomState.this.updateBlankChildren( rv, blankNode );
		}
	}
	private class CustomCascade extends org.lgna.croquet.Cascade< T > {
		public CustomCascade( org.lgna.croquet.Group group, Class< T > componentType ) {
			super( group, java.util.UUID.fromString( "a69de613-0642-4ab2-99d2-6c517d96b3d3" ), componentType, new CustomBlank() );
		}
		@Override
		protected org.lgna.croquet.edits.Edit<? extends org.lgna.croquet.Cascade<T>> createEdit(org.lgna.croquet.history.CascadeCompletionStep<T> step, T[] values) {
			return CustomState.this.createEdit( step, CustomState.this.value, values[ 0 ] );
		}
	}
	private final CustomCascade cascade;
	private final T value;
	public CustomState( org.lgna.croquet.Group group, java.util.UUID id, org.lgna.croquet.ItemCodec< T > itemCodec ) {
		super( group, id, itemCodec );
		this.cascade = new CustomCascade( group, itemCodec.getValueClass() );
		this.value = null;
	}
	@Override
	protected void localize() {
	}
	public org.lgna.croquet.Cascade< T > getCascade() {
		return this.cascade;
	}
	protected abstract org.lgna.croquet.edits.Edit<? extends org.lgna.croquet.Cascade<T>> createEdit( org.lgna.croquet.history.CascadeCompletionStep<T> step, T prevValue, T nextValue );
	protected abstract java.util.List< org.lgna.croquet.CascadeBlankChild > updateBlankChildren( java.util.List< org.lgna.croquet.CascadeBlankChild> rv, org.lgna.croquet.cascade.BlankNode< T > blankNode );
	@Override
	public T getValue() {
		return this.value;
	}
}

class CustomItemStateEdit<T> extends org.lgna.croquet.edits.ItemStateEdit< org.lgna.croquet.ItemState< T >, T > {
	public CustomItemStateEdit( org.lgna.croquet.history.CompletionStep< org.lgna.croquet.ItemState<T> > completionStep, T prevValue, T nextValue ) {
		super( completionStep, prevValue, nextValue );
	}
	public CustomItemStateEdit( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder, Object step ) {
		super( binaryDecoder, step );
	}
	@Override
	protected void doOrRedoInternal( boolean isDo ) {
	}
	@Override
	protected void undoInternal() {
	}
}

class EnumState<T extends Enum<T>> extends CustomState< T > { 
	public EnumState( org.lgna.croquet.Group group, java.util.UUID id, Class<T> cls ) {
		super( group, id, edu.cmu.cs.dennisc.toolkit.croquet.codecs.EnumCodec.getInstance( cls ) );
	}

	@Override
	protected org.lgna.croquet.edits.Edit<? extends org.lgna.croquet.Cascade<T>> createEdit(org.lgna.croquet.history.CascadeCompletionStep<T> step, T prevValue, T nextValue) {
		return new CustomItemStateEdit( null, prevValue, nextValue );
	}
	@Override
	protected java.util.List< org.lgna.croquet.CascadeBlankChild > updateBlankChildren( java.util.List< org.lgna.croquet.CascadeBlankChild > rv, org.lgna.croquet.cascade.BlankNode< T > blankNode ) {
		for( T value : this.getItemCodec().getValueClass().getEnumConstants() ) {
			rv.add( EnumConstantFillIn.getInstance( value ) );
		}
		return rv;
	}
}

class CustomStateDropDown extends org.lgna.croquet.components.ViewController< javax.swing.AbstractButton, org.lgna.croquet.Cascade<?> > {
	public CustomStateDropDown( org.lgna.croquet.Cascade<?> cascade ) {
		super( cascade );
	}
	@Override
	protected javax.swing.AbstractButton createAwtComponent() {
		class JDropDown extends javax.swing.AbstractButton {
			public JDropDown() {
//				this.setAction( CustomStateDropDown.this.getModel().getAction() );
				this.setModel( new javax.swing.DefaultButtonModel() );
			}
		}
		return new JDropDown();
	}
}


class CascadePanel extends org.lgna.croquet.components.BorderPanel {
	public CascadePanel() {
		this.setMinimumPreferredWidth( 640 );
		this.setMinimumPreferredHeight( 480 );
		this.addComponent( MyCascade.getInstance().getPopupPrepModel().createPopupButton(), Constraint.PAGE_START );
		EnumState< ZodiacSigns > customState = new EnumState< ZodiacSigns >( null, java.util.UUID.fromString( "03338045-ede5-4e09-bfc0-db74335055a6" ), ZodiacSigns.class );
		this.addComponent( customState.getCascade().getPopupPrepModel().createPopupButton(), Constraint.PAGE_END );
	}
}

/**
 * @author Dennis Cosgrove
 */
public class TestCascade extends org.lgna.croquet.Application {
	@Override
	protected org.lgna.croquet.components.Component< ? > createContentPane() {
		return new CascadePanel();
	}

	@Override
	public org.lgna.croquet.DropReceptor getDropReceptor( org.lgna.croquet.DropSite dropSite ) {
		return null;
	}

	@Override
	protected void handleAbout( org.lgna.croquet.triggers.Trigger trigger ) {
	}

	@Override
	protected void handlePreferences( org.lgna.croquet.triggers.Trigger trigger ) {
	}

	@Override
	protected void handleQuit( org.lgna.croquet.triggers.Trigger trigger ) {
		System.exit( 0 );
	}

	@Override
	protected void handleWindowOpened( java.awt.event.WindowEvent e ) {
	}

	@Override
	protected void handleOpenFile( org.lgna.croquet.triggers.Trigger trigger ) {
	}

	public static void main( String[] args ) {
		TestCascade application = new TestCascade();
		application.initialize( args );
		application.getFrame().pack();
		application.getFrame().setVisible( true );
	}
}
