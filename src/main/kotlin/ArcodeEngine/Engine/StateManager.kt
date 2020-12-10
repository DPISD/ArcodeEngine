package ArcodeEngine.Engine

import java.util.*

class StateManager {
    companion object {
        private val states: Stack<State> = Stack()
        private var currentState: State? = null

        fun tickState() {
            if (currentState != null) {
                var lastTime = System.nanoTime()
                val amountOfTicks = 60.0
                val ns = 1000000000 / amountOfTicks
                var delta = 0.0
                var timer = System.currentTimeMillis()
                var frames = 0
                while (true) {
                    val now = System.nanoTime()
                    delta += (now - lastTime) / ns
                    lastTime = now
                    while (delta >= 1) {
                        currentState?.tick()
                        delta--
                    }
                    currentState?.render()
                    frames++
                    if (System.currentTimeMillis() - timer > 1000) {
                        timer += 1000
                        println("FPS: $frames")
                        frames = 0
                    }
                }
            }
        }

        fun pushState(state: State) {
            states.push(state)
            currentState = state
        }

        fun popState(): State {
            return states.pop()
        }
    }
}