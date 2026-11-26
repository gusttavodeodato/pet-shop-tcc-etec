package dev.deodato.tcc.petshop.util;

import dev.deodato.tcc.petshop.model.Agendamento;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class Agendamentos {

    private Agendamentos() {}

    public static List<Agendamento> semRepeticao(List<Agendamento> lista) {
        Map<Long, Agendamento> mapa = new LinkedHashMap<>();
        for(Agendamento a : lista) {
            mapa.putIfAbsent(a.getId(), a);
        }
        return new ArrayList<>(mapa.values());
    }
}
