package com.example.notesapp

import java.util.*

data class MenuItem(val label: String, val action: () -> Unit)

class Menu(
    private val title: String,
    private val items: List<MenuItem>
) {
    private val scanner = Scanner(System.`in`)

    fun show() {
        println(title)
        items.forEachIndexed { index, item ->
            println("${index + 1}. ${item.label}")
        }
    }

    fun selectOption(input: String) {
        val index = input.toIntOrNull()
        if (index != null && index in 1..items.size) {
            val selectedItem = items[index - 1]
            selectedItem.action()
        } else {
            println("Неверный ввод. Пожалуйста, выберите существующий пункт меню.")
        }
    }
}
