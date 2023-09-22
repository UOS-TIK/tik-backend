package com.tik.server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TikServerApplication

fun main(args: Array<String>) {
	runApplication<TikServerApplication>(*args)
}
