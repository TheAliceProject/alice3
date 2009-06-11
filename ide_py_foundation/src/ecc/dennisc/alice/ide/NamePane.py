import java
import javax
import edu

import ecc

from TextPaneWithValidation import TextPaneWithValidation

class NamePane( TextPaneWithValidation ):
	def __init__( self, selected, siblings ):
		self._selected = selected
		self._siblings = siblings
		if self._selected:
			initialText = self._selected.getName()
		else:
			initialText = ""
		TextPaneWithValidation.__init__( self, "name:", initialText )
		
	def _isValidText( self, text ):
		if self._selected and self._selected.getName()==text:
			return True
		else:
			for sibling in self._siblings:
				if sibling.getName() == text:
					return False
			return True
