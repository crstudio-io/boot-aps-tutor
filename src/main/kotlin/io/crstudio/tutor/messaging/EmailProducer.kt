package io.crstudio.tutor.messaging

import io.crstudio.tutor.messaging.model.SignInMailParams
import io.crstudio.tutor.messaging.model.SignUpMailParams

interface EmailProducer {
    fun signInEmail(params: SignInMailParams)
    fun signUpEmail(params: SignUpMailParams)
}