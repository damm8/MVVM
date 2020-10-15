package com.company.mvvm;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.company.mvvm.databinding.FragmentMiHipotecaBinding;

public class MiHipotecaBadLeakFragment extends Fragment {
    private FragmentMiHipotecaBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return (binding = FragmentMiHipotecaBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.calcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                double capital = Double.parseDouble(binding.capital.getText().toString());
                int plazo = Integer.parseInt(binding.plazo.getText().toString());

                SimuladorHipoteca.Solicitud solicitud = new SimuladorHipoteca.Solicitud(capital, plazo);

                new AsyncTask<SimuladorHipoteca.Solicitud, Void, Double>(){

                    @Override
                    protected Double doInBackground(SimuladorHipoteca.Solicitud... solicitudes) {
                        SimuladorHipoteca simulador = new SimuladorHipoteca();
                        return simulador.calcular(solicitudes[0]);
                    }

                    @Override
                    protected void onPostExecute(Double cuota) {
                        super.onPostExecute(cuota);

                        binding.cuota.setText(String.format("%.2f",cuota));
                    }
                }.execute(solicitud);
            }
        });
    }
}
