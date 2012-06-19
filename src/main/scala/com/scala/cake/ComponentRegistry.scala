package com.scala.cake

/**
 * 
 * @see http://jonasboner.com/2008/10/06/real-world-scala-dependency-injection-di/
 * @see http://vimeo.com/23547652
 */
object ComponentRegistry extends
  UserServiceComponent with
  UserRepositoryComponent
{
  val userRepository = new UserRepository
  val userService = new UserService
}
