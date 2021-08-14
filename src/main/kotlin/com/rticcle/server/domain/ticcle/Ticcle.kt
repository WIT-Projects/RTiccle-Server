package com.rticcle.server.domain.ticcle

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection="Ticcle")
data class Ticcle (
    var userId: String,

    @Id
    var id: ObjectId,

    var group: Group,
    //var date: Date,
    var title: String,
    //var mainImage: String, // Should be limited?

    var attachment: String, // Binary?
    var isFile: Boolean, // true -> attachment is file / false -> attachment is link
    var tagList: MutableList<String>,
    var contentList: MutableList<SmallTiccle>
        )

enum class Group {
    BOOK, BLOG, NEWS, WEB, SNS, ETC
}