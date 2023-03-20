package com.sardordev.uploaded.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sardordev.uploaded.databinding.ItemCardBinding
import com.sardordev.uploaded.model.UsersData

class UserAdapter(val list: List<UsersData>,val listener:ClickItem) : RecyclerView.Adapter<UserAdapter.VH>() {

    inner class VH(val binding: ItemCardBinding) : RecyclerView.ViewHolder(binding.root) {

        fun onbind(usersData: UsersData) {
            binding.ttvsurname.text = usersData.surname
            binding.tvfirstname.text = usersData.firstName
            binding.tvage.text = usersData.age

            Glide.with(itemView.context).load(usersData.imgUrl).into(binding.profileImage)

            itemView.setOnClickListener {
                listener.click(usersData)
            }

            if (usersData.verify ){
                binding.imgverify.isVisible = true
            }




        }
    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = VH(
        ItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.onbind(list[position])
    }

    interface ClickItem{

        fun click(usersData: UsersData)
    }
}