package com.example.lab43

import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.lab43.databinding.ItemUserBinding
import com.example.lab43.model.Pizza


interface UserActionListener {

    fun onUserMove(pizza: Pizza, moveBy: Int)

    fun onUserDelete(pizza: Pizza)

    fun onUserDetails(pizza: Pizza)

}

class UsersAdapter(
    private val actionListener: UserActionListener
) : RecyclerView.Adapter<UsersAdapter.UsersViewHolder>(), View.OnClickListener {

    var pizzas: List<Pizza> = emptyList()
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    override fun onClick(v: View) {
        val pizza = v.tag as Pizza
        when (v.id) {
            R.id.moreImageViewButton -> {
                showPopupMenu(v)
            }
            else -> {
                actionListener.onUserDetails(pizza)
            }
        }
    }

    override fun getItemCount(): Int = pizzas.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemUserBinding.inflate(inflater, parent, false)

        binding.root.setOnClickListener(this)
        binding.moreImageViewButton.setOnClickListener(this)

        return UsersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        val user = pizzas[position]
        with(holder.binding) {
            holder.itemView.tag = user
            moreImageViewButton.tag = user

            userNameTextView.text = user.name
            userCompanyTextView.text = user.price

            if (!user.image.isNullOrBlank()) {
                Glide.with(photoImageView.context)
                    .load(user.image)
                    .circleCrop()
                    .placeholder(R.drawable.ic_user_avatar)
                    .error(R.drawable.ic_user_avatar)
                    .into(photoImageView)
            } else {
                Glide.with(photoImageView.context).clear(photoImageView)
                photoImageView.setImageResource(R.drawable.ic_user_avatar)
            }
        }
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(view.context, view)
        val context = view.context
        val pizza = view.tag as Pizza
        val position = pizzas.indexOfFirst { it.id == pizza.id }

        //popupMenu.menu.add(0, ID_MOVE_UP, Menu.NONE, context.getString(R.string.move_up)).apply {
         //   isEnabled = position > 0
       // }
       // popupMenu.menu.add(0, ID_MOVE_DOWN, Menu.NONE, context.getString(R.string.move_down)).apply {
         //   isEnabled = position < users.size - 1
        //}
        popupMenu.menu.add(0, ID_REMOVE, Menu.NONE, context.getString(R.string.remove))

        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                //ID_MOVE_UP -> {
                //    actionListener.onUserMove(user, -1)
                //}
                //ID_MOVE_DOWN -> {
                //    actionListener.onUserMove(user, 1)
                //}
                //ID_REMOVE -> {
                //    actionListener.onUserDelete(user)
                //}
            }
            return@setOnMenuItemClickListener true
        }

        popupMenu.show()
    }

    class UsersViewHolder(
        val binding: ItemUserBinding
    ) : RecyclerView.ViewHolder(binding.root)

    companion object {
        //private const val ID_MOVE_UP = 1
        //private const val ID_MOVE_DOWN = 2
        private const val ID_REMOVE = 3
    }
}
