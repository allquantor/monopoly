package com.haw.monopoly.util

import org.slf4j.LoggerFactory

/**
 * Created by Ivan Morozov on 24/10/15.
 */
trait MonoLogger {
    @transient protected lazy val log = LoggerFactory getLogger this.getClass.getName
}
