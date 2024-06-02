package io.crstudio.tutor.problem.model

import jakarta.persistence.*

@Entity
@Table(name = "TEST_CASE")
data class TestCase(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?,
    @Lob
    @Column(columnDefinition = "TEXT")
    val input: String?,
    @Lob
    @Column(columnDefinition = "TEXT")
    val output: String?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prob_id")
    val problem: Problem
)
