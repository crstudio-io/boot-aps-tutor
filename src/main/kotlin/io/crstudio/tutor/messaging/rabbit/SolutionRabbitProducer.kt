package io.crstudio.tutor.messaging.rabbit

import io.crstudio.tutor.messaging.SolutionProducer
import io.crstudio.tutor.messaging.model.GradePayload
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

@Component
class SolutionRabbitProducer(
    @Qualifier("solutionQueue")
    val jobQueue: Queue,
    val rabbitTemplate: RabbitTemplate,
): SolutionProducer {
    override fun sendPayload(gradePayload: GradePayload) = rabbitTemplate.convertAndSend(
        jobQueue.name, Json.encodeToString(gradePayload))
}