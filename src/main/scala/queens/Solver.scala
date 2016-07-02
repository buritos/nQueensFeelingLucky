package queens

import scala.collection.mutable
import scala.util.Random

object Solver
{
  val MAX_TRIES = 128

  type Position = (Int, Int)

  /**
    * Attempts to find a solution for n queens.
    *
    * @param n the number of queens to place
    * @param maxTries the maximum number of tries
    * @return tuple3 of
    *         sequence of column indexes for queens in conflict if a solution was not found,
    *         the number of tries it took to place the queens,
    *         sequence of the column indexes where each queen was placed.
    */
  def solve(n: Int, maxTries: Int = MAX_TRIES): (Seq[Int], Int, Seq[Int]) = {

    def attacking(position: Position, queen: Position): Boolean = {
      val (pR, pC) = position
      val (qR, qC) = queen
      if (position == queen) false
      else qR == pR || math.abs(pR-qR) == math.abs(pC-qC)
    }

    val range = 0 until n

    def conflicts(col: Int, placed: Seq[Int]): Seq[Position] = {
      val p = range.par
      (for (i <- p) yield {
        val c = (for {
          (q, r) <- new ZipIterator(placed.iterator, range.iterator)
          if attacking((i, col), (q, r))
        } yield 1).sum
        (i, c)
      }).seq
    }

    val found = mutable.Map[Int, Seq[Int]]()

    def min(conflicts: Seq[Position]): Int = {
      var min = Int.MaxValue
      found.clear()
      for (i <- range) {
        val (row, c) = conflicts(i)
        min = math.min(min, c)
        val f = found.getOrElseUpdate(c, Seq())
        found.update(c, f :+ row)
      }
      val minConflicts = found(min)
      if (minConflicts.nonEmpty)
        minConflicts(Random.nextInt(minConflicts.size))
      else
        Random.nextInt(n)
    }

    def placeQueens(k: Int): Seq[Int] = {
      def loop(queens: Seq[Int], column: Int): Seq[Int] = {
        if (column == k) queens
        else {
          val row = min(conflicts(column, queens))
          loop(queens :+ row, column + 1)
        }
      }
      loop(Vector(), 0)
    }

    var queens = placeQueens(n)

    def conflicted(placed: Seq[Int]): Seq[Int] = {
      for {
        column <- range
        (r, c) <- new ZipIterator(placed.iterator, range.iterator)
        if attacking((placed(column), column), (r, c))
      } yield column
    }

    var inConflict = conflicted(queens)

    var count = 0
    while(inConflict.nonEmpty && count < maxTries) {
      val next = inConflict(Random.nextInt(inConflict.size))
      queens = queens.updated(next, min(conflicts(next, queens)))
      inConflict = conflicted(queens)
      count += 1
    }

    (inConflict, count, queens)
  }
}

class ZipIterator[A, B](a: Iterator[A], b: Iterator[B]) extends Iterator[(A, B)] {
  override def hasNext: Boolean = a.hasNext && b.hasNext

  override def next(): (A, B) = (a.next(), b.next())
}
