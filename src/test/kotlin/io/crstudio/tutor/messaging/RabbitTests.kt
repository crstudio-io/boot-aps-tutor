package io.crstudio.tutor.messaging

import io.crstudio.tutor.messaging.model.GradePayload
import io.crstudio.tutor.messaging.model.SignInMailParams
import io.crstudio.tutor.messaging.rabbit.EmailRabbitProducer
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.thymeleaf.context.Context
import org.thymeleaf.spring6.SpringTemplateEngine

@SpringBootTest
@ActiveProfiles("test")
class RabbitTests{
    @Autowired
    lateinit var templateEngine: SpringTemplateEngine

    @Autowired
    lateinit var rabbitTemplate: RabbitTemplate

    @Test
    @DisplayName("solution mq standalone")
    fun testSolutionMessage(
        @Qualifier("solutionQueue")
        queue : Queue
    ) {
        val code = "public class Main {\n    public static void main(String[] args) {\n        System.out.println(\"Hello World!\");\n    }\n}"
        val payload = GradePayload(0, 0, code)

        rabbitTemplate.convertAndSend(queue.name, Json.encodeToString(payload))
    }

    @Test
    @DisplayName("solution producer")
    fun testSolutionProducer(
        @Autowired
        solutionProducer: SolutionProducer
    ) {
        val code = "public class Main {\n    public static void main(String[] args) {\n        System.out.println(\"Hello World!\");\n    }\n}"
        solutionProducer.sendPayload(GradePayload(0, 0, code))
    }

    @Test
    @DisplayName("test template engine")
    fun testTemplateEngine() {
        val context = Context()
        context.setVariable("serviceHost", "http://lcoalhost:8080")
        context.setVariable("email", "aquashdw@gmail.com")
        context.setVariable("signInLink", "http://localhost:8080")
        val htmlBody = templateEngine.process("email/signin", context)
        println(htmlBody)
    }

    @Test
    @DisplayName("email mq standalone")
    fun testEmailMessage(
        @Qualifier("emailSenderQueue")
        queue : Queue
    ) {
        val payload = EmailRabbitProducer.Payload(
            subject = "test",
            body = "test email",
            to = listOf("aquashdw@gmail.com"),
        )
        rabbitTemplate.convertAndSend(queue.name, Json.encodeToString(payload))
    }

    @Test
    @DisplayName("email producer - signin")
    fun testSignInEmail(
        @Autowired
        emailProducer: EmailProducer
    ) = emailProducer.signInEmail(
        SignInMailParams(
        "aquashdw@gmail.com",
        "/link"
    )
    )

}