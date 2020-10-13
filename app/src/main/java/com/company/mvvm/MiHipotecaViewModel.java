package com.company.mvvm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MiHipotecaViewModel extends AndroidViewModel {

    Executor executor;

    SimuladorHipoteca simulador;

    MutableLiveData<Double> cuota = new MutableLiveData<>();
    MutableLiveData<Boolean> errorCapital = new MutableLiveData<>();
    MutableLiveData<Boolean> errorPlazos = new MutableLiveData<>();
    MutableLiveData<Boolean> calculando = new MutableLiveData<>();

    public MiHipotecaViewModel(@NonNull Application application) {
        super(application);

        executor = Executors.newSingleThreadExecutor();
        simulador = new SimuladorHipoteca();
    }

    public void calcular(double capital, int plazo) {

        final SolicitudHipoteca solicitud = new SolicitudHipoteca(capital, plazo);

        executor.execute(new Runnable() {
            @Override
            public void run() {

                calculando.postValue(true);

                simulador.calcular(solicitud, new SimuladorHipoteca.Callback() {
                    @Override
                    public void cuandoTengaExito(double cuotaResultante) {
                        errorCapital.postValue(false);
                        errorPlazos.postValue(false);
                        cuota.postValue(cuotaResultante);
                    }

                    @Override
                    public void cuandoHayaError(SimuladorHipoteca.Error error) {
                        if (error.CAPITAL_NEGATIVO) {
                            errorCapital.postValue(true);
                        }

                        if (error.PLAZO_NEGATIVO) {
                            errorPlazos.postValue(true);
                        }
                    }

                    @Override
                    public void cuandoFinalice() {
                        calculando.postValue(false);
                    }
                });
            }
        });
    }
}
