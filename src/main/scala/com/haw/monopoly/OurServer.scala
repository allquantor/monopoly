package com.haw.monopoly

import com.haw.monopoly.core.LocationRepository
import com.haw.monopoly.data.DataModule
import com.haw.monopoly.web.WebModule
import com.typesafe.config.ConfigFactory
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.ServletHolder
import org.eclipse.jetty.webapp.WebAppContext
import org.slf4j.LoggerFactory

object OurServer extends App with WebModule with DataModule {
  val logger = LoggerFactory.getLogger(getClass)
  val config = ConfigFactory.load()
  val server = new Server(config.getInt("http.port"))
  val webCtx = new WebAppContext()
  webCtx.setContextPath(config.getString("http.path"))
  webCtx.setResourceBase("/WEB-INF")

  webCtx.addServlet(new ServletHolder(boardsController), "/boards/*")


  server.setHandler(webCtx)
  server.start
  logger.info("OurServer started.")
  server.join

  override def locationRepository: LocationRepository = ???
}
