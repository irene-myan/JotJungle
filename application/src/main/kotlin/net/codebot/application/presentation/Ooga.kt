// Copyright (c) 2023 Irene Martirosyan, Patrick Telfer, Stephen Cao, Tam Nguyen

package net.codebot.application.presentation

import javafx.animation.Interpolator
import javafx.animation.PathTransition
import javafx.animation.RotateTransition
import javafx.animation.Timeline
import javafx.geometry.Pos
import javafx.scene.image.ImageView
import javafx.scene.layout.StackPane
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import javafx.scene.shape.ArcTo
import javafx.scene.shape.ClosePath
import javafx.scene.shape.MoveTo
import javafx.scene.shape.Path
import javafx.util.Duration

class Ooga {
    private val monkey = ImageView("monkeyDab.png")
    private var mediaPlayer : MediaPlayer? = null

    private val rotate = RotateTransition()
    private val circle = PathTransition()
    private var running = false

    init {
        try {
            val sound = Media("https://cdn.pixabay.com/audio/2021/08/04/audio_69b90804eb.mp3")
            mediaPlayer = MediaPlayer(sound)
            mediaPlayer!!.cycleCount = Timeline.INDEFINITE
        } catch (e: Exception) {
            println(e)
        }

        monkey.isPreserveRatio = true
        monkey.fitHeight = 200.0

        rotate.node = monkey
        rotate.duration = Duration.seconds(0.5)
        rotate.cycleCount = Timeline.INDEFINITE
        rotate.interpolator = Interpolator.LINEAR
        rotate.byAngle = 360.0

        circle.node = monkey
        circle.duration = Duration.seconds(1.5)
        circle.path = createEllipsePath(300.0, 100.0, 150.0, 150.0, 0.0)
        circle.orientation = PathTransition.OrientationType.NONE
        circle.cycleCount = Timeline.INDEFINITE
        circle.isAutoReverse = false
        circle.interpolator = Interpolator.LINEAR
    }

    fun run(mainPain: StackPane) {
        if (running) {
            mainPain.children.remove(monkey)
            circle.stop()
            rotate.stop()
            mediaPlayer?.stop()
        } else {
            mainPain.children.add(monkey)
            StackPane.setAlignment(monkey, Pos.CENTER)
            circle.play()
            rotate.play()
            mediaPlayer?.play()
        }
        running = !running
    }

    // Adapted from: https://stackoverflow.com/questions/14171856/javafx-2-circle-path-for-animation
    private fun createEllipsePath(
        centerX: Double,
        centerY: Double,
        radiusX: Double,
        radiusY: Double,
        rotation: Double
    ): Path {
        val path = Path()

        val moveTo = MoveTo()
        moveTo.x = 0.0
        moveTo.y = 0.0

        val arcTo = ArcTo()
        arcTo.x = centerX - radiusX + 1 // to simulate a full 360 degree circle
        arcTo.y = centerY - radiusY
        arcTo.isSweepFlag = false
        arcTo.isLargeArcFlag = true
        arcTo.radiusX = radiusX
        arcTo.radiusY = radiusY
        arcTo.xAxisRotation = rotation

        path.elements.addAll(moveTo, arcTo, ClosePath())
        path.isSmooth = true
        return path
    }
}