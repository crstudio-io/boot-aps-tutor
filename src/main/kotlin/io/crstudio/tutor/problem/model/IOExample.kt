package io.crstudio.tutor.problem.model

import jakarta.persistence.*

@Entity
@Table(name = "IO_EXAMPLE")
data class IOExample(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?,

    @Column(columnDefinition = "TEXT")
    val inputExample: String?,
    @Column(columnDefinition = "TEXT")
    val outputExample: String?,
    @Column(columnDefinition = "TEXT")
    val description: String?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prob_id")
    val problem: Problem
)
