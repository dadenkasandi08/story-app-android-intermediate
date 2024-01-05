package com.kasandi.storyapp

import com.kasandi.storyapp.data.remote.response.Story

object DataDummy {
    fun generateDummyStoriesResponse(): List<Story> {
        val items: MutableList<Story> = arrayListOf()
        for (i in 0..100) {
            items.add(Story(
                "2023 $i",
                "desc $i",
                i.toString(),
                0.1+i,
                1.0+i,
                "author $i",
                "url $i"
            ))
        }
        return items
    }
}