package com.example

import org.apache.logging.log4j.LogManager

class Logger{
    private val logger = LogManager.getLogger("ApplicationKt".javaClass)

    fun log(s: String) {
        logger.info(s)
    }
}