package io.crstudio.tutor.messaging

import io.crstudio.tutor.messaging.model.SignInMailParams

interface EmailProducer {
    fun signInEmail(params: SignInMailParams)
}