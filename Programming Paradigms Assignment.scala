
object Assignment {
  def main(args: Array[String]) {

    //Test
    //a
    val a = new pictureOne(new rowOne(new Triangle(4, 4, "Green")))
    println("Picture One: " + a.toString())

    //b
    val b = new pictureTwo(new rowOne(new Square(5, "Red")),
      new rowThree(new Square(5, "Red"), new Circle(5, "Green"), new Triangle(4, 9, "Blue")))
    println("Picture Two: " + b.toString())

    //c
    val c = new pictureThree(new rowOne(new Circle(5, "Blue")),
      new rowOne(new Triangle(4, 4, "Green")),
      new rowOne(new Square(4, "Red")))
    println("Picture Three: " + c.toString())
    println("Total area of the picture: " + c.picArea())

    //p
    val p = new pictureThree(new rowOne(new Circle(5, "Blue")),
      new rowTwo(new Triangle(4, 4, "Green"), new Triangle(3, 9, "Green")),
      new rowThree(new Square(4, "Red"), new Triangle(4, 4, "Green"), new Triangle(3, 9, "Green")))
    println(p.toString())

    //Using the rules
    val WorldRuler = new WorldRuler();
    // WorldRuler.Check(a)

    //area
    val area = new GreatAreaCompressor()
    //area.calc(a)

  }
}

//Problem: 1
//Shapes
abstract class Shapes(x: String) {
  //name of shape
  def shapeName(): String
  //area of shape
  def shapeArea(): Double
  //color
  def shapeColor() = x match {
    case "Red" =>
      "R"
    case "Green" =>
      "G"
    case "Blue" =>
      "B"
  }
  override def toString = "[ " + shapeName() + " | " + shapeColor() + " ]" + " " + "[ Area: " + shapeArea() + " ]"
}

//shape 1
// Square 
case class Square(side: Int, x: String) extends Shapes(x) {
  def this(a: String, b: Int) = this(b, a)
  def shapeName() = "S"
  def shapeArea() = (side * side)
}

//shape 2
// Triangle 
case class Triangle(base: Int, height: Int, x: String) extends Shapes(x) {
  def this(a: String, b: Int, c: Int) = this(b, c, a)
  def shapeName() = "T"
  def shapeArea() = (base * (height / 2))
}

//shape 3
// Circle 
case class Circle(radius: Int, x: String) extends Shapes(x) {
  def this(a: String, b: Int) = this(b, a)
  def shapeName() = "C"
  def shapeArea() = /* 3.14 */ (Math.PI * (radius * radius))
}

//Pictures
abstract class pictures {
  def picArea(): Double
}

//3 types of pictures

//picture with 1 row
//row (a)
case class pictureOne(a: row) extends pictures {
  override def toString = a.toString
  def picArea() = a.picArea()
}

//picture with 2 rows
//row (a,b)
case class pictureTwo(a: row, b: row) extends pictures {
  def picArea() = a.picArea() + b.picArea()
  override def toString = {
    a.toString + b.toString
  }
}

//picture with 3 rows
//row (a,b,c)
case class pictureThree(a: row, b: row, c: row) extends pictures {
  def picArea() = a.picArea() + b.picArea() + c.picArea()
  override def toString = {
    a.toString + b.toString + c.toString
  }
}

//rows
abstract class row {
  def picArea(): Double
}

//row one can only have one shape
case class rowOne(a: Shapes) extends row {
  //area of the picture is the total area of the shapes in a row
  def picArea() = a.shapeArea()
  override def toString = {
    a.toString
  }
}

//row two can have 2 shapes 
case class rowTwo(a: Shapes, b: Shapes) extends row {
  //area of the pictures is the total area of the shapes in a row
  def picArea() = a.shapeArea() + b.shapeArea()
  override def toString = {
    a.toString + " " + b.toString
  }
}

//row three can have 3 shapes
case class rowThree(a: Shapes, b: Shapes, c: Shapes) extends row {
  //area of the pictures is the total area of the shapes in a row
  def picArea() = a.shapeArea() + b.shapeArea() + c.shapeArea()
  override def toString = {
    a.toString + " " + b.toString + " " + c.toString
  }
}

//remove row for problem 5 (rule 2) 
case class remove() extends row {
  def picArea() = 0
  override def toString = {
    " Row Removed "
  }
}

/*
 * Problem 4
 * 
 */
class GreatAreaCompressor {

  def calcP(p: pictures) {
    if (p != null) {
      p.picArea()
    }
  }
  def calcR(r: row) {
    if (r != null) {
      r.picArea()
    }

  }
  def calcS(s: Shapes) {
    if (s != null)
      s.shapeArea()
  }

}

/*
 * Problem 5
 * 
 * set of rules to follow
 * 
 */
class WorldRuler {

  //Pass in a picture 
  // Check which picture type (picture one, two or three)
  def Check(x: pictures): pictures = x match {

    case pictureOne(a) => pictureOne(apply(a))
    case pictureTwo(a, b) => pictureTwo(apply(a), apply(b))
    case pictureThree(a, b, c) => pictureThree(apply(a), apply(b), apply(c))
  }

