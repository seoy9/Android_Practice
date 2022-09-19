package hong.sy.viewpager2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import hong.sy.viewpager2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    // 전역 변수로 바인딩 객체 선언
    private var mBinding: ActivityMainBinding? = null
    // 매번 null 체크를 할 필요 없이 편의성을 위해 바인딩 변수 재 선언
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)

        // 자동 생성된 뷰 바인딩 클래스에서의 inflate라는 메서드를 활용해서
        // 액티비티에서 사용할 바인딩 클래스의 인스턴스 생성
        mBinding = ActivityMainBinding.inflate(layoutInflater)

        // getRoot 메서드로 레이아웃 내부의 최상위 위치 뷰의
        // 인스턴스를 활용하여 생성된 뷰를 액티비티에 표시 합니다.
        setContentView(binding.root)

        // 이제부터 binding 바인딩 변수를 활용하여 마음 껏 xml 파일 내의 뷰 id 접근이 가능해집니다.

        val pageMarginPx = resources.getDimensionPixelOffset(R.dimen.pageMargin)
        val pagerWidth = resources.getDimensionPixelOffset(R.dimen.pageWidth)
        val screenWidth = resources.displayMetrics.widthPixels
        val offsetPx = screenWidth - pageMarginPx - pagerWidth

        binding.viewPagerIdol.setPageTransformer { page, position ->
            page.translationX = position * -offsetPx
        }

        binding.viewPagerIdol.offscreenPageLimit = 1
        binding.viewPagerIdol.adapter = ViewPagerAdapter(getIconList())
        binding.viewPagerIdol.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        binding.springDotsIndicator.setViewPager2(binding.viewPagerIdol) // indicator 설정
    }

    private fun getIconList(): ArrayList<Int> {
        return arrayListOf<Int>(
            R.drawable.idol1,
            R.drawable.idol2,
            R.drawable.idol3,
            R.drawable.idol4
        )
    }
}