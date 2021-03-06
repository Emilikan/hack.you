package com.example.app_for_rightech_iot_cloud;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class History extends Fragment {

    private TimePicker mTimePicker;
    private TextView lastDay;
    private TextView now;

    private static final String BASE_URL = "https://rightech.lab.croc.ru/";

    private Animation mEnlargeAnimation;

    private Calendar calendar = Calendar.getInstance();

    private TextView textViewNowDate;
    private TextView textViewNowTime;
    private TextView textViewRNTemp;
    private TextView textViewIndicatorRN;
    private TextView textViewTemp;
    private TextView textViewDensity;
    private TextView textViewLevel;
    private TextView textViewPumpWork;
    private TextView textViewWorkTime;
    private TextView textViewNotWorkTime;
    private TextView textViewDifference;
    private TextView textViewFixTime;
    private TextView textViewFixDate;
    private TextView textViewWorkReset;
    private TextView textViewOnTimeH;
    private TextView textViewOnTimeM;
    private TextView textViewOffTimeH;
    private TextView textViewOffTimeM;

    private String thisTime;
    private String thisDate;

    private JsonArray crutchJsonArray;

    private String id;
    private String name;
    CardView cardView;
    private JsonArray allStateForTime;
    private ArrayList<Long> allTimeForTime = new ArrayList<>();

    private Context context = getContext();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_hisrory, container, false);
        context = getContext();
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        now = rootView.findViewById(R.id.now);
        lastDay = rootView.findViewById(R.id.lastDay);
        if (preferences.getString("theme", "light").equals("dark")){
            lastDay.setTextColor(Color.parseColor("#3CC1D4"));
            now.setTextColor(Color.parseColor("#3CC1D4"));
            rootView.findViewById(R.id.container).setBackgroundColor(Color.parseColor("#18191D"));
            rootView.findViewById(R.id.constraint1).setBackgroundResource(R.drawable.dark_frame);
            rootView.findViewById(R.id.constraint2).setBackgroundResource(R.drawable.dark_frame);
            rootView.findViewById(R.id.constraint3).setBackgroundResource(R.drawable.dark_frame);
            rootView.findViewById(R.id.constraint4).setBackgroundResource(R.drawable.dark_frame);
            rootView.findViewById(R.id.constraint5).setBackgroundResource(R.drawable.dark_frame);
            rootView.findViewById(R.id.constraint6).setBackgroundResource(R.drawable.dark_frame);
            rootView.findViewById(R.id.constraint7).setBackgroundResource(R.drawable.dark_frame);
            rootView.findViewById(R.id.constraint8).setBackgroundResource(R.drawable.dark_frame);
            rootView.findViewById(R.id.constraint9).setBackgroundResource(R.drawable.dark_frame);
            rootView.findViewById(R.id.constraint10).setBackgroundResource(R.drawable.dark_frame);
            rootView.findViewById(R.id.constraint13).setBackgroundResource(R.drawable.dark_frame);
            rootView.findViewById(R.id.constraint14).setBackgroundResource(R.drawable.dark_frame);
            rootView.findViewById(R.id.constraint15).setBackgroundResource(R.drawable.dark_frame);
            rootView.findViewById(R.id.constraint16).setBackgroundResource(R.drawable.dark_frame);

        }
        else{
            lastDay.setTextColor(Color.parseColor("#FF0000FF"));
            now.setTextColor(Color.parseColor("#FF0000FF"));
            rootView.findViewById(R.id.container).setBackgroundColor(Color.parseColor("#ffffff"));
            rootView.findViewById(R.id.constraint1).setBackgroundResource(R.drawable.frame);
            rootView.findViewById(R.id.constraint2).setBackgroundResource(R.drawable.frame);
            rootView.findViewById(R.id.constraint3).setBackgroundResource(R.drawable.frame);
            rootView.findViewById(R.id.constraint4).setBackgroundResource(R.drawable.frame);
            rootView.findViewById(R.id.constraint5).setBackgroundResource(R.drawable.frame);
            rootView.findViewById(R.id.constraint6).setBackgroundResource(R.drawable.frame);
            rootView.findViewById(R.id.constraint7).setBackgroundResource(R.drawable.frame);
            rootView.findViewById(R.id.constraint8).setBackgroundResource(R.drawable.frame);
            rootView.findViewById(R.id.constraint9).setBackgroundResource(R.drawable.frame);
            rootView.findViewById(R.id.constraint10).setBackgroundResource(R.drawable.frame);
            rootView.findViewById(R.id.constraint13).setBackgroundResource(R.drawable.frame);
            rootView.findViewById(R.id.constraint14).setBackgroundResource(R.drawable.frame);
            rootView.findViewById(R.id.constraint15).setBackgroundResource(R.drawable.frame);
            rootView.findViewById(R.id.constraint16).setBackgroundResource(R.drawable.frame);
        }


        mTimePicker = rootView.findViewById(R.id.timePicker);

        mTimePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
        mTimePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
        mTimePicker.setIs24HourView(true);

        lastDay.setText(setInitialDateTime());

        textViewNowDate = rootView.findViewById(R.id.text_view_date);
        textViewNowTime = rootView.findViewById(R.id.text_view_time);
        textViewRNTemp = rootView.findViewById(R.id.text_view_temperature);
        textViewIndicatorRN = rootView.findViewById(R.id.text_view_rN);
        textViewTemp = rootView.findViewById(R.id.text_view_SOZ);
        textViewDensity = rootView.findViewById(R.id.text_view_density);
        textViewLevel = rootView.findViewById(R.id.text_view_level);
        textViewPumpWork = rootView.findViewById(R.id.text_view_pump_work);
        textViewWorkTime = rootView.findViewById(R.id.text_view_work_time);
        textViewNotWorkTime = rootView.findViewById(R.id.text_view_notWork_time);
        textViewDifference = rootView.findViewById(R.id.text_view_difference);
        textViewOnTimeH = rootView.findViewById(R.id.text_view_on_timeH);
        textViewOnTimeM = rootView.findViewById(R.id.text_view_on_timeM);
        textViewOffTimeH = rootView.findViewById(R.id.text_view_off_timeH);
        textViewOffTimeM = rootView.findViewById(R.id.text_view_off_timeM);

        id = preferences.getString("id", null);
        name = preferences.getString("name", null);

        thisDate = setInitialDateTime();
        thisTime = calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + ":59";

        now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                Fragment fragment = new MainFragment();
                assert fragmentManager != null;
                fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
            }
        });

        lastDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(getView());
            }
        });

        //получение времени
        mTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                thisTime = hourOfDay + ":" + minute + ":59";
                newStateWhenNewTime(thisDate, thisTime);
            }
        });

        if(getContext()!=null) {
            newStateWhenNewDate(1, thisDate, thisTime);
        }


        return rootView;
    }
    /*@Override
    public void onPause() {
        super.onPause();
        cardView.clearAnimation();
    }
    */

    public void setDate(View v) {
        new DatePickerDialog(context, d,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            thisDate = setInitialDateTime();

            // для того, чтобы обнулить. На всякий случай
            allStateForTime = new JsonArray();
            allTimeForTime = new ArrayList<>();

            lastDay.setText(setInitialDateTime());
            newStateWhenNewDate(2, thisDate, thisTime);
        }
    };

    // для календаря
    private String setInitialDateTime() {
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        return df.format(calendar.getTime());
    }

    // устанавливаем значения
    private void setValues(JsonElement state) {
        String timeObject = getDataFromJson(state, "_ts"); // время объекта
        String tempPh = getDataFromJson(state, "temp_ph"); // температура по данным pH-метра (в градусах по цельсию)
        String tempRef = getDataFromJson(state, "temp_ref"); // температура по данным рефактометра (в градусах по цельсию)
        String level = getDataFromJson(state, "level"); // уровень СОЖ
        String emulsioncalc = getDataFromJson(state, "emulsioncalc"); // концентрация эмульсии
        String ph = getDataFromJson(state, "ph"); // показатель pH
        String active = getDataFromJson(state, "ctrl_wrd_work"); // состояние насоса
        String workTime = getDataFromJson(state, "worktime"); // время работы
        String idleTime = getDataFromJson(state, "idletime"); // время простоя
        String nTonH = getDataFromJson(state, "ntonh"); // время включения (часы)
        String nTonM = getDataFromJson(state, "ntonm"); // время включения (минуты)
        String nTofH = getDataFromJson(state, "ntofh"); // время выключения (часы)
        String nTofM = getDataFromJson(state, "ntofm"); // время выключения (минуты)
        String timeDiff = getDataFromJson(state, "timediff");
        String prevTime = getDataFromJson(state, "prevtime"); // время фиксации

        if (active.equals("true")) {
            active = "Да";
        } else if (active.equals("false")) {
            active = "Нет";
        }

        long timeObj;
        long timePr;
        long timeW;
        long timeIdle;

        SimpleDateFormat formatDate = new SimpleDateFormat("dd.MM.yyyy");
        SimpleDateFormat formatTime = new SimpleDateFormat("hh:mm:ss");

        try {
            timeObj = Long.parseLong(timeObject);
            Date dateObj = new Date(timeObj / 1000);
            textViewNowDate.setText(formatDate.format(dateObj));
            textViewNowTime.setText(formatTime.format(dateObj));

        } catch (Exception e) {
            textViewNowDate.setText("Null");
            textViewNowTime.setText("Null");
        }


        try {
            timeW = Long.parseLong(workTime);
            Date timeWork = new Date(timeW / 1000);

            textViewWorkTime.setText(Integer.parseInt(workTime)/(3600000*24)+"");
        } catch (Exception e) {
        }

        try {
            timeIdle = Long.parseLong(idleTime);
            Date timeStop = new Date(timeIdle / 1000);
            textViewNotWorkTime.setText(idleTime);
        } catch (Exception e) {
            textViewWorkTime.setText("Null");
            textViewNotWorkTime.setText("Null");
        }

        textViewRNTemp.setText(round(tempPh, 2) + " \u2103"); // температура сож
        textViewIndicatorRN.setText(Double.toString(round(ph, 2)));
        textViewTemp.setText(round(tempRef, 2) + " \u2103"); // температура сож
        textViewDensity.setText(round(emulsioncalc, 2) + " %"); // концентрация эмульсии
        textViewLevel.setText(round(level, 2) + " м"); // уровень сож в м
        textViewPumpWork.setText(active); // работает ли насос
        textViewDifference.setText(timeDiff);
        textViewOnTimeH.setText(nTonH);
        textViewOnTimeM.setText(nTonM);
        textViewOffTimeH.setText(nTofH);
        textViewOffTimeM.setText(nTofM);


    }

    // получаем значение из json
    private String getDataFromJson(JsonElement state, String id) {
        String result = "0";
        try {
            if (state.getAsJsonObject().get(id) != null) {
                result = state.getAsJsonObject().get(id).getAsString();
            }
        }catch (Exception e){
            if(getContext() != null) {
                Toast.makeText(getContext(), "Произошла ошибка. Вероятнее всего у выбранного объекта отсутствуют часть полей", Toast.LENGTH_LONG).show();
            }
        }
        return result;
    }

    // округляем до places знаков после запятой (принимает String значения)
    private static double round(String value, int places) {
        try {
            if (places < 0) throw new IllegalArgumentException();

            BigDecimal bd = new BigDecimal(value);
            bd = bd.setScale(places, RoundingMode.HALF_UP);
            return bd.doubleValue();
        } catch (Exception e){
            return 0;
        }
    }

    // возвращает true, если есть подключение к интернету
    private static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    // функция принимает дату и время и отдает милисекунды
    private long getMillisecond(String myDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(myDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long miliS = date.getTime();
        return miliS;
    }

    // записываем все милисекунды за день в массив и продолжаем
    private void getAllMs(int a, final String thisDate, String thisTime) {
        final long thisMs = getMillisecond(thisDate + " " + thisTime);
        final long startMS = getMillisecond(thisDate + " " + "00:00:00");
        long endMs = getMillisecond(thisDate +  " " + "23:59:59");

        if (getContext()!= null && !isOnline(getContext())) {
            if(a != 1) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Warning")
                        .setMessage("Нет доступа в интернет. Проверьте наличие связи")
                        .setCancelable(false)
                        .setNegativeButton("Ок, закрыть",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        } else if (getContext()!=null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiHistory apiHistory = retrofit.create(ApiHistory.class);

            apiHistory.allObject(id, startMS, endMs).enqueue(new Callback<JsonArray>() {
                @Override
                public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                    if (response.body() != null) {
                        ArrayList<Long> arrOfAllTimeInDay = new ArrayList<>();
                        for (int i = 0; i < response.body().size(); i++) {
                            String timeStr = response.body().get(i).getAsJsonObject().get("time").getAsString();
                            arrOfAllTimeInDay.add(Long.parseLong(timeStr));
                        }
                        allStateForTime = response.body();
                        allTimeForTime = arrOfAllTimeInDay;
                        getNewState(thisDate, thisMs, arrOfAllTimeInDay, response.body());
                    } else {
                        if(context!=null) {
                            Toast.makeText(context, "Нет ответа от сервера", Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<JsonArray> call, Throwable t) {
                    if(context!=null) {
                        Toast.makeText(context, "error " + t, Toast.LENGTH_SHORT).show();
                        Log.i("Request", "error " + t);
                    }
                }
            });
        }
    }

    // определяем ближайшее время и получаем объект
    private void getNewState(String date, long miliS, ArrayList<Long> allDate, JsonArray allState) {
        Collections.sort(allDate);
        JsonElement thisState = null;
        try {
            long findTime = allDate.get(0);
            for (int i = 0; i < allDate.size(); i++) {
                if (i != 0 && allDate.get(i) <= miliS) {
                    findTime = allDate.get(i);
                } else if (i != 0 && allDate.get(i) > miliS) {
                    break;
                }
            }

            for (int i = 0; i < allState.size(); i++) {
                if ((allState.get(i).getAsJsonObject().get("time").getAsString()).equals(Long.toString(findTime))) {
                    thisState = allState.get(i);
                }
            }

            if (thisState != null) {
                setValues(thisState);
            } else {
                setActualDate(date);
                //Toast.makeText(getContext(), "В этот день ничего не найдено. Выберете другой день", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            setActualDate(date);
            //Toast.makeText(getContext(), "В этот день ничего не найдено. Выберете другой день", Toast.LENGTH_LONG).show();
        }

    }

    // метод обновления информации на основе новой даты
    private void newStateWhenNewDate(int a, String thisDate, String thisTime) {
        if(a != 1) {
            if (getContext() != null && !isOnline(getContext())) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Warning")
                        .setMessage("Нет доступа в интернет. Проверьте наличие связи")
                        .setCancelable(false)
                        .setNegativeButton("Ок, закрыть",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
                                        SharedPreferences.Editor editor = preferences.edit();
                                        editor.putString("forAlertDialog", null);
                                        editor.apply();
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            } else if (getContext() != null) {
                getAllMs(2, thisDate, thisTime);
            }
        } else {
            getAllMs(1, thisDate, thisTime);
        }

    }

    // чтобы каждый раз при смене времени не обращаться к серверу
    private void newStateWhenNewTime(String thisDate, String thisTime) {
        long thisMs = getMillisecond(thisDate + " " + thisTime);
        if(allTimeForTime != null && allStateForTime != null && allStateForTime.size()>0) {
            getNewState(thisDate, thisMs, allTimeForTime, allStateForTime);
        } else {
            // если вдруг что-то пошло не так
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            if(preferences.getString("forAlertDialog", null) == null) {
                newStateWhenNewDate(2, thisDate, thisTime);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("forAlertDialog", "1");
                editor.apply();

            } else {
                newStateWhenNewDate(1, thisDate, thisTime);
            }
        }
    }

    // получаем актуальную дату и время
    private void setActualDate(final String thisDate){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiGetAllObjects apiGetAllObjects = retrofit.create(ApiGetAllObjects.class);

        apiGetAllObjects.allObjects().enqueue(new Callback<JsonArray>() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.body() != null && context != null) {
                    Log.i("Request", response.body().toString());
                    // переписать, чтобы можно было выбирать из toolbar
                    JsonElement actualElement = findElement(id, name, response.body());
                    if(actualElement != null){
                        final JsonElement state = actualElement.getAsJsonObject().get("state");
                        final long stateDateInMs = Long.parseLong(getDataFromJson(state, "_ts"))/1000;
                        long startTimeOfThisDay = getMillisecond(thisDate + " " + "00:00:00");
                        Log.i("FFF", startTimeOfThisDay + ", " + stateDateInMs);
                        // если дата объекта не совпадает с текущей, то говорим об этом пользователю (при этом говорим только 1 раз и запоминаем выбор пользователя)

                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
                        String showAlert = preferences.getString("showAlert", null);
                        if(showAlert == null) {
                            if (stateDateInMs < startTimeOfThisDay) {
                                // выводим alertDialog с предложением перейти на актуальное время объекта
                                if (getContext() != null) {
                                    AlertDialog.Builder ad;
                                    ad = new AlertDialog.Builder(getContext());
                                    ad.setTitle("Info");  // заголовок
                                    ad.setMessage("Дата объекта не совпадает с датой устройства"); // сообщение
                                    ad.setPositiveButton("Отобразить текущее состояние объекта", new DialogInterface.OnClickListener() {
                                        @RequiresApi(api = Build.VERSION_CODES.M)
                                        public void onClick(DialogInterface dialog, int arg1) {
                                            dateUpdate(stateDateInMs);
                                            setValues(state);
                                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                                            SharedPreferences.Editor editor = preferences.edit();
                                            editor.putString("showAlert", "true");
                                            editor.apply();
                                        }
                                    });
                                    ad.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int arg1) {
                                            dialog.cancel();
                                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                                            SharedPreferences.Editor editor = preferences.edit();
                                            editor.putString("showAlert", "false");
                                            editor.apply();
                                        }
                                    });
                                    ad.setCancelable(true);
                                    ad.show();
                                }
                            } else {
                                if (getContext() != null) {
                                    AlertDialog.Builder ad;
                                    ad = new AlertDialog.Builder(getContext());
                                    ad.setTitle("Info");  // заголовок
                                    ad.setMessage("В этот день ничего не найдено. Выберете другой день"); // сообщение
                                    ad.setPositiveButton("Отобразить текущее состояние объекта", new DialogInterface.OnClickListener() {
                                        @RequiresApi(api = Build.VERSION_CODES.M)
                                        public void onClick(DialogInterface dialog, int arg1) {
                                            dateUpdate(stateDateInMs);
                                            setValues(state);
                                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                                            SharedPreferences.Editor editor = preferences.edit();
                                            editor.putString("showAlert", "true");
                                            editor.apply();
                                        }
                                    });
                                    ad.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int arg1) {
                                            dialog.cancel();
                                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                                            SharedPreferences.Editor editor = preferences.edit();
                                            editor.putString("showAlert", "false");
                                            editor.apply();
                                        }
                                    });
                                    ad.setCancelable(true);
                                    ad.show();
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString("showAlert", null);
                                    editor.apply();
                                }
                            }
                        } else if(showAlert.equals("true")){
                            dateUpdate(stateDateInMs);
                            setValues(state);
                        }
                    } else {
                        Toast.makeText(context, "Неправильный ответ сервера", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(context, "Нет ответа от сервера", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Toast.makeText(context, "error " + t, Toast.LENGTH_SHORT).show();
                Log.i("Request", "error " + t);
            }
        });
    }

    // обновляем дату и время на дату и время объекта
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void dateUpdate(long actualDateInMs){
        SimpleDateFormat formatDate = new SimpleDateFormat("dd.MM.yyyy");
        SimpleDateFormat formatTime = new SimpleDateFormat("hh:mm:ss");
        SimpleDateFormat formatHour = new SimpleDateFormat("hh");
        SimpleDateFormat formatMinute = new SimpleDateFormat("mm");
        Date actualDate = new Date(actualDateInMs);

        String actualDay = formatDate.format(actualDate);
        String actualHour = formatHour.format(actualDate);
        String actualMinute = formatMinute.format(actualDate);

        thisDate = formatDate.format(actualDate);
        thisTime = formatTime.format(actualDate);

        calendar.setTime(actualDate);
        mTimePicker.setHour(Integer.parseInt(actualHour));
        mTimePicker.setMinute(Integer.parseInt(actualMinute));

        lastDay.setText(actualDay);
    }

    // функция, которая принимаем id и name выбранного пользователем объекта и по этой информации возвращает необходимый объект
    private JsonElement findElement(String id, String name, JsonArray response) {
        JsonElement nowElement = null;
        for (int i = 0; i < response.size(); i++) {
            // находим необходимый нам объект
            String newId = response.get(i).getAsJsonObject().get("_id").getAsString();
            String newName = response.get(i).getAsJsonObject().get("name").getAsString();
            if ((newId.equals(id)) && (newName.equals(name))) {
                nowElement = response.get(i);
                break;
            }
        }
        return nowElement;

    }



}
