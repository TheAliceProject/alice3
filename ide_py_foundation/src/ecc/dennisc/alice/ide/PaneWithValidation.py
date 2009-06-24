import java
import javax
import edu

class PaneWithValidation( javax.swing.JPanel, edu.cmu.cs.dennisc.pattern.Validator ):
	def __init__( self, inset=8 ):
		javax.swing.JPanel.__init__( self )
		self.setBorder( javax.swing.border.EmptyBorder( inset, inset, inset, inset ) )
		self._inputPane = None
	def setInputPane(self, inputPane):
		self._inputPane = inputPane
	def isValid( self ):
		raise "Override"
	def updateOKButton(self):
		if self._inputPane:
			self._inputPane.updateOKButton()
	def fireOKButtonIfPossible(self):
		if self._inputPane:
			self._inputPane.fireOKButtonIfPossible()

