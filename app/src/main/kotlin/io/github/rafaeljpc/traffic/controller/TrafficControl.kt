package io.github.rafaeljpc.traffic.controller

import java.time.Duration
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


class TrafficControl(
    private val redStateTime: Duration,
    yellowStateTime: Duration,
) {
    val direction1: TrafficLight = TrafficLight("L1", yellowStateTime, TrafficState.GREEN) {
        direction2.open()
        changeState()
    }
    val direction2: TrafficLight = TrafficLight("L2", yellowStateTime, TrafficState.RED) {
        direction1.open()
        changeState()
    }

    private val executor = Executors.newSingleThreadScheduledExecutor()

    fun start() {
        direction1.close()
    }

    private fun changeState() {
        executor.schedule(
            {
                if (direction1.isClosed()) {
                    direction2.close()
                } else {
                    direction1.close()
                }
            },
            redStateTime.seconds, TimeUnit.SECONDS
        )

    }
}