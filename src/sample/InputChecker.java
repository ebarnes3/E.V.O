package sample;
//this portion of code ensures that the string the user inputs is a valid expression. This checker does not allow any variables.
import jdk.internal.util.xml.impl.Input;

import java.util.HashSet;
import java.util.Scanner;
public class InputChecker {
    HashSet<String> returnErrors = new HashSet<String>();
    Boolean valid = true;

    //    public static void main (String args[]) {
//        String userInput;
//        //ask user for input until the input is not an empty string
//        do {
//            Scanner scan = new Scanner(System.in);
//            userInput = scan.nextLine();
//            userInput = userInput.replaceAll("\\s", ""); //Remove all whitespaces from entered string
//        }while (userInput.isEmpty());
//        parMatch(userInput);
//        userInput= dotCheck(userInput);
//        userInput = parenthesisOp(userInput);
//        validSimpChar(userInput);
//        System.out.println(userInput);
//    }
    public InputChecker(){

    }
    // this function ensures that no variables are allowed
    public void noVar(String entry) {
        for(int i=0; i<entry.length(); i++) {
            if(Character.isLetter(entry.charAt(i)) && entry.charAt(i) != 'r'){
                returnErrors.add("No variables allowed.");
                valid = false;
            }
        }
    }

    //this function ensures that the parenthesis are correct
    public  void parMatch(String entry) {
        int parens = 0;
        for (int i =0; i<entry.length();i++) {
            if(entry.charAt(i) =='(') {
                parens++;
            }
            if(entry.charAt(i) ==')') {
                parens--;
            }
            if(parens < 0) {
                returnErrors.add("Parentheses do not match.");
                //System.out.println("Parentheses do not match.");
                valid = false;
            }
        }
        if(parens != 0) {
            returnErrors.add("Parentheses do not match.");
            //System.out.println("Parentheses do not match.");
            valid = false;
        }
    }
    //verfies decimals and adds 0 to decimals. Ex. .23 = 0.23
    public String dotCheck (String entry) {
        if(entry.charAt(0) == '.') {
            entry = "0" + entry;
        }
        if(entry.charAt(entry.length()-1) == '.') {
            returnErrors.add("Last term cannot end in '.'");
            //System.out.println("Last term cannot end in '.'");
            valid = false;
            //System.exit(0);
        }
        for(int i=1; i<entry.length(); i++) {
            if(entry.charAt(i) == '.') {
                if(!Character.isDigit(entry.charAt(i-1))) {
                    entry = entry.substring(0,i) + '0' + entry.substring(i,entry.length());
                }
            }
        }
        for(int i=0; i<entry.length(); i++) {
            String temp = "";
            if(entry.charAt(i) == '.') {
                int j = i;
                int dotCount = 0;
                while((entry.charAt(j) == '.' || Character.isDigit(entry.charAt(j))) && j != entry.length()-1) {
                    temp = temp + entry.charAt(j);
                    j++;
                }
                for(int k=0; k<temp.length();k++) {
                    if(temp.charAt(k) == '.') {
                        dotCount++;
                    }
                    if(dotCount>1){
                        returnErrors.add("Incorrect decimals detected");
                        //System.out.println("Incorrect decimals detected");
                        // System.exit(0);
                        valid = false;
                    }
                }
            }
        }
        return entry;
    }
    //if the user does not have a * when they are entering a string with parentheses, parenthesisOp will add it.
    public String parenthesisOp (String entry) {
        for(int i=1; i<entry.length()-1; i++) {
            char temp = entry.charAt(i);
            if(temp == '('){
                if (Character.isDigit(entry.charAt(i-1)) || entry.charAt(i-1) == ')') {
                    entry = entry.substring(0,i) + '*' + entry.substring(i,entry.length());
                }
            }
            if(temp == ')'){
                if (Character.isDigit(entry.charAt(i+1)) || entry.charAt(i+1) == '(') {
                    entry = entry.substring(0,i+1) + '*' + entry.substring(i+1,entry.length());
                }
            }
        }
        return entry;
    }
    //this function checks to make sure that if an operation or parenthesis is used that the character on both sides are valid
    public void validSimpChar (String entry) {
        if((isOp(entry.charAt(0)) && entry.charAt(0) != '-')) {
            returnErrors.add("First character is invalid.");
            valid = false;
        }
        if(isOp(entry.charAt(entry.length()-1))) {
            returnErrors.add("Last character is invalid.");
            valid = false;
        }
        for (int i=1; i<entry.length()-1; i++) {
            char temp = entry.charAt(i);
            char tempa = entry.charAt(i-1);
            char tempb = entry.charAt(i+1);
            if (!Character.isDigit(temp)) {
                switch(temp) {
                    case '(':
                        if(!isOp(tempa) && tempa != '(' || tempb == 'r') {
                            returnErrors.add("Illegal operator beside parenthesis.");
                            //System.out.println("Invalid entry.");
                            //System.exit(0);
                            valid = false;
                        }
                        break;
                    case ')':
                        if(!isOp(tempb) && tempb != ')' || tempa == 'r') {
                            returnErrors.add("Illegal operator beside parenthesis.");
                            //System.out.println("Invalid entry.");
                            //System.exit(0);
                            valid = false;
                        }
                        break;
                    //this checks the char on both sides of the operator for validity
                    case '*': case '/': case '^':
                        if (!(Character.isDigit(tempa) || tempa == ')') || (!(Character.isDigit(tempb) || tempb == '(' || tempb == '-'))) {
                            returnErrors.add("Operations are not correct! a");
                            //System.out.println("Operations are not correct!");
                            //System.exit(0);
                            valid = false;
                        }
                        break;
                    case '+': case '-':
                        if(!Character.isDigit(tempb) && (tempb == '*' || tempb == '/' || tempb == '^' || tempb == 'r')) {
                            returnErrors.add("Operations are not correct!");
                            //System.out.println("Operations are not correct! b");
                            //System.exit(0);
                            valid = false;
                        }
                        break;
                    case 'r':
                        if(tempa == '.') {
                            returnErrors.add("Operations are not correct!");
                            valid = false;
                        }
                        break;
                    default:
                        if(temp != '.') {
                            returnErrors.add("Invalid character entered!");
                            //System.out.println("Invalid character entered!");
                            //System.exit(0);
                            valid = false;
                        }
                }
            }
        }
    }
    //return true if the character inputed is an operation
    public static boolean isOp (char entry) {
        if (entry == '+' || entry == '-' || entry == '/' || entry == '*' || entry == '^' || entry == 'r') {
            return true;
        }
        return false;
    }
}