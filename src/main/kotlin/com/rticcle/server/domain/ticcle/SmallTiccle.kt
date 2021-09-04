package com.rticcle.server.domain.ticcle

data class SmallTiccle (
    var title: String,
    var link: String, // original content link
    var imageList: MutableList<Photo>, // limit: 2
    var content: String // limit: 300
        )