package hong.sy.todolist

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class TodoListAdapter(private val context: Context) : RecyclerView.Adapter<TodoListAdapter.ViewHolder>() {

    var datas = mutableListOf<TodoListData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoListAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_recycler, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: TodoListAdapter.ViewHolder, position: Int) {
        holder.bind(datas[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val btnCheck: ImageButton = itemView.findViewById(R.id.btn_check)
        private val txtTodo: TextView = itemView.findViewById(R.id.tv_todo)

        fun bind(item: TodoListData) {
            Glide.with(itemView).load(item.btnImg).into(btnCheck)
            txtTodo.text = item.title.toString()

            // isDone에 따라 >> 완료 체크+회색+취소선 / 미완료 미체크+검정색
            if(item.isDone) {
                txtTodo.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG)
                txtTodo.setTextColor(Color.GRAY)
            } else {
                txtTodo.setPaintFlags(0)
                txtTodo.setTextColor(Color.BLACK)
            }

            val pos = adapterPosition

            if(pos != RecyclerView.NO_POSITION) {
                itemView.setOnClickListener {
                    listner?.onItemClick(itemView, item, pos)
                }
            }

            if(pos != RecyclerView.NO_POSITION) {
//                itemView.setOnLongClickListener(View.OnLongClickListener {
//                    Log.d("Delete", item.title + " Delete")
//
//                    return@OnLongClickListener true
//                })
//                itemView.setOnLongClickListener(View.OnLongClickListener {
//                    itemLongClick?.onLongClick(itemView, item, pos)
//
//                    return@OnLongClickListener true
//                })

                itemView.setOnLongClickListener {
                    longListner?.onLongClick(itemView, item, pos)

                    return@setOnLongClickListener true
                }
            }
        }
    }

    interface OnItemClickListner {
        fun onItemClick(v: View, data: TodoListData, pos: Int)
    }
    private var listner: OnItemClickListner? = null
    fun setOnItemClickListener(listner: OnItemClickListner) {
        this.listner = listner
    }

    interface OnItemLongClickListner {
        fun onLongClick(v: View, data: TodoListData, pos: Int)
    }
    private var longListner: OnItemLongClickListner? = null
    fun setOnItemLongClickListener(listner: OnItemLongClickListner) {
        this.longListner = listner
    }
}