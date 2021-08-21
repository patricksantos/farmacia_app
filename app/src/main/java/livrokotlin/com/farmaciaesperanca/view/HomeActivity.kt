package livrokotlin.com.farmaciaesperanca.view

import android.app.TaskStackBuilder
import android.content.ClipData
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Vibrator
import android.view.ActionMode
import com.google.android.material.bottomsheet.BottomSheetBehavior
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import android.view.MenuItem
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.smarteist.autoimageslider.IndicatorAnimations
import com.smarteist.autoimageslider.IndicatorView.draw.drawer.Drawer
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.app_bar_home.badge
import kotlinx.android.synthetic.main.content_home.*
import kotlinx.android.synthetic.main.nav_header_home.*
import kotlinx.android.synthetic.main.nav_header_home.view.*
import livrokotlin.com.farmaciaesperanca.view.conta.CestaComprasActivity
import livrokotlin.com.farmaciaesperanca.R
import livrokotlin.com.farmaciaesperanca.model.database
import livrokotlin.com.farmaciaesperanca.view.chat.ChatActivity
import livrokotlin.com.farmaciaesperanca.util.*
import livrokotlin.com.farmaciaesperanca.view.HomeActivity.Companion.verAdm
import livrokotlin.com.farmaciaesperanca.view.categorias.*
import livrokotlin.com.farmaciaesperanca.view.chat.ChatListActivity
import livrokotlin.com.farmaciaesperanca.view.conta.MeusPedidosActivity
import livrokotlin.com.farmaciaesperanca.view.conta.MinhaContaActivity
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

@Suppress("DEPRECATION")
class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onResume() {
        super.onResume()

        if( produtosMinhaCesta.isNotEmpty() ) {
            badge.visibility = View.VISIBLE
            badge.setNumber(produtosMinhaCesta.size)
        }else
            badge.visibility = View.GONE

