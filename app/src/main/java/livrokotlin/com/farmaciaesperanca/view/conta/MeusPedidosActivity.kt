package livrokotlin.com.farmaciaesperanca.view.conta

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_meus_pedidos.*
import kotlinx.android.synthetic.main.activity_pedidos.*
import kotlinx.android.synthetic.main.content_cesta_produtos.*
import livrokotlin.com.farmaciaesperanca.R
import livrokotlin.com.farmaciaesperanca.model.Pedido
import livrokotlin.com.farmaciaesperanca.util.PedidoAdapter
import livrokotlin.com.farmaciaesperanca.util.nomeDataBase
import livrokotlin.com.farmaciaesperanca.view.creditcard.CreditCardActivity

class MeusPedidosActivity : AppCompatActivity() {

    lateinit var mAdapter: PedidoAdapter
    lateinit var mRecyclerView: RecyclerView
    val list = ArrayList<Pedido>()

    override fun onResume() {
        super.onResume()

        mRecyclerView = list_meus_pedidos
        updatePedidos()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meus_pedidos)

        updatePedidos()

    }

    private fun updatePedidos(){

        FirebaseFirestore.getInstance()
            .collection("Pedidos")
            .orderBy("time", Query.Direction.DESCENDING)
            .addSnapshotListener { querySnapshot, _ ->

                val docChanges = querySnapshot!!.documents
                list.clear()

                for( doc in docChanges ){
                    val listPedido = doc.toObject(Pedido().javaClass)
                    if( listPedido!!.nome.toString() == nomeDataBase )
                        list.add(listPedido)
                }

                if( list.isEmpty() )
                    pedido_empty_list.visibility = View.VISIBLE
                else
                    pedido_empty_list.visibility = View.GONE

                setupRecycler()
            }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)

        val item = menu!!.findItem(R.id.seach_menu)
        val searchView: SearchView = item.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                mAdapter.filter.filter(newText)
                return false
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    private fun setupRecycler()
    {
        // Configurando o gerenciador de layout para ser uma lista.
        val layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        mRecyclerView.layoutManager = layoutManager

        // Adiciona o adapter que irá anexar os objetos à lista.
        // Está sendo criado com lista vazia, pois será preenchida posteriormente.
        mAdapter = PedidoAdapter(list, this, true)
        mRecyclerView.adapter = mAdapter

        val mDividerItemDecoration = DividerItemDecoration(mRecyclerView.context, layoutManager.orientation)
        mDividerItemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider)!!)

        // Configurando um dividr entre linhas, para uma melhor visualização.
        mRecyclerView.addItemDecoration(
            mDividerItemDecoration
        )
    }

}
