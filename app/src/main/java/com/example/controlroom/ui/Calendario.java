package com.example.controlroom.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.DatePicker;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.builders.DatePickerBuilder;
import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.example.adapter.ReservaAdapter;
import com.example.adapter.ReservasAllAdapter;
import com.example.controlroom.R;
import com.example.model.ControlSalas;
import com.example.services.VerificadorReservasAll;
import com.example.services.VerificadorSalasReservadas;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Calendario extends Fragment {
    private View view;
    private CalendarView calendarView;
    private ListView listEventos;
    private List<EventDay> mEventDays = new ArrayList<>();
    private SharedPreferences preferences;
    public static final String userPreferences = "userPreferences";
    private String verificadorReserva;
    private List <String> controlSalasString = new ArrayList<>();
    private List<ControlSalas> controlSalasList = new ArrayList<>();

    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_calendario, container, false);


         calendarView = (CalendarView) view.findViewById(R.id.calendarView);
        Calendar calendar = Calendar.getInstance();
        reservasAll();


//        try {
//            calendarView.setDate(calendar);
//        } catch (OutOfDateRangeException e) {
//            e.printStackTrace();
//        }

//        final List<Calendar> calendars = new ArrayList<>();
//        calendarView.setSelectedDates(calendars);


//
//        calendarView.setOnDayClickListener(new OnDayClickListener() {
//            @Override
//            public void onDayClick(EventDay eventDay) {
//                Calendar clickedDayCalendar = eventDay.getCalendar();
//                Toast.makeText(getContext(),eventDay.toString(), Toast.LENGTH_LONG).show();
//
//
//            }
//        });
        return view;

    }

    private void reservasAll() {
        preferences = getActivity().getSharedPreferences(userPreferences, Context.MODE_PRIVATE);
        verificadorReserva = null;

        try {
            verificadorReserva = new VerificadorReservasAll().execute(preferences.getString("userId", null)).get();
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


                listEventos = (ListView) view.findViewById(R.id.eventos_listview);
                ReservasAllAdapter adapter = new ReservasAllAdapter(controlSalasList, getActivity());

                listEventos.setAdapter(adapter);
            }

        }catch (Exception e) {
            e.printStackTrace();
        }

    }

}
