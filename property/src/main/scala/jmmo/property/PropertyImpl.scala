package jmmo.property

/**
 * User: Tomas
 * Date: 04.09.13
 * Time: 12:22
 *
 * A Property which updates it value and fire ChangedValueEvent only if new value not equals old one
 */
class PropertyImpl[A](val name: String = "", protected var value: A = jmmo.util.sysDefault[A]) extends PropertyBase[A] {

  def apply(): A = value

  protected def updateInner(value: A) {
    this.value = value
  }
}

object PropertyImpl {

  def apply[A](name: String = "", value: A = jmmo.util.sysDefault[A]) = new PropertyImpl[A](name, value)
}
