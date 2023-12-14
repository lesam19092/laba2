package org.example;

public class Checks {

    public static class MyException extends Exception {
        public MyException() {
            super("Вы ввели некорректные данные ");
        }
    }

    public static void checkName(String name) throws MyException, NullPointerException {
        final String s = "ЯЮЭЬЫЪЩШЧЦХФУТСРПОНМЛКЙИЗЖЁЕДГВБАяюэьыъщшчцхфутсрпонмлкйизжёедгвба ";
        if (name != null && !name.isEmpty()) {

            for (int i = 0; i < name.length(); i++) {
                if (s.indexOf((name.charAt(i))) < 0) {
                    throw new MyException();
                }
            }
        } else throw new NullPointerException();

    }

    public static void checkId(Integer id) throws MyException {
        if (id < 0) throw new MyException();
    }

    public static void checkDuration(Integer duration ) throws MyException{
        if (duration<0) throw new MyException();
    }

    public static void checkAge(Integer age) throws  MyException {
        if (age < 0 || age > 100) throw new MyException();
    }

    public static void checkRoom(Integer room) throws MyException {
        if (room < 0 || room>=21) throw new MyException();
    }


}
