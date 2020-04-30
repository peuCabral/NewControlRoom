package com.example.controlroom.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.adapter.ReservaAdapter;
import com.example.adapter.ReservasAllAdapter;
import com.example.controlroom.R;
import com.example.model.ControlSalas;
import com.example.services.VerificadorSalasReservadas;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MinhasReservas extends Fragment {

    private View view;
    private SharedPreferences preferences;
    public static final String userPreferences = "userPreferences";
    private String verificadorReserva;
    private List <String> controlSalasString = new ArrayList<>();
    private List<ControlSalas> controlSalasList = new ArrayList<>();
    private ListView listEventos;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.minhas_reservas, container, false);


        minhasReservas();
        return view;
    }

    public void minhasReservas() {
        preferences = getActivity().getSharedPreferences(userPreferences, Context.MODE_PRIVATE);
        verificadorReserva = null;

        try {
            verificadorReserva = new VerificadorSalasReservadas().execute(preferences.getString("userId", null)).get();
            JSONArray reservasJson = new JSONArray(verificadorReserva);

            if (reservasJson.length() > 0) {

                for (int i = 0; i < reservasJson.length(); i++) {
                    JSONObject jsonObjectReserva = reservasJson.getJSONObject(i);

                    if (jsonObjectReserva.has("idUsuario") && jsonObjectReserva.has("id")) {

                        int idUser = jsonObjectReserva.getInt("idUsuario");
                        int idSala = jsonObjectReserva.getInt("idSala");
                        int idReserva = jsonObjectReserva.getInt("id");

                        String descricaoReserva = jsonObjectReserva.getString("descricao");
                        String dataHoraInicio = jsonObjectReserva.getString("dataHoraInicio");
                        String dataHoraFim = jsonObjectReserva.getString("dataHoraFim");

                        ControlSalas controlSalas = new ControlSalas();

                        controlSalas.setIdSala(idSala);
                        controlSalas.setDescricaoReserva(descricaoReserva);
                        controlSalas.setIdUser(idUser);
                        controlSalas.setIdReserva(idReserva);

                        controlSalas.setNomeSala("Sala para reuniao");

                        System.out.println("data inicio");

                        //data
                        String data = dataHoraInicio.split("T")[0];
                        controlSalas.setDataReserva(data.split("-")[2] + "/" + data.split("-")[1]);

                        //hour//

                        String horarioInicioSplit = dataHoraInicio.split("T")[1];
                        String horarioInicioStr = horarioInicioSplit.split(":00Z")[0];

                        String horarioFimSplit = dataHoraFim.split("T")[1];
                        String horarioFimStr = horarioFimSplit.split(":00Z")[0];

                        controlSalas.setHorarioInicio(horarioFimStr.concat(" - " + horarioInicioStr));


                        controlSalasList.add(controlSalas);
                        controlSalasString.add(controlSalas.getDescricaoReserva());
                    }
                }


                 listEventos = view.findViewById(R.id.lista_eventos);

                ReservaAdapter adapter = new ReservaAdapter(controlSalasList, getActivity());
                listEventos.setAdapter(adapter);
            }

        }catch (Exception e) {
            e.printStackTrace();
        }


    }
}

