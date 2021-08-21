package livrokotlin.com.farmaciaesperanca.view.savvyGerenciador

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_pedido_descricao.*
import livrokotlin.com.farmaciaesperanca.R
import livrokotlin.com.farmaciaesperanca.model.Pedido
import livrokotlin.com.farmaciaesperanca.model.User
import livrokotlin.com.farmaciaesperanca.util.MyFragment
import livrokotlin.com.farmaciaesperanca.view.chat.ChatActivity
import livrokotlin.com.farmaciaesperanca.view.conta.EnderecoUsuarioActivity
import org.jetbrains.anko.startActivity

class PedidoDescricaoActivity : AppCompatActivity() {

    companion object{
        var item: Pedido? = null
        var verMeusPedidos: Boolean = false
    }

    override fun onResume() {
        super.onResume()

        if( verMeusPedidos ){
            btn_pedido_status.visibility = View.GONE
            btn_chat_pedido.visibility = View.GONE
            btn_novo_endereco.visibility = View.VISIBLE
        }

        txt_pedido_codigo.text = item!!.codigo
        txt_pedido_cpf.text = item!!.cpf
        txt_pedido_endereco.text = item!!.endereco
        txt_pedido_nome.text = item!!.nome

        if( item!!.entrega == true)
            txt_pedido_entrega.text = "Cliente solicitou a Entrega do Pedido"
        else
            txt_pedido_entrega.text = "Buscar na Loja"

        if( item!!.status == true ) {
            txt_pedido_status.text = "Entregue"
            box_pedido.visibility = View.GONE
            box_pedido_check.visibility = View.VISIBLE
            btn_novo_endereco.visibility = View.GONE
        }
        else {
            box_pedido.visibility = View.VISIBLE
            box_pedido_check.visibility = View.GONE
            txt_pedido_status.text = "Não Entregue"
        }

        txt_pedido_telefone.text = item!!.telefone
        txt_pedido_transacao.text = item!!.transacao
        txt_pedido_valor_entrega.text = item!!.valorEntrega.toString()
        txt_pedido_valor_total.text = item!!.valorTotal.toString()

        txt_pedido_produtos.text = ""
        item!!.produtos!!.forEach {
            txt_pedido_produtos.text = "Nome: ${it.nome} Quantidade: ${it.qtd}\n${txt_pedido_produtos.text}"
        }

        if( item!!.status == true )
            btn_pedido_status.visibility = View.GONE


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pedido_descricao)

        btn_novo_endereco.setOnClickListener {
            chamarMyDialog()
        }

        btn_pedido_status.setOnClickListener {

            txt_pedido_status.text = "Entregue"
            box_pedido.visibility = View.GONE
            package_confirmation.visibility = View.VISIBLE
            btn_pedido_status.visibility = View.GONE

            FirebaseFirestore.getInstance()
                .collection("Pedidos")
                .document(item!!.codigo.toString())
                .update("status", true)

        }

        btn_chat_pedido.setOnClickListener {

            val user = User(
                nome = item!!.nome,
                cpf = item!!.cpf
            )

            ChatActivity.usuario = user
            ChatActivity.chatAdm = true
            startActivity<ChatActivity>()
        }

    }

    fun chamarMyDialog(){
        val myFragment = MyFragment()
        MyFragment.codPedido = item!!.codigo.toString()
        myFragment.show(supportFragmentManager,"my_fragment")
    }

}
