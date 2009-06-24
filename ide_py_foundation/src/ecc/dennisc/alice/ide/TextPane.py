import java
import javax
import edu

import ecc

class TextPane( javax.swing.JPanel ):
	def __init__( self, label, initialText, inset=8 ):
		javax.swing.JPanel.__init__( self )
		self._textVC = javax.swing.JTextField( 10 );
		self._textVC.getDocument().addDocumentListener( ecc.dennisc.swing.event.FilteredDocumentAdapter( self._handleTextChanged ) )
		self._textVC.addActionListener( ecc.dennisc.swing.event.ActionAdapter( self._handleTextActionPerformed ) )
		if initialText:
			self._textVC.setText( initialText )
			self._textVC.selectAll()

		self.setLayout( java.awt.BorderLayout() )
		self.add( javax.swing.JLabel( label, javax.swing.JLabel.TRAILING ), java.awt.BorderLayout.WEST )
		self.add( self._textVC, java.awt.BorderLayout.CENTER )
		
		self.setBorder( javax.swing.border.EmptyBorder( inset, inset, inset, inset ) )

	def getText( self ):
		return self._textVC.getText()

	def _handleTextChanged(self, text):
		raise "Override"
	def _handleTextActionPerformed(self, e):
		raise "Override"
