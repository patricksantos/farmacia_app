package livrokotlin.com.farmaciaesperanca.util

import android.text.Editable
import livrokotlin.com.farmaciaesperanca.model.Produto

var cpfGlobal:Editable? = null
var admCpf: String? = null

var cpfDataBase: String? = null
var nomeDataBase: String? = null
var senhaDataBase: String? = null

var searchIntProduto: Int? = null

var detalhesProduto: Produto? = null

val imageSliderList = mutableListOf<String>()

var somaTotalProdutosGlobal: Double = 0.0

val produtosGlobal = mutableListOf<Produto>()

val produtosMinhaCesta = mutableListOf<Produto>()

val nomesProdutosGlobal = mutableListOf<String>()
val nomesProdutosSaudeGlobal = mutableListOf<String>()
val nomesProdutosInfantilGlobal = mutableListOf<String>()
val nomesProdutosBelezaGlobal = mutableListOf<String>()
val nomesProdutosHigieneGlobal = mutableListOf<String>()

val produtosGlobalSaude = ArrayList<Produto>()
val produtosGlobalInfantil = ArrayList<Produto>()
val produtosGlobalHigiene = ArrayList<Produto>()
val produtosGlobalBeleza = ArrayList<Produto>()

private val weightCPF = intArrayOf(11, 10, 9, 8, 7, 6, 5, 4, 3, 2)
private val weightCNPJ = intArrayOf(6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2)

fun String.isValidCPF(): Boolean {
    if (this.length != 11)
        return false

    val digit1 = digitCalc(this.substring(0, 9), weightCPF)
    val digit2 = digitCalc(this.substring(0, 9) + digit1, weightCPF)

    return this == (this.substring(0, 9) + digit1.toString() + digit2.toString())
}

fun String.isValidCNPJ(): Boolean {
    if (this.length != 14)
        return false

    val digit1 = digitCalc(this.substring(0, 12), weightCNPJ)
    val digit2 = digitCalc(this.substring(0, 12) + digit1, weightCNPJ)

    return this == (this.substring(0, 12) + digit1.toString() + digit2.toString())
}

private fun digitCalc(
    str: String,
    weight: IntArray
): Int {

    var sum = 0
    var index = str.length - 1
    var digit: Int

    while (index >= 0) {
        digit = Integer.parseInt(str.substring(index, index + 1))
        sum += digit * weight[weight.size - str.length + index]
        index--
    }

    sum = 11 - sum % 11

    return if (sum > 9)
        0
    else
        sum


}


