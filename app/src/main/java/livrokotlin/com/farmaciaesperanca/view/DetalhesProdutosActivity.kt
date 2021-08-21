package livrokotlin.com.farmaciaesperanca.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_detalhes_produtos.*
import livrokotlin.com.farmaciaesperanca.view.conta.CestaComprasActivity
import livrokotlin.com.farmaciaesperanca.R
import livrokotlin.com.farmaciaesperanca.model.Produto
import livrokotlin.com.farmaciaesperanca.model.database
import livrokotlin.com.farmaciaesperanca.util.detalhesProduto
import livrokotlin.com.farmaciaesperanca.util.produtosMinhaCesta
import nl.dionsegijn.steppertouch.OnStepCallback
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.startActivity

class DetalhesProdutosActivity : AppCompatActivity() {

    override fun onResume() {
        super.onResume()

        if ( detalhesProduto != null ){
            val produto = detalhesProduto!!
            txt_nomeProdutoDetalhes.text = produto.nome.toString()
            txt_preçoProdutoDetalhes.text = produto.valor.toString()
            txt_qtdProdutoDetalhes.text = produto.qtd.toString()
            txt_descricao.text = produto.descricao.toString()
            Glide.with(this).load(produto.fotoUrl).into(imagem_produtoDetalhes)

        }else
            finishFromChild(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        detalhesProduto = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhes_produtos)

        var qtd = 0

        stepperTouch.minValue = 0
        stepperTouch.maxValue = detalhesProduto!!.qtd!!.toInt()
        stepperTouch.sideTapEnabled = true
        stepperTouch.addStepCallback(object : OnStepCallback {
            override fun onStep(value: Int, positive: Boolean) {
                qtd = value
            }
        })

        btn_addProdutoCesta.setOnClickListener {

            if( qtd > 0){

                val produto = Produto(
                    nome = detalhesProduto!!.nome,
                    qtd = qtd,
                    categoria = detalhesProduto!!.categoria,
                    descricao = detalhesProduto!!.descricao,
                    foto = detalhesProduto!!.foto,
                    fotoUrl = detalhesProduto!!.fotoUrl,
                    valor = detalhesProduto!!.valor
                )

                saveData(produto.nome.toString(), produto.categoria.toString(), produto.valor.toString(), produto.qtd.toString(), produto.fotoUrl.toString())

                if( !containsProd(produto) )
                {
                    produtosMinhaCesta.add(produto)
                    val snackbar = Snackbar.make(detalhes_produto, "Produto adicionado à cesta", Snackbar.LENGTH_LONG)
                    snackbar.show()
                    startActivity<CestaComprasActivity>()
                }else
                {
                    val snackbar = Snackbar.make(detalhes_produto, "Este produto já se encontra na sua cesta", Snackbar.LENGTH_LONG)
                    snackbar.show()
                }

            }else
            {
                val snackbar = Snackbar.make(detalhes_produto, "Insira a quantidade", Snackbar.LENGTH_LONG)
                snackbar.show()
            }
        }

        btn_compras2.setOnClickListener {
            startActivity<CestaComprasActivity>()
        }

        btn_voltar.setOnClickListener {
            finishFromChild(this@DetalhesProdutosActivity)
        }

        btn_up_descricao.setOnClickListener {

            var view = View.VISIBLE

            if( txt_descricao.visibility == view )
            {
                view = View.GONE
                divideDesc.visibility = view
                txt_descricao.visibility = view
            }else{
                divideDesc.visibility = view
                txt_descricao.visibility = view
            }

        }

    }

    private fun saveData(nome:String, categoria:String, valor:String, qtd:String, fotoUrl:String )
    {
        database.use {
            insert(
                "produtosMinhaCesta",
                "nome" to nome,
                "categoria" to categoria,
                "valor" to valor,
                "qtd" to qtd,
                "fotoUrl" to fotoUrl
            )
        }
    }

    private fun containsProd(item: Produto): Boolean {

        produtosMinhaCesta.forEach {
            if( it.nome.equals(item.nome) )
                return true
        }

        return false
    }

}
