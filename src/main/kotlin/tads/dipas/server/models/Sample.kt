package tads.dipas.server.models

import lombok.Data
import tads.dipas.server.models.entity.Entity
import javax.persistence.*

@Data
@javax.persistence.Entity
class Sample (
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    var id: Long? = null,
    @OneToMany
    val entities:List<Entity>? = null,
    @OneToMany
    val images:List<Image>? = null,
)