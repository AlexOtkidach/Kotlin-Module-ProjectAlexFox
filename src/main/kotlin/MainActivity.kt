package com.example.notesapp

import java.util.*

fun main() {
    val app = MainActivity()
    app.run()
}

class MainActivity {
    private val scanner = Scanner(System.`in`)
    private val archives = mutableListOf<Archive>()
    private val mainMenu: Menu

    init {
        val menuItems = mutableListOf<MenuItem>()

        menuItems.add(MenuItem("Выбрать архив") { showArchiveList() })
        menuItems.add(MenuItem("Создать архив") { createArchive() })
        menuItems.add(MenuItem("Выход") { exitApp() })

        mainMenu = Menu("Главное меню:", menuItems)
    }

    fun run() {
        while (true) {
            mainMenu.show()
            val input = readUserInput()
            mainMenu.selectOption(input)
        }
    }

    private fun createArchive() {
        val archiveName = readArchiveName()
        val archive = Archive(archiveName)
        archives.add(archive)
        println("Архив \"$archiveName\" успешно создан.")
        showNoteListInMenu(archive)
    }

    private fun readArchiveName(): String {
        return readUserInput("Введите название архива: ")
    }

    private fun showArchiveList() {
        while (true) {
            val archiveListMenu = createArchiveListMenu()

            archiveListMenu.show()
            val input = readUserInput()
            archiveListMenu.selectOption(input)
            val selectedArchiveIndex = input.toIntOrNull()
            if (selectedArchiveIndex != null && selectedArchiveIndex > 0 && selectedArchiveIndex <= archives.size) {
                val selectedArchive = archives[selectedArchiveIndex - 1]
                showNoteListInMenu(selectedArchive)
            } else if (selectedArchiveIndex == 0) {
                break
            } else {
                println("Некорректный выбор. Пожалуйста, попробуйте еще раз.")
            }
        }
    }

    private fun createArchiveListMenu(): Menu {
        val menuItems = mutableListOf<MenuItem>()

        if (archives.isEmpty()) {
            menuItems.add(MenuItem("Нет доступных архивов") { })
        } else {
            for ((index, archive) in archives.withIndex()) {
                menuItems.add(MenuItem("${index + 1}. ${archive.name}") { showNoteListInMenu(archive) })
            }
        }

        menuItems.add(MenuItem("${menuItems.size + 1}. Создать архив") { createArchive() })
        if (archives.isNotEmpty()) {
            menuItems.add(MenuItem("${menuItems.size + 1}. Добавить новый архив") { showArchiveList() })
        }

        menuItems.add(MenuItem("Выход") { exitApp() })

        return Menu("Список архивов:", menuItems)
    }

    private fun showNoteListInMenu(archive: Archive) {
        while (true) {
            val noteListMenu = createNoteListMenu(archive)

            noteListMenu.show()
            val input = readUserInput()
            noteListMenu.selectOption(input)
            val selectedNoteIndex = input.toIntOrNull()
            if (selectedNoteIndex != null && selectedNoteIndex > 0 && selectedNoteIndex <= archive.notes.size) {
                val selectedNote = archive.notes[selectedNoteIndex - 1]
                showNoteDetails(selectedNote)
                readUserInput()
            } else if (selectedNoteIndex == 0) {
                break
            } else {
                println("Некорректный выбор. Пожалуйста, попробуйте еще раз.")
            }
        }
    }

    private fun createNoteListMenu(archive: Archive): Menu {
        val menuItems = mutableListOf<MenuItem>()

        for ((index, note) in archive.notes.withIndex()) {
            menuItems.add(MenuItem("${index + 1}. ${note.title}") { showNoteDetails(note) })
        }

        menuItems.add(MenuItem("${menuItems.size + 1}. Вернуться") { showArchiveList() })
        menuItems.add(MenuItem("${menuItems.size + 2}. Добавить новую заметку") { addNoteToArchive(archive) })

        return Menu("Список заметок:", menuItems)
    }

    private fun showNoteDetails(note: Note) {
        println("Заметка: ${note.title}")
        println("Текст: ${note.text}")
        println("Вернуться")
    }

    private fun exitApp() {
        println("Выход")
        System.exit(0)
    }

    private fun readUserInput(message: String = ""): String {
        if (message.isNotEmpty()) {
            print(message)
        }
        return scanner.nextLine()
    }


    private fun addNoteToArchive(archive: Archive) {
        val noteTitle = readNoteTitle()
        val noteText = readNoteText()
        val note = Note(noteTitle, noteText)
        archive.addNote(note)
        println("Заметка \"$noteTitle\" успешно добавлена.")
    }

    private fun readNoteTitle(): String {
        return readUserInput("Введите заголовок заметки (Вернуться): ")
    }

    private fun readNoteText(): String {
        return readUserInput("Введите текст заметки (Вернуться): ")
    }
    private fun readUserInputExample() {
        val input = readUserInput()
    }
}
