package org.apache.usergrid

import scala.scalajs.js
import scala.scalajs.js.annotation._

@JSName("Apigee.Entity")
@js.native
class ApigeeEntity extends js.Object {
  def this(options: js.Dynamic) = this()
  def getConnections(connector: String, callback: js.Function2[Boolean, Any, Unit]): Unit = js.native
  def connect(connector: String, connected: ApigeeEntity, callback: js.Function2[Boolean, Any, Unit]): Unit = js.native
  def disconnect(connector: String, connected: ApigeeEntity, callback: js.Function2[Boolean, Any, Unit]): Unit = js.native
}