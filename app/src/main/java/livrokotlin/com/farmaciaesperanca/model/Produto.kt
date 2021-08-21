package livrokotlin.com.farmaciaesperanca.model

import android.graphics.Bitmap

data class Produto(
    var nome:String? = null,
    var categoria: String? = null,
    var descricao: String? = null,
    var valor: Double? = null,
    var foto: Bitmap? = null,
    var fotoUrl: String? = null,
    var qtd: Int? = null
)




