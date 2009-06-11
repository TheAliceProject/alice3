import java
import javax
import edu

from edu.cmu.cs.dennisc import alice
from edu.cmu.cs.dennisc import awt
from org.alice import apis
from edu.cmu.cs.dennisc import lookingglass

import ecc
	
class AbstractTextRenderer( edu.cmu.cs.dennisc.lookingglass.overlay.Renderer ):
	def __init__( self ):
		self._bounds = None
		self._font = java.awt.Font( None, java.awt.Font.BOLD, 24 )
	def getText( self ):
		raise "Override"
	
	def getTextX( self ):
		return int( - self._bounds.getX() )
	def getTextY( self ):
		return int( - self._bounds.getY() ) 
	
	def getWidth( self ):
		return self._bounds.getWidth()
	def getHeight( self ):
		return self._bounds.getHeight()
	def layoutIfNecessary( self, g, isHighlighted, isPressed, isSelected ):
		fm = g.getFontMetrics( self._font );
		self._bounds = fm.getStringBounds( self.getText(), g );
		
	
	def _drawText(self, g2, x, y ):
		text = self.getText()
		g2.drawString( text, x, y );
	
	def paint( self, g2, isHighlighted, isPressed, isSelected ):
		width = int( self.getWidth() + 0.999 )
		height = int( self.getHeight() + 0.999 )
		DROP_SHADOW_OFFSET = 4

		if( isPressed ):
			forePaint = java.awt.Color( 128, 128, 0 )
			backPaint = None
		else:
			if( isHighlighted ):
				forePaint = java.awt.Color.WHITE
				backPaint = java.awt.Color.BLACK
			else:
				forePaint = java.awt.Color.YELLOW
				backPaint = java.awt.Color.DARK_GRAY


		#g2.setColor( java.awt.Color.BLUE )
		#g2.fillRect( 0, 0, int( self._bounds.width ), int( self._bounds.height ) )

		g2.setRenderingHint( java.awt.RenderingHints.KEY_TEXT_ANTIALIASING, java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON );
		x = self.getTextX()
		y = self.getTextY()
		g2.setFont( self._font )
		if backPaint:
			g2.setPaint( backPaint )
			self._drawText( g2, x+2, y+2 )
		g2.setPaint( forePaint )
		self._drawText( g2, x, y )

class ExpandContractToggleRenderer( AbstractTextRenderer ):
	PAD = 16
	PAD_2 = PAD + PAD
	EXPANDED_TEXT = "edit program"
	CONTRACTED_TEXT = "add instances"
	
	def __init__( self ):
		AbstractTextRenderer.__init__( self )
		self._isExpanded = False
	
	def getText( self ):
		if self._isExpanded:
			return ExpandContractToggleRenderer.EXPANDED_TEXT
		else:
			return ExpandContractToggleRenderer.CONTRACTED_TEXT

	def isExpanded( self ):
		return self._isExpanded
	def setExpanded( self, isExpanded ):
		self._isExpanded = isExpanded
	def toggleExpanded( self ):
		self._isExpanded = not self._isExpanded
	
	def layoutIfNecessary( self, g, isHighlighted, isPressed, isSelected ):
		AbstractTextRenderer.layoutIfNecessary( self, g, isHighlighted, isPressed, isSelected )
		self._bounds.x -= ExpandContractToggleRenderer.PAD
		self._bounds.y -= ExpandContractToggleRenderer.PAD
		self._bounds.width += ExpandContractToggleRenderer.PAD_2
		self._bounds.height += ExpandContractToggleRenderer.PAD_2

	def _drawText(self, g2, x, y ):
		AbstractTextRenderer._drawText( self, g2, x, y )
		g2.setStroke( java.awt.BasicStroke( 3.0, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_ROUND ) )
		if self.isExpanded():
			x = 0
			y = 8
			minor = 4
			MAJOR = 32
			g2.drawLine( x+minor, y+MAJOR, x+minor, y+minor )
			g2.drawLine( x+minor, y+minor, x+MAJOR, y+minor )
			minor += 6
			MAJOR -= 4
			g2.drawLine( x+minor, y+MAJOR, x+minor, y+minor )
			g2.drawLine( x+minor, y+minor, x+MAJOR, y+minor )
		else:
			x = int( self._bounds.width )
			y = int( self._bounds.height ) - 4
			minor = -4
			MAJOR = -32
			g2.drawLine( x+minor, y+MAJOR, x+minor, y+minor )
			g2.drawLine( x+minor, y+minor, x+MAJOR, y+minor )
			minor += -6
			MAJOR -= -4
			g2.drawLine( x+minor, y+MAJOR, x+minor, y+minor )
			g2.drawLine( x+minor, y+minor, x+MAJOR, y+minor )
	

class ExpandContractToggleButton( alice.ide.overlay.Button ):
	def __init__( self ):
		alice.ide.overlay.Button.__init__( self, ecc.dennisc.alice.ide.operations.window.ToggleExpandContractSceneEditorOperation(), ExpandContractToggleRenderer() )

