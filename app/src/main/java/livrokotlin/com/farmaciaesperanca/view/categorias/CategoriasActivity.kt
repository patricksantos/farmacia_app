package livrokotlin.com.farmaciaesperanca.view.categorias

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_categorias.*
import livrokotlin.com.farmaciaesperanca.*
import livrokotlin.com.farmaciaesperanca.view.conta.CestaComprasActivity
import livrokotlin.com.farmaciaesperanca.util.*
import org.jetbrains.anko.startActivity

class CategoriasActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categorias)

        if( produtosMinhaCesta.isNotEmpty() ) {
            badge.visibility = View.VISIBLE
            badge.setNumber(produtosMinhaCesta.size)
        }else
            badge.visibility = View.GONE

        val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,
            nomesProdutosGlobal
        )
        et_buscador_globalCategorias.setAdapter(arrayAdapter)

        btn_voltarCategoria.setOnClickListener {
            finishFromChild(this)
        }

        btn_comprasCategoria.setOnClickListener {
            startActivity<CestaComprasActivity>()
            finishFromChild(this)
        }

        btn_categoria_saudeC.setOnClickListener {
            startActivity<CategoriaSaudeActivity>()
            finishFromChild(this)
        }

        btn_categoria_infantilC.setOnClickListener {
            startActivity<CategoriaInfantilActivity>()
            finishFromChild(this)
        }

        btn_categoria_higieneC.setOnClickListener {
            startActivity<CategoriaHigieneActivity>()
            finishFromChild(this)
        }

        btn_categoria_belezaC.setOnClickListener {
            finishFromChild(this)
            startActivity<CategoriaBelezaActivity>()
        }

        btn_searchGlobalCategorias.setOnClickListener {
            searchIntProduto = buscarProduto()
            if( searchIntProduto == null)
            {
                val snackbar = Snackbar.make(categoriasActivity, "Produto não encontrado", Snackbar.LENGTH_LONG)
                snackbar.show()
            }
        }

    }

    private fun buscarProduto(): Int?{

        val item= et_buscador_globalCategorias.text.toString()
        et_buscador_globalCategorias.text.clear()

        if( nomesProdutosSaudeGlobal.contains(item) ){

            startActivity<CategoriaSaudeActivity>()
            return nomesProdutosSaudeGlobal.indexOf(item)

        }else if( nomesProdutosInfantilGlobal.contains(item) ){

            startActivity<CategoriaInfantilActivity>()
            return nomesProdutosInfantilGlobal.indexOf(item)

        }else if( nomesProdutosBelezaGlobal.contains(item) ){

            startActivity<CategoriaBelezaActivity>()
            return nomesProdutosBelezaGlobal.indexOf(item)

        }else if( nomesProdutosHigieneGlobal.contains(item) ){

            startActivity<CategoriaHigieneActivity>()
            return nomesProdutosHigieneGlobal.indexOf(item)

        }

        return null

    }

}
