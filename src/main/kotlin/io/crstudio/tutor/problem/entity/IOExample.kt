package io.crstudio.tutor.problem.entity

import jakarta.persistence.*

@Entity
@Table(name = "IO_EXAMPLE")
data class IOExample(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?,
    val inputExample: String?,
    val outputExample: String?,
    val description: String?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prob_id")
    val problem: Problem
)
