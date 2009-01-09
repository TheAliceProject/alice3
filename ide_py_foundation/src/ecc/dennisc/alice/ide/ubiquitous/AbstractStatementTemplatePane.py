from edu.cmu.cs.dennisc import alice

class AbstractStatementTemplatePane( alice.ide.editors.ubiquitous.UbiquitousStatementTemplatePane ):
	def __init__( self, cls ):
		alice.ide.editors.ubiquitous.UbiquitousStatementTemplatePane.__init__( self, cls, apply( cls, self._createEmptyStatementArgs() ) )
	def _createEmptyStatementArgs( self ):
		return self._createActualStatementArgs()
	def _createActualStatementArgs( self ):
		raise "Override"
	def createStatement( self, e ):
		return apply( self.getCls(), self._createActualStatementArgs() )
