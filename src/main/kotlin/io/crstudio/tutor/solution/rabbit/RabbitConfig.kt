package io.crstudio.tutor.solution.rabbit

import org.springframework.amqp.core.Queue
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitConfig {
    @Bean
    fun queue(): Queue = Queue(
        "java_runner_queue",
        true,
        false,
        false
    )
}