package io.crstudio.tutor.messaging.rabbit

import io.crstudio.tutor.messaging.EmailProducer
import io.crstudio.tutor.messaging.model.SignInMailParams
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import org.thymeleaf.context.Context
import org.thymeleaf.spring6.SpringTemplateEngine

@Component
class EmailRabbitProducer(
    @Qualifier("emailSenderQueue")
    val jobQueue: Queue,
    val rabbitTemplate: RabbitTemplate,
    val templateEngine: SpringTemplateEngine,
) : EmailProducer {
    override fun signInEmail(params: SignInMailParams) {
        val context = Context()
        context.setVariable("serviceHost", params.host)
        context.setVariable("email", params.email)
        context.setVariable("signInLink", params.link)
        val htmlBody = templateEngine.process("email/signin", context)
        val payload = Payload(
            subject = "Sign In Request",
            body = htmlBody,
            to = listOf(params.email)
        )
        rabbitTemplate.convertAndSend(jobQueue.name, Json.encodeToString(payload))
    }

    @Serializable
    data class Payload(
        val subject: String,
        val body: String,
        val to: List<String>,
    )
}