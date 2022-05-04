package ru.hits.android.axolot

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.hits.android.axolot.databinding.ActivityBlueprintBinding
import ru.hits.android.axolot.view.BlockView

class BlueprintActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBlueprintBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBlueprintBinding.inflate(layoutInflater)
        setContentView(binding.blueprintLayout)

        binding.addBlock.setOnClickListener() {
            binding.blueprintLayout.addView(BlockView(this, null))
        }
    }

}