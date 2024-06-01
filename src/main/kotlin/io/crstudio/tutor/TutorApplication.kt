package io.crstudio.tutor

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TutorApplication

fun main(args: Array<String>) {
	runApplication<TutorApplication>(*args)
}
