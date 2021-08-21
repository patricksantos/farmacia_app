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
import livrokotlin.com.farmaciaesperanca.view.savvyGerenciador.PedidoDescricaoActivity
import org.jetbrains.anko.startActivity

class PedidoAdapter(private val pedido: ArrayList<Pedido>, val context: Context, val ver: Boolean) : Filterable, RecyclerView.Adapter<PedidoHolder>()
{
    var listPedidos: ArrayList<Pedido> = ArrayList(pedido)

    override fun getFilter(): Filter {
        return filterText
    }

    val filterText = object : Filter() {
        override fun performFiltering(charSequence: CharSequence): FilterResults {

            val filteredList = ArrayList<Pedido>()

            if( charSequence.toString().isEmpty() ){
                filteredList.addAll(listPedidos)
            }else{
                listPedidos.forEach {
                    if( it.nome!!.toLowerCase().contains(charSequence.toString().toLowerCase()) )
                        filteredList.add(it)
                    else if( it.codigo!!.toLowerCase().contains(charSequence.toString().toLowerCase()) )
                        filteredList.add(it)
                }
            }

            val filterResults = FilterResults()
            filterResults.values = filteredList
            return filterResults
        }

        override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
            pedido.clear()
            pedido.addAll(filterResults.values as Collection<Pedido>)
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PedidoHolder {
        return PedidoHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_pedido, parent, false))
    }

    override fun getItemCount(): Int {
        return if (pedido.isNotEmpty()) pedido.size else 0
    }

    override fun onBindViewHolder(holder: PedidoHolder, position: Int)
    {
        val item =  pedido.get(position)
        holder.codigo.text = item.codigo
        holder.title.text = item.nome

        if( item.status == true ){
            holder.image.visibility = View.GONE
            holder.imageChecked.visibility = View.VISIBLE
        }else{
            holder.image.visibility = View.VISIBLE
            holder.imageChecked.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {

            if( ver )
                PedidoDescricaoActivity.verMeusPedidos = true

            PedidoDescricaoActivity.item = item
            context.startActivity<PedidoDescricaoActivity>()
        }

    }

}




