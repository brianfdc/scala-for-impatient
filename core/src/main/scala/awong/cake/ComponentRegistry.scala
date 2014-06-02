package awong.cake

/**
 * For Real World Scala series, see
 * <ul>
 * <li>http://jonasboner.com/2008/10/01/real-world-scala-introduction/</li>
 * <li>http://jonasboner.com/2008/10/06/real-world-scala-dependency-injection-di/</li>
 * <li>http://jonasboner.com/2008/12/09/real-world-scala-managing-cross-cutting-concerns-using-mixin-composition-and-aop/</li>
 * <li>http://jonasboner.com/2008/12/11/real-world-scala-fault-tolerant-concurrent-asynchronous-components/</li>
 * <li>http://jonasboner.com/2008/02/06/aop-style-mixin-composition-stacks-in-scala/</li>
 * </ul>
 * 
 * 
 * @see http://vimeo.com/23547652
 */
object ComponentRegistry extends
  UserServiceComponent with
  UserRepositoryComponent
{
  val userRepository = new UserRepository
  val userService = new UserService
}
