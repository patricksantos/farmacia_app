package livrokotlin.com.farmaciaesperanca.util

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import livrokotlin.com.farmaciaesperanca.model.Pedido
import livrokotlin.com.farmaciaesperanca.model.Produto
import livrokotlin.com.farmaciaesperanca.model.database
import livrokotlin.com.farmaciaesperanca.view.DetalhesProdutosActivity
import org.jetbrains.anko.db.insert

class LineAdapter(private val produto: ArrayList<Produto>, val context: Context, val badge: badgeClass? = null) : Filterable, RecyclerView.Adapter<LineHolder>()
{
    var listPedidos: ArrayList<Produto> = ArrayList(produto)

    override fun getFilter(): Filter {
        return filterText
    }

    val filterText = object : Filter() {
        override fun performFiltering(charSequence: CharSequence): FilterResults {

            val filteredList = ArrayList<Produto>()

            if( charSequence.toString().isEmpty() ){
                filteredList.addAll(listPedidos)
            }else{
                listPedidos.forEach {
                    if( it.nome!!.toLowerCase().contains(charSequence.toString().toLowerCase()) )
                        filteredList.add(it)
                }
            }

            val filterResults = FilterResults()
            filterResults.values = filteredList
            return filterResults
        }

        override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
            produto.clear()
            produto.addAll(filterResults.values as Collection<Produto>)
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LineHolder {
        return LineHolder(LayoutInflater.from(parent.context).inflate(livrokotlin.com.farmaciaesperanca.R.layout.item_categoria, parent, false))
    }

    override fun getItemCount(): Int {
        return if (produto.isNotEmpty()) produto.size else 0
    }

    override fun onBindViewHolder(holder: LineHolder, position: Int)
    {
        val item =  produto.get(position)
        holder.title.text = item.nome
        holder.qtd.text = item.qtd.toString()
        holder.valor.text = item.valor.toString()

        Glide.with(context).load(item.fotoUrl).into(holder.image)

        holder.btn.setOnClickListener {
            if( !containsProd(item) )
            {

                val produto = Produto(
                    nome = item.nome,
                    qtd = 1,
                    categoria = item.categoria,
                    descricao = item.descricao,
                    foto = item.foto,
                    fotoUrl = item.fotoUrl,
                    valor = item.valor
                )

                saveData(
                    produto.nome.toString(),
                    produto.categoria.toString(),
                    produto.valor.toString(),
                    produto.qtd.toString(),
                    produto.fotoUrl.toString()
                )

                produtosMinhaCesta.add(produto)
                val a = badge
                a!!.att()
                val snackbar = Snackbar.make(holder.itemView, "Produto adicionado à cesta", Snackbar.LENGTH_LONG)
                snackbar.show()
            }
            else
            {
                val snackbar = Snackbar.make(holder.itemView, "Este produto já se encontra na sua cesta", Snackbar.LENGTH_LONG)
                snackbar.show()
            }
        }

        holder.itemView.setOnClickListener {
            detalhesProduto = produto.get(position)
            val intent = Intent(context, DetalhesProdutosActivity::class.java)
            context.startActivity(intent)
        }
    }

    private fun containsProd(item: Produto): Boolean {

        produtosMinhaCesta.forEach {
            if( it.nome.equals(item.nome) )
                return true
        }

        return false
    }

    private fun saveData(nome:String, categoria:String, valor:String, qtd:String, fotoUrl:String )
    {
        context.database.use {
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

}