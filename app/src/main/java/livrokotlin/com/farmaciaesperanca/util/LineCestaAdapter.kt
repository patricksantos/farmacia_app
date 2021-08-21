package livrokotlin.com.farmaciaesperanca.util

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.jetbrains.anko.db.delete
import cn.pedant.SweetAlert.SweetAlertDialog
import livrokotlin.com.farmaciaesperanca.model.Produto
import livrokotlin.com.farmaciaesperanca.model.database
import livrokotlin.com.farmaciaesperanca.view.categorias.CategoriasActivity
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.startActivity


class LineCestaAdapter(private val produtos: MutableList<Produto>, val txt: TextView, val context: Context) : RecyclerView.Adapter<LineCestaHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LineCestaHolder {
        return LineCestaHolder(LayoutInflater.from(parent.context).inflate(livrokotlin.com.farmaciaesperanca.R.layout.item_cesta_compras, parent, false))
    }

    override fun getItemCount(): Int {
        return if (produtos.isNotEmpty()) produtos.size else 0
    }

    override fun onBindViewHolder(holder: LineCestaHolder, position: Int)
    {
        val item =  produtos.get(position)

        var qtdProduto = 0
        produtosGlobal.forEach {
            if( it.nome == item.nome )
                qtdProduto = it.qtd!!.toInt()
        }

        holder.qtd.text = item.qtd.toString()
        holder.title.text = item.nome
        holder.valor.text = item.valor.toString()

        Glide.with(context).load(item.fotoUrl).into(holder.image)

        val precoF = item.valor!! * item.qtd!!.toInt()
        holder.valorFinal.text = precoF.toString()

        if( item.foto != null )
            Glide.with(context).load(item.foto).into(holder.image)

        holder.btnRemove.setOnClickListener {

            SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Remover item")
                .setContentText("Você deseja remover este item?")
                .setCancelText("Não")
                .setCancelClickListener {
                    it.cancel()
                }
                .setConfirmText("Sim")
                .setConfirmClickListener {
                    deleteMinhaCesta(item.nome.toString())
                    somaTotalProdutosGlobal -= item.valor!! * item.qtd!!.toInt()
                    produtos.removeAt(position)
                    notifyItemRemoved(position)
                    txt.text = somaTotalProdutosGlobal.toString()

                    ver()
                    it.dismissWithAnimation()
                    /*it.setTitleText("Removido!")
                        .setContentText("O item foi removido da sua cesta de compras")
                        .setCancelClickListener(null)
                        .setConfirmText("OK")
                        .setConfirmClickListener(null)
                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE)*/

                }
                .show()

        }

        holder.btnMais.setOnClickListener {
            if( holder.qtd.text.toString().toInt() < qtdProduto ) {
                item.qtd = item.qtd.toString().toInt() + 1
                holder.qtd.text = (holder.qtd.text.toString().toInt() + 1).toString()
                holder.valorFinal.text = (item.valor!! * holder.qtd.text.toString().toInt()).toString()
                somaTotalProdutosGlobal += item.valor!!
                txt.text = somaTotalProdutosGlobal.toString()
                deleteMinhaCesta(item.nome.toString())
                saveData(item.nome.toString(), item.categoria.toString(), item.valor.toString(), item.qtd.toString(), item.fotoUrl.toString())
            }
        }

        holder.btnMenos.setOnClickListener {
            if( holder.qtd.text.toString().toInt() > 0 ) {
                item.qtd = item.qtd.toString().toInt() - 1
                holder.qtd.text = (holder.qtd.text.toString().toInt() - 1).toString()
                holder.valorFinal.text = (item.valor!! * holder.qtd.text.toString().toInt()).toString()
                somaTotalProdutosGlobal -= item.valor!!
                txt.text = somaTotalProdutosGlobal.toString()
                deleteMinhaCesta(item.nome.toString())
                saveData(item.nome.toString(), item.categoria.toString(), item.valor.toString(), item.qtd.toString(), item.fotoUrl.toString())
            }
        }

    }

    fun deleteMinhaCesta(nome:String){
        context.database.use {
            delete("produtosMinhaCesta", "nome = {nome}",	"nome" to nome)
        }
    }

    fun saveData(nome:String, categoria:String, valor:String, qtd:String, fotoUrl:String )
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

    fun ver()
    {
        if( produtosMinhaCesta.isEmpty() ){

            SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Cesta vazia")
                .setContentText("Nenhum produtos adicionado\n" + "Acesse nossas categorias e veja nossos produtos.")
                .setCancelText("Não")
                .setConfirmText("Sim")
                .setConfirmClickListener {
                    context.startActivity<CategoriasActivity>()
                }
                .show()
        }

    }

}