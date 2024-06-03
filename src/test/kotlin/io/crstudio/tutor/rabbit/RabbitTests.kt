package io.crstudio.tutor.rabbit

import io.crstudio.tutor.solution.model.GradePayload
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class RabbitTests{
    @Autowired
    lateinit var jobQueue : Queue
    @Autowired
    lateinit var rabbitTemplate: RabbitTemplate

    @Test
    @DisplayName("produce message")
    fun testProduceMessage() {
        val code = "public class Main {\n    public static void main(String[] args) {\n        System.out.println(\"Hello World!\");\n    }\n}"
        val payload = GradePayload(0, 0, code)

        rabbitTemplate.convertAndSend(jobQueue.name, Json.encodeToString(payload))
    }
}