package com.capstone.nutrieasy.ui.main.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.capstone.nutrieasy.R
import com.capstone.nutrieasy.databinding.ActivityHistoryDetailBinding
import com.capstone.nutrieasy.helper.Result
import com.capstone.nutrieasy.helper.changeFormatDate
import com.capstone.nutrieasy.ui.authorization.signin.SigninFragment
import com.capstone.nutrieasy.ui.main.home.HistoryViewModel

class HistoryDetailActivity : AppCompatActivity() {
    companion object {
        const val HISTORY_ID = "HISTORY_ID"
    }

    private var id: String = "0"
    private lateinit var binding: ActivityHistoryDetailBinding
    private val storyViewModel: HistoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHistoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportActionBar?.title = "Detail Story"

        id = intent.getStringExtra(HISTORY_ID).toString()
        setLoading(true)

        storyViewModel.getDetailHistory(id)

        setDetailStory()
    }

    @SuppressLint("SetTextI18n")
    private fun setDetailStory() {

        storyViewModel.getDetailHistory(id).observe(this) { detailStoryResponse ->
            when (detailStoryResponse){
                is Result.Loading -> setLoading(true)
                is Result.Success -> {
                    setLoading(false)
                    with(binding) {
                        Glide.with(this@HistoryDetailActivity)
                            .load(detailStoryResponse.data?.story?.photoUrl)
                            .into(storyImg)
                        date.text = "Date created: ${changeFormatDate(detailStoryResponse.data?.story?.createdAt as String)}"
                        titleView.text = detailStoryResponse.data.story.name

                        val nutrientsDetail = detailStoryResponse.data.story.nutrientsDetailList?.joinToString(separator = "\n") { detail ->
                            "${detail.name}: ${detail.value}"
                        }
                        descView.text = nutrientsDetail
                    }
                }
                else -> {
                    setLoading(false)
                    if (detailStoryResponse.code == 401) {
                        PreferenceManager.getDefaultSharedPreferences(this).edit().clear().apply()
                        val intent = Intent(this, SigninFragment::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()
                    }
                    Toast.makeText(this@HistoryDetailActivity, detailStoryResponse.message, Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}