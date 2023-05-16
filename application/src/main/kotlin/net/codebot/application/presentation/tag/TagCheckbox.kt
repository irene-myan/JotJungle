// Copyright (c) 2023 Irene Martirosyan, Patrick Telfer, Stephen Cao, Tam Nguyen

package net.codebot.application.presentation.tag

import javafx.scene.control.CheckBox
import javafx.scene.layout.HBox
import net.codebot.shared.domain.Tag

class TagCheckbox(tag: Tag) : HBox() {
    val checkbox = CheckBox()
    val tagPill = TagPill(tag)

    init {
        this.spacing = 10.0
        this.children.addAll(checkbox, tagPill)
    }
}