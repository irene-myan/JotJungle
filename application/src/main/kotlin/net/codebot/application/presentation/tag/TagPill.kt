// Copyright (c) 2023 Irene Martirosyan, Patrick Telfer, Stephen Cao, Tam Nguyen

package net.codebot.application.presentation.tag

import javafx.scene.control.Label
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import net.codebot.shared.domain.SERVER_ADDRESS
import net.codebot.shared.domain.Tag
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration

class TagPill(_tag: Tag) : Label() {
    init {
        val tag = getTag(_tag.id)
        this.text = tag.name
        val rgbString = "rgb(${(tag.color.red * 255).toInt()}, ${(tag.color.green * 255).toInt()}, ${(tag.color.blue * 255).toInt()})"
        this.style = "-fx-background-color: $rgbString;" +
                "-fx-background-radius: 20;" +
                "-fx-background-insets: -4;"
    }
}


fun getTag(id: Int) : Tag {
    try {
        // GET
        val client = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .followRedirects(HttpClient.Redirect.NEVER)
            .connectTimeout(Duration.ofSeconds(20))
            .build()

        val request = HttpRequest.newBuilder()
            .uri(URI.create(SERVER_ADDRESS + "tags/" + id))
            .GET()
            .build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        val tag = Json.decodeFromString<Tag>(response.body())

        return tag
    } catch (e: Error) {
        print(e.message)
    }
    return Tag("")
}
