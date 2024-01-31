package com.devgordiano.myrepository.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.devgordiano.myrepository.R
import com.devgordiano.myrepository.data.RepositoryFactory
import com.devgordiano.myrepository.databinding.ActivityMainBinding
import com.devgordiano.myrepository.domain.Repository
import com.devgordiano.myrepository.network.RepoService
import com.devgordiano.myrepository.ui.adapter.RepositoryAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class MainActivity : AppCompatActivity() {
    lateinit var repoApi: RepoService
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRetrofit()
        setupListener()

        getUser()?.let {
            binding.etUser.text = SpannableStringBuilder(it)
            getRepo(it)
        }
    }

    private fun setupPreferences(user: String){
        saveUser(user)
        getUser()
    }

    private fun getUser(): String? {
        val sharedPref = getSharedPreferences(getString(R.string.saved_user), Context.MODE_PRIVATE)
        val result = sharedPref.getString(getString(R.string.saved_user_default), "")
        return result
    }

    private fun saveUser(user: String) {
        val sharedPref = this?.getSharedPreferences(getString(R.string.saved_user),Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()){
            putString(getString(R.string.saved_user_default), user)
            apply()
        }
    }

    private fun setupListener() {
        binding.btnFind.setOnClickListener {
            val user = binding.etUser.text.toString()
            getRepo(user)
            saveUser(user)

        }
    }
    private fun getRepo(user: String){
        repoApi.getAllRepo(user).enqueue(object : Callback<List<Repository>>{
            override fun onResponse(
                call: Call<List<Repository>>,
                response: Response<List<Repository>>
            ) {
                if (response.isSuccessful){
                    response.body()?.let {
                        setupErro(false)
                        setupList(it)
                    }
                }else{
                    setupErro(true)

                }
            }

            override fun onFailure(call: Call<List<Repository>>, t: Throwable) {
                setupErro(erro = true)
                Log.e("Eroo", "Verifique se o usuario esta correto")
            }

        })

    }
    private fun setupRetrofit() {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        repoApi = retrofit.create(RepoService::class.java)
    }
    private fun setupList(listRepo: List<Repository>) {
        val adapterRepo = RepositoryAdapter(listRepo)
        binding.rvListRepository.adapter = adapterRepo

        adapterRepo.shareName = {repository ->
            setupShare(repository)
        }
    }
    private fun setupErro(erro: Boolean){
        binding.erroFind.isVisible = erro
    }

    private fun setupShare(repository: Repository){
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, repository.name)
            type = "text/plain"
        }
        val shereIntent = Intent.createChooser(sendIntent, null)
        startActivity(shereIntent)
    }

}