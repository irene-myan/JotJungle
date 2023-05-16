// Copyright (c) 2023 Irene Martirosyan, Patrick Telfer, Stephen Cao, Tam Nguyen

package net.codebot.shared.domain

import javafx.scene.paint.Color
import kotlinx.serialization.Serializable

@Serializable(with = TagSerialzer::class)
class Tag(var name: String, var color: Color = Color.BLUE, val id: Int = -1) {
    override fun toString(): String {
        return name
    }
}
