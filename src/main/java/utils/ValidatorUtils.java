package utils;

import exceptions.NonNumericInputException;

import java.time.LocalDate;

public class ValidatorUtils {

    private ValidatorUtils(){
        throw new UnsupportedOperationException("Utility klasa");
    }

    public static boolean isValidOib(String oib){
        if(oib == null || oib.length() != 11){
            return false;
        }

        for(int i = 0; i < oib.length(); i++){
            char znak = oib.charAt(i);

            if(!Character.isDigit(znak)){
                return false;
            }
        }
        return true;
    }

    public static boolean isValidEmail(String email){
        return (email != null && !email.isEmpty()) && email.contains("@");
    }

    public static boolean isNumberphoneValid(String phoneNumber){

        if((phoneNumber == null ||phoneNumber.isEmpty()) || phoneNumber.contains(" "))
        {
            return false;
        }

        int duljina = phoneNumber.length();
        if(duljina < 9 || duljina > 11){
            return false;
        }

        for(int i = 0; i < phoneNumber.length(); i++){
            char znak = phoneNumber.charAt(i);
            if(!Character.isDigit(znak)){
                return false;
            }
        }
        return true;
    }

    public static boolean isNameValid(String name, String surname){
        return isNamePartValid(name) && isNamePartValid(surname);
    }

    private static boolean isNamePartValid(String value){
        if(value.isEmpty() || !Character.isAlphabetic(value.charAt(0))){
            return false;
        }
        for(int i = 0; i < value.length(); i++){
            char znak = value.charAt(i);
            if(!Character.isAlphabetic(znak) && znak != ' ' && znak != '-' && znak != '\''){
                return false;
            }
        }
        return true;
    }

    public static int parseNumber(String text) throws NonNumericInputException {
        try {
            return Integer.parseInt(text.trim());
        } catch (NumberFormatException _) {
            throw new NonNumericInputException("'" + text + "' nije cijeli broj.");
        }
    }

    public static boolean isBirthdayValid(LocalDate birthday){

        return !birthday.isAfter(LocalDate.now()) && birthday.getYear() >= 1930;

    }
}
