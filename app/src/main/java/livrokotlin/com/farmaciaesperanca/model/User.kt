package livrokotlin.com.farmaciaesperanca.model

data class User(
    var nome: String? = null,
    var email: String? = null,
    var rg: String? = null,
    var cpf: String? = null,
    var telefone: String? = null,
    var endereco: String? = null,
    var password: String? = null,
    var token: String? = null,
    val online: Boolean = false
)