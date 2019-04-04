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
package edu.cmu.cs.dennisc.pathfinding;

import edu.cmu.cs.dennisc.java.lang.ThreadUtilities;
import edu.cmu.cs.dennisc.java.util.Lists;

import java.awt.Component;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * @author Dennis Cosgrove
 */
public class Grid {
  private Random s_random = new Random(System.currentTimeMillis());
  private Cell[][] m_cells;

  public Grid(int rowCount, int columnCount) {
    m_cells = new Cell[rowCount][columnCount];
    for (int row = 0; row < rowCount; row++) {
      for (int column = 0; column < columnCount; column++) {
        m_cells[row][column] = new Cell(row, column);
      }
    }
  }

  public int getRowCount() {
    return m_cells.length;
  }

  public int getColumnCount() {
    if (m_cells.length > 0) {
      return m_cells[0].length;
    } else {
      return 0;
    }
  }

  private Cell getAdjacentCell(Cell cell, Heading heading) {
    int row = cell.getRow();
    int column = cell.getColumn();
    switch (heading) {
    case NORTH:
    case NORTH_EAST:
    case NORTH_WEST:
      row--;
      break;
    case SOUTH:
    case SOUTH_EAST:
    case SOUTH_WEST:
      row++;
      break;
    }
    switch (heading) {
    case EAST:
    case NORTH_EAST:
    case SOUTH_EAST:
      column++;
      break;
    case WEST:
    case NORTH_WEST:
    case SOUTH_WEST:
      column--;
      break;
    }

    //todo: replace with bounds check?
    try {
      return m_cells[row][column];
    } catch (ArrayIndexOutOfBoundsException aioobe) {
      return Cell.OUT_OF_BOUNDS_CELL;
    }
  }

  public Cell getCellAt(int row, int column) {
    return m_cells[row][column];
  }

  public Cell getRandomCell() {
    return getCellAt(s_random.nextInt(getRowCount()), s_random.nextInt(getColumnCount()));
  }

  public boolean isWalkableBetweenNeighbors(Cell src, Cell dst) {
    //todo: remove this check?
    if (src.isOccupied()) {
      return false;
    }
    if (dst.isOccupied()) {
      return false;
    }
    int srcRow = src.getRow();
    int srcColumn = src.getColumn();
    int dstRow = dst.getRow();
    int dstColumn = dst.getColumn();

    if ((srcRow != dstRow) && (srcColumn != dstColumn)) {
      if (getCellAt(srcRow, dstColumn).isOccupied()) {
        return false;
      }
      if (getCellAt(dstRow, srcColumn).isOccupied()) {
        return false;
      }
    }
    return true;
  }

  //todo: cache neighbors?
  private Cell[] getNeighbors(Cell src) {
    Cell[] neighbors = new Cell[8];
    neighbors[0] = getAdjacentCell(src, Heading.NORTH);
    neighbors[1] = getAdjacentCell(src, Heading.NORTH_EAST);
    neighbors[2] = getAdjacentCell(src, Heading.EAST);
    neighbors[3] = getAdjacentCell(src, Heading.SOUTH_EAST);
    neighbors[4] = getAdjacentCell(src, Heading.SOUTH);
    neighbors[5] = getAdjacentCell(src, Heading.SOUTH_WEST);
    neighbors[6] = getAdjacentCell(src, Heading.WEST);
    neighbors[7] = getAdjacentCell(src, Heading.NORTH_WEST);
    return neighbors;
  }

  private Cell getCellWithMinimumF(Set<Cell> open, Cell dst) {
    int fMin = Integer.MAX_VALUE;
    Cell cellMin = null;
    for (Cell cell : open) {
      int g = cell.getG();
      int h = cell.getH(dst);
      int f = g + h;
      if (f < fMin) {
        fMin = f;
        cellMin = cell;
      }
    }
    return cellMin;
  }

  public List<Cell> findShortestPathBetween(Cell src, Cell dst, Set<Cell> open, Set<Cell> closed, Component observer) {
    //todo: remove?
    for (int row = 0; row < getRowCount(); row++) {
      for (int column = 0; column < getColumnCount(); column++) {
        m_cells[row][column].setParent(null);
      }
    }
    for (Cell neighbor : getNeighbors(src)) {
      if (neighbor.isOutOfBounds() || !isWalkableBetweenNeighbors(src, neighbor)) {
        //pass
      } else {
        neighbor.setParent(src);
        //neighbor.setParentCache( src );
        open.add(neighbor);
      }
    }
    closed.add(src);
    while (!open.isEmpty()) {
      if (observer != null) {
        observer.repaint();
        ThreadUtilities.sleep(100);
      }
      Cell bestNeighbor = getCellWithMinimumF(open, dst);
      if (bestNeighbor.equals(dst)) {
        List<Cell> path = Lists.newArrayListWithInitialCapacity(closed.size() + 1);
        Cell cell = dst;
        while (cell != null) {
          path.add(cell);
          cell = cell.getParent();
        }
        //todo: reverse list
        return path;
      } else {
        open.remove(bestNeighbor);
        closed.add(bestNeighbor);
        int gCurrent = bestNeighbor.getG();
        for (Cell cell : getNeighbors(bestNeighbor)) {
          if (cell.isOutOfBounds() || !isWalkableBetweenNeighbors(bestNeighbor, cell) || closed.contains(cell)) {
            //pass
          } else {
            Cell parentCache;
            if (open.contains(cell)) {
              if ((gCurrent + bestNeighbor.getGToNeighbor(cell)) < cell.getG()) {
                parentCache = bestNeighbor;
              } else {
                parentCache = cell.getParent();
              }
            } else {
              parentCache = bestNeighbor;
              open.add(cell);
            }
            cell.setParent(parentCache);
          }
        }
      }
    }
    return null;
  }

  public List<Cell> findShortestPathBetween(Cell src, Cell dst) {
    if (src.equals(dst)) {
      List<Cell> path = Lists.newArrayListWithInitialCapacity(2);
      path.add(src);
      path.add(dst);
      return path;
    } else {
      return findShortestPathBetween(src, dst, new HashSet<Cell>(), new HashSet<Cell>(), null);
    }
  }
}