  def apply(x: row): row = x match {

    // to avoid error if picture one is passed in (at Check)
    case rowOne(Square(a, b)) => new rowOne(new Square(a, b))
    case rowOne(Triangle(a, b, c)) => new rowOne(new Triangle(a, b, c))
    case rowOne(Circle(a, b)) => new rowOne(new Circle(a, b))

    /*
     * Rule:1
     * 
     * if there are only "two elements in the row", and these elements are of
			the same shape, these should be merged together; for squares you add
			the sides together, for triangles you add bases together and heights
			together, for circles you add radiuses together
			* 
			 */

    //For "two elements in the row"
    //Only row two have two shapes
    //for squares you add the sides together 
    case rowTwo(Square(a, b), Square(c, d)) =>
      val newSquare = a + c; new rowOne(new Square(newSquare, b))

    //for triangles you add bases together and heights together
    case rowTwo(Triangle(a, b, c), Triangle(d, e, f)) =>
      val base = a + d
      val height = b + e; new rowOne(new Triangle(base, height, c))

    //for circles you add radiuses together
    case rowTwo(Circle(a, b), Circle(c, d)) =>
      val radius = a + c; new rowOne(new Circle(radius, b))

    case rowThree(Circle(a, b), Circle(c, d), Square(e, f)) =>
      val radius = a + c; new rowOne(new Circle(radius, b))

    /******************************************************************************************************************************************/

    /*
       * Rule:2
       * 
       * if there are three shapes in a row, and all are of a different colour, 
       * the row should be removed and picture shrinks
       * 
       */

    //Only row 3 can have 3 shapes 
    case rowThree(a, b, c) =>
      val shapeOne = a.shapeColor(); val shapeTwo = b.shapeColor(); val shapeThree = c.shapeColor()

      if (shapeOne != shapeTwo && shapeOne != shapeThree && shapeTwo != shapeThree) {
        remove()
      } else (shapeOne == shapeTwo && shapeOne == shapeThree && shapeTwo == shapeThree)
      rowThree(a, b, c)

    /******************************************************************************************************************************************/

    /*
       * Rule:3
       * 
       * if there are three shapes in a row, and all are of a different type, 
       * shapes should get twice times bigger,
       * 
       */

    //Only row 3 can have 3 shapes
    //for square multiply sides by 2
    //for triangle multiply height and base by 2    
    //for circle multipky radius by 2

    //Square,Circle,Triangle
    case rowThree(Square(a, b), Circle(c, d), Triangle(e, f, g)) => new rowThree(new Square(a, b), new Circle((c * 2), d), new Triangle((e * 2), (f * 2), g))

    //Square,Triangle,Circle
    case rowThree(Square(a, b), Triangle(e, f, g), Circle(c, d)) => new rowThree(new Square((a * 2), b), new Triangle((e * 2), (f * 2), g), new Circle((c * 2), d))

    //Triangle,Square,Circle
    case rowThree(Triangle(e, f, g), Square(a, b), Circle(c, d)) => new rowThree(new Triangle((e * 2), (f * 2), g), new Square((a * 2), b), new Circle((c * 2), d))

    //Triangle,Circle,Square
    case rowThree(Triangle(e, f, g), Circle(c, d), Square(a, b)) => new rowThree(new Triangle((e * 2), (f * 2), g), new Circle((c * 2), d), new Square((a * 2), b))

    //Circle,Triangle,Square
    case rowThree(Circle(c, d), Triangle(e, f, g), Square(a, b)) => new rowThree(new Circle((c * 2), d), new Triangle((e * 2), (f * 2), g), new Square((a * 2), b))

    //Circle,Square,Triangle
    case rowThree(Circle(c, d), Square(a, b), Triangle(e, f, g)) => new rowThree(new Circle((c * 2), d), new Square((a * 2), b), new Triangle((e * 2), (f * 2), g))

    /**********************************************************************************************************************************************/

    /*
     * Rule: 4
     * 
     * if there are three shapes in a row, 
     * and first and third are of the same type, swap them.
     */
    ///Only row 3 can have 3 shapes

    //if first and last are same shapes, swap them
    //if all shapes are of same type
    case rowThree(Square(a, b), Square(c, d), Square(e, f)) => new rowThree(new Square(e, f), new Square(c, d), new Square(a, b))

    case rowThree(Triangle(a, b, c), Triangle(d, e, f), Triangle(g, h, i)) => new rowThree(new Triangle(g, h, i), new Triangle(d, e, f), new Triangle(a, b, c))

    case rowThree(Circle(a, b), Circle(c, d), Circle(e, f)) => new rowThree(new Circle(e, f), new Circle(c, d), new Circle(a, b))

    //square
    case rowThree(Square(a, b), Triangle(c, d, e), Square(f, g)) => new rowThree(new Square(f, g), new Triangle(c, d, e), new Square(a, b))

    case rowThree(Square(a, b), Circle(c, d), Square(e, f)) => new rowThree(new Square(e, f), new Circle(c, d), new Square(a, b))

    //triangle
    case rowThree(Triangle(a, b, c), Square(d, e), Triangle(f, g, h)) => new rowThree(new Triangle(f, g, h), new Square(d, e), new Triangle(a, b, c))

    case rowThree(Triangle(a, b, c), Circle(d, e), Triangle(f, g, h)) => new rowThree(new Triangle(f, g, h), new Circle(d, e), new Triangle(a, b, c))

    //circle
    case rowThree(Circle(a, b), Triangle(c, d, e), Circle(f, g)) => new rowThree(new Circle(f, g), new Triangle(c, d, e), new Circle(a, b))

    case rowThree(Circle(a, b), Square(c, d), Circle(e, f)) => new rowThree(new Circle(e, f), new Square(c, d), new Circle(a, b))

  }
}