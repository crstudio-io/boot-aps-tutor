package io.crstudio.tutor.messaging

import io.crstudio.tutor.messaging.model.SignInMailParams
import io.crstudio.tutor.messaging.model.SignUpMailParams
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Primary
@Profile("dev", "test")
@Component
class LogEmailProducer : EmailProducer{
    private final val logger = LoggerFactory.getLogger(this.javaClass)

    override fun signInEmail(params: SignInMailParams) {
        logger.debug(params.toString())
    }

    override fun signUpEmail(params: SignUpMailParams) {
        logger.debug(params.toString())
    }
}