package livrokotlin.com.farmaciaesperanca.util

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_promocao.view.*
import livrokotlin.com.farmaciaesperanca.R
import livrokotlin.com.farmaciaesperanca.model.Produto
import livrokotlin.com.farmaciaesperanca.view.DetalhesProdutosActivity

class PromocaoAdapter(private val produto: List<Produto>, val context: Context) : RecyclerView.Adapter<PromocaoAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_promocao, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return produto.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val produto = produto[position]
        holder.bindView(produto, context)

        holder.itemView.setOnClickListener {
            detalhesProduto = produto
            val intent = Intent(context, DetalhesProdutosActivity::class.java)
            context.startActivity(intent)
        }
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindView(produto: Produto, context: Context) {

            val txt_nome = itemView.txt_nomeProduto_promocao
            val txt_preco = itemView.txt_preçoProduto_promocao
            val image = itemView.imagem_produto_promocao

            Glide.with(context).load(produto.fotoUrl).into(image)
            txt_nome.text = produto.nome
            txt_preco.text = produto.valor.toString()

        }

    }

}