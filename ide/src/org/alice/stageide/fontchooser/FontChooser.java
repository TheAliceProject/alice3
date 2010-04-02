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
package org.alice.stageide.fontchooser;

/**
 * @author Dennis Cosgrove
 */
public class FontChooser extends javax.swing.JPanel {
	private class Pane extends javax.swing.JPanel {
		private javax.swing.JLabel m_title = new javax.swing.JLabel();
		//private javax.swing.JTextField m_field = new javax.swing.JTextField();
		protected javax.swing.JList m_list = new javax.swing.JList();
		public Pane( String titleText ) {
			m_title.setText( titleText );
			setLayout( new java.awt.GridBagLayout() );
			addComponents( new java.awt.GridBagConstraints() );
			
			m_list.addListSelectionListener( new javax.swing.event.ListSelectionListener() {
				public void valueChanged( javax.swing.event.ListSelectionEvent e ) {
					if( e.getValueIsAdjusting() ) {
						//pass
					} else {
						FontChooser.this.updateSample();
					}
				}
			} );
		}
		protected void addComponents( java.awt.GridBagConstraints gbc ) {
			gbc.fill = java.awt.GridBagConstraints.BOTH;
			gbc.gridwidth = java.awt.GridBagConstraints.REMAINDER;
			gbc.weightx = 1.0;
			gbc.weighty = 0.0;
			add( m_title, gbc );
			//add( m_field, gbc );
			gbc.weighty = 1.0;
			add( new javax.swing.JScrollPane( m_list ), gbc );
		}
	}
	private class FamilyPane extends Pane {
		public FamilyPane() {
			super( "Family:" );
			m_list.setListData( new String[] { "Serif", "SansSerif" } );
		}
		public org.alice.apis.moveandturn.font.FamilyAttribute getFamilyAttribute() {
			Object value = m_list.getSelectedValue();
			org.alice.apis.moveandturn.font.FamilyAttribute rv;
			if( value.equals( "Serif" ) ) {
				rv = org.alice.apis.moveandturn.font.FamilyConstant.SERIF;
			} else {
				rv = org.alice.apis.moveandturn.font.FamilyConstant.SANS_SERIF;
			}
			return rv;
		}
		public void setFamilyAttribute( org.alice.apis.moveandturn.font.FamilyAttribute familyAttribute ) {
			if( familyAttribute == org.alice.apis.moveandturn.font.FamilyConstant.SERIF ) {
				m_list.setSelectedValue( "Serif", true );
			} else {
				m_list.setSelectedValue( "SansSerif", true );
			}
		}
	}
	private class StylePane extends Pane {
		public StylePane() {
			super( "Style:" );
			m_list.setListData( new String[] { "Regular", "Bold", "Italic", "Bold Italic" } );
		}
		public org.alice.apis.moveandturn.font.WeightAttribute getWeightAttribute() {
			Object value = m_list.getSelectedValue();
			org.alice.apis.moveandturn.font.WeightAttribute rv;
			if( value != null && ( value.equals( "Bold" ) || value.equals( "Bold Italic" ) ) ) {
				rv = org.alice.apis.moveandturn.font.WeightConstant.BOLD;
			} else {
				rv = org.alice.apis.moveandturn.font.WeightConstant.REGULAR;
			}
			return rv;
		}
		public org.alice.apis.moveandturn.font.PostureAttribute getPostureAttribute() {
			Object value = m_list.getSelectedValue();
			org.alice.apis.moveandturn.font.PostureAttribute rv;
			if( value != null && ( value.equals( "Italic" ) || value.equals( "Bold Italic" ) ) ) {
				rv = org.alice.apis.moveandturn.font.PostureConstant.OBLIQUE;
			} else {
				rv = org.alice.apis.moveandturn.font.PostureConstant.REGULAR;
			}
			return rv;
		}
		public void setStyleAttributes(  org.alice.apis.moveandturn.font.WeightAttribute weight, org.alice.apis.moveandturn.font.PostureAttribute posture ) {
			boolean isBold = ( weight == org.alice.apis.moveandturn.font.WeightConstant.BOLD );
			boolean isItalic = ( posture == org.alice.apis.moveandturn.font.PostureConstant.OBLIQUE );
			Object selectedValue;
			if( isBold ) {
				if( isItalic ) {
					selectedValue = "Bold Italic";
				} else {
					selectedValue = "Bold";
				}
			} else {
				if( isItalic ) {
					selectedValue = "Italic";
				} else {
					selectedValue = "Regular";
				}
			}
			m_list.setSelectedValue( selectedValue, true );
		}
	}
	private class SizePane extends Pane {
		public SizePane() {
			super( "Size:" );
			m_list.setListData( new String[] { "8", "9", "10", "12", "14", "18", "24", "32", "48", "64", "96" } );
		}
		
