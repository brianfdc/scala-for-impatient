package scala.staircase

import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

@RunWith(classOf[JUnitRunner])
class CircuitSimulationSpec extends scala.awong.AbstractWordSpec {

  object TestSimulation extends BasicCircuitSimulation {
    def InverterDelay = 1
    def AndGateDelay = 3
    def OrGateDelay = 5
  }
  
  "My simulation" must {
    "Probe a few things" in {
      val input1, input2, sum, carry = new Wire

      TestSimulation.probe("sum", sum)
      TestSimulation.probe("carry", carry)
      TestSimulation.halfAdder(input1, input2, sum, carry)

      input1.setSignal(true)
      TestSimulation.run
      sum.getSignal should be (true)
      carry.getSignal should be (false)

      input2.setSignal(true)
      TestSimulation.run
      sum.getSignal should be (false)
      carry.getSignal should be (true)
    }
  }
}