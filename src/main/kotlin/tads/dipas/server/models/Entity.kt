package tads.dipas.server.models

import lombok.Data
import tads.dipas.server.models.Descriptor
import tads.dipas.server.models.Image
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany

@Data
@Entity
class Entity (
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    var id: Long? = null,
    val name:String = "",

    @OneToMany
    val images:List<Image>? = null,

    @OneToMany
    val descriptors:List<Descriptor>? = null,
)