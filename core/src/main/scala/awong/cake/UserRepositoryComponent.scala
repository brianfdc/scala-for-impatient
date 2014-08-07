package awong.cake

trait UserRepositoryComponent {
  val userRepository: UserRepository
  
  class UserRepository extends awong.LoggingLike {
    def authenticate(username: String, password: String): User = { 
      val user = new User(username, password)
      logger.debug("authenticating user: " + user)
      user
    }
    def create(user: User) = logger.debug("creating user: " + user)
    def delete(user: User) = logger.debug("deleting user: " + user)
  }
}