fun main() {
    val calculator = DearCalculator(0.1)

    val portfolio = mapOf("META" to 25000000.0, "TSLA" to 55000000.0, "MNST" to 20000000.0)
    println(calculator.portDear(portfolio))
    println(calculator.portDearString(portfolio))
}