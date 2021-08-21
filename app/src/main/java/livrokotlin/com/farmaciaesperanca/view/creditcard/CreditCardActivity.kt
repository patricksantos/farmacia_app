package livrokotlin.com.farmaciaesperanca.view.creditcard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_credit_card.*
import livrokotlin.com.farmaciaesperanca.R
import livrokotlin.com.farmaciaesperanca.util.CreditCardListAdapter
import livrokotlin.com.farmaciaesperanca.util.CreditCardsDataBase
import org.jetbrains.anko.startActivity

class CreditCardActivity : AppCompatActivity() {

    var callbackMainButtonUpdate : (Boolean)->Unit = {}
    var callbackBlockFields : (Boolean)->Unit = {}
    var callbackRemoveItem : (Boolean)->Unit = {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_credit_card)
        initItems()

        /*
         * Para liberar o back button na barra de topo da
         * atividade.
         * */
        supportActionBar?.setDisplayHomeAsUpEnabled( true )
        supportActionBar?.setDisplayShowHomeEnabled( true )

        btn_new_creditCard.setOnClickListener {
            startActivity<NewCreditCardActivity>()
        }
    }

    /*
     * Método que inicializa a lista de cartões de crédito.
     * */
    private fun initItems(){
        rv_credit_cards.setHasFixedSize( false )

        val layoutManager = LinearLayoutManager( this )
        rv_credit_cards.layoutManager = layoutManager

        val adapter = CreditCardListAdapter(
            this,
            CreditCardsDataBase.getItems()
        )
        adapter.registerAdapterDataObserver( RecyclerViewObserver() )
        rv_credit_cards.adapter = adapter
    }

    fun blockFields( status: Boolean ) {
        callbackBlockFields( status )
    }

    fun isMainButtonSending( status: Boolean ) {
        callbackMainButtonUpdate( status )
        callbackRemoveItem( status )
    }

    /*
     * Método utilizado para receber os callbacks do adapter
     * do RecyclerView para assim poder atualizar os itens
     * de adapter.
     * */
    fun callbacksToUpdateItem(
        mainButtonUpdate: (Boolean)->Unit,
        blockFields: (Boolean)->Unit,
        removeItem: (Boolean)->Unit ){

        callbackMainButtonUpdate = mainButtonUpdate
        callbackBlockFields = blockFields
        callbackRemoveItem = removeItem
    }

    /*
     * Método responsável por invocar o Dialog de password antes
     * que o envio do formulário ocorra. Dialog necessário em
     * alguns formulários críticos onde parte da validação é a
     * verificação da senha.
     * */
    fun callPasswordDialog(){

    }

    fun mainAction( view: View? = null ){
        blockFields( true )
        isMainButtonSending( true )
    }

    /*
     * Com o RecyclerView.AdapterDataObserver é possível
     * escutar o tamanho atual da lista de itens vinculada
     * ao RecyclerView e caso essa lista esteja vazia, então
     * podemos apresentar uma mensagem ao usuário informando
     * sobre a lista vazia.
     * */
    inner class RecyclerViewObserver : RecyclerView.AdapterDataObserver() {

        override fun onItemRangeRemoved( positionStart: Int, itemCount: Int ) {
            super.onItemRangeRemoved( positionStart, itemCount )

            tv_empty_list.visibility =
                if( rv_credit_cards.adapter!!.itemCount == 0 )
                    View.VISIBLE
                else
                    View.GONE
        }
    }

}
