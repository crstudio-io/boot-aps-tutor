package io.crstudio.tutor.problem.entity

import jakarta.persistence.*

@Entity
@Table(name = "TEST_CASE")
data class TestCase(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?,
    val input: String?,
    val output: String?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prob_id")
    val problem: Problem
)
