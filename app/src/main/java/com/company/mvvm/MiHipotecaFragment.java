package com.company.mvvm;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.company.mvvm.databinding.FragmentMiHipotecaBinding;


public class MiHipotecaFragment extends Fragment {
    private FragmentMiHipotecaBinding binding;
    private MiHipotecaViewModel miHipotecaViewModel;
    private double capital;
    private int plazo;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return (binding = FragmentMiHipotecaBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        miHipotecaViewModel = new ViewModelProvider(this).get(MiHipotecaViewModel.class);

        binding.calcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validar()) {
                    miHipotecaViewModel.calcular(capital, plazo);
                }
            }
        });

        miHipotecaViewModel.cuota.observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double cuota) {
                binding.cuota.setText(String.format("%.2f",cuota));
            }
        });

        miHipotecaViewModel.calculando.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean calculando) {
                if (calculando) {
                    binding.calculando.setVisibility(View.VISIBLE);
                } else {
                    binding.calculando.setVisibility(View.GONE);
                }
            }
        });

        miHipotecaViewModel.errorCapital.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean error) {
                if (error) {
                    binding.capital.setError("El capital debe ser positivo");
                } else {
                    binding.capital.setError(null);
                }
            }
        });

        miHipotecaViewModel.errorPlazos.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean error) {
                if (error) {
                    binding.plazo.setError("El plazo no puede ser negativo");
                } else {
                    binding.plazo.setError(null);
                }
            }
        });
    }

    boolean validar() {

        boolean error = false;

        try {
            capital = Double.parseDouble(binding.capital.getText().toString());
        } catch (Exception e){
            binding.capital.setError("Introduzca un número");
            error = true;
        }

        try {
            plazo = Integer.parseInt(binding.plazo.getText().toString());
        } catch (Exception e){
            binding.plazo.setError("Introduzca un número");
            error = true;
        }

        return !error;
    }
}