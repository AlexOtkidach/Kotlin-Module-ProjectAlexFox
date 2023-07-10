package com.example.notesapp

import java.util.*

fun main() {
    val app = MainActivity()
    app.run()
}

class MainActivity {
    private val scanner = Scanner(System.`in`)
    private val archives = mutableListOf<Archive>()

    fun run() {
        val mainMenu = createMainMenu()

        while (true) {
            mainMenu.show()
            val input = readUserInput()
            mainMenu.selectOption(input)
        }
    }

    private fun createMainMenu(): Menu {
        val menuItems = mutableListOf<MenuItem>()

        menuItems.add(MenuItem("Выбрать архив") { showArchiveList() })
        menuItems.add(MenuItem("Создать архив") { createArchive() })
        menuItems.add(MenuItem("Выход") { exitApp() })

        return Menu("Главное меню:", menuItems)
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

        menuItems.add(MenuItem("${menuItems.size + 1}. Создать архив") { createNewArchive() })

        if (archives.isNotEmpty()) {
            menuItems.add(MenuItem("${menuItems.size + 1}. Добавить новый архив") { showArchiveList() })
        }

        menuItems.add(MenuItem("Выход") { exitApp() })

        return Menu("Список архивов:", menuItems)
    }

    private fun createNewArchive() {
        println("Создание нового архива")

        val archiveName = readArchiveName()
        if (archiveName == "0") {
            return
        }

        val existingArchive = archives.find { it.name == archiveName }
        if (existingArchive != null) {
            println("Архив \"$archiveName\" уже существует.")
            return
        }

        val archive = Archive(archiveName)
        archives.add(archive)
        println("Архив \"$archiveName\" успешно создан.")
        showNoteListInMenu(archive)
    }

    private fun createNoteListMenu(archive: Archive): Menu {
        val menuItems = mutableListOf<MenuItem>()

        for ((index, note) in archive.notes.withIndex()) {
            menuItems.add(MenuItem("${index + 1}. ${note.title}") { showNoteDetails(note) })
        }

        menuItems.add(MenuItem("${menuItems.size + 1}. Вернуться") { showArchiveList() })
        menuItems.add(MenuItem("${menuItems.size + 2}. Добавить новую заметку") { addNoteToArchive(archive) })

        return Menu("Список заметок в архиве \"${archive.name}\":", menuItems)
    }

    private fun showNoteListInMenu(archive: Archive) {
        val noteListMenu = createNoteListMenu(archive)

        noteListMenu.show()
        val input = readUserInput()
        noteListMenu.selectOption(input)
        val selectedNoteIndex = input.toIntOrNull()
        if (selectedNoteIndex != null && selectedNoteIndex > 0 && selectedNoteIndex <= archive.notes.size) {
            val selectedNote = archive.notes[selectedNoteIndex - 1]
            showNoteDetails(selectedNote)
            readUserInput()
            showNoteListInMenu(archive)
        } else if (selectedNoteIndex == 0) {
            showNoteListInMenu(archive)
        } else {
            println("Некорректный выбор. Пожалуйста, попробуйте еще раз.")
        }
    }

    private fun addNoteToArchive(archive: Archive) {
        println("Добавление новой заметки")

        val title = readNoteTitle()
        if (title == "0") {
            return
        }

        val text = readNoteText()
        if (text == "0") {
            return
        }

        val note = Note(title, text)
        archive.addNote(note)

        println("Заметка успешно добавлена.")
    }

    private fun showArchiveList() {
        if (archives.isEmpty()) {
            println("Нет доступных архивов.")
            return
        }

        val archiveListMenu = createArchiveListMenu()
        archiveListMenu.show()
        val input = readUserInput()
        archiveListMenu.selectOption(input)
    }

    private fun createArchive() {
        println("Создание архива")

        val archiveName = readArchiveName()
        if (archiveName == "0") {
            return
        }

        val existingArchive = archives.find { it.name == archiveName }
        if (existingArchive != null) {
            println("Архив \"$archiveName\" уже существует.")
            return
        }

        val archive = Archive(archiveName)
        archives.add(archive)
        println("Архив \"$archiveName\" успешно создан.")
    }

    private fun readArchiveName(): String {
        print("Введите название архива (Вернуться): ")
        return readUserInput()
    }

    private fun readNoteTitle(): String {
        print("Введите заголовок заметки (Вернуться): ")
        return readUserInput()
    }

    private fun readNoteText(): String {
        print("Введите текст заметки (Вернуться): ")
        return readUserInput()
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

    private fun readUserInput(): String {
        print("Выберите пункт меню: ")
        return scanner.nextLine()
    }
}
