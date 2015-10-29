package com.haw.monopoly

import com.haw.monopoly.util.MonoLogger
import com.haw.monopoly.web.WebModule
import com.typesafe.config.ConfigFactory
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.ServletHolder
import org.eclipse.jetty.webapp.WebAppContext

object Client extends App with WebModule with MonoLogger {


  val config = ConfigFactory.load()
  val server = new Server(config.getInt("http.port"))
  val webCtx = new WebAppContext()
  webCtx.setContextPath(config.getString("http.path"))
  webCtx.setResourceBase("/WEB-INF")

  webCtx.addServlet(new ServletHolder(diceController), "/dice/*")
  webCtx.addServlet(new ServletHolder(decksController), "/decks/*")


  server.setHandler(webCtx)
  server.start
  log.info("Server started.")
  server.join
}
