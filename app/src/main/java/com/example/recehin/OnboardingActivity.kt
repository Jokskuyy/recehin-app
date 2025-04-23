package com.example.recehin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


class OnboardingActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var btnNext: Button
    private lateinit var btnSkip: Button
    private lateinit var btnLogin: Button
    private lateinit var btnRegister: Button
    private lateinit var tabLayout: TabLayout

    private val onboardingPages = listOf(
        OnboardingPage(
            "Kelola Keuangan Dengan Mudah",
            "Catat setiap transaksi keuangan Anda dengan praktis dan cepat",
            R.drawable.ic_logo_aplikasi
        ),
        OnboardingPage(
            "Monitoring Keuangan",
            "Pantau pengeluaran dan pemasukan Anda kapan saja dan dimana saja",
            R.drawable.ic_logo_aplikasi
        ),
        OnboardingPage(
            "Analisis Keuangan",
            "Dapatkan insight keuangan untuk pengaturan dana yang lebih baik",
            R.drawable.ic_logo_aplikasi
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        viewPager = findViewById(R.id.view_pager)
        btnNext = findViewById(R.id.btn_next)
        btnSkip = findViewById(R.id.btn_skip)
        btnLogin = findViewById(R.id.btn_login)
        btnRegister = findViewById(R.id.btn_register)
        tabLayout = findViewById(R.id.tab_layout)

        // Set adapter for viewpager
        val adapter = OnboardingPagerAdapter(this, onboardingPages)
        viewPager.adapter = adapter

        // Connect TabLayout with ViewPager2
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            // No title for tabs
        }.attach()

        // Next button click listener
        btnNext.setOnClickListener {
            if (viewPager.currentItem < onboardingPages.size - 1) {
                viewPager.currentItem += 1
            } else {
                // Already at the last page, show login/register buttons
                showLoginRegisterButtons()
            }
        }

        // Skip button click listener
        btnSkip.setOnClickListener {
            // Go to the last page
            viewPager.currentItem = onboardingPages.size - 1
        }

        // Login button click listener
        btnLogin.setOnClickListener {
            startActivity(Intent(this, HalamanLoginActivity::class.java))
            finish()
        }

        // Register button click listener
        btnRegister.setOnClickListener {
            startActivity(Intent(this, HalamanRegisterActivity::class.java))
            finish()
        }

        // Page change listener
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == onboardingPages.size - 1) {
                    // Last page
                    btnNext.text = "Mulai"
                    btnSkip.visibility = View.GONE
                } else {
                    btnNext.text = "Lanjut"
                    btnSkip.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun showLoginRegisterButtons() {
        btnLogin.visibility = View.VISIBLE
        btnRegister.visibility = View.VISIBLE
        btnNext.visibility = View.GONE
    }
}

data class OnboardingPage(
    val title: String,
    val description: String,
    val imageResId: Int
)

// This class would be in a separate file, but for simplicity, I'm including it here
class OnboardingPagerAdapter(
    private val activity: AppCompatActivity,
    private val pages: List<OnboardingPage>
) : androidx.recyclerview.widget.RecyclerView.Adapter<OnboardingPagerAdapter.OnboardingViewHolder>() {

    class OnboardingViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tv_title)
        val tvDescription: TextView = itemView.findViewById(R.id.tv_description)
        val ivImage: ImageView = itemView.findViewById(R.id.iv_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_onboarding, parent, false)
        return OnboardingViewHolder(view)
    }

    override fun onBindViewHolder(holder: OnboardingViewHolder, position: Int) {
        val page = pages[position]
        holder.tvTitle.text = page.title
        holder.tvDescription.text = page.description
        holder.ivImage.setImageResource(page.imageResId)
    }

    override fun getItemCount(): Int = pages.size
}