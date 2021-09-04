package com.rticcle.server.domain.ticcle

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection="Ticcle")
data class Ticcle (
    var userId: String,

    @Id
    var id: ObjectId,
    var date: Date,

    var group: Group,
    var title: String,
    var link: String, // original content link
    var tagList: MutableList<String>,
    var mainImage: String,

    var contentList: MutableList<SmallTiccle> // limit: 3

        )

enum class Group {
    BOOK, BLOG, NEWS, WEB, SNS, ETC
}