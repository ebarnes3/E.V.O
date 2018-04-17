package sample;

//this portion of code ensures that the string the user inputs is a valid expression. This checker does not allow any variables.
public class NewInputChecker {

    public static String validate(String userInput) {
        if (userInput.isEmpty()) return null;
        userInput = userInput.replaceAll("\\s+", "");
        for (int i = 0; i < userInput.length(); i++) {
            if (Character.isLetter(userInput.charAt(i)) && userInput.charAt(i) != 'r') {
                System.out.println("No variables allowed.");
                return null;
            }
        }
        if (!parMatch(userInput)) return null;
        userInput = dotCheck(userInput);
        if (userInput == null) return null;
        userInput = parenthesisOp(userInput);
        if (userInput == null) return null;
        if (!validSimpChar(userInput)) return null;
        return userInput;
    }
    //this function ensures that the parenthesis are correct

    public static boolean parMatch(String entry) {
        int parens = 0;
        for (int i = 0; i < entry.length(); i++) {
            if (entry.charAt(i) == '(') {
                parens++;
            }
            if (entry.charAt(i) == ')') {
                parens--;
            }
            if (parens < 0) {
                System.out.println("Parentheses do not match.");
                return false;
            }
        }
        if (parens != 0) {
            System.out.println("Parentheses do not match.");
            return false;
        }
        return true;
    }
    //verfies decimals and adds 0 to decimals. Ex. .23 = 0.23

    public static String dotCheck(String entry) {
        if (entry.charAt(0) == '.') {
            entry = "0" + entry;
        }
        if (entry.charAt(entry.length() - 1) == '.') {
            System.out.println("Last term cannot end in '.'");
            return null;
        }
        for (int i = 1; i < entry.length(); i++) {
            if (entry.charAt(i) == '.') {
                if (!Character.isDigit(entry.charAt(i - 1))) {
                    entry = entry.substring(0, i) + '0' + entry.substring(i, entry.length());
                }
            }
        }
        for (int i = 0; i < entry.length(); i++) {
            String temp = "";
            if (entry.charAt(i) == '.') {
                int j = i;
                int dotCount = 0;
                while ((entry.charAt(j) == '.' || Character.isDigit(entry.charAt(j))) && j != entry.length() - 1) {
                    temp = temp + entry.charAt(j);
                    j++;
                }
                for (int k = 0; k < temp.length(); k++) {
                    if (temp.charAt(k) == '.') {
                        dotCount++;
                    }
                    if (dotCount > 1) {
                        System.out.println("Incorrect decimals detected");
                        return null;
                    }
                }
            }
        }
        return entry;
    }
    //if the user does not have a * when they are entering a string with parentheses, parenthesisOp will add it.

    public static String parenthesisOp(String entry) {
        for (int i = 1; i < entry.length() - 1; i++) {
            char temp = entry.charAt(i);
            if (temp == '(') {
                if (Character.isDigit(entry.charAt(i - 1)) || entry.charAt(i - 1) == ')') {
                    entry = entry.substring(0, i) + '*' + entry.substring(i, entry.length());
                }
            }
            if (temp == ')') {
                if (Character.isDigit(entry.charAt(i + 1)) || entry.charAt(i + 1) == '(') {
                    entry = entry.substring(0, i + 1) + '*' + entry.substring(i + 1, entry.length());
                }
            }
        }
        return entry;
    }
    //this function checks to make sure that if an operation or parenthesis is used that the character on both sides are valid

    public static boolean validSimpChar(String entry) {
        if ((isOp(entry.charAt(0)) && entry.charAt(0) != '-')) {
            System.out.println("First character is invalid.");
            return false;
        }
        if (isOp(entry.charAt(entry.length() - 1))) {
            System.out.println("Last character is invalid.");
            return false;
        }
        for (int i = 1; i < entry.length() - 1; i++) {
            char temp = entry.charAt(i);
            char tempa = entry.charAt(i - 1);
            char tempb = entry.charAt(i + 1);
            if (!Character.isDigit(temp)) {
                switch (temp) {
                    case '(':
                        if (!isOp(tempa) && tempa != '(' || tempb == 'r') {
                            System.out.println("Invalid parentheses.");
                            return false;
                        }
                        break;
                    case ')':
                        if (!isOp(tempb) && tempb != ')' || tempa == 'r') {
                            System.out.println("Invalid parentheses.");
                            return false;
                        }
                        break;
                    //this checks the char on both sides of the operator for validity
                    case '*':
                    case '/':
                    case '^':
                        if (!(Character.isDigit(tempa) || tempa == ')') || (!(Character.isDigit(tempb) || tempb == '(' || tempb == '-'))) {
                            System.out.println("Operations are not correct!");
                            return false;
                        }
                        break;
                    case '+':
                    case '-':
                        if (!Character.isDigit(tempb) && (tempb == '*' || tempb == '/' || tempb == '^' || tempb == 'r')) {
                            System.out.println("Operations are not correct!");
                            return false;
                        }
                        break;
                    case 'r':
                        if (tempa == '.') {
                            System.out.println("Operations are not correct!");
                            return false;
                        }
                        break;
                    default:
                        if (temp != '.') {
                            System.out.println("Invalid character entered!");
                            return false;
                        }
                }
            }
        }
        return true;
    }
    //return true if the character inputed is an operation

    public static boolean isOp(char entry) {
        if (entry == '+' || entry == '-' || entry == '/' || entry == '*' || entry == '^' || entry == 'r') {
            return true;
        }
        return false;
    }
}