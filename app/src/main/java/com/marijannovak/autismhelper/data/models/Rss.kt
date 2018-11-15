package com.marijannovak.autismhelper.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.marijannovak.autismhelper.config.Constants.Companion.TABLE_RSS
import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "rss")
data class RSS (
    @field:Element(name = "channel", required = false)
    var channel: Channel,
    @field:Attribute(name = "npr", required = false)
    var npr: String,
    @field:Attribute(name = "nprml", required = false)
    var nprml: String,
    @field:Attribute(name = "itunes", required = false)
    var itunes: String,
    @field:Attribute(name = "content", required = false)
    var content: String,
    @field:Attribute(name = "dc", required = false)
    var dc: String,
    @field:Attribute(name = "version", required = false)
    var version: String
) {
    constructor(): this(Channel(), "", "", "", "", "", "")
}

data class Channel (
        @field:Element(name = "title", required = false)
        var title: String,
        @field:Element(name = "link", required = false)
        var link: String,
        @field:Element(name = "description", required = false)
        var description: String,
        @field:Element(name = "language", required = false)
        var language: String,
        @field:Element(name = "copyright", required = false)
        var copyright: String,
        @field:Element(name = "generator", required = false)
        var generator: String,
        @field:Element(name = "lastBuildDate", required = false)
        var lastBuildDate: String,
        @field:Element(name = "image", required = false)
        var image: Image,
        @field:ElementList(name = "item", inline = true, required = false)
        var feedItems: MutableList<FeedItem>
) {
    constructor(): this("", "", "", "", "", "", "", Image(), mutableListOf())
}

data class Image (
        @field:Element(name = "url", required = false)
        var url: String,
        @field:Element(name = "title", required = false)
        var title: String,
        @field:Element(name = "link", required = false)
        var link: String
) {
    constructor(): this("", "", "")
}

@Entity(tableName = TABLE_RSS)
@Root(name = "item")
data class FeedItem (
        @field:Element(name = "title", required = false)
        var title: String,
        @field:Element(name = "description", required = false)
        var description: String,
        @field:Element(name = "pubDate", required = false)
        var pubDate: String,
        @field:Element(name = "link", required = false)
        var link: String,
        @field:Element(name = "guid", required = false)
        @PrimaryKey
        var guid: String,
        @field:Element(name = "encoded", required = false)
        var encoded: String,
        @field:Element(name = "creator", required = false)
        var creator: String
) {
    constructor(): this("", "", "", "", "", "", "")
}
