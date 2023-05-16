// Copyright (c) 2023 Irene Martirosyan, Patrick Telfer, Stephen Cao, Tam Nguyen

package net.codebot.application.presentation

import net.codebot.shared.domain.Note

// Reference https://git.uwaterloo.ca/cs346/public/sample-code
interface IView {
    fun addedNote(note: Note)
}