package com.marijannovak.autismhelper.data.models
import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.marijannovak.autismhelper.config.Constants.Companion.TABLE_RSS

data class Author (
        @SerializedName("name")
        var name: String? = ""
)

@Entity(tableName = TABLE_RSS)
data class FeedItem (
        var summary: String = "",
        var image: String = "",
        @SerializedName("date_modified")
        var dateModified: String = "",
        @SerializedName("date_published")
        var datePublished: String = "",
        @Embedded(prefix = "author_")
        var author: Author,
        @PrimaryKey
        var id: String = "",
        var title: String = "",
        var url: String = "",
        @SerializedName("content_html")
        var contentHtml: String = "",
        @Ignore
        var tags: List<String>?) {

    constructor() : this("", "", "", "", Author(), "", "", "", "", emptyList())
}

data class Feed(
        @SerializedName("home_page_url")
        var homePageUrl: String = "",
        var author: Author?,
        var icon: String = "",
        var description: String = "",
        @SerializedName("feed_url")
        var feedUrl: String = "",
        var title: String = "",
        var version: String = "",
        var items: List<FeedItem>?
) {
    constructor(): this("", null, "", "", "", "", "", emptyList())
}


