package io.crstudio.tutor.auth.repo

import java.util.*

/**
 * Proxy interface that returns a principal based on identity.
 */
interface UserIdProxy<P, ID> {
    fun findById(id: ID): Optional<P>
}