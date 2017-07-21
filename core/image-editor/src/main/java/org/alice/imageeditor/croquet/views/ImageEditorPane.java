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
package org.alice.imageeditor.croquet.views;

/**
 * @author Dennis Cosgrove
 */
public class ImageEditorPane extends org.lgna.croquet.views.MigPanel {
	private final java.awt.event.FocusListener comboBoxEditorFocusListener = new java.awt.event.FocusListener() {
		@Override
		public void focusGained( java.awt.event.FocusEvent e ) {
			javax.swing.SwingUtilities.invokeLater( new Runnable() {
				@Override
				public void run() {
					javax.swing.ComboBoxEditor editor = getComposite().getJComboBox().getEditor();
					java.awt.Component editorComponent = editor.getEditorComponent();
					if( editorComponent instanceof javax.swing.text.JTextComponent ) {
						javax.swing.text.JTextComponent jTextComponent = (javax.swing.text.JTextComponent)editorComponent;
						String rootDirectoryPath = getComposite().getRootDirectoryState().getValue();
						String path = jTextComponent.getText();
						if( path.startsWith( rootDirectoryPath ) ) {
							int startIndex = rootDirectoryPath.length();
							if( path.length() > startIndex ) {
								char c = path.charAt( startIndex );
								if( ( c == '/' ) || ( c == '\\' ) ) {
									startIndex += 1;
								}
							}
							int endIndex = path.length();
							if( path.endsWith( ".png" ) ) {
								endIndex -= 4;
							}
							jTextComponent.select( startIndex, endIndex );
						} else {
							jTextComponent.selectAll();
						}
					} else {
						editor.selectAll();
					}
				}
			} );
		}

		@Override
		public void focusLost( java.awt.event.FocusEvent e ) {
		}
	};

	private final org.lgna.croquet.event.ValueListener<java.awt.Image> imageListener = new org.lgna.croquet.event.ValueListener<java.awt.Image>() {
		@Override
		public void valueChanged( org.lgna.croquet.event.ValueEvent<java.awt.Image> e ) {
			repaint();
		}
	};

	private final org.lgna.croquet.event.ValueListener<String> pathListener = new org.lgna.croquet.event.ValueListener<String>() {
		@Override
		public void valueChanged( org.lgna.croquet.event.ValueEvent<String> e ) {
			updatePathLabel( e.getNextValue() );
		}
	};

	private final org.lgna.croquet.event.ValueListener<Boolean> repaintImageViewListener = new org.lgna.croquet.event.ValueListener<Boolean>() {
		@Override
		public void valueChanged( org.lgna.croquet.event.ValueEvent<Boolean> e ) {
			jImageView.repaint();
		}
	};

	private final org.lgna.croquet.event.ValueListener<Boolean> revalidateImageViewAndResizeWindowIfNecessaryListener = new org.lgna.croquet.event.ValueListener<Boolean>() {
		@Override
		public void valueChanged( org.lgna.croquet.event.ValueEvent<Boolean> e ) {
			jImageView.revalidate();
			org.lgna.croquet.views.AbstractWindow<?> window = getRoot();
			if( window != null ) {
				java.awt.Dimension size = window.getSize();
				java.awt.Dimension preferredSize = window.getAwtComponent().getPreferredSize();
				if( ( size.width < preferredSize.width ) || ( size.height < preferredSize.height ) ) {
					window.setSize( Math.max( size.width, preferredSize.width ), Math.max( size.height, preferredSize.height ) );
				}
			}
		}
	};

	private class SelectToolActionListener implements java.awt.event.ActionListener {
		private final org.alice.imageeditor.croquet.Tool tool;

		public SelectToolActionListener( org.alice.imageeditor.croquet.Tool tool ) {
			this.tool = tool;
		}

		@Override
		public void actionPerformed( java.awt.event.ActionEvent e ) {
			getComposite().getToolState().setValueTransactionlessly( this.tool );
		}
	}

