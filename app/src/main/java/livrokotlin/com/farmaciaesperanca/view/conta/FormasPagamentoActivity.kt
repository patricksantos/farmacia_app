package livrokotlin.com.farmaciaesperanca.view.conta

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_formas_pagamento.*
import livrokotlin.com.farmaciaesperanca.R
import livrokotlin.com.farmaciaesperanca.model.Pedido
import livrokotlin.com.farmaciaesperanca.model.User
import livrokotlin.com.farmaciaesperanca.model.database
import livrokotlin.com.farmaciaesperanca.util.cpfDataBase
import livrokotlin.com.farmaciaesperanca.util.produtosMinhaCesta
import livrokotlin.com.farmaciaesperanca.util.somaTotalProdutosGlobal
import livrokotlin.com.farmaciaesperanca.view.CompraFinalizadaActivity
import livrokotlin.com.farmaciaesperanca.view.DetalhesProdutosActivity
import livrokotlin.com.farmaciaesperanca.view.HomeActivity
import livrokotlin.com.farmaciaesperanca.view.creditcard.CreditCardActivity
import livrokotlin.com.farmaciaesperanca.view.savvyGerenciador.PedidoDescricaoActivity.Companion.item
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.startActivity

class FormasPagamentoActivity : AppCompatActivity() {

    lateinit var pedido: Pedido

    companion object{
        var tipoEntrega:Boolean = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formas_pagamento)

        pagamento_avista.setOnClickListener {

            FirebaseFirestore.getInstance()
                .collection("Contas")
                .document(cpfDataBase.toString())
                .get()
                .addOnSuccessListener {

                    val user = it.toObject(User().javaClass)
                    val id =FirebaseFirestore.getInstance().collection("Pedidos").document().id

                    pedido = Pedido(
                        codigo = id,
                        produtos =  produtosMinhaCesta,
                        transacao = "avista",
                        entrega = tipoEntrega,
                        cpf = user!!.cpf!!.toString(),
                        nome = user.nome.toString(),
                        telefone = user.telefone!!,
                        endereco = user.endereco.toString(),
                        status = false,
                        time = System.currentTimeMillis(),
                        valorTotal = produtosMinhaCesta.sumByDouble {
                            it.valor!!.toDouble() * it.qtd!!.toDouble()
                        }
                    )

                    FirebaseFirestore.getInstance()
                        .collection("Pedidos")
                        .document(id)
                        .set(pedido)

                    produtosMinhaCesta.clear()
                    database.use {
                        delete("produtosMinhaCesta")
                    }

                    startActivity<CompraFinalizadaActivity>()
                    CompraFinalizadaActivity.codigoProduto = pedido.codigo.toString()
                    finishFromChild(this)
                }

        }

        pagamento_cartao_credito.setOnClickListener {

            startActivity<CreditCardActivity>()

        }

    }

}
