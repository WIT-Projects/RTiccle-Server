package com.rticcle.server.domain.ticcle

data class SmallTiccle (
    var title: String,
    var attachment: String,
    var isFile: Boolean, // true -> attachment is file / false -> attachment is link
    var imageList: List<Photo>, // max 3
    var content: String // max 300
        )