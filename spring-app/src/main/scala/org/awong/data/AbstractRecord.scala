package org.awong.data


import javax.persistence.{ MappedSuperclass, GeneratedValue, Id }

@MappedSuperclass
abstract class AbstractRecord {
  @Id
  @GeneratedValue
  var id: Long = 0

  def getId: Long = id
}