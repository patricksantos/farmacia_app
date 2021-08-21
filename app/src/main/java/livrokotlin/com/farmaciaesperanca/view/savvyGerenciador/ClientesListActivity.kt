package livrokotlin.com.farmaciaesperanca.view.savvyGerenciador

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_clientes_list.*
import livrokotlin.com.farmaciaesperanca.R
import livrokotlin.com.farmaciaesperanca.model.User
import livrokotlin.com.farmaciaesperanca.util.ClienteAdapter

class ClientesListActivity : AppCompatActivity() {

    lateinit var mAdapter: ClienteAdapter
    lateinit var mRecyclerView: RecyclerView
    val list = ArrayList<User>()

    override fun onResume() {
        super.onResume()

        mRecyclerView = list_clientes
        updatePedidos()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clientes_list)

        updatePedidos()

    }

    private fun updatePedidos() {

        FirebaseFirestore.getInstance()
            .collection("Contas")
            .orderBy("nome", Query.Direction.DESCENDING)
            .addSnapshotListener { querySnapshot, _ ->

                val docChanges = querySnapshot!!.documents
                list.clear()

                for( doc in docChanges ){
                    val listUser = doc.toObject(User().javaClass)
                    if( listUser!!.cpf.toString() != "ZmFybWFjaWFlc3BlcmFuw6dh" )
                        list.add(listUser)
                }

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
        mAdapter = ClienteAdapter(list, this)
        mRecyclerView.adapter = mAdapter

        val mDividerItemDecoration = DividerItemDecoration(mRecyclerView.context, layoutManager.orientation)
        mDividerItemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider_blue)!!)

        // Configurando um dividr entre linhas, para uma melhor visualização.
        mRecyclerView.addItemDecoration(
            mDividerItemDecoration
        )
    }


}