        produtosGlobal.addAll(produtosGlobalSaude)
        produtosGlobal.addAll(produtosGlobalBeleza)
        produtosGlobal.addAll(produtosGlobalHigiene)
        produtosGlobal.addAll(produtosGlobalInfantil)

    }

    companion object{
        var verAdm = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        if( imageSliderList.isNotEmpty() )
            setSliderView()
        else
            imageSliders()

        recyclerView()

        val bottomSheet = findViewById<View>(R.id.btn_botton_sheet)

        val mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        mBottomSheetBehavior.setBottomSheetCallback( object : BottomSheetBehavior.BottomSheetCallback(){
            override fun onSlide(p0: View, p1: Float) {
            }

            override fun onStateChanged(p0: View, p1: Int) {

                if(mBottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED){

                    btn_imagemPromo.rotation = 180f

                }else if(mBottomSheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED){

                    btn_imagemPromo.rotation = 0f

                }

            }
        })

        btn_botton_sheet_upDown.setOnClickListener {
            vibrate()

            if(mBottomSheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED){

                mBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                btn_imagemPromo.rotation = 180f

            }else{

                mBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                btn_imagemPromo.rotation = 0f

            }

        }

        val arrayAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_expandable_list_item_1,
            nomesProdutosGlobal
        )

        et_buscador_global.setAdapter(arrayAdapter)

        init()

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)

    }

    @Suppress("CAST_NEVER_SUCCEEDS")
    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val mN = R.id.nav_meu_negocio as MenuItem

        if( admCpf == getString(R.string.adm_codigo) )
            mN.isVisible = true

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean
    {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_minha_conta -> {
                startActivity<MinhaContaActivity>()
            }
            R.id.nav_meus_pedidos -> {
                startActivity<MeusPedidosActivity>()
            }
            R.id.nav_minha_cesta -> {
                startActivity<CestaComprasActivity>()
            }
            R.id.nav_logout -> {
                FirebaseAuth.getInstance().signOut()
                if( FirebaseAuth.getInstance().uid == null ){
                    database.use {
                        delete("DadosLogin")
                    }

                    startActivity<LoginActivity>()
                    finishFromChild(this)
                }
            }
            R.id.nav_send ->{
                ChatActivity.chatAdm = false
                startActivity<ChatActivity>()
            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)

        return true

    }

    private fun init()
    {
        btn_categoria_verMais.setOnClickListener {
            vibrate()
            startActivity<CategoriasActivity>()
        }

        btn_categoria_saude.setOnClickListener {
            vibrate()
            startActivity<CategoriaSaudeActivity>()
        }

        btn_categoria_infantil.setOnClickListener {
            vibrate()
            startActivity<CategoriaInfantilActivity>()
        }

        btn_categoria_higiene.setOnClickListener {
            vibrate()
            startActivity<CategoriaHigieneActivity>()
        }

        btn_categoria_beleza.setOnClickListener {
            vibrate()
            startActivity<CategoriaBelezaActivity>()
        }

        btn_verMais.setOnClickListener {
            vibrate()
            startActivity<CategoriasActivity>()
        }

        btn_compras.setOnClickListener {
            vibrate()
            startActivity<CestaComprasActivity>()
        }

        btn_searchGlobal.setOnClickListener {
            searchIntProduto = buscarProduto()
            if( searchIntProduto == null){
                val snackbar = Snackbar.make(mk, "Produto não encontrado", Snackbar.LENGTH_LONG)
                snackbar.show()
            }
        }

        nossa_localizacao.setOnClickListener {
            vibrate()
            startActivity<MapsActivity>()
        }

        chat_home.setOnClickListener {
            vibrate()
            if( verAdm )
            {
                toast("Entre no chat administrativo na tela Meu Negócio")
            }else
            {
                ChatActivity.chatAdm = false
                startActivity<ChatActivity>()
            }
        }
    }

    private fun buscarProduto(): Int?{

        val item= et_buscador_global.text.toString()
        et_buscador_global.text.clear()

        when {
            nomesProdutosSaudeGlobal.contains(item) -> {

                startActivity<CategoriaSaudeActivity>()
                return nomesProdutosSaudeGlobal.indexOf(item)

            }
            nomesProdutosInfantilGlobal.contains(item) -> {

                startActivity<CategoriaInfantilActivity>()
                return nomesProdutosInfantilGlobal.indexOf(item)

            }
            nomesProdutosBelezaGlobal.contains(item) -> {

                startActivity<CategoriaBelezaActivity>()
                return nomesProdutosBelezaGlobal.indexOf(item)

            }
            nomesProdutosHigieneGlobal.contains(item) -> {

                startActivity<CategoriaHigieneActivity>()
                return nomesProdutosHigieneGlobal.indexOf(item)

            }
            else -> return null
        }

    }

    private fun setSliderView(){

        val adapter = SliderAdapter(this)
        imageSlider.sliderAdapter = adapter

        imageSlider.startAutoCycle()
        imageSlider.setIndicatorAnimation(IndicatorAnimations.WORM)
        imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
        imageSlider.autoCycleDirection = SliderView.AUTO_CYCLE_DIRECTION_RIGHT
        imageSlider.indicatorSelectedColor = Color.WHITE
        imageSlider.indicatorUnselectedColor = Color.GRAY
        imageSlider.scrollTimeInSec = 4

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

                val adapter = SliderAdapter(this)
                imageSlider.sliderAdapter = adapter

                imageSlider.startAutoCycle()
                imageSlider.setIndicatorAnimation(IndicatorAnimations.WORM)
                imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
                imageSlider.autoCycleDirection = SliderView.AUTO_CYCLE_DIRECTION_RIGHT
                imageSlider.indicatorSelectedColor = Color.WHITE
                imageSlider.indicatorUnselectedColor = Color.GRAY
                imageSlider.scrollTimeInSec = 4

                llProgressBar.visibility = View.GONE

            }
    }

    private fun recyclerView(){

        val recyclerView = recyclerView_categoria_promocao
        recyclerView.adapter = PromocaoAdapter(produtosGlobal, this)

        val layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL)
        recyclerView.layoutManager = layoutManager

    }

    private fun vibrate(){

        val vibe = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibe.vibrate(30)

    }

}
