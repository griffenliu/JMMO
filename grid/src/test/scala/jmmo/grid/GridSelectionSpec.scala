/*
 * Copyright (C) 2013 Tomas Shestakov. <https://github.com/Megaprog/JMMO>
 */

package jmmo.grid

import org.scalatest.{Failed, Canceled, WordSpec}
import org.scalatest.matchers.ShouldMatchers
import jmmo.util.{Point2d, Cell}

/**
 * This specification only valid if cellSize = 10
 *
 * All grid traits except [[jmmo.grid.SingleLevelPosCache]] suppose
 * what each object in the grid is in the center of the cell it occupies.
 *
 * [[jmmo.grid.SingleLevelPosCache]] suppose what each object in the grid is in
 * `objectPosition(object)` position in outer (points) coordinate system.
 * @author Tomas Shestakov
 */
class GridSelectionSpec(gridSelection: AbstractGrid[Dummy]) extends WordSpec with ShouldMatchers {

  def this() = this(new TestGrid(10, 100, 100) with STArrayGridData[Dummy]
    with STImmSetGridData[Dummy]
    with MTBlockingGridData[Dummy])

  import gridSelection._

  val o0 = new Dummy(Point2d(0, 0), Set(Cell(0, 0)))
  val o1 = new Dummy(Point2d(15, 30), Set(Cell(0, 0)))
  val o2 = new Dummy(Point2d(18, 49), Set(Cell(0, 0)))
  val o3 = new Dummy(Point2d(40, 30), Set(Cell(0, 0)))
  val o4 = new Dummy(Point2d(65, 25), Set(Cell(-1, 0), Cell(0, 0), Cell(1, 0)))
  val o5 = new Dummy(Point2d(outerWidth - 1, outerHeight - 1), Set(Cell(0, 0)))

  override protected def withFixture(test: NoArgTest) = {
    //innitialize grid
    addObject(o0)
    addObject(o1)
    addObject(o2)
    addObject(o3)
    addObject(o4)
    addObject(o5)

    try
      if (cellSize == 10) test() else Canceled("cell size is too small") //run test only when cellSize = 10

    finally {
      //clear grid
      removeObject(o0)
      removeObject(o1)
      removeObject(o2)
      removeObject(o3)
      removeObject(o4)
      removeObject(o5)
    }
  }

