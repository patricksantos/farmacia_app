package livrokotlin.com.farmaciaesperanca.model

data class Pedido(
    val codigo: String? = null,
    val transacao: String? = null,//Tipo de pagamento
    val endereco: String? = null,
    val nome: String? = null,
    val telefone: String? = null,
    val cpf: String? = null,
    val produtos: MutableList<Produto>? = null, //Produtos do Pedido
    val entrega: Boolean? = null,//Se vai ter entrega ou não
    val status: Boolean? = null, //Status do Pedido
    val time: Long? = null,
    var valorEntrega: Double? = null,
    var valorTotal: Double? = null
)