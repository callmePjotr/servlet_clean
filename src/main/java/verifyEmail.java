import java.util.regex.Pattern;


public class verifyEmail {

    public static boolean patternMatches(String emailAddress, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(emailAddress)
                .matches();
    }



    public static boolean testUsingRFC5322Regex(String validate_this_mail) {
        //String emailAddress = "username@domain.com";
        String regexPattern = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

        if(verifyEmail.patternMatches(validate_this_mail, regexPattern)){
            return true;
        }else{
            return false;
        }


    }

}

