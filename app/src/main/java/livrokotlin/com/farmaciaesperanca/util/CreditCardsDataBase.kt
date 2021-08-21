package livrokotlin.com.farmaciaesperanca.util

import livrokotlin.com.farmaciaesperanca.model.CreditCard

class CreditCardsDataBase {

    companion object{

        fun getItems()
                = mutableListOf(
            CreditCard(
                "6502",
                "Visa",
                "Tony Stark"
            ),
            CreditCard(
                "9270",
                "Mastercard",
                "Scarlett Johansson"
            ),
            CreditCard(
                "661",
                "American Express",
                "Margot Robbie"
            )
        )
    }
}