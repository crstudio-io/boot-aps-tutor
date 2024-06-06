package io.crstudio.tutor.solution.model

import io.crstudio.tutor.auth.model.User
import io.crstudio.tutor.problem.model.Problem
import jakarta.persistence.*

@Entity
@Table(name = "SOLUTION")
data class Solution(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?,
    @Column(columnDefinition = "TEXT")
    val code: String,

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
    PENDING, GRADING, SUCCESS, FAIL, ERROR
}
