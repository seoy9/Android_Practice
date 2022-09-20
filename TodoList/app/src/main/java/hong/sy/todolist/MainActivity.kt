package hong.sy.todolist

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import hong.sy.todolist.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    // 전역 변수로 바인딩 객체 선언
    private var mBinding: ActivityMainBinding? = null
    // 매번 null 체크를 할 필요 없이 편의성을 위해 바인딩 변수 재 선언
    private val binding get() = mBinding!!

    // room db
    private lateinit var db: TodoDatabase

    lateinit var todoListAdpter: TodoListAdapter
    val datas = mutableListOf<TodoListData>()

    private var search = ""

    // 액티비티가 생성되면 가장 먼저 실행되는 메서드
    // 액티비티 최초 실행 시에만 해야 할 작업
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)

        // 자동 생성된 뷰 바인딩 클래스에서의 inflate라는 메서드를 활용해서 액티비티에서 사용할 바인딩 클래스의 인스턴스 생성
        mBinding = ActivityMainBinding.inflate(layoutInflater)

        // getRoot 메서드로 레이아웃 내부의 최상위 위치 뷰의 인스턴스를 활용하여 생성된 뷰를 액티비티에 표시
        setContentView(binding.root)

        // 이제부터 binding 바인딩 변수를 활용하여 마음 껏 xml 파일 내의 뷰 id 접근이 가능

        db = TodoDatabase.getInstance(applicationContext)!!

        initRecycler()

        binding.btnAdd.setOnClickListener {
            moveToAddPage()
        }

        todoListAdpter.setOnItemClickListener(object: TodoListAdapter.OnItemClickListner {
            override fun onItemClick(v: View, data: TodoListData, pos: Int) {
                val btnCheck: ImageButton = v.findViewById(R.id.btn_check)
                val txtTodo: TextView = v.findViewById(R.id.tv_todo)

                if(data.isDone) {
                    data.isDone = false
                    data.btnImg = R.drawable.non_check_box
                    Glide.with(v).load(data.btnImg).into(btnCheck)
                    txtTodo.paintFlags = txtTodo.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                    txtTodo.setTextColor(Color.BLACK)
                    CoroutineScope(Dispatchers.IO).launch {
                        db.todoDao().updateOne(data.title, data.btnImg, data.isDone, data.id)
                    }
                } else {
                    data.isDone = true
                    data.btnImg = R.drawable.check_box
                    Glide.with(v).load(data.btnImg).into(btnCheck)
                    txtTodo.paintFlags = txtTodo.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    txtTodo.setTextColor(Color.GRAY)
                    CoroutineScope(Dispatchers.IO).launch {
                        db.todoDao().updateOne(data.title, data.btnImg, data.isDone, data.id)
                    }
                }
            }
        })

        todoListAdpter.setOnItemLongClickListener(object : TodoListAdapter.OnItemLongClickListner {
            override fun onLongClick(v: View, data: TodoListData, pos: Int) {
                val builder = AlertDialog.Builder(this@MainActivity)
                builder.setTitle("삭제")
                    .setMessage(data.title + " 삭제하시겠습니까?")
                    .setPositiveButton("삭제",
                        DialogInterface.OnClickListener { dialog, id ->
                            val r = Runnable {
                                db.todoDao().deleteTodoById(data.id)
                            }
                            val thread = Thread(r)
                            thread.start()

                            refreshData()

                            todoListAdpter.notifyDataSetChanged()
                        })
                    .setNegativeButton("취소", null)
                builder.show()
            }
        })

        binding.searchView.isSubmitButtonEnabled = true

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                val r = Runnable {
                    val todoes = db.todoDao().searchTodo(search)

                    datas.clear()

                    for(todo in todoes) {
                        datas.add(TodoListData(todo.id, todo.title, todo.btnImg, todo.isDone))
                    }
                    todoListAdpter.datas = datas
                }

                if(search == "") {
                    refreshData()
                } else {
                    val thread = Thread(r)
                    thread.start()
                }

                todoListAdpter.notifyDataSetChanged()

                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                if(p0 != null) {
                    search = p0

                    if(p0 == "") {
                        refreshData()

                        todoListAdpter.notifyDataSetChanged()
                    }
                    return true
                }
                return false
            }
        })
    }

    // 액티비티가 일시정지 되었다가 돌아오는 경우 호출
    override fun onResume() {
        super.onResume()

        if(intent.hasExtra("title")) {
            val title = intent.getStringExtra("title").toString()
            val btnImg = R.drawable.non_check_box
            val isDone = false

            val r = Runnable {
                db.todoDao().insert(Todo(title, btnImg, isDone))
            }

            val thread = Thread(r)
            thread.start()

            refreshData()
            todoListAdpter.notifyDataSetChanged()
        }
    }

    // 뒤로가기 한 번 누르면 검색창 닫히고, 한 번 더 누르면 뒤로 감
    override fun onBackPressed() {
        if(!binding.searchView.isIconified) {
            binding.searchView.isIconified = true
        } else {
            super.onBackPressed()
        }
    }

    // 앱을 종료하는 경우 호출
    // onStop에서 아직 해제하지 못한 리소스 작업
    // 액티비티가 파괴될 때
    override fun onDestroy() {
        // onDestory 에서 binding class 인스턴스 참조를 정리해 줘야 함
        mBinding = null
        super.onDestroy()
    }

    private fun moveToAddPage() {
        val intent = Intent(this,AddTodoActivity::class.java)
        startActivity(intent)
    }

    private fun initRecycler() {
        todoListAdpter = TodoListAdapter(this)
        binding.rvTodoList.adapter = todoListAdpter
        binding.rvTodoList.addItemDecoration(VerticalItemDecorator(20))
        binding.rvTodoList.addItemDecoration(HorizontalItemDecorator(20))

        val r = Runnable {
            val todoes = db.todoDao().getAll()

            datas.clear()

            for(todo in todoes) {
                datas.add(TodoListData(todo.id, todo.title, todo.btnImg, todo.isDone))
            }

            todoListAdpter.datas = datas
        }

        val thread = Thread(r)
        thread.start()

        todoListAdpter.notifyDataSetChanged()
    }

    private fun refreshData() {
        val r = Runnable {
            val todoes = db.todoDao().getAll()

            datas.clear()

            for(todo in todoes) {
                datas.add(TodoListData(todo.id, todo.title, todo.btnImg, todo.isDone))
            }

            todoListAdpter.datas = datas
        }

        val thread = Thread(r)
        thread.start()
    }
}
