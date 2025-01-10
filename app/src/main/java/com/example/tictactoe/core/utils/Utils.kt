package com.example.tictactoe.core.utils

class Utils {
    companion object {
        /**
         * utility method for maximum `System.out` efficiency while debugging 🙃
         * */
        fun printDebugger(tag: String, value: Any?) {
            print("$tag:    ")
            println(value)
        }
    }
}