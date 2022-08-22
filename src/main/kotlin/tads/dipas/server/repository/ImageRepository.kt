package tads.dipas.server.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import tads.dipas.server.model.Image

@Repository
interface ImageRepository  : JpaRepository<Image, Long>{
    @Query("select i from Image i where i.user.id = ?1")
    fun findAllByUser_Id(user_Id: Long): List<Image>
}