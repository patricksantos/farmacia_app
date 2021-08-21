package livrokotlin.com.farmaciaesperanca.util

import android.widget.Filter
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import livrokotlin.com.farmaciaesperanca.R
import livrokotlin.com.farmaciaesperanca.model.Contato
import livrokotlin.com.farmaciaesperanca.model.User
import livrokotlin.com.farmaciaesperanca.view.chat.ChatActivity
import org.jetbrains.anko.startActivity

class ChatAdapter(private val contato: ArrayList<Contato>, val context: Context) : Filterable, RecyclerView.Adapter<ChatHolder>()
{
    var listContato: ArrayList<Contato> = ArrayList(contato)
    val adm = "07937295562"

    override fun getFilter(): Filter {
        return filterText
    }

    val filterText = object : Filter() {
        override fun performFiltering(charSequence: CharSequence): FilterResults {

            val filteredList = ArrayList<Contato>()

            if( charSequence.toString().isEmpty() ){
                filteredList.addAll(listContato)
            }else{
                listContato.forEach {
                    if( it.nome!!.toLowerCase().contains(charSequence.toString().toLowerCase()) )
                        filteredList.add(it)
                }
            }

            val filterResults = FilterResults()
            filterResults.values = filteredList
            return filterResults
        }

        override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
            contato.clear()
            contato.addAll(filterResults.values as Collection<Contato>)
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatHolder {
        return ChatHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false))
    }

    override fun getItemCount(): Int {
        return if (contato.isNotEmpty()) contato.size else 0
    }

    override fun onBindViewHolder(holder: ChatHolder, position: Int)
    {
        val item =  contato.get(position)
        holder.nome.text = item.nome
        holder.lastMsg.text = item.lastMessage

        holder.itemView.setOnClickListener {

            val user = User(
                nome = item.nome,
                cpf = item.fromId
            )

            ChatActivity.usuario = user
            ChatActivity.chatAdm = true
            context.startActivity<ChatActivity>()
        }

        holder.btn.setOnClickListener {

            FirebaseFirestore.getInstance().collection("Chat")
                .document("ADM")
                .collection(adm)
                .document(item.fromId.toString())
                .delete()

            FirebaseFirestore.getInstance().collection("Chat")
                .document(item.fromId.toString())
                .delete()

            FirebaseFirestore.getInstance().collection("Chat")
                .document("UsuariosChat")
                .collection("Usuarios")
                .document(item.fromId.toString())
                .delete()

            contato.removeAt(position)
            notifyItemRemoved(position)
        }

    }

}




