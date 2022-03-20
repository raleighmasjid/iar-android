package org.raleighmasjid.iar.model.json

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Announcements(
    val special: Post?,
    val featured: Post?,
    val posts: List<Post>
) {
    fun postIds(): Set<String> {
        val postIds = mutableSetOf<String>()
        special?.let {
            postIds.add(it.id.toString())
        }
        featured?.let {
            postIds.add(it.id.toString())
        }
        val ids = posts.map { it.id.toString() }
        postIds.addAll(ids)

        return postIds
    }
}
