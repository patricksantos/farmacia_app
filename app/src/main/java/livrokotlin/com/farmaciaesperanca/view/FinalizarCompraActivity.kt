package livrokotlin.com.farmaciaesperanca.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_finalizar_compra.*
import livrokotlin.com.farmaciaesperanca.R
import livrokotlin.com.farmaciaesperanca.view.conta.FormasPagamentoActivity
import livrokotlin.com.farmaciaesperanca.util.somaTotalProdutosGlobal
import org.jetbrains.anko.startActivity

class FinalizarCompraActivity : AppCompatActivity() {

    override fun onResume() {
        super.onResume()
        valorPedido_tela_final.text = somaTotalProdutosGlobal.toString()
        valorTotal_tela_final.text = somaTotalProdutosGlobal.toString()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finalizar_compra)

        retirar_loja.setOnClickListener {
            if(receber.isChecked)
                receber.isChecked = false
        }

        btn_pagamento.setOnClickListener {
            if( receber.isChecked || retirar_loja.isChecked ){
                finishFromChild(this)
                FormasPagamentoActivity.tipoEntrega = receber.isChecked
                startActivity<FormasPagamentoActivity>()
            }else
            {
                val snackbar = Snackbar.make(finalizarCompra, "Informe o modo de entrega", Snackbar.LENGTH_LONG)
                snackbar.show()
            }
        }

        receber.setOnClickListener {
            if(receber.isChecked){
                val view = View.VISIBLE
                preco_frete.visibility = view
            }else if(!receber.isChecked){
                val view = View.GONE
                preco_frete.visibility = view
            }

            if( retirar_loja.isChecked )
                retirar_loja.isChecked = false
        }

    }
}
