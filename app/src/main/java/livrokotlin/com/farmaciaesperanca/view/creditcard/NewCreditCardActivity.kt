package livrokotlin.com.farmaciaesperanca.view.creditcard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.TextView
import com.santalu.maskedittext.MaskEditText
import kotlinx.android.synthetic.main.activity_new_credit_card.*
import livrokotlin.com.farmaciaesperanca.R
import livrokotlin.com.farmaciaesperanca.util.isValidCNPJ
import livrokotlin.com.farmaciaesperanca.util.isValidCPF

class NewCreditCardActivity : AppCompatActivity(), View.OnFocusChangeListener, TextView.OnEditorActionListener {

    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        //callPasswordDialog()
        return false
    }

    companion object{
        const val TAB_TITLE = R.string.config_credit_cards_tab_new
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_credit_card)

        bt_add_credit_card.setOnClickListener{
            /*
             * O método mainAction() é invocado no lugar
             * de callPasswordDialog(), pois aqui não há
             * necessidade de dialog de senha para a
             * adição de cartão de crédito.
             * */
            mainAction()
        }

        et_credit_card_security_code.setOnEditorActionListener( this )
        met_credit_card_owner_reg.onFocusChangeListener = this
    }

    private fun mainAction(view: View? = null ){
        blockFields( true )
        isMainButtonSending( true )
    }

    override fun onFocusChange(view: View, hasFocus: Boolean) {
        var mask = "" /* Sem máscara. */
        val editText = view as MaskEditText

        /*
         * Garantindo que o conteúdo em teste terá apenas
         * números.
         * */
        val pattern = Regex("[^0-9 ]")
        val content = editText
            .text
            .toString()
            .replace( pattern, "" )

        if( !hasFocus ){
            if( content.isValidCPF() ){
                /* Máscara CPF. */
                mask = "###.###.###-##"
            }
            else if( content.isValidCNPJ() ){
                /* Máscara CNPJ. */
                mask = "##.###.###/####-##"
            }
        }

        /*
         * Aplicando a nova máscara de acordo com o
         * padrão de código presente em campo.
         * */
        editText.mask = mask

        /*
         * Para que a nova máscara seja aplicada é preciso
         * forçar uma atualização no campo.
         * */
        editText.setText( content )
    }

    fun blockFields( status: Boolean ){
        met_credit_card_number.isEnabled = !status
        sp_credit_card_enterprise.isEnabled = !status
        et_credit_card_owner_name.isEnabled = !status
        met_credit_card_owner_reg.isEnabled = !status
        sp_credit_card_expiry_month.isEnabled = !status
        et_credit_card_expiry_year.isEnabled = !status
        et_credit_card_security_code.isEnabled = !status
        bt_add_credit_card.isEnabled = !status
    }

    fun isMainButtonSending( status: Boolean ){
        bt_add_credit_card.text =
            if( status )
                getString( R.string.add_credit_card_going )
            else
                getString( R.string.add_credit_card )
    }

}
