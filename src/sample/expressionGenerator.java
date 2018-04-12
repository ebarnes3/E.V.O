//this portion of code will create a random mathematical expression that needs to be simplified
package sample;

public class expressionGenerator {
    public static String validExpression (String exp) {
        for (int i = 0; i < exp.length()-1; i++) {
            if (exp.charAt(i) == '(' && exp.charAt(i+1) == ')') {
                exp = exp.substring(0,i) + exp.substring(i+2, exp.length());
            }
        }
        for(int i = 0; i < exp.length(); i++) {
            if (exp.charAt(i) == '(') {
                char tempb = exp.charAt(i+1);
                if(tempb != '-' && tempb != '(' && !Character.isDigit(tempb)) {
                    exp = exp.substring(0,i-1) + exp.charAt(i) + exp.charAt(i-1) +exp.substring(i+1,exp.length());
                }
            }
            if (exp.charAt(i) == ')') {
                char tempa = exp.charAt(i-1);
                if(tempa != ')' && !Character.isDigit(tempa)) {
                    exp = exp.substring(0,i-1) + exp.charAt(i) + exp.charAt(i-1) +exp.substring(i+1,exp.length());
                }
            }
        }
        return exp;
    }
    public static String parExpression(String exp) {
        double numOfPar = Math.random();
        if (numOfPar < .5) {
            numOfPar = 0;
        }
        else {
            numOfPar = 2;
        }
        while(numOfPar != 0) {
            int whereOp = (int)Math.floor(Math.random()*exp.length())-5; //2 - length-3
            if(whereOp < 2) {
                whereOp = 2;
            }
            int extra = exp.length()-whereOp;
            int whereCl = (int)Math.floor(Math.random()*extra)+whereOp;
            if(whereCl == whereOp) {
                whereCl += 3;
            }
            exp = exp.substring(0,whereOp) + '(' + exp.substring(whereOp,whereCl) + ')' + exp.substring(whereCl,exp.length());
            numOfPar -=2;
        }
        return exp;
    }
    public static String raNum () {
        String op = "";
        int num;
        double rand = (Math.random() * 5); // 0 - 5
        if(rand < 1.25) {
            num = (int) Math.floor(Math.random() * 15) + 2; // 2 - 16
            op = "+" + num;
        }
        else if(rand < 2.5) {
            num = (int) Math.floor(Math.random() * 15) + 2; // 2 - 16
            op = "-" + num;
        }
        else if(rand < 3.75) {
            num = (int) Math.floor(Math.random() * 6) + 2; // 2 - 7
            op = "*" + num;
        }
        else if(rand < 4.5) {
            num = (int) Math.floor(Math.random() * 4) + 2; // 2 - 5
            op = "/" + num;
        }
        else if (rand < 4.75) {
            num = (int) Math.floor(Math.random() * 2) + 2; // 2 - 3
            op = "^" + num;
        }
        else {
            num = (int) Math.floor(Math.random() * 2) + 2; // 2 - 3
            op = "r" + num;
        }
        return op;
    }
}