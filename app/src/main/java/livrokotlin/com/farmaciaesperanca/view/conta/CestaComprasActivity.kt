package livrokotlin.com.farmaciaesperanca.view.conta

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import kotlinx.android.synthetic.main.activity_cesta_compras.*
import kotlinx.android.synthetic.main.activity_credit_card.*
import kotlinx.android.synthetic.main.content_cesta_produtos.*
import livrokotlin.com.farmaciaesperanca.*
import livrokotlin.com.farmaciaesperanca.view.categorias.CategoriasActivity
import livrokotlin.com.farmaciaesperanca.view.FinalizarCompraActivity
import livrokotlin.com.farmaciaesperanca.util.*
import org.jetbrains.anko.*

class CestaComprasActivity : AppCompatActivity() {

    lateinit var mAdapter: LineCestaAdapter
    lateinit var mRecyclerView: RecyclerView

    override fun onResume() {
        super.onResume()

        if( produtosMinhaCesta.isEmpty() ){

            cesta_empty_list.visibility = View.VISIBLE

            SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Cesta vazia")
                .setContentText("Nenhum produto adicionado\n" + "Acesse nossas categorias e veja nossos produtos.")
                .setCancelText("Não")
                .setCancelClickListener {
                    finishFromChild(this@CestaComprasActivity)
                }
                .setConfirmText("Sim")
                .setConfirmClickListener {
                    startActivity<CategoriasActivity>()
                    finishFromChild(this@CestaComprasActivity)
                }
                .show()
        }else
            cesta_empty_list.visibility = View.GONE

        somaTotalProdutosGlobal = produtosMinhaCesta.sumByDouble { it.valor!! * it.qtd!!.toInt() }
        txt_precoProduto_total.text = somaTotalProdutosGlobal.toString()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cesta_compras)

        mRecyclerView = list_item_cesta
        setupRecycler()

        btn_comprar.setOnClickListener {
            finishFromChild(this)
            startActivity<FinalizarCompraActivity>()
        }

        btn_continuarComprar.setOnClickListener {
            finishFromChild(this)
            startActivity<CategoriasActivity>()
        }

    }

    private fun setupRecycler() {

        // Configurando o gerenciador de layout para ser uma lista.
        val layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        mRecyclerView.layoutManager = layoutManager

        // Adiciona o adapter que irá anexar os objetos à lista.
        // Está sendo criado com lista vazia, pois será preenchida posteriormente.
        mAdapter = LineCestaAdapter(produtosMinhaCesta, txt_precoProduto_total,  this)
        mRecyclerView.adapter = mAdapter

        val mDividerItemDecoration = DividerItemDecoration(mRecyclerView.context, layoutManager.orientation)
        mDividerItemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider)!!)

        // Configurando um dividr entre linhas, para uma melhor visualização.
        mRecyclerView.addItemDecoration(
            mDividerItemDecoration
        )
    }

}