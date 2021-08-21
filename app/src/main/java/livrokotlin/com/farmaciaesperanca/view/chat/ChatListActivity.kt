package livrokotlin.com.farmaciaesperanca.view.chat

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
import kotlinx.android.synthetic.main.activity_chat_list.*
import livrokotlin.com.farmaciaesperanca.R
import livrokotlin.com.farmaciaesperanca.model.Contato
import livrokotlin.com.farmaciaesperanca.util.*

class ChatListActivity : AppCompatActivity() {

    lateinit var mAdapter: ChatAdapter
    lateinit var mRecyclerView: RecyclerView
    val list = ArrayList<Contato>()

    override fun onResume() {
        super.onResume()

        mRecyclerView = list_chat
        boolNotification(false)
        fatchUser()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_list)

        fatchUser()

    }

    fun boolNotification( status: Boolean){
        FirebaseFirestore.getInstance()
            .collection("Chat")
            .document("Notification")
            .update("status", status)
    }

    private fun fatchUser(){

        FirebaseFirestore.getInstance().collection("Chat")
            .document("UsuariosChat")
            .collection("Usuarios")
            .orderBy("time", Query.Direction.DESCENDING)
            .addSnapshotListener { querySnapshot, _ ->

                val docs = querySnapshot!!.documents
                list.clear()

                for(doc in docs){
                    val user = doc.toObject(Contato().javaClass)
                    list.add(user!!)
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
        mAdapter = ChatAdapter(list, this)
        mRecyclerView.adapter = mAdapter

        //val mDividerItemDecoration = DividerItemDecoration(mRecyclerView.context, layoutManager.orientation)
        //mDividerItemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider)!!)

        // Configurando um dividr entre linhas, para uma melhor visualização.
        mRecyclerView.addItemDecoration( DividerItemDecoration(mRecyclerView.context, layoutManager.orientation ))
    }


}
