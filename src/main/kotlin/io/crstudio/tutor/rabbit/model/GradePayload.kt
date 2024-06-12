package io.crstudio.tutor.rabbit.model

import kotlinx.serialization.Serializable

@Serializable
data class GradePayload(
    val id: Long?,
    val pid: Long?,
    val code: String?,
)