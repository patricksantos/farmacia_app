package livrokotlin.com.farmaciaesperanca.model

data class Message(
    val text: String? = null,
    val photo: String? = null,
    val time: Long? = null,
    val fromId: String? = null,
    val toId: String? = null
)