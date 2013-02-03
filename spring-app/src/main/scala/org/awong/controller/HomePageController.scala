package org.awong.controller

import org.springframework.stereotype.Controller
import org.springframework.beans.factory.annotation.Autowired
import org.hibernate.SessionFactory
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod._
import org.springframework.web.servlet.ModelAndView
import org.awong.repository.CustomerRepository

@Controller
class HomePageController {
  implicit def sessionFactory2Session(sf: SessionFactory) = sf.getCurrentSession;

  @Autowired
  val customerRepository: CustomerRepository = null

  @RequestMapping(value = Array("/home"), method = Array(GET))
  def loadCustomers() =
    new ModelAndView("home", "customers", customerRepository.getAll)
}