	private static final javax.swing.KeyStroke CROP_KEY_STROKE = javax.swing.KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_F12, 0 );
	private static final javax.swing.KeyStroke RECTANGLE_KEY_STROKE = javax.swing.KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_F11, 0 );
	private final java.awt.event.ActionListener cropListener = new SelectToolActionListener( org.alice.imageeditor.croquet.Tool.CROP_SELECT );
	private final java.awt.event.ActionListener rectangleListener = new SelectToolActionListener( org.alice.imageeditor.croquet.Tool.ADD_RECTANGLE );

	private final JImageEditorView jImageView;

	private final edu.cmu.cs.dennisc.javax.swing.JShowLabel jPathLabel = new edu.cmu.cs.dennisc.javax.swing.JShowLabel();

	private final org.lgna.croquet.views.Button saveButton;
	private final org.lgna.croquet.views.Button cropButton;

	public ImageEditorPane( org.alice.imageeditor.croquet.ImageEditorFrame composite ) {
		super( composite, "fill", "[grow 0][grow 100]16[grow 0]", "[grow,shrink]16[grow 0, shrink 0][grow 0, shrink 0][grow 0, shrink 0]" );

		this.jImageView = new JImageEditorView( composite );
		this.saveButton = composite.getSaveOperation().createButton();

		javax.swing.JComboBox jComboBox = composite.getJComboBox();
		jComboBox.setRenderer( new org.alice.imageeditor.croquet.views.renderers.FilenameListCellRenderer() );
		jComboBox.setMaximumRowCount( 24 );
		jComboBox.setMinimumSize( new java.awt.Dimension( 0, 0 ) );

		this.getAwtComponent().add( this.jImageView, "align center, spanx 2" );

		org.lgna.croquet.views.MigPanel panel = new org.lgna.croquet.views.MigPanel( null, "insets 0, fill" );

		this.cropButton = composite.getCropOperation().createButton();
		panel.addComponent( composite.getToolState().createVerticalDefaultRadioButtons(), "growx, wrap" );
		panel.addComponent( this.cropButton, "growx, split 2" );
		panel.addComponent( composite.getUncropOperation().createButton(), "growx, wrap" );
		panel.addComponent( composite.getClearOperation().createButton(), "growx, wrap" );
		panel.addComponent( composite.getDropShadowState().createCheckBox(), "growx, wrap" );
		panel.addComponent( org.lgna.croquet.views.Separator.createInstanceSeparatingTopFromBottom(), "growx, gap bottom 16, wrap" );
		panel.addComponent( composite.getShowInScreenResolutionState().getSidekickLabel().createLabel(), "split 2" );
		panel.addComponent( composite.getShowInScreenResolutionState().createHorizontalToggleButtons(), "growx, wrap" );
		panel.addComponent( composite.getShowDashedBorderState().createCheckBox(), "growx, wrap" );
		panel.addComponent( composite.getCopyOperation().createButton(), "pushy, aligny bottom, growx" );
		this.addComponent( panel, "aligny top, grow, shrink, wrap" );

		this.addComponent( composite.getRootDirectoryState().getSidekickLabel().createLabel(), "align right" );
		this.addComponent( composite.getRootDirectoryState().createTextField(), "split 2, growx, shrinkx" );
		this.addComponent( composite.getBrowseOperation().createButton() );
		this.addComponent( this.saveButton, "spany 3, grow, wrap" );

		this.addComponent( new org.lgna.croquet.views.Label( "file:" ), "align right" );
		this.getAwtComponent().add( jComboBox, "growx, shrinkx, wrap" );
		this.getAwtComponent().add( this.jPathLabel, "skip 1" );

		this.jPathLabel.setForeground( java.awt.Color.DARK_GRAY );

		this.updatePathLabel( composite.getPathHolder().getValue() );
	}

	private void updatePathLabel( String nextPath ) {
		if( edu.cmu.cs.dennisc.java.io.FileUtilities.isValidPath( nextPath ) ) {
			//pass
		} else {
			edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "INVALID PATH:", nextPath );
		}

		this.jPathLabel.setText( nextPath );
		if( nextPath != null ) {
			java.awt.Component awtEditorComponent = this.getComposite().getJComboBox().getEditor().getEditorComponent();
			if( awtEditorComponent instanceof javax.swing.JTextField ) {
				javax.swing.JTextField jTextField = (javax.swing.JTextField)awtEditorComponent;
				boolean isEqual = jTextField.getText().contentEquals( nextPath );
				boolean isShowing = false == ( isEqual || org.alice.imageeditor.croquet.ImageEditorFrame.INVALID_PATH_NOT_A_DIRECTORY.contentEquals( nextPath ) || org.alice.imageeditor.croquet.ImageEditorFrame.INVALID_PATH_EMPTY_SUB_PATH.contentEquals( nextPath ) );
				this.jPathLabel.setShowing( isShowing );
			}
		}
	}

	@Override
	public org.alice.imageeditor.croquet.ImageEditorFrame getComposite() {
		return (org.alice.imageeditor.croquet.ImageEditorFrame)super.getComposite();
	}

	public java.awt.Image render() {
		org.alice.imageeditor.croquet.ImageEditorFrame composite = getComposite();
		java.awt.Image image = composite.getImageHolder().getValue();
		if( image != null ) {
			int width;
			int height;
			java.awt.Rectangle selection = composite.getCropSelectHolder().getValue();
			assert selection == null : selection;
			java.awt.Rectangle crop = composite.getCropCommitHolder().getValue();
			if( crop != null ) {
				width = crop.width;
				height = crop.height;
			} else {
				width = image.getWidth( this.jImageView );
				height = image.getHeight( this.jImageView );
			}
			java.awt.image.BufferedImage rv = new java.awt.image.BufferedImage( width, height, java.awt.image.BufferedImage.TYPE_INT_BGR );
			java.awt.Graphics2D g2 = (java.awt.Graphics2D)rv.getGraphics();
			this.jImageView.render( g2 );
			g2.dispose();
			return rv;
		} else {
			return null;
		}
	}

	public void setDefaultButtonToSave() {
		this.getRoot().setDefaultButton( this.saveButton );
	}

	public void setDefaultButtonToCrop() {
		this.getRoot().setDefaultButton( this.cropButton );
	}

	@Override
	public void handleCompositePreActivation() {
		this.getComposite().getShowInScreenResolutionState().addNewSchoolValueListener( this.revalidateImageViewAndResizeWindowIfNecessaryListener );
		this.getComposite().getShowDashedBorderState().addNewSchoolValueListener( this.repaintImageViewListener );
		this.getComposite().getDropShadowState().addNewSchoolValueListener( this.repaintImageViewListener );
		this.getComposite().getImageHolder().addAndInvokeValueListener( this.imageListener );
		this.getComposite().getPathHolder().addAndInvokeValueListener( this.pathListener );
		java.awt.Component awtEditorComponent = this.getComposite().getJComboBox().getEditor().getEditorComponent();
		awtEditorComponent.addFocusListener( this.comboBoxEditorFocusListener );
		this.setDefaultButtonToSave();
		javax.swing.SwingUtilities.invokeLater( new Runnable() {
			@Override
			public void run() {
				getComposite().getJComboBox().getEditor().getEditorComponent().requestFocusInWindow();
			}
		} );
		this.registerKeyboardAction( this.cropListener, CROP_KEY_STROKE, Condition.WHEN_IN_FOCUSED_WINDOW );
		this.registerKeyboardAction( this.rectangleListener, RECTANGLE_KEY_STROKE, Condition.WHEN_IN_FOCUSED_WINDOW );
		super.handleCompositePreActivation();
	}

	@Override
	public void handleCompositePostDeactivation() {
		super.handleCompositePostDeactivation();
		this.unregisterKeyboardAction( RECTANGLE_KEY_STROKE );
		this.unregisterKeyboardAction( CROP_KEY_STROKE );
		java.awt.Component awtEditorComponent = this.getComposite().getJComboBox().getEditor().getEditorComponent();
		awtEditorComponent.removeFocusListener( this.comboBoxEditorFocusListener );
		this.getComposite().getPathHolder().removeValueListener( this.pathListener );
		this.getComposite().getImageHolder().removeValueListener( this.imageListener );
		this.getComposite().getDropShadowState().removeNewSchoolValueListener( this.repaintImageViewListener );
		this.getComposite().getShowDashedBorderState().removeNewSchoolValueListener( this.repaintImageViewListener );
		this.getComposite().getShowInScreenResolutionState().removeNewSchoolValueListener( this.revalidateImageViewAndResizeWindowIfNecessaryListener );
	}
}
