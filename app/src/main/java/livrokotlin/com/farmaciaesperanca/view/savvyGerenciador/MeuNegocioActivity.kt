package livrokotlin.com.farmaciaesperanca.view.savvyGerenciador

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.app_bar_meu_negocio.*
import livrokotlin.com.farmaciaesperanca.R
import livrokotlin.com.farmaciaesperanca.model.database
import livrokotlin.com.farmaciaesperanca.util.imageSliderList
import livrokotlin.com.farmaciaesperanca.view.HomeActivity
import livrokotlin.com.farmaciaesperanca.view.LoginActivity
import livrokotlin.com.farmaciaesperanca.view.conta.MinhaContaActivity
import livrokotlin.com.farmaciaesperanca.view.chat.ChatListActivity
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.startActivity

class MeuNegocioActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onResume() {
        super.onResume()
        verBadge()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meu_negocio)

        toolbar()

        btn_adm_chat.setOnClickListener {
            startActivity<ChatListActivity>()
        }

        btn_notification.setOnClickListener {
            startActivity<ChatListActivity>()
        }

        btn_adm_edito.setOnClickListener {
            startActivity<SliderEditActivity>()
        }

        btn_adm_pedido.setOnClickListener {
            startActivity<PedidosActivity>()
        }

        btn_adm_clientes.setOnClickListener {
            startActivity<ClientesListActivity>()
        }

    }

    fun toolbar()
    {
        val toolbar: Toolbar = findViewById(R.id.toolbar_meu_negocio)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout_meu_negocio)
        val navView: NavigationView = findViewById(R.id.nav_view_meu_negocio)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout_meu_negocio)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_home_meu_negocio -> {
                if( imageSliderList.isEmpty() )
                    imageSliders()

                HomeActivity.verAdm = true
                startActivity<HomeActivity>()
            }
            R.id.nav_minha_conta_meu_negocio -> {
                startActivity<MinhaContaActivity>()
            }
            R.id.nav_meus_pedidos_meu_negocio -> {
                startActivity<PedidosActivity>()
            }
            R.id.nav_logout_meu_negocio -> {
                FirebaseAuth.getInstance().signOut()
                if( FirebaseAuth.getInstance().uid == null ){
                    database.use {
                        delete("DadosLogin")
                    }

                    startActivity<LoginActivity>()
                    finishFromChild(this)
                }
            }
        }

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout_meu_negocio)
        drawerLayout.closeDrawer(GravityCompat.START)

        return true

    }

    private fun verBadge()
    {
        FirebaseFirestore.getInstance()
            .collection("Chat")
            .document("Notification")
            .get()
            .addOnSuccessListener {
                val bool = it.data!!["status"] as Boolean
                if( bool ){
                    badge_meu_negocio.setNumber(1)
                    badge_meu_negocio.visibility = View.VISIBLE
                }
                else{
                    badge_meu_negocio.setNumber(1)
                    badge_meu_negocio.visibility = View.GONE
                }
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

}
