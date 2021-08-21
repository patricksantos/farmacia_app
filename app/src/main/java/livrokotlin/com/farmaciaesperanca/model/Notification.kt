package livrokotlin.com.farmaciaesperanca.model

data class Notification(
    val text: String? = null,
    val photo: String? = null,
    val time: Long? = null,
    val fromId: String? = null,
    val fromName: String? = null,
    val toId: String? = null,
    val toName: String? = null
)