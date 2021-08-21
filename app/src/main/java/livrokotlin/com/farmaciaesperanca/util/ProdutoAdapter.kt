package livrokotlin.com.farmaciaesperanca.util

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import livrokotlin.com.farmaciaesperanca.R
import livrokotlin.com.farmaciaesperanca.model.Produto

class ProdutoAdapter(context: Context): ArrayAdapter<Produto>(context, 0) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val v: View

        if(convertView != null){
            v = convertView
        }else{
            v = LayoutInflater.from(context).inflate(R.layout.item_categoria, parent, false)
        }

        val item = getItem(position)

        //val image = v.findViewById<ImageView>(R.id.imagem_produto)
        val txt_nome = v.findViewById<TextView>(R.id.txt_nomeProduto)
        val txt_preco = v.findViewById<TextView>(R.id.txt_preçoProduto)
        val txt_qtd = v.findViewById<TextView>(R.id.txt_qtdProduto)

        if(item != null){
            txt_nome.text = item.nome
            txt_preco.text = item.valor.toString()
            txt_qtd.text = item.qtd.toString()
        }

        return v

    }

}