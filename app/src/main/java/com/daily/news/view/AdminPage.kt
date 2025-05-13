package com.daily.news.view

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.daily.news.databinding.AdminFormBinding
import com.google.firebase.database.*
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.database.FirebaseDatabase



class AdminPage : AppCompatActivity() {
    private lateinit var binding: AdminFormBinding
    private lateinit var database: DatabaseReference

    val options = FirebaseOptions.Builder()
        .setDatabaseUrl("https://console.firebase.google.com/project/x7-news-db24e/database/x7-news-db24e-default-rtdb/data/~2F")
        .setApiKey("AIzaSyB4EY_FNIH8pln6e1tmtMPKaTeJxa5px4I")
        .setApplicationId("com.daily.news")
        .build()

    val secondaryApp = FirebaseApp.initializeApp(this, options, "24x7 News")

    val secondDatabase = FirebaseDatabase.getInstance(secondaryApp!!)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = AdminFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val ref = secondDatabase.getReference("news_article")

        binding.edtId.setText(ref.key)
        // Firebase database reference
        database = FirebaseDatabase.getInstance().getReference("news_article")

        // Realtime listener
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val data = snapshot.value
                println("Data updated: $data")
            }

            override fun onCancelled(error: DatabaseError) {
                println("Failed to read value: ${error.toException()}")
            }
        })

        // Writing data
        val message = mapOf("user" to "Ali", "text" to "Hello from Kotlin!")
        database.setValue(message)
    }

}
