package controllers

import khttp.get

class BitcoinController {

    private var currPrice: Double = 0.0
    private var lastUpdated: Long = 0

    init {
        updateBtcPrice()
    }

    fun getBtcPrice(): Double {
        // Update price if it has been more than 15 minutes
        if (System.currentTimeMillis() - lastUpdated >= 900000) {
            updateBtcPrice()
        }

        return currPrice
    }

    private fun updateBtcPrice() {
        val r = get("https://blockchain.info/ticker", timeout = 1.0)
        lastUpdated = System.currentTimeMillis()
        currPrice = try {
            r.jsonObject.getJSONObject("USD").getDouble("last")
        } catch (e: Exception) {
            0.0
        }
    }
}