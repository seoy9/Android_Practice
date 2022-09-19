package hong.sy.todolist

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Todo(
    var title: String,
    var btnImg: Int,
    var isDone: Boolean
){
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}
