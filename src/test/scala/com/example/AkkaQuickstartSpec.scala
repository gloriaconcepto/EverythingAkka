//#full-example
package com.example

import akka.actor.testkit.typed.scaladsl.ScalaTestWithActorTestKit
import org.scalatest.wordspec.AnyWordSpecLike
import com.example.Shipper.Shipment
import com.example.Noitifier.Notification

//#definition
class AkkaQuickstartSpec
    extends ScalaTestWithActorTestKit
    with AnyWordSpecLike {
//#definition

  "Testing response from shipper to notification" must {
    //#test
    "reply to notifier" in {
      val replyProbe = createTestProbe[Notification]()
      val underTest = spawn(Shipper())
      underTest ! Shipment(0, "Umbrella", 1, replyProbe.ref)
      replyProbe.expectMessage(Notification(0, true))
    }
    //#test
  }

}
//#full-example
