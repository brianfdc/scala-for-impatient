package org.awong.data

import reflect.BeanProperty
import javax.persistence.Entity

@Entity
class Customer extends AbstractRecord {
  @BeanProperty
  var name: String = null;
  
  override def toString = "[Customer: id = " + id + ", name = " + name + "]"
}