package com.company;

import java.util.HashMap;

//Класс для хранения экземпляров записей
public class Record {

    String date; // дату не обрабатываем, как значение, поэтому оставляем String - разницы никакой
    HashMap<String, Integer> map; //мапа для хранения уникальных пар - "Причина/Кол-во повторов"


    // Конструктор для новой записи
    public Record (String date,HashMap <String,Integer> map)
    {
        this.date = date;
        this.map =  new HashMap<String, Integer>(map);
    }
}
