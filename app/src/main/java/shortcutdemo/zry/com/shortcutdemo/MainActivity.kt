package shortcutdemo.zry.com.shortcutdemo

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        ShortcutHelper.init(this)
        ShortcutHelper.addShortcut(this)

        val intent = Intent(this, ComposeActivity::class.java)
        startActivity(intent)
    }
}
