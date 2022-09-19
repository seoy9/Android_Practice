package hong.sy.recyclerview

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import hong.sy.recyclerview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    lateinit var multiAdapter: MultiviewAdpater
    val datas = mutableListOf<MultiData>()

    private lateinit var profileAdapter: ProfileAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multiview)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initRecycler()
    }

    private fun initRecycler() {
        multiAdapter = MultiviewAdpater(this)
        binding.rvProfile.adapter = multiAdapter
        binding.rvProfile.addItemDecoration(VerticalItemDecorator(20))
        binding.rvProfile.addItemDecoration(HorizontalItemDecorator(10))

        datas.apply {
            add(MultiData(image = R.drawable.profile1, name = "mary", age = 24, multi_type3))
            add(MultiData(image = R.drawable.profile3, name = "jenny", age = 26, multi_type2))
            add(MultiData(image = R.drawable.profile2, name = "jhon", age = 27, multi_type1))
            add(MultiData(image = R.drawable.profile5, name = "ruby", age = 21, multi_type2))
            add(MultiData(image = R.drawable.profile4, name = "yuna", age = 23, multi_type3))

            multiAdapter.datas = datas
            multiAdapter.notifyDataSetChanged()
        }
    }
}