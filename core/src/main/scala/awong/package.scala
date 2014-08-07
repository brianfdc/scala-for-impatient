package object awong {
  import com.typesafe.config.ConfigFactory
  
  /** 
   *  An alias for the `Nothing` type.
   *  Denotes that the type should be filled in.
   */
  type ??? = Nothing

  /**
   * An alias for the `Any` type.
   * Denotes that the type should be filled in.
   */
  type *** = Any
  
  object ScalaLearnVersions {
    lazy val _0_0_1 = "0.0.1-SNAPSHOT"
    lazy val _0_0_2 = "0.0.2-SNAPSHOT"
  }
  
  trait ScalaForImpatientExercise extends LoggingLike {
    
  }
  
  object Defaults {
    lazy val config = ConfigFactory.load
    lazy val defaultEncoding = config.getString("application.defaultEncoding")
    lazy val defaultLang= config.getString("application.lang")
    lazy val defaultCountry = config.getString("application.country")
    lazy val defaultLocale = new java.util.Locale(defaultLang, defaultCountry)
  }
}