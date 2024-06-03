package io.crstudio.tutor.solution.rabbit

import io.crstudio.tutor.solution.model.GradePayload
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Component

@Component
class SolutionProducer(
    val jobQueue: Queue,
    val rabbitTemplate: RabbitTemplate,
) {
    fun sendPayload(gradePayload: GradePayload) = rabbitTemplate.convertAndSend(
        jobQueue.name, Json.encodeToString(gradePayload))
}