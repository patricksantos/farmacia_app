package livrokotlin.com.farmaciaesperanca.util

import android.widget.Filter
import livrokotlin.com.farmaciaesperanca.model.Pedido
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import livrokotlin.com.farmaciaesperanca.R
import livrokotlin.com.farmaciaesperanca.model.User
import livrokotlin.com.farmaciaesperanca.view.savvyGerenciador.ClienteDescricaoActivity
import org.jetbrains.anko.startActivity

class ClienteAdapter(private val cliente: ArrayList<User>, val context: Context) : Filterable, RecyclerView.Adapter<ClienteHolder>()
{
    var listPedidos: ArrayList<User> = ArrayList(cliente)

    override fun getFilter(): Filter {
        return filterText
    }

    val filterText = object : Filter() {
        override fun performFiltering(charSequence: CharSequence): FilterResults {

            val filteredList = ArrayList<User>()

            if( charSequence.toString().isEmpty() ){
                filteredList.addAll(listPedidos)
            }else{
                listPedidos.forEach {
                    if( it.nome!!.toLowerCase().contains(charSequence.toString().toLowerCase()) )
                        filteredList.add(it)
                    else if( it.cpf!!.toLowerCase().contains(charSequence.toString().toLowerCase()) )
                        filteredList.add(it)
                }
            }

            val filterResults = FilterResults()
            filterResults.values = filteredList
            return filterResults
        }

        override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
            cliente.clear()
            cliente.addAll(filterResults.values as Collection<User>)
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClienteHolder {
        return ClienteHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_cliente, parent, false))
    }

    override fun getItemCount(): Int {
        return if (cliente.isNotEmpty()) cliente.size else 0
    }

    override fun onBindViewHolder(holder: ClienteHolder, position: Int)
    {
        val item =  cliente.get(position)

        holder.txt_nome.text = item.nome
        holder.txt_cpf.text = item.cpf

        holder.itemView.setOnClickListener {
            ClienteDescricaoActivity.user = item
            context.startActivity<ClienteDescricaoActivity>()
        }

    }

}