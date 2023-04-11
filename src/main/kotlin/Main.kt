import java.lang.IndexOutOfBoundsException
import kotlin.math.ceil
import kotlin.math.floor

const val SEAT_PRICE = 10
const val BACK_SEAT_PRICE = 8
const val SEATS_THRESHOLD = 60

data class Sales(val soldSeats: MutableList<Pair<Int, Int>>, var currentIncome: Int)

fun printCinema(seats: MutableList<MutableList<Char>>) {
    println("Cinema:")
    print(" ")
    for (i in 1..seats[0].size) {
        print(" $i")
    }
    println()
    for (i in 0 until seats.size) {
        print("${i + 1} ")
        println(seats[i].joinToString(" "))
    }
}

fun calculateIncome(rows: Int, seatsPerRow: Int): Int {
    println("Total income:")
    val totalSeats: Int = rows * seatsPerRow
    if (totalSeats <= SEATS_THRESHOLD) {
        return (totalSeats * SEAT_PRICE)
    }
    return ((floor(rows / 2.0) * seatsPerRow * SEAT_PRICE) + (ceil(rows / 2.0) * seatsPerRow * BACK_SEAT_PRICE)).toInt()
}

fun calculatePrice(seats: MutableList<MutableList<Char>>, row: Int): Int {
    val rows: Int = seats.size
    val seatsPerRow: Int = seats[0].size
    val totalSeats: Int = rows * seatsPerRow
    if (totalSeats <= SEATS_THRESHOLD) {
        return SEAT_PRICE
    } else {
        if (row > rows / 2.0) {
            return BACK_SEAT_PRICE
        }
        return SEAT_PRICE
    }
}

fun buyTicket(seats: MutableList<MutableList<Char>>, sales: Sales) {
    var seatBought = false
    do {
        print("Enter a row number: ")
        val row = readln().toInt()
        print("Enter a seat number in that row: ")
        val seat = readln().toInt()
        try {
            if (sales.soldSeats.contains(Pair(row, seat))) {
                throw Exception("That ticket has already been purchased")
            }
            else {
                seats[row - 1][seat - 1] = 'B'
                val ticketPrice = calculatePrice(seats, row)
                println("Ticket price: \$$ticketPrice")
                sales.currentIncome = sales.currentIncome + ticketPrice
                sales.soldSeats.add(Pair(row, seat))
                seatBought = true
            }
        } catch (e: IndexOutOfBoundsException) {
            println("Wrong input!")
        } catch (e: Exception) {
            println(e.message)
        }
    } while (!seatBought)
}

fun printStatistics(seats: MutableList<MutableList<Char>>, sales: Sales, totalIncome: Int) {
    val totalSeats = seats.size * seats[0].size
    val percentage = (sales.soldSeats.size / totalSeats.toDouble()) * 100
    val formatPercentage = "%.2f".format(percentage)
    println(
        """
        Number of purchased tickets: ${sales.soldSeats.size}
        Percentage: $formatPercentage%
        Current income: ${'$'}${sales.currentIncome}
        Total income: ${'$'}${totalIncome}
    """.trimIndent()
    )
}

fun main() {
    print("Enter the number of rows: ")
    val rows = readln().toInt()
    print("Enter the number of seats in each row: ")
    val seatsPerRow = readln().toInt()
    val sales = Sales(mutableListOf(), 0)
    val totalIncome = calculateIncome(rows, seatsPerRow)
    val seats = MutableList(rows) { MutableList(seatsPerRow) { 'S' } }
    do {
        println(
            """
            1. Show the seats
            2. Buy a ticket
            3. Statistics
            0. Exit
        """.trimIndent()
        )
        print("> ")
        val option = readln().toInt()
        when (option) {
            1 -> printCinema(seats)
            2 -> buyTicket(seats, sales)
            3 -> printStatistics(seats, sales, totalIncome)
            else -> {}
        }
    } while (option != 0)
}