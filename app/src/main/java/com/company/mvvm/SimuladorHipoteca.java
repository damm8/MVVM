package com.company.mvvm;

public class SimuladorHipoteca {
    public static class Error {
        boolean CAPITAL_NEGATIVO;
        boolean PLAZO_NEGATIVO;
    }

    interface Callback {
        void cuandoTengaExito(double cuota);
        void cuandoHayaError(Error error);
        void cuandoFinalice();
    }

    public void calcular(SolicitudHipoteca solicitud, Callback callback) {
        double interes = 0;
        try {
            Thread.sleep(2500);
            interes = 0.01605;
        } catch (InterruptedException e) {}

        Error error = new Error();

        if (solicitud.capital < 0) {
            error.CAPITAL_NEGATIVO = true;
        }

        if (solicitud.plazo < 0) {
            error.PLAZO_NEGATIVO = true;
        }

        if(error.CAPITAL_NEGATIVO || error.PLAZO_NEGATIVO) {
            callback.cuandoHayaError(error);
        } else {
            callback.cuandoTengaExito(solicitud.capital*interes/12/(1-Math.pow(1+(interes/12),-solicitud.plazo*12)));
        }
        callback.cuandoFinalice();
    }

    public double calcular(SolicitudHipoteca solicitud) {
        double interes = 0;
        try {
            Thread.sleep(10000);
            interes = 0.01605;
        } catch (InterruptedException e) {}

        return solicitud.capital*interes/12/(1-Math.pow(1+(interes/12),-solicitud.plazo*12));
    }
}