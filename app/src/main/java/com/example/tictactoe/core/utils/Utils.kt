package com.example.tictactoe.core.utils

class Utils {
    companion object {
        /**
         * I created this utility for maximum `System.out` efficiency while debugging 🙃
         * */
        fun printDebugger(tag: String, value: Any) {
            print("$tag:    ")
            println(value)
        }
    }
}