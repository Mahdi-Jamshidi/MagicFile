package ir.magiccodes.magicfile

import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import ir.magiccodes.magicfile.databinding.ActivityMainBinding
// View type linear => 0
// View type grid => 1
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    companion object{
        var ourViewType = 0
        var ourSpanCount = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val file = getExternalFilesDir(null)!!
        val path = file.path

        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.frame_main_container , FileFragment(path))
        transaction.commit()
    }
}