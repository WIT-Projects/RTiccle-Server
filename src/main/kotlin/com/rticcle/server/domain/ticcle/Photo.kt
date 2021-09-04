package com.rticcle.server.domain.ticcle

import org.bson.types.Binary
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

data class Photo (
    var message: String,
    var image: Binary
    )