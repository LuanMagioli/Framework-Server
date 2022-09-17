package tads.dipas.server.model.Unit

import javax.persistence.*

@Entity
class Unit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    var id: Long? = null

    descriptors
}