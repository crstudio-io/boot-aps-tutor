package io.crstudio.tutor.rabbit

import io.crstudio.tutor.rabbit.model.GradePayload
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

@Component
class SolutionProducer(
    @Qualifier("solutionQueue")
    val jobQueue: Queue,
    val rabbitTemplate: RabbitTemplate,
) {
    fun sendPayload(gradePayload: GradePayload) = rabbitTemplate.convertAndSend(
        jobQueue.name, Json.encodeToString(gradePayload))
}