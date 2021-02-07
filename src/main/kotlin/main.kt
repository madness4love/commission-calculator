/*все суммы в копейках*/
val withoutCommissionLimitInMonth = 7_500_000 // только для карт Maestro и Mastercard
val excessCardLimit = 0.006 //процентная ставка при превышении лимита по картам Maestro и Mastercard
val additionalCommisson = 2_000// доп комиссия 20р при превышении лимита по картам Maestro и Mastercard
val visaMirCommission = 0.0075 // 0,75% комиисия за переводы с карт Visa и Мир
val minVisaMirComission = 3_500 // минимальная комиссия за переводы с карт Visa и Мир

// лимиты при переводы с карт
val dayLimitCard = 15_000_000
val monthLimitCard = 60_000_000

// лимиты при переводе со счета VKPay
val dayLimitVKPay = 1_500_000
val monthLimitVKPay = 4_000_000

fun main() {
    val transferType = "Maestro"
    val transferAmount = 10_000_000
    val dayAmount = 0
    val monthAmount = 0

    if (!isCorrectTransferType(transferType)) {
        println("Некорректный формат перевода")
        return
    }


    if (!isInDayLimit(transferType, transferAmount, dayAmount)) {
        println("Превышен лимит переводов за день")
        return
    }
    if (!isInMonthLimit(transferType, transferAmount, monthAmount)) {
        println("Превышен лимит переводов за месяц")
        return
    }

    val commission = calculateCommission(transferType, monthAmount, transferAmount)

    println("Коммиссия за перевод посредством $transferType составляет $commission копеек")
}

fun getDayLimit(transferType: String) : Int {
    return when (transferType) {
        "Maestro", "Mastercard", "Visa", "Мир" -> dayLimitCard
        "VK Pay" ->  dayLimitVKPay
        else -> -1
    }
}

fun getMonthLimit(transferType: String): Int {
    return when (transferType) {
        "Maestro", "Mastercard", "Visa", "Мир" -> return monthLimitCard
        "VK Pay" -> return monthLimitVKPay
        else -> -1
    }
}

fun isInDayLimit(transferType: String, transferAmount: Int, dayAmount : Int): Boolean {
    return (transferAmount + dayAmount) < getDayLimit(transferType)
}

fun isInMonthLimit(transferType: String, transferAmount: Int, monthAmount : Int): Boolean {
    return (transferAmount + monthAmount) < getMonthLimit(transferType)
}

fun getMasterMaestroCommission(transferAmount: Int, monthAmount: Int) : Int {
    return if ((transferAmount + monthAmount) < withoutCommissionLimitInMonth) {
        0
    } else {
        (transferAmount * excessCardLimit + additionalCommisson).toInt()
    }
}

fun getVisaMirCommission(transferAmount: Int) : Int {
   val commission = (transferAmount * visaMirCommission).toInt()
   return when {
       commission < minVisaMirComission ->
           minVisaMirComission
       else ->  commission
    }
}

fun isCorrectTransferType(transferType: String) : Boolean {
    return when (transferType) {
        "Maestro", "Mastercard", "Visa", "Мир", "VK Pay" -> true
        else -> false
    }
}

fun calculateCommission(transferType: String = "VK Pay", monthAmount: Int, transferAmount: Int): Int {
    return when (transferType) {
        "Maestro", "Mastercard" -> getMasterMaestroCommission(transferAmount, monthAmount)
        "Visa", "Мир" -> getVisaMirCommission(transferAmount)
        "VK Pay" -> 0
        else -> -1
    }
}