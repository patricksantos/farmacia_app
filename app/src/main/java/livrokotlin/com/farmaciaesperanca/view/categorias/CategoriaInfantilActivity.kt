package livrokotlin.com.farmaciaesperanca.view.categorias

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_categoria_infantil.*
import kotlinx.android.synthetic.main.activity_categoria_infantil.llProgressBar
import livrokotlin.com.farmaciaesperanca.*
import livrokotlin.com.farmaciaesperanca.model.Produto
import livrokotlin.com.farmaciaesperanca.view.conta.CestaComprasActivity
import livrokotlin.com.farmaciaesperanca.util.*
import org.jetbrains.anko.startActivity

class CategoriaInfantilActivity : AppCompatActivity() {

    lateinit var mAdapter: LineAdapter
    lateinit var mRecyclerView: RecyclerView

    override fun onResume() {
        super.onResume()

        if( produtosMinhaCesta.isNotEmpty() ) {
            badge.visibility = View.VISIBLE
            badge.setNumber(produtosMinhaCesta.size)
        }else
            badge.visibility = View.GONE

        mRecyclerView = listItem_categoria_infantil
        if( produtosGlobalInfantil.isEmpty() )
            carregarProdutos()
        setupRecycler()

        if( searchIntProduto != null ){
            listItem_categoria_infantil.layoutManager!!.scrollToPosition(searchIntProduto!!)
            searchIntProduto = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categoria_infantil)

        //val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, nomesProdutosInfantilGlobal)
        //et_buscador_globalInfantil.setAdapter(arrayAdapter)

        btn_searchInfantil.setOnClickListener {
            if( listItem_categoria_infantil.layoutManager != null )
                listItem_categoria_infantil.layoutManager!!.scrollToPosition(buscarItem())
        }

        btn_comprasInfantil.setOnClickListener {

            startActivity<CestaComprasActivity>()

        }

        btn_voltarInfantil.setOnClickListener {

            finishFromChild(this)

        }
    }

    private fun buscarItem(): Int{

        val item= et_buscador_globalInfantil.text.toString().toLowerCase()
        et_buscador_globalInfantil.text.clear()
        val posicao: Int

        posicao = nomesProdutosInfantilGlobal.indexOf(item)

        return posicao

    }

    private fun setupRecycler() {

        // Configurando o gerenciador de layout para ser uma lista.
        val layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        mRecyclerView.layoutManager = layoutManager

        // Adiciona o adapter que irá anexar os objetos à lista.
        // Está sendo criado com lista vazia, pois será preenchida posteriormente.
        mAdapter = LineAdapter(produtosGlobalInfantil, this,badgeClass(badge))
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
            .collection("Infantil")
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
                        produtosGlobalInfantil.add(value)
                        nomesProdutosGlobal.add(value.nome.toString())
                        nomesProdutosInfantilGlobal.add(value.nome.toString())
                    }
                }
                llProgressBar.visibility = View.GONE
                mAdapter = LineAdapter(produtosGlobalInfantil, this)

            }.addOnCanceledListener {
                carregarProdutos()
            }.addOnFailureListener {
                carregarProdutos()
            }

    }

}
