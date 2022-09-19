package hong.sy.recyclerview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import hong.sy.recyclerview.databinding.ActivityProfileDetailBinding

class ProfileDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileDetailBinding

    lateinit var name : String
    var img : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_detail)

        binding = ActivityProfileDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        name = intent.getSerializableExtra("name") as String
        img = intent.getSerializableExtra("img") as Int

        Glide.with(this).load(img).into(binding.imgProfile)
        binding.tvName.text = name
    }
}