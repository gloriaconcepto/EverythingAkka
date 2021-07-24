//#full-example
package com.example

import akka.actor.typed.ActorRef
import akka.actor.typed.ActorSystem
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import com.example.OrderProcessor.Order
import com.example.Shipper.Shipment
import  com.example.Noitifier.Notification
//import com.example.GreeterMain.SayHello

//#greeter-actor
// object Greeter {
//   final case class Greet(whom: String, replyTo: ActorRef[Greeted])
//   final case class Greeted(whom: String, from: ActorRef[Greet])

//   def apply(): Behavior[Greet] = Behaviors.receive { (context, message) =>
//     context.log.info("Hello {}!", message.whom)
//     //#greeter-send-messages
//     message.replyTo ! Greeted(message.whom, context.self)
//     //#greeter-send-messages
//     Behaviors.same
//   }
// }
//#greeter-actor

//#greeter-bot
// object GreeterBot {

//   def apply(max: Int): Behavior[Greeter.Greeted] = {
//     bot(0, max)
//   }

//   private def bot(greetingCounter: Int, max: Int): Behavior[Greeter.Greeted] =
//     Behaviors.receive { (context, message) =>
//       val n = greetingCounter + 1
//       context.log.info("Greeting {} for {}", n, message.whom)
//       if (n == max) {
//         Behaviors.stopped
//       } else {
//         message.from ! Greeter.Greet(message.whom, context.self)
//         bot(n, max)
//       }
//     }
//}
//#greeter-bot

//#greeter-main
// object GreeterMain {

//   final case class SayHello(name: String)

//   def apply(): Behavior[SayHello] =
//     Behaviors.setup { context =>
//       //#create-actors
//       val greeter = context.spawn(Greeter(), "greeter")
//       //#create-actors

//       Behaviors.receiveMessage { message =>
//         //#create-actors
//         val replyTo = context.spawn(GreeterBot(max = 3), message.name)
//         //#create-actors
//         greeter ! Greeter.Greet(message.name, replyTo)
//         Behaviors.same
//       }
//     }
// }
//#greeter-main
object  Noitifier{
  final  case class Notification(orderIndex:Int,sucess:Boolean)

  /// applying the behavioural style
  def apply(): Behavior[Notification] = Behaviors.receive { (context, message) =>
    context.log.info(message.toString())
    println("Notification actor")
    //do mutate the behavior let it runs on the same head
    Behaviors.same
  }
}
object Shipper {
  final case class Shipment(id: Int, produkt: String, number: Int,replyTo:ActorRef[Notification])
  def apply(): Behavior[Shipment] = Behaviors.receive { (context, message) =>
    context.log.info(message.toString())
    println("shipper actor")
    message.replyTo ! Notification(message.id,true)
    //do mutate the behavior let it runs on the same head
    Behaviors.same
  }

}

object OrderProcessor {
  final case class Order(id: Int, produkt: String, number: Int)
  def apply(): Behavior[Order] = Behaviors.setup { context =>
    val shipperRef = context.spawn(Shipper(), "shipper")
    val notificationRef=context.spawn(Noitifier(),"notifier")
    Behaviors.receiveMessage { message =>
      shipperRef ! Shipment(message.id, message.produkt, message.number,notificationRef)
      println(message.toString())
      //do mutate the behavior let it runs on the same head
      Behaviors.same
    }
  }
}
//#main-class
object AkkaQuickstart extends App {
  //#actor-system
  val orderProcessor: ActorSystem[OrderProcessor.Order] =
    ActorSystem(OrderProcessor(), "orders")
  //#actor-system

  //#main-send-messages
  orderProcessor ! Order(0, "jakky", 4)
  orderProcessor ! Order(2, "okon", 4)
  //#main-send-messages
}
//#main-class
//#full-example
