package io.github.rafaeljpc.traffic.controller

import io.github.rafaeljpc.traffic.controller.TrafficState.RED
import java.time.Duration
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class TrafficLight(
    private val description: String,
    private val yellowStateTime: Duration,
    initialState: TrafficState,
    private val closeListener: () -> Unit,
) {
    var state: TrafficState = initialState
        private set

    private val executor = Executors.newSingleThreadScheduledExecutor()

    fun close() {
        println("$description is closing")
        state = TrafficState.YELLOW
        executor.schedule({
            synchronized(state) {
                state = RED
                println("$description closed")
                closeListener.invoke()
            }
        }, yellowStateTime.seconds, TimeUnit.SECONDS)
    }

    fun open() {
        state = TrafficState.GREEN
        println("$description opened")
    }

    fun isClosed() = state == RED
}