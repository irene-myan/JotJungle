// Copyright (c) 2023 Irene Martirosyan, Patrick Telfer, Stephen Cao, Tam Nguyen

package net.codebot.application.presentation

import javafx.event.Event
import javafx.event.EventType

// Custom Events Reference: https://fxdocs.github.io/docs/html5/#_event_handling
class LayoutEvent(eventType: EventType<out Event?>?)
    : Event(eventType) {
    companion object {
        val LIST = EventType(ANY, "LIST")
        val NOTE = EventType(ANY, "NOTE")
    }

}