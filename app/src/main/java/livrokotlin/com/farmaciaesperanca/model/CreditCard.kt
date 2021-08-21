package livrokotlin.com.farmaciaesperanca.model

class CreditCard(
    val number: String,
    val enterprise: String,
    val ownerFullName: String,
    val token: String? = null,
    val ownerRegNum: String = "",
    val expyrMonth: Int = 0,
    val expyrYear: Int = 0,
    val securityCode: String = ""
){

    fun getNumberAsHidden() = String.format("**** **** **** $number")

    fun getOwnerFullNameAsHidden(): String{
        val nameList = ownerFullName.split(" ")

        val firstName = nameList.first().substring(0, 2)
        val lastName = nameList.last()

        return String.format("$firstName... $lastName")
    }

}