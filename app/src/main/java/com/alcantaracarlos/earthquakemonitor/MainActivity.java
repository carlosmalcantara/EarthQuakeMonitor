package com.alcantaracarlos.earthquakemonitor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.alcantaracarlos.earthquakemonitor.api.EqApiClient;
import com.alcantaracarlos.earthquakemonitor.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //preparando databinding
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        MainViewModel viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        binding.eqRecycler.setLayoutManager(new LinearLayoutManager(this));

        EqAdapter adapter = new EqAdapter();

        adapter.setOnItemClickListener(earthquake ->
                Toast.makeText(MainActivity.this, earthquake.getPlace(),
                        Toast.LENGTH_SHORT).show());

        binding.eqRecycler.setAdapter(adapter);

        viewModel.getEqList().observe(this, eqList -> {
            adapter.submitList(eqList);

            if (eqList.isEmpty()) {
                binding.emptyView.setVisibility(View.VISIBLE);
            } else {
                binding.emptyView.setVisibility(View.GONE);
            }
        });

        viewModel.getEarthquakes();

    }
}