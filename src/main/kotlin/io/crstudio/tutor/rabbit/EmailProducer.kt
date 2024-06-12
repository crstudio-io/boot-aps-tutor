package io.crstudio.tutor.rabbit

import io.crstudio.tutor.rabbit.model.SignInMailParams

interface EmailProducer {
    fun signInEmail(params: SignInMailParams)
}