package com.rticcle.server.domain.ticcle

import org.bson.types.Binary
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection="photo")
data class Photo (
    @Id
    var id: ObjectId,

    var token: String,
    var message: String,
    var image: Binary
    )