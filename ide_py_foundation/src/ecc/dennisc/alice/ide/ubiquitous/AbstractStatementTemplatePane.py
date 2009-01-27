from edu.cmu.cs.dennisc import alice

class AbstractStatementTemplatePane( alice.ide.editors.ubiquitous.UbiquitousStatementTemplatePane ):
	def __init__( self, statementCls ):
		self._statementCls = statementCls
		alice.ide.editors.ubiquitous.UbiquitousStatementTemplatePane.__init__( self, apply( self._statementCls, self._createEmptyStatementArgs() ) )
	def _createEmptyStatementArgs( self ):
		return self._createActualStatementArgs()
	def _createActualStatementArgs( self ):
		raise "Override"
	def getStatementClass(self):
		return self._statementCls
	def createStatement( self, e ):
		return apply( self.getStatementClass(), self._createActualStatementArgs() )
