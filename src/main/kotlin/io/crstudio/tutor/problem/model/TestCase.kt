package io.crstudio.tutor.problem.model

import jakarta.persistence.*

@Entity
@Table(name = "TEST_CASE")
class TestCase(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?,
    @Column(columnDefinition = "TEXT")
    val input: String?,
    @Column(columnDefinition = "TEXT")
    val output: String?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prob_id")
    val problem: Problem
)
