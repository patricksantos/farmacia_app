package livrokotlin.com.farmaciaesperanca.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_compra_finalizada.*
import livrokotlin.com.farmaciaesperanca.R
import livrokotlin.com.farmaciaesperanca.util.cpfDataBase
import livrokotlin.com.farmaciaesperanca.view.chat.ChatActivity
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class CompraFinalizadaActivity : AppCompatActivity() {

    companion object{
        var codigoProduto = ""
    }

    override fun onResume() {
        super.onResume()
        codigo_produto.text = codigoProduto
        delivery_latest.playAnimation()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compra_finalizada)

        delivery_latest.repeatCount = 20

        chat_pedido.setOnClickListener {
            if( cpfDataBase == "07937295562" )
            {
                toast("Entre no chat administrativo na tela Meu Negócio")
            }else
            {
                ChatActivity.chatAdm = false
                startActivity<ChatActivity>()
                finishFromChild(this)
            }
        }

        btn_pedido_home.setOnClickListener {
            finishFromChild(this)
        }

    }
}
