package tads.dipas.server.models.entity

import lombok.Data
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Data
@Entity
class Descriptor (
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    var id: Long? = null,
    val name:String = "",
    val description:String = "",
    val value:Double = 0.0,
    val isCategorical:Boolean = false //Categorical or numeric
)