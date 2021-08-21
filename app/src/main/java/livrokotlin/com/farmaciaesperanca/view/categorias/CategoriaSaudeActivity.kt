package livrokotlin.com.farmaciaesperanca.view.categorias

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_categoria_saude.*
import livrokotlin.com.farmaciaesperanca.*
import livrokotlin.com.farmaciaesperanca.view.conta.CestaComprasActivity
import livrokotlin.com.farmaciaesperanca.util.*
import org.jetbrains.anko.startActivity
import androidx.recyclerview.widget.DividerItemDecoration
import livrokotlin.com.farmaciaesperanca.util.LineAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_categoria_saude.badge
import kotlinx.android.synthetic.main.activity_categoria_saude.llProgressBar
import livrokotlin.com.farmaciaesperanca.model.Produto

class CategoriaSaudeActivity : AppCompatActivity() {

    lateinit var mAdapter: LineAdapter
    lateinit var mRecyclerView: RecyclerView

    override fun onResume() {
        super.onResume()

        if( produtosMinhaCesta.isNotEmpty() ) {
            badge.visibility = View.VISIBLE
            badge.setNumber(produtosMinhaCesta.size)
        }else
            badge.visibility = View.GONE

        mRecyclerView = listItem_categoria_saude
        setupRecycler()

        if( searchIntProduto != null ){
            listItem_categoria_saude.layoutManager!!.scrollToPosition(searchIntProduto!!)
            searchIntProduto = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categoria_saude)

        //val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_2, nomesProdutosSaudeGlobal)
        //et_buscador_globalSaude.setAdapter(arrayAdapter)

        btn_searchSaude.setOnClickListener {
            if( listItem_categoria_saude.layoutManager != null )
                listItem_categoria_saude.layoutManager!!.scrollToPosition(buscarItem())
        }

        btn_comprasSaude.setOnClickListener {

            startActivity<CestaComprasActivity>()

        }

        btn_voltarSaude.setOnClickListener {

            finishFromChild(this)

        }
    }

    private fun buscarItem(): Int{

        val item= et_buscador_globalSaude.text.toString().toLowerCase()
        et_buscador_globalSaude.text.clear()
        val posicao: Int

        posicao = nomesProdutosSaudeGlobal.indexOf(item)

        return posicao

    }

    private fun setupRecycler()
    {
        // Configurando o gerenciador de layout para ser uma lista.
        val layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        mRecyclerView.layoutManager = layoutManager

        // Adiciona o adapter que irá anexar os objetos à lista.
        // Está sendo criado com lista vazia, pois será preenchida posteriormente.
        mAdapter = LineAdapter(produtosGlobalSaude, this, badgeClass(badge))
        mRecyclerView.adapter = mAdapter

        val mDividerItemDecoration = DividerItemDecoration(mRecyclerView.context, layoutManager.orientation)
        mDividerItemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider)!!)

        // Configurando um dividr entre linhas, para uma melhor visualização.
        mRecyclerView.addItemDecoration(
            mDividerItemDecoration
        )
    }

    private fun carregarProdutos()
    {
        llProgressBar.visibility = View.VISIBLE

        val docRef = FirebaseFirestore.getInstance().collection("Produtos")
            .document("Categorias")
            .collection("Saude")
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {

                    for (doc in document)
                    {
                        val value = Produto(
                            nome = doc.data["nome"].toString(),
                            descricao = doc.data["descricao"].toString(),
                            valor = doc.data["valor"].toString().toDouble(),
                            qtd = doc.data["quantidade"].toString().toInt(),
                            fotoUrl = doc.data["foto"].toString()
                        )
                        produtosGlobalSaude.add(value)
                        nomesProdutosGlobal.add(value.nome.toString())
                        nomesProdutosSaudeGlobal.add(value.nome.toString())
                    }
                }
                llProgressBar.visibility = View.GONE
                mAdapter = LineAdapter(produtosGlobalSaude, this)

            }.addOnCanceledListener {
                carregarProdutos()
            }.addOnFailureListener {
                carregarProdutos()
            }

    }

}
