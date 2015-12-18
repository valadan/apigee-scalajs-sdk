package org.apache.usergrid.models

/**
 * @author valadan
 */

import scalajs.js
import scalajs.js.JSON
import scala.concurrent.{Promise, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import org.apache.usergrid._

trait Entity[T <: Entity[T]] {
  self =>
  
  def toLiteral: js.Dynamic

  def as(dyn: js.Dynamic): T

  def toEntities(dyn: js.Dynamic): js.Array[js.Dynamic] = {
    dyn.entities.asInstanceOf[js.Array[js.Dynamic]]
  }
  
  def toEntity(dyn: js.Dynamic): js.Dynamic = {
    toEntities(dyn).apply(0)
  }
  
  def save(implicit client: ApigeeClient): Future[T] = {
    val p = Promise[T]()
    val callback = new Function2[Boolean, Any, Unit]{
      def apply(error: Boolean, response: Any): Unit = {
        if(error){
          p.failure(new UnsupportedOperationException(JSON.stringify(response.asInstanceOf[js.Any])))
        } else {
          p.success(as(toEntity(response.asInstanceOf[js.Dynamic])))
        }       
      }
    }
    client.createEntity(this.toLiteral, callback)
    p.future
  }
  
  def getConnections(connector: String)(implicit client: ApigeeClient): Future[js.Dynamic] = {
    val options = js.Dynamic.literal()
    options.updateDynamic("client")(client)
    options.updateDynamic("data")(this.toLiteral)
    val entity = new ApigeeEntity(options)
    val p = Promise[js.Dynamic]()
    val callback = new Function2[Boolean, Any, Unit]{
      def apply(error: Boolean, response: Any): Unit = {
        if(error){
          p.failure(new UnsupportedOperationException(JSON.stringify(response.asInstanceOf[js.Any])))
        } else {
          p.success(response.asInstanceOf[js.Dynamic])
        }       
      }
    }
    entity.getConnections(connector, callback)
    p.future
  }
  
  def connecting(implicit client: ApigeeClient): (ApigeeEntity, String, ApigeeEntity, (Boolean, Any) => Unit) => Unit
        = (_: ApigeeEntity).connect(_: String, _: ApigeeEntity, _: ((Boolean, Any) => Unit))
        
  def disconnecting(implicit client: ApigeeClient): (ApigeeEntity, String, ApigeeEntity, (Boolean, Any) => Unit) => Unit
        = (_: ApigeeEntity).disconnect(_: String, _: ApigeeEntity, _: ((Boolean, Any) => Unit))        
  
  def prepareConnect[C <: Entity[C]](connector: String, connected: C)(fn: (ApigeeEntity, String, ApigeeEntity, (Boolean, Any) => Unit) => Unit)(implicit client: ApigeeClient): Future[Unit] = {
    val options = js.Dynamic.literal()
    options.updateDynamic("client")(client)
    options.updateDynamic("data")(this.toLiteral)   
    val entity = new ApigeeEntity(options)

    val connectedOptions = js.Dynamic.literal()
    connectedOptions.updateDynamic("data") (connected.toLiteral)
    connectedOptions.updateDynamic("client")(client)
    val connectedEntity = new ApigeeEntity(connectedOptions)
    val p = Promise[Unit]()
    val callback = new Function2[Boolean, Any, Unit]{
      def apply(error: Boolean, response: Any): Unit = {
        if(error){
          p.failure(new UnsupportedOperationException(JSON.stringify(response.asInstanceOf[js.Any])))
        } else {
          p.success(())
        }       
      }
    }
    fn(entity, connector, connectedEntity, callback)
    p.future
  }
  
  def connect[C <: Entity[C]](connector: String, connected: C)(implicit client: ApigeeClient): Future[Unit] = {
    prepareConnect(connector, connected)(connecting)
  }

  def disconnect[C <: Entity[C]](connector: String, connected: C)(implicit client: ApigeeClient): Future[Unit] = {
    prepareConnect(connector, connected)(disconnecting)
  }

}
