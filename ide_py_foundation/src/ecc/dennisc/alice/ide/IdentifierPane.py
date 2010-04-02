#print "-->", __name__

import ecc
from NamePane import NamePane

class IdentifierPane( NamePane ):
	def _isValidText( self, text ):
		return ecc.dennisc.alice.vm.isValidIdentifier( text ) and NamePane._isValidText( self, text ) 

#print "<--", __name__
