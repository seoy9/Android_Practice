package hong.sy.todolist

import androidx.room.*

@Dao
interface TodoDao {
    // 테이블에 데이터 삽입
    @Insert
    fun insert(todo: Todo)

    // 테이블의 데이터 수정
    @Update
    fun update(todo: Todo)

    // 테이블의 데이터 삭제
    @Delete
    fun delete(todo: Todo)

    // 테이블의 모든 값을 가져오기
    @Query("SELECT * FROM Todo")
    fun getAll(): List<Todo>

    // 'id'에 해당하는 todo를 삭제
    @Query("DELETE FROM Todo WHERE id = :id")
    fun deleteTodoById(id: Int)

    // 'id'에 해당하는 todo 수정
    @Query("UPDATE Todo SET title = :title, btnImg = :btnImg, isDone = :isDone WHERE id = :id")
    fun updateOne(title: String, btnImg: Int, isDone: Boolean, id: Int)

    // 'title'에 해당하는 todo 가져오기
    @Query("SELECT * FROM Todo WHERE title LIKE '%' || :pattern || '%'")
    fun searchTodo(pattern: String): List<Todo>
}