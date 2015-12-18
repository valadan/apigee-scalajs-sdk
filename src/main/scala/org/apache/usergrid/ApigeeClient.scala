package org.apache.usergrid

import scala.scalajs.js
import scala.scalajs.js.annotation._

@JSName("Apigee.Client")
@js.native
class ApigeeClient extends js.Object {
  def this(org: String, app: String) = this()
  def createEntity(options: js.Dynamic, callback: js.Function2[Boolean, Any, Unit]): Unit = js.native
}