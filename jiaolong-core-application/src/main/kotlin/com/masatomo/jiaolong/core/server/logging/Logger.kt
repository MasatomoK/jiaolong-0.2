package com.masatomo.jiaolong.core.server.logging

import org.slf4j.Logger
import org.slf4j.LoggerFactory


fun getLogger(): Logger = Thread.currentThread().stackTrace[1].className
    .let { LoggerFactory.getLogger(it) }
