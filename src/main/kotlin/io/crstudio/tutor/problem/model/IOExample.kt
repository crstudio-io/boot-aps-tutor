package io.crstudio.tutor.problem.model

import jakarta.persistence.*

@Entity
@Table(name = "IO_EXAMPLE")
class IOExample(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?,

    @Column(columnDefinition = "TEXT")
    var inputExample: String?,
    @Column(columnDefinition = "TEXT")
    var outputExample: String?,
    @Column(columnDefinition = "TEXT")
    var description: String?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prob_id")
    val problem: Problem,
)

data class IOExampleDto(
    val id: Long?,
    val inputExample: String?,
    val outputExample: String?,
    val description: String?,
) {
    companion object {
        fun fromEntity(ioExample: IOExample) = IOExampleDto(
            id = ioExample.id,
            inputExample = ioExample.inputExample,
            outputExample = ioExample.outputExample,
            description = ioExample.description,
        )
    }
}