  "Any AbstractGrid except SingleLevelPosCache with objects:" should {

    info("o0 = Dummy(Point2d(0, 0), Set(Cell(0, 0)))")
    info("o1 = Dummy(Point2d(15, 30), Set(Cell(0, 0)))")
    info("o2 = Dummy(Point2d(18, 49), Set(Cell(0, 0)))")
    info("o3 = Dummy(Point2d(40, 30), Set(Cell(0, 0)))")
    info("o4 = Dummy(Point2d(65, 25), Set(Cell(-1, 0), Cell(0, 0), Cell(1, 0)))")
    info("o5 = Dummy(Point2d(outerWidth - 1, outerHeight - 1), Set(Cell(0, 0)))")

    "provide getObjectsInRect" which {

      "returns o3 and o4 on call getObjectsInRect(Point2d(18,20),Point2d(59, 49))" in {
        getObjectsInRect(Point2d(18, 20), Point2d(59, 49)) should be(Set(o3, o4))
      }

      "returns o1, o2, o3, o4 on call getObjectsInRect(Point2d(15,20),Point2d(59, 49))" in {
        getObjectsInRect(Point2d(15, 20), Point2d(59, 49)) should be(Set(o1, o2, o3, o4))
      }

      "returns non objects on call getObjectsInRect(Point2d(40,30),Point2d(40, 30))" in {
        getObjectsInRect(Point2d(40, 30), Point2d(40, 30)) should be(Set())
      }

      "returns o3 on call getObjectsInRect(Point2d(45,35),Point2d(45, 35))" in {
        getObjectsInRect(Point2d(45, 35), Point2d(45, 35)) should be(Set(o3))
      }

      "returns non objects  on call getObjectsInRect(Point2d(45,35),Point2d(44, 35))" in {
        getObjectsInRect(Point2d(45, 35), Point2d(44, 35)) should be(Set())
      }

      "returns non objects  on call getObjectsInRect(Point2d(45,35),Point2d(45, 34))" in {
        getObjectsInRect(Point2d(45, 35), Point2d(45, 34)) should be(Set())
      }
      "returns all objects in Grid on call getObjectsInRect(Point2d(0,0),Point2d(outerWidth-1,outerHeight-1))" in {
        getObjectsInRect(Point2d(0, 0), Point2d(outerWidth - 1, outerHeight - 1)) should be(Set(o0, o1, o2, o3, o4, o5))
      }
    }

    "provide getObjectsInRectCell" which {

      "returns o1, o2, o3, o4 on call getObjectsInRectCell(pointToCell(18,20),pointToCell(59, 49))" in {
        getObjectsInRectCell(pointToCell(18, 20), pointToCell(59, 49)) should be(Set(o1, o2, o3, o4))
      }

      "returns o3 on call getObjectsInRectCell(Cell(4,3),Cell(4, 3))" in {
        getObjectsInRectCell(Cell(4, 3), Cell(4, 3)) should be(Set(o3))
      }

      "returns non objects on call getObjectsInRectCell(Cell(4,3),Cell(3, 3))" in {
        getObjectsInRectCell(Cell(4, 3), Cell(3, 3)) should be(Set())
      }

      "returns non objects on call getObjectsInRectCell(Cell(4,3),Cell(4, 2))" in {
        getObjectsInRectCell(Cell(4, 3), Cell(4, 2)) should be(Set())
      }

      "returns all objects in Grid on call getObjectsInRectCell(Cell(0,0),Cell(width-1,height-1))" in {
        getObjectsInRectCell(Cell(0, 0), Cell(width - 1, height - 1)) should be(Set(o0, o1, o2, o3, o4, o5))
      }
    }

    "provide getObjectsInRadius" which {

      "returns o3 on call getObjectsInRadius(Point2d(40,30),15)" in {
        getObjectsInRadius(Point2d(40, 30), 15) should be(Set(o3))
      }

      "returns o3 and o4 on call getObjectsInRadius(Point2d(40,30),16)" in {
        getObjectsInRadius(Point2d(40, 30), 16) should be(Set(o3, o4))
      }

      "returns o3 and o4 on call getObjectsInRadius(Point2d(40,30),25)" in {
        getObjectsInRadius(Point2d(40, 30), 25) should be(Set(o3, o4))
      }

      "returns o1, o3, o4 on call getObjectsInRadius(Point2d(40,30),26)" in {
        getObjectsInRadius(Point2d(40, 30), 26) should be(Set(o1, o3, o4))
      }

      "returns non objects on call getObjectsInRadius(Point2d(40,30),1)" in {
        getObjectsInRadius(Point2d(40, 30), 1) should be(Set())
      }
      "returns non objects on call getObjectsInRadius(Point2d(45,35),0)" in {
        getObjectsInRadius(Point2d(45, 35), 0) should be(Set())
      }

      "returns o3 on call getObjectsInRadius(Point2d(45,35),1)" in {
        getObjectsInRadius(Point2d(45, 35), 1) should be(Set(o3))
      }
    }

    "provide getObjectsInRadiusCell" which {

      "returns non objects on call getObjectsInRadiusCell(Cell(4,3),0)" in {
        getObjectsInRadiusCell(Cell(4, 3), 0) should be(Set())
      }

      "returns o3 on call getObjectsInRadiusCell(Cell(4,3),1)" in {
        getObjectsInRadiusCell(Cell(4, 3), 1) should be(Set(o3))
      }

      "returns o3 and o4 on call getObjectsInRadiusCell(Cell(4,3),2)" in {
        getObjectsInRadiusCell(Cell(4, 3), 2) should be(Set(o3, o4))
      }

      "returns o1, o3, o4 on call getObjectsInRadiusCell(Cell(4,3),3)" in {
        getObjectsInRadiusCell(Cell(4, 3), 3) should be(Set(o1, o3, o4))
      }
    }
  }

}
