package sample;

//this portion of code ensures that the string the user inputs is a valid expression. This checker does not allow any variables.
import java.util.*;
public class InputVarChecker {
    static String var = "";
    public static void main (String args[]) {
        var = "";
        String userInput = "";
        //ask user for input until the input is not an empty string
        do {
            Scanner scan = new Scanner(System.in);
            userInput = scan.nextLine();
            userInput = userInput.replaceAll("\\s", ""); //Remove all whitespaces from entered string
        }while (userInput.isEmpty());
        //select first letter as the variable, if more than one variable is identified then abort.
        for(int i=0; i<userInput.length(); i++) {
            if(Character.isLetter(userInput.charAt(i))){
                if(var.isEmpty()){
                    var = var + userInput.charAt(i);
                } else if (!var.equals(Character.toString(userInput.charAt(i)))) {
                    System.out.println("Too many different variables identified.");
                    System.exit(0);
                }
            }
        }
        if (var.isEmpty()) {
            System.out.println("No variable detected, try again.");
            System.exit(0);
        }
        if(userInput.charAt(0) == '.') {
            userInput = '0' + userInput;
        }
        for(int i=0; i<userInput.length(); i++) {
            String temp = "";
            if(userInput.charAt(i) == '.') {
                int j = i;
                int dotCount = 0;
                while((userInput.charAt(j) == '.' || Character.isDigit(userInput.charAt(j)) || Character.isLetter(userInput.charAt(j))) && j != userInput.length()-1) {
                    temp = temp + userInput.charAt(j);
                    j++;
                }
                for(int k=0; k<temp.length();k++) {
                    if(temp.charAt(k) == '.') {
                        dotCount++;
                    }
                    if(dotCount>1){
                        System.out.println("Incorrect decimals detected");
                        System.exit(0);
                    }
                }
            }
        }
        parMatch(userInput);
        userInput = parenVarOp(userInput);
        validSimpChar(userInput);
        System.out.println(userInput);
        System.out.println(var);
    }
    //this function ensures that the parenthesis are correct
    public static void parMatch(String entry) {
        int parens = 0;
        for (int i =0; i<entry.length();i++) {
            if(entry.charAt(i) =='(') {
                parens++;
            }
            if(entry.charAt(i) ==')') {
                parens--;
            }
            if(parens < 0) {
                System.out.println("Parentheses do not match.");
                System.exit(0);
            }
        }
        if(parens != 0) {
            System.out.println("Parentheses do not match.");
            System.exit(0);
        }
    }
    //if the user does not have a * when they are entering a string with parentheses/variables, parenthesisOp will add it.
    public static String parenVarOp (String entry) {
        for(int i=1; i<entry.length()-1; i++) {
            if(entry.charAt(i) == '.') {
                if (!Character.isDigit(entry.charAt(i-1))){
                    entry = entry.substring(0,i) + '0' + entry.substring(i,entry.length());
                }
            }
        }
        for(int i=1; i<entry.length()-1; i++) {
            char temp = entry.charAt(i);
            if(temp == '('){
                if (Character.isDigit(entry.charAt(i-1)) || entry.charAt(i-1) == ')' || Character.isLetter(entry.charAt(i-1))) {
                    entry = entry.substring(0,i) + '*' + entry.substring(i,entry.length());
                }
            }
            if(temp == ')'){
                if (Character.isDigit(entry.charAt(i+1)) || entry.charAt(i+1) == '(' || Character.isLetter(entry.charAt(i-1))) {
                    entry = entry.substring(0,i+1) + '*' + entry.substring(i+1,entry.length());
                }
            }
            if(temp == var.charAt(0)){
                if (Character.isDigit(entry.charAt(i-1)) || Character.isLetter(entry.charAt(i-1))){
                    entry = entry.substring(0,i) + '*' + entry.substring(i,entry.length());
                }
                else if (Character.isDigit(entry.charAt(i+1)) || Character.isLetter(entry.charAt(i-1))) {
                    entry = entry.substring(0,i+1) + '*' + entry.substring(i+1,entry.length());
                }
            }
        }
        if(entry.length() > 1) {
            if(entry.charAt(0) == var.charAt(0) && (Character.isDigit(entry.charAt(1)) || Character.isLetter(entry.charAt(1)))) {
                entry = entry.substring(0,1) + '*' + entry.substring(1,entry.length());
            }
            if(entry.charAt(entry.length()-1) == var.charAt(0) && (Character.isDigit(entry.charAt(entry.length()-2)) || Character.isLetter(entry.charAt(entry.length()-2)))) {
                entry = entry.substring(0,entry.length()-1) + '*' + entry.substring(entry.length()-1,entry.length());
            }
        }
        return entry;
    }
    //this function checks to make sure that if an operation or parenthesis is used that the character on both sides are valid
    public static void validSimpChar (String entry) {
        if(!Character.isDigit(entry.charAt(0)) && !Character.isLetter(entry.charAt(0)) && entry.charAt(0) != '(' && entry.charAt(0) != '-' ||
                (!Character.isLetter(entry.charAt(entry.length()-1)) && !Character.isDigit(entry.charAt(entry.length()-1))) && entry.charAt(entry.length()-1) != ')') {
            System.out.println("First/Last character is invalid.");
            System.exit(0);
        }
        //this checks the char on both sides of the operator for validity
        for (int i=1; i<entry.length()-1; i++) {
            char temp = entry.charAt(i);
            char tempa = entry.charAt(i-1);
            char tempb = entry.charAt(i+1);
            if (!Character.isDigit(temp)) {
                switch(temp) {
                    case '(':
                        if(!isOp(tempa) && tempa != '(' && !Character.isLetter(tempa)) {
                            System.out.println("Invalid entry.");
                            System.exit(0);
                        }
                        break;
                    case ')':
                        if(!isOp(tempb) && tempb != ')' && !Character.isLetter(tempb)) {
                            System.out.println("Invalid entry.");
                            System.exit(0);
                        }
                        break;
                    case '*': case '/': case '^':
                        if (!(Character.isDigit(tempa) || Character.isLetter(tempa) || tempa == ')')||(!(Character.isDigit(tempb) || Character.isLetter(tempb) || tempb == '('
                                || tempb == '-'))) {
                            System.out.println("Operations are not correct!");
                            System.exit(0);
                        }
                        break;
                    case '+': case '-': //check here and below
                        if(!Character.isDigit(tempb) && !Character.isLetter(tempb) && (tempb == '*' || tempb == '/' || tempb == '^')) {
                            System.out.println("Operations are not correct! 2");
                            System.exit(0);
                        }
                        break;
                    case '.':
                        if(!Character.isDigit(tempa) || !Character.isDigit(tempb)) {
                            System.out.println("'.' has been misplaced!");
                            System.exit(0);
                        }
                        break;
                    default:
                        if(temp != var.charAt(0)) {
                            System.out.println("Invalid character entered!");
                            System.exit(0);
                        }
                        break;
                }
            }
        }
    }
    //return true if the character inputed is an operation
    public static boolean isOp (char entry) {
        if (entry == '+' || entry == '-' || entry == '/' || entry == '*' || entry == '^') {
            return true;
        }
        return false;
    }
}