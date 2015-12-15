package com.haw.monopoly


import com.haw.monopoly.core.services.YellowPagesService
import com.haw.monopoly.data.DataModule
import com.haw.monopoly.web.WebModule
import com.typesafe.config.ConfigFactory
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.ServletHolder
import org.eclipse.jetty.webapp.WebAppContext
import org.slf4j.LoggerFactory


object OurServer extends App with DataModule with WebModule {

  val logger = LoggerFactory.getLogger(getClass)
  val config = ConfigFactory.load()
  val server = new Server(config.getInt("http.port"))
  val webCtx = new WebAppContext()
  webCtx.setContextPath(config.getString("http.path"))
  webCtx.setResourceBase("/WEB-INF")



  webCtx.addServlet(new ServletHolder(boardsController), "/boards/*")
  webCtx.addServlet(new ServletHolder(gameController), "/games/*")
  webCtx.addServlet(new ServletHolder(playerController), "/player/*")
  webCtx.addServlet(new ServletHolder(eventController), "/event/*")
  webCtx.addServlet(new ServletHolder(brokerController), "/broker/*")

  server.setHandler(webCtx)
  server.start

  logger.info("OurServer started.")

  YellowPagesService.registerOurService

  server.join


}
