package com.example

import org.slf4j.LoggerFactory

class Logger{
    private val logger = LoggerFactory.getLogger(javaClass)

    fun log(s: String) {
        logger.info(s)
    }
}