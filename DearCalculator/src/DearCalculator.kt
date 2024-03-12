import kotlin.math.sqrt

class DearCalculator {
    private val confidence: Int
    private val zScore: Double
    private val spDev: Double

    // confidence is how much risk you are willing to take on as a percent of event rarity you expect might happen.
    // higher the percent higher the risk.
    constructor(confidence: Double){
        this.confidence = (confidence*100).toInt()
        this.zScore = 1.285//fetchZ(confidence)
        this.spDev = 0.013705//fetchSPDev()
    }

    // Print the meaning of the dear value
    fun portDearString(portfolio:Map<String,Double>) {
        val dear = portDear(portfolio)
        println("Over a 1-day holding period there is a " +
                "${this.confidence}% chance that this $${portfolio.values.sum()} portfolio of " +
                "${portfolio.keys} will lose $$dear or more.")
    }

    // how much you will lose given event risk occurrence:
    // Over a 1-day holding period there is a ... (confidence) chance that this ... (value) portfolio of ... (stocks) will lose ...($dear) or more.
    fun portDear(portfolio:Map<String,Double>) : Double {     //Map consists of a ticker (string) and the amount of the stock (double)
        //collect external values needed
        val stockDears = mutableMapOf<String,Double>()
        val combinations = combinations(portfolio)
        val correlations = fetchCorrelation(combinations)

        //collect dears of all stocks
        val iterator = portfolio.entries.iterator()
        while (iterator.hasNext()) {
            val (stock, amount) = iterator.next()
            stockDears[stock] = stockDear(stock, amount)
        }

        //calculate the dear of the portfolio
        val portfolioSumOfSquares = stockDears.values.sumOf { it * it }
        val diversification = correlations.keys.sumOf { key ->
            val correlationValue = correlations[key]
            val portfolioDear1 = stockDears[key[0]]
            val portfolioDear2 = stockDears[key[1]]

            //make sure to adjust to check for these nulls
            (2 * correlationValue!! * portfolioDear1!! * portfolioDear2!!)
        }

        val portDear = sqrt(portfolioSumOfSquares + diversification)
        println(diversification)
        return portDear
    }

    //finds the dear of a given individual stock in the portfolio
    private fun stockDear(ticker:String, amount:Double) : Double {
        val stockDear = amount*fetchBeta(ticker)*(this.zScore*this.spDev)
        return stockDear
    }

    //creates a list of all possible combinations of 2 stocks in portfolio (n choose 2)
    private fun combinations(portfolio: Map<String, Double>): List<List<String>> {
        val combinations = mutableListOf<List<String>>()
        val list = portfolio.keys.toList()

        if (list.size < 2) {
            return listOf()
        }

        for (i in 0 until list.size - 1) {
            for (j in i + 1 until list.size) {
                combinations.add(listOf(list[i], list[j]))
            }
        }
        return combinations
    }



    private fun fetchZ(confidence:Double) : Double {
        //code - find z-score for normal distribution:
        // note: normal distribution is not an accurate measure but works for now
        return 0.0
    }
    private fun fetchSPDev() : Double {
        //code - find the standard deviation of the s&p500 given a select time frame (must be the same for correlations!)
        return 0.0
    }
    private fun fetchBeta(ticker:String) : Double {
        //code - collect beta for the given stock: volatility measure
        val betas = mutableMapOf<String, Double>(
            "META" to 1.21,
            "TSLA" to 2.25,
            "MNST" to 0.83
        )
        var beta = 0.0
        betas[ticker]?.let {
            beta = it
        }
        return beta //0.0
    }
    private fun fetchCorrelation(combinations: List<List<String>>) : Map<List<String>, Double> {
        // REMEMBER TO USE THE CORRECT TIME PERIODS AND MAKE SURE THEY ARE MOVING IN THE CORRECT DIRECTION IN TIME!
        //val correlations = mutableMapOf<List<String>, Double>()
        val correlations = mutableMapOf<List<String>, Double>(listOf("META","TSLA") to 0.352231,
            listOf("META","MNST") to 0.409969, listOf("MNST","TSLA") to 0.295826)
        // code - find the historical correlations given a select time frame (discuss and research our standard time frame)
        return correlations
    }
}