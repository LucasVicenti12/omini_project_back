package com.example.myChat

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(
	scanBasePackages = [
		"com.example.myChat",
		"org.jetbrains.exposed.spring",
	]
)
class MyChatApplication

fun main(args: Array<String>) {
	runApplication<MyChatApplication>(*args)
}
