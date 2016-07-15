package com.company;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) throws IOException {

        ArrayList<Record> list = new ArrayList<>(); // массив для всех записей
        HashMap<String, Integer> map = new HashMap<>(); // временная мапа для одной записи
        FileInputStream file = new FileInputStream("L:/items_full.log");
        Scanner scanner = new Scanner(file);
        Integer change;
        String currentDate = "";//вначале нет даты, чтобы от чего-то отталкиваться в условии
        String id = "";
        String type = "";
        String date = "";
        // регулярки для нужных выборок (Дата, ID - он два раза, берем первый, Кол-во замен, причина замены)
        Pattern patternDate = Pattern.compile("(0[1-9]|[12][0-9]|3[01])[- /.](0[1-9]|1[012])[- /.](19|20)\\d\\d");
        Pattern patternId = Pattern.compile("itemID=(\\d+)");
        Pattern patternChange = Pattern.compile("change=\\+(\\d+)");
        Pattern patternType = Pattern.compile("context=\\{type:(\\w+)");

        while (scanner.hasNext()) { // читаем очередную строку, пока есть что читать
            change = 0;
            String log = scanner.nextLine();
            // и "разбирает" ее...
            Matcher matcherDate = patternDate.matcher(log);
            while (matcherDate.find()) currentDate = matcherDate.group(0);
            Matcher matcherId = patternId.matcher(log);
            while (matcherId.find()) id = matcherId.group(1);
            Matcher matcherChange = patternChange.matcher(log);
            while (matcherChange.find()) change = Integer.parseInt(matcherChange.group(1));
            Matcher matcherType = patternType.matcher(log);
            while (matcherType.find()) type = matcherType.group(1);

            // с полученной строкой надо или добавить в мапу, если 1 запись, или с повторной датой
            if (date.equals(currentDate) || date.equals("")) {
                if (id.equals("1101") && change > 0) {
                    if (map.containsKey(type))
                        map.put(type, map.get(type) + change);
                    else map.put(type, change);
                }
                date = currentDate;
            } else {
                // или бросить предыдущий набор в лист и начать работу со следующей датой
                list.add(new Record(date, map));
                map.clear();
                if (id.equals("1101") && change > 0) {
                    if (map.containsKey(type))
                        map.put(type, map.get(type) + change);
                    else map.put(type, change);
                }
                date = currentDate;
            }
        }
        // выгрузка последнего набора, сравнивать не с чем - поэтому принудительно
        list.add(new Record(date, map));
        scanner.close();
        file.close();

        // вывод содержимого массива записей о использовании.
        for (int i = 0; i < list.size(); i++) {
            System.out.println("[" + list.get(i).date + "]");
            for (HashMap.Entry<String, Integer> item : list.get(i).map.entrySet()) {
                System.out.println(item.getKey() + ": " + item.getValue());
            }
            System.out.println();
        }
    }
}