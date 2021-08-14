package com.rticcle.server.domain.ticcle

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.stereotype.Repository

@Repository
class TiccleRepository {
    @Autowired
    private lateinit var mongoTemplate: MongoTemplate

    fun save(ticcle: Ticcle){
        mongoTemplate.save(ticcle)
    }
}