		public org.alice.apis.moveandturn.font.SizeAttribute getSizeAttribute() {
			Object value = m_list.getSelectedValue();
			if( value instanceof String ) {
				return new org.alice.apis.moveandturn.font.SizeValue( Float.valueOf( (String) value ) );
			} else {
				return null;
			}
		}
		public void setSizeAttribute( org.alice.apis.moveandturn.font.SizeAttribute sizeAttribute ) {
			int size = sizeAttribute.getValue().intValue();
			m_list.setSelectedValue( Integer.toString( size ), true );
		}
//		@Override
//		protected void addComponents( java.awt.GridBagConstraints gbc ) {
//			super.addComponents( gbc );
//		}
	}
	private FamilyPane m_familyPane = new FamilyPane();
	private StylePane m_stylePane = new StylePane();
	private SizePane m_sizePane = new SizePane();
	private javax.swing.JLabel m_sample = new javax.swing.JLabel() {
		@Override
		public void paint( java.awt.Graphics g ) {
			((java.awt.Graphics2D)g).setRenderingHint( java.awt.RenderingHints.KEY_TEXT_ANTIALIASING, java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON );
			super.paint( g );
		}
	};
	public FontChooser() {
		this( new org.alice.apis.moveandturn.Font() );
	}
	public FontChooser( org.alice.apis.moveandturn.Font font ) {
		setSampleText( null );
		m_sample.setHorizontalAlignment( javax.swing.SwingConstants.CENTER );
		m_sample.setVerticalAlignment( javax.swing.SwingConstants.CENTER );

		final int INSET = 8;
		setLayout( new java.awt.GridBagLayout() );
		java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
		gbc.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gbc.fill = java.awt.GridBagConstraints.BOTH;
		
		gbc.gridy = 0;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		add( m_familyPane, gbc );
		gbc.insets.left = INSET;
		add( m_stylePane, gbc );
		gbc.insets.left = 0;
		//add( m_sizePane, gbc );
		
		
		gbc.gridy = 1;
		gbc.anchor = java.awt.GridBagConstraints.CENTER;
		gbc.gridwidth = 2;
		gbc.weighty = 0.0;
		add( m_sample, gbc );

		gbc.gridy = 2;
		gbc.weighty = 1.0;
		add( new javax.swing.JPanel(), gbc );
		
		setValue( font );
	}
	
	public org.alice.apis.moveandturn.Font getValue() {
		org.alice.apis.moveandturn.font.FamilyAttribute family = m_familyPane.getFamilyAttribute();
		org.alice.apis.moveandturn.font.WeightAttribute weight = m_stylePane.getWeightAttribute();
		org.alice.apis.moveandturn.font.PostureAttribute posture = m_stylePane.getPostureAttribute();
		org.alice.apis.moveandturn.font.SizeAttribute size = m_sizePane.getSizeAttribute();
		if( size != null ) {
			return new org.alice.apis.moveandturn.Font( family, weight, posture, size );
		} else {
			return null;
		}
	}
	public void setValue( org.alice.apis.moveandturn.Font font ) {
		m_familyPane.setFamilyAttribute( font.getFamily() );
		m_stylePane.setStyleAttributes( font.getWeight(), font.getPosture() );
		m_sizePane.setSizeAttribute( font.getSize() );
	}
	
	public void setSampleText( String sampleText ) {
		if( sampleText != null && sampleText.length() > 0 ) {
			//pass
		} else {
			sampleText = "AaBbYyZz";
		}
		m_sample.setText( sampleText );
	}
	
	private void updateSample() { 
		org.alice.apis.moveandturn.Font font = getValue();
		if( font != null ) {
			m_sample.setFont( font.getAsAWTFont() );
		}
	}
}
