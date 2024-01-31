package com.devgordiano.myrepository.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.devgordiano.myrepository.databinding.CardRepositoryBinding

import com.devgordiano.myrepository.domain.Repository

class RepositoryAdapter(val repositorys: List<Repository>): RecyclerView.Adapter<RepositoryAdapter.ViewHolder>(){
    var shareName: (Repository) -> Unit = {

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CardRepositoryBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = repositorys.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = repositorys[position].name
        holder.share.setOnClickListener{
            val repo = repositorys[position]
            shareName(repo)
        }
    }
    class ViewHolder(binding: CardRepositoryBinding): RecyclerView.ViewHolder(binding.root){
        val title: TextView
        val share: ImageButton
        init {
            title = binding.tvRapositoryName
            share = binding.ibtnShare
        }

    }

}