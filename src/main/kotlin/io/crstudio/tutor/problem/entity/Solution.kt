package io.crstudio.tutor.problem.entity

import io.crstudio.tutor.auth.entity.User
import jakarta.persistence.*

@Entity
@Table(name = "SOLUTION")
data class Solution(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?,
    @Lob
    val code: String?,

    @Enumerated(value = EnumType.STRING)
    val lang: Lang = Lang.JAVA17,
    @Enumerated(value = EnumType.STRING)
    val status: Status = Status.PENDING,

    val score: Int = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prob_id")
    val problem: Problem,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User
)

enum class Lang {
    JAVA17
}

enum class Status {
    PENDING, GRADING, SUCCESS, FAIL
}
