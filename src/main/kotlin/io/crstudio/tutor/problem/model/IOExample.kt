package io.crstudio.tutor.problem.model

import jakarta.persistence.*

@Entity
@Table(name = "IO_EXAMPLE")
data class IOExample(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?,

    @Lob
    @Column(columnDefinition = "TEXT")
    val inputExample: String?,
    @Lob
    @Column(columnDefinition = "TEXT")
    val outputExample: String?,
    val description: String?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prob_id")
    val problem: Problem
)
