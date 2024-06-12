package io.crstudio.tutor.messaging.rabbit

import org.springframework.amqp.core.Queue
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitConfig {
    @Bean(name = ["solutionQueue"])
    fun solutionQueue(): Queue = Queue(
        "java_runner_queue",
        true,
        false,
        false,
    )

    @Bean(name = ["emailSenderQueue"])
    fun emailSenderQueue(): Queue = Queue(
        "send_mail_queue",
        true,
        false,
        false,
    )
}