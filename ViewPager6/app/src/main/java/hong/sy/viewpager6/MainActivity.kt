package hong.sy.viewpager6

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import hong.sy.viewpager6.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var list = mutableListOf(R.drawable.banner1, R.drawable.banner2, R.drawable.banner3)
    private var numBanner = 3 // 배너 갯수
    private var currentPosition = Int.MAX_VALUE / 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.infiniteViewPager.adapter = InfiniteAdapter(list)
        binding.infiniteViewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.infiniteViewPager.setCurrentItem(currentPosition, false) // 현재 위치를 지정
        binding.textViewTotalBanner.text = numBanner.toString()

        // 현재 몇번째 배너인지 보여주는 숫자를 변경함
        binding.infiniteViewPager.apply {
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    binding.textViewCurrentBanner.text = "${(position % 3) + 1}" // 여기서도 %3
                }
            })
        }

        binding.linearLayoutSeeAll.setOnClickListener {
            Toast.makeText(this, "모두 보기 클릭했음", Toast.LENGTH_SHORT).show()
        }
    }
}