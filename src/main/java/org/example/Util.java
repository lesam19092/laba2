package org.example;

public class Util {


    public static boolean isIdPositive(int id) {
        return id >= 0;
    }

    public static boolean checkName(String name) {
        final String s = "ЯЮЭЬЫЪЩШЧЦХФУТСРПОНМЛКЙИЗЖЁЕДГВБАяюэьыъщшчцхфутсрпонмлкйизжёедгвба ";
        if (name != null && !name.isEmpty()) {

            for (int i = 0; i < name.length(); i++) {
                if (s.indexOf((name.charAt(i))) < 0) {
                    return false;
                }
            }
        } else return false;

        return true;
    }


    public static boolean checkAge(int age)  {
        return  age >= 0 || age < 100;
    }

    public static boolean checkRoom(int room)  {
        return room>=0;
    }

    public static boolean checkType(String type)  {
        if (type != null && !type.isEmpty()) {
            if (type.equals("люкс") || type.equals("обычный")) {
                return true;
            } else {
                return false;
            }
        } return false;
    }

}
