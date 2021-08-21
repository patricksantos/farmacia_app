package livrokotlin.com.farmaciaesperanca.view.savvyGerenciador

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_cliente_descricao.*
import livrokotlin.com.farmaciaesperanca.R
import livrokotlin.com.farmaciaesperanca.model.User
import livrokotlin.com.farmaciaesperanca.view.chat.ChatActivity
import org.jetbrains.anko.startActivity

class ClienteDescricaoActivity : AppCompatActivity() {

    companion object{
        var user: User? = null
    }

    override fun onResume() {
        super.onResume()
        txt_cpf_cliente.text = user!!.cpf
        txt_email_cliente.text = user!!.email
        txt_endereco_cliente.text = user!!.endereco
        txt_nome_cliente.text = user!!.nome
        txt_telefone_cliente.text = user!!.telefone
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cliente_descricao)

        btn_chat_cliente.setOnClickListener {
            ChatActivity.usuario = user
            ChatActivity.chatAdm = true
            startActivity<ChatActivity>()
        }

    }
}
