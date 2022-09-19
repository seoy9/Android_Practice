package hong.sy.todolist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hong.sy.todolist.databinding.ActivityAddTodoBinding

class AddTodoActivity : AppCompatActivity() {
    // 전역 변수로 바인딩 객체 선언
    private var mBinding: ActivityAddTodoBinding? = null
    // 매번 null 체크를 할 필요 없이 편의성을 위해 바인딩 변수 재 선언
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_add_todo)

        // 자동 생성된 뷰 바인딩 클래스에서의 inflate라는 메서드를 활용해서
        // 액티비티에서 사용할 바인딩 클래스의 인스턴스 생성
        mBinding = ActivityAddTodoBinding.inflate(layoutInflater)

        // getRoot 메서드로 레이아웃 내부의 최상위 위치 뷰의
        // 인스턴스를 활용하여 생성된 뷰를 액티비티에 표시 합니다.
        setContentView(binding.root)

        // 이제부터 binding 바인딩 변수를 활용하여 마음 껏 xml 파일 내의 뷰 id 접근이 가능해집니다.
        //initRecycler()

        binding.btnBack.setOnClickListener {
            moveToMainPage()
        }

        binding.btnSave.setOnClickListener {
            addTodo()
        }
    }

    // 액티비티가 파괴될 때
    override fun onDestroy() {
        // onDestory 에서 binding class 인스턴스 참조를 정리해 줘야 함
        mBinding = null
        super.onDestroy()
    }

    private fun moveToMainPage() {
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun addTodo() {
        val title = binding.edTitle.text.toString()

        // 결과 돌려줄 인텐트 생성 후 저장
        val intent = Intent(this, MainActivity::class.java)

        // 값 담기
        intent.putExtra("title", title)

        // 단순 데이터 전달 시
        startActivity(intent)

        finish()
    }
}