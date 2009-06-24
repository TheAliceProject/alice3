import java
import javax
import edu

import ecc

from TextPane import TextPane

class TextPaneWithValidation( TextPane, edu.cmu.cs.dennisc.pattern.Validator ):
	def __init__( self, label, initialText ):
		self._inputPane = None
		TextPane.__init__( self, label, initialText )
	def setInputPane(self, inputPane):
		self._inputPane = inputPane
	def _isValidText(self, name ):
		raise "Override"
	def isValid( self ):
		return self._isValidText( self.getText() )
	def _handleTextChanged(self, text):
		if self.isValid():
			self._textVC.setForeground( java.awt.Color.BLACK )
		else:
			self._textVC.setForeground( java.awt.Color.RED )
		if self._inputPane:
			self._inputPane.updateOKButton()
	def _handleTextActionPerformed(self, e):
		if self._inputPane:
			self._inputPane.fireOKButtonIfPossible()
