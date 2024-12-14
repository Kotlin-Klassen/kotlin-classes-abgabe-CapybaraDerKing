
data class Book(
    val title: String,
    val author: String,
    val genre: Genre,
    val status: BookStatus
)

enum class Genre(val description: String) {
    FICTION("Fictional stories and novels"),
    NON_FICTION("..."),
    SCIENCE("Blub"),
    HISTORY("..."),
    CHILDREN("...");

    fun printDescription() {
        println(description)
    }
}

sealed class BookStatus {
    object Available : BookStatus() {
        override fun displayStatus() = "The book is available."
    }

    data class CheckedOut(val dueDate: String) : BookStatus() {
        override fun displayStatus() = "The book is checked out. Due date: $dueDate."
    }

    data class Reserved(val reservedBy: String) : BookStatus() {
        override fun displayStatus() = "The book is reserved by $reservedBy."
    }

    abstract fun displayStatus(): String
}



class Library {
    private val books = mutableListOf<Book>()

    fun addBook(book: Book) {
        books.add(book)
        println("Book added: ${book.title}")
    }

    fun searchBook(query: String): List<Book> {
        return books.filter { it.title.contains(query, ignoreCase = true) || it.author.contains(query, ignoreCase = true) }
    }

    fun displayBooks() {
        books.forEach { book ->
            println("\nTitle: ${book.title}")
            println("Author: ${book.author}")
            println("Genre: ${book.genre}")
            println("Status: ${book.status.displayStatus()}")
            println("\nGenre Description:")
            book.genre.printDescription()
        }
    }

    class LibraryAddress(val street: String, val city: String, val zipCode: String) {
        fun printAddress() {
            println("Library Address: $street, $city, $zipCode")
        }
    }

    inner class LibraryMember(val name: String, val memberID: Int) {
        fun checkoutBook(book: Book, dueDate: String) {
            val index = books.indexOf(book)
            if (index != -1 && books[index].status is BookStatus.Available) {
                books[index] = books[index].copy(status = BookStatus.CheckedOut(dueDate))
                println("Book checked out: ${book.title} by $name. Due date: $dueDate")
            } else {
                println("Cannot checkout book: ${book.title} is not available.")
            }
        }

        fun reserveBook(book: Book) {
            val index = books.indexOf(book)
            if (index != -1 && books[index].status is BookStatus.Available) {
                books[index] = books[index].copy(status = BookStatus.Reserved(name))
                println("Book reserved: ${book.title} by $name.")
            } else {
                println("Cannot reserve book: ${book.title} is not available.")
            }
        }
    }
}

fun main() {
    val library = Library()

    val book1 = Book(
        title = "Computer Networks",
        author = "Jim Kurose",
        genre = Genre.SCIENCE,
        status = BookStatus.Available
    )

    val book2 = Book(
        title = "Object first with java",
        author = "B. K.",
        genre = Genre.SCIENCE,
        status = BookStatus.Available
    )

    library.addBook(book1)
    library.addBook(book2)

    val address = Library.LibraryAddress("123 Main St", "Booktown", "12345")
    address.printAddress()

    val member = library.LibraryMember("Alice", 101)
    member.checkoutBook(book1, "2024-12-31")
    member.reserveBook(book2)

    println("\nLibrary Books:")
    library.displayBooks()

    println("\nSearch Results:")
    val results = library.searchBook("1984")
    results.forEach { println(it.title) }
}
