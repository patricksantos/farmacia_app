package livrokotlin.com.farmaciaesperanca.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.iid.FirebaseInstanceId
import livrokotlin.com.farmaciaesperanca.R
import livrokotlin.com.farmaciaesperanca.model.DadosLogin
import livrokotlin.com.farmaciaesperanca.model.Produto
import livrokotlin.com.farmaciaesperanca.model.User
import livrokotlin.com.farmaciaesperanca.model.database
import livrokotlin.com.farmaciaesperanca.util.*
import livrokotlin.com.farmaciaesperanca.view.savvyGerenciador.MeuNegocioActivity
import org.jetbrains.anko.db.parseList
import org.jetbrains.anko.db.parseOpt
import org.jetbrains.anko.db.rowParser
import org.jetbrains.anko.db.select
import org.jetbrains.anko.startActivity

class SplashActivity : AppCompatActivity() {

    val TAG = "MainActivity"

    override fun onResume() {
        super.onResume()

        imageSliders()

        if( nomesProdutosGlobal.isEmpty() ){
            saude()
            infantil()
            higiene()
            beleza()
        }

        database.use {

            select("produtosMinhaCesta").exec{

                val parser = rowParser{
                        nome: String,
                        categoria: String,
                        valor: String,
                        qtd: String,
                        fotoUrl: String->

                    Produto(
                        nome = nome,
                        categoria = categoria,
                        valor = valor.toDouble(),
                        qtd = qtd.toInt(),
                        fotoUrl = fotoUrl
                    )
                }

                val minhaCesta = parseList(parser)

                if( minhaCesta.isNotEmpty() )
                    produtosMinhaCesta.addAll(minhaCesta)

            }

            select("DadosLogin").exec{

                val parser = rowParser{
                        nome: String,
                        cpf: String,
                        senha: String->

                    DadosLogin(nome, cpf, senha)
                }

                val dataLogin = parseOpt(parser)

                cpfDataBase = dataLogin?.cpf
                nomeDataBase = dataLogin?.nome
                //senhaDataBase = dataLogin?.senha

                if( cpfDataBase != null ) {
                    admVerf()
                    updateToken(cpfDataBase.toString())
                }

            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        var time: Long = 3000

        Handler().postDelayed( Runnable {

            if( nomesProdutosGlobal.isEmpty() )
                time += time

            if( imageSliderList.isEmpty() )
                imageSliders()
            else
                time += time

            if( FirebaseAuth.getInstance().uid == null )
            {
                startActivity<MainActivity>()
                finishFromChild(this)
            }else
            {
                if( admCpf == getString(R.string.adm_codigo) )
                {
                    startActivity<MeuNegocioActivity>()
                    finishFromChild(this)
                }
                else
                {
                    HomeActivity.verAdm = false
                    startActivity<HomeActivity>()
                    finishFromChild(this)
                }
            }

        }, time)

    }

    fun admVerf()
    {
        FirebaseFirestore.getInstance()
            .collection("Contas")
            .document(cpfDataBase.toString())
            .get().addOnSuccessListener {

                val user = it.toObject(User().javaClass)

                if( user!!.cpf.toString() == getString(R.string.adm_codigo) )
                    admCpf = user.cpf.toString()

            }
    }

    private fun saude()
    {
        val docRef = FirebaseFirestore.getInstance().collection("Produtos")
            .document("Categorias")
            .collection("Saude")
            docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {

                    for (doc in document)
                    {
                        val value = Produto(
                            nome = doc.data["nome"].toString(),
                            descricao = doc.data["descricao"].toString(),
                            valor = doc.data["valor"].toString().toDouble(),
                            qtd = doc.data["quantidade"].toString().toInt(),
                            fotoUrl = doc.data["foto"].toString()
                        )
                        produtosGlobalSaude.add(value)
                        nomesProdutosGlobal.add(value.nome.toString().toLowerCase())
                        nomesProdutosSaudeGlobal.add(value.nome.toString().toLowerCase())
                    }

                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }

    }

    private fun infantil()
    {
        val docRef = FirebaseFirestore.getInstance().collection("Produtos")
            .document("Categorias")
            .collection("Infantil")
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {

                    for (doc in document)
                    {
                        val value = Produto(
                            nome = doc.data["nome"].toString(),
                            descricao = doc.data["descricao"].toString(),
                            valor = doc.data["valor"].toString().toDouble(),
                            qtd = doc.data["quantidade"].toString().toInt(),
                            fotoUrl = doc.data["foto"].toString()
                        )
                        produtosGlobalInfantil.add(value)
                        nomesProdutosGlobal.add(value.nome.toString().toLowerCase())
                        nomesProdutosInfantilGlobal.add(value.nome.toString().toLowerCase())
                    }

                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }

    }

    private fun higiene()
    {
        val docRef = FirebaseFirestore.getInstance().collection("Produtos")
            .document("Categorias")
            .collection("Higiene")
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {

                    for (doc in document)
                    {
                        val value = Produto(
                            nome = doc.data["nome"].toString(),
                            descricao = doc.data["descricao"].toString(),
                            valor = doc.data["valor"].toString().toDouble(),
                            qtd = doc.data["quantidade"].toString().toInt(),
                            fotoUrl = doc.data["foto"].toString()
                        )
                        produtosGlobalHigiene.add(value)
                        nomesProdutosGlobal.add(value.nome.toString().toLowerCase())
                        nomesProdutosHigieneGlobal.add(value.nome.toString().toLowerCase())
                    }

                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }

    }

    private fun beleza()
    {
        val docRef = FirebaseFirestore.getInstance().collection("Produtos")
            .document("Categorias")
            .collection("Beleza")
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {

                    for (doc in document)
                    {
                        val value = Produto(
                            nome = doc.data["nome"].toString(),
                            descricao = doc.data["descricao"].toString(),
                            valor = doc.data["valor"].toString().toDouble(),
                            qtd = doc.data["quantidade"].toString().toInt(),
                            fotoUrl = doc.data["foto"].toString()
                        )
                        produtosGlobalBeleza.add(value)
                        nomesProdutosGlobal.add(value.nome.toString().toLowerCase())
                        nomesProdutosBelezaGlobal.add(value.nome.toString().toLowerCase())
                    }

                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }

    }

    @Suppress("DEPRECATION")
    private fun updateToken(cpf: String){

        val token = FirebaseInstanceId.getInstance().token
        val uid = FirebaseAuth.getInstance().uid

        if( uid != null )
        {
            FirebaseFirestore.getInstance()
                .collection("Contas")
                .document(cpf)
                .update("token", token)
        }

    }

    fun imageSliders()
    {
        FirebaseFirestore.getInstance()
            .collection("Configuracoes")
            .document("Slider")
            .get().addOnSuccessListener {

                val image1 = it.data!!["slider1"] as String
                val image2 = it.data!!["slider2"] as String
                val image3 = it.data!!["slider3"] as String
                val image4 = it.data!!["slider4"] as String

                imageSliderList.add(0, image1)
                imageSliderList.add(1, image2)
                imageSliderList.add(2, image3)
                imageSliderList.add(3, image4)
            }
    }


    fun att(){
        FirebaseFirestore.getInstance()
            .collection("Configuracoes")
            .document("Slider")
            .get().addOnSuccessListener {

                val image1 = it.data!!["slider1"] as String
                val image2 = it.data!!["slider2"] as String
                val image3 = it.data!!["slider3"] as String
                val image4 = it.data!!["slider4"] as String

                imageSliderList.add(0, image1)
                imageSliderList.add(1, image2)
                imageSliderList.add(2, image3)
                imageSliderList.add(3, image4)
            }
    }

}
