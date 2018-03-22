/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evo.mathApp.model;

import java.util.*;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//class
public class InToPost {
    //stack for opperands
    private Stack theStack;
    //stack for arithmetick
    private Stack numberStack;
    //input where the user will enter the input
    private static String input;
    //output
    private String output = "";
    //variables to help seperate numbers
    private static int count = 0, numCount = 0, calculateIndex;
    //to show the steps that change over time
    private static String step;
    //to store the steps
    private static ArrayList al = new ArrayList();
    //Is set to true if first number if negative
    private static boolean isNumberNegative = false, onParenthesis = false, parenNegative = false, lastOperationDone = false;
    //To hold the signs of the operands
    private static Queue<String> sign = new LinkedList<String>();
    private static Queue<String> parenQ = new LinkedList<String>();

    //constructor class
    public InToPost(String in) {
        input = in;
        int stackSize = input.length();
        theStack = new Stack(stackSize);
        numberStack = new Stack(stackSize);
    }

    //method uses a queue to store the sign of the values example: stores '-' for -9
    public void signs(){

        char ch = input.charAt(0), c = input.charAt(1), cbefore;
        boolean onNumber;

        if(ch != '-' && ch != '('){
            sign.add("+");
        }

        if(ch == '('){
            parenQ.add("+");
        }

        for(int i = 1; i < input.length(); i++){

            ch = input.charAt(i);
            c = input.charAt(i-1);

            //sets onNumber true if ch is a number
            if(ch == '0' || ch == '1' || ch == '2' || ch == '3' || ch == '4' || ch == '5' || ch == '6' || ch == '7' || ch == '8' || ch == '9'){
                onNumber = true;
            }else{onNumber = false;}

            if(ch == '(' && c == '-'){
                parenQ.add("-");
            }else{//add to parenthesis queue "+"
                if(onNumber == false &&  ch == '('){parenQ.add("+");}
            }

            if(onNumber){

                switch(c){
                    case '-':
                        sign.add("-");
                        break;
                    case '(':
                    case '+':
                    case '*':
                    case '/':
                    case '^':
                    case 'r':
                        sign.add("+");
                        break;
                }
            }

        }

        System.out.println(parenQ);
    }

   /*
   This method will remove all negative symbols between operators that for
   example: 9*-9 will be 9*9 or 9/-9 = 9/9. This is done for any operater that
   has higher priority than '+' or '-' their not needed becuase the signs queue
   stores the sign of the numbers already.

   The reason why the extra negatives must be moved is becuase when the
   expression is translated from infix to postfix it will cuase problems later in
   the calculate method which becuase the signs odrdering will try to access the stack
   where no elements are cuasing a out of bounds Exceptions.
   */

    //This method is handles if there are two operators next to one another. For example: 4 3 - / (postfix) = 4/-3 (infix)
    public String handleNegativeNumber(){
        String firstHalf, secondHalf;

        char ch, c = 'f';
        ch = input.charAt(0);

        if(ch == '-'){
            input = input.substring(1,input.length());
        }


        for(int i = 0; i < input.length(); i++){
            ch = input.charAt(i);

            if(i < input.length()-1){c = input.charAt(i+1);}


            switch(ch){
                case '(':
                case '*':
                case '/':
                case '^':
                case 'r':

                    if(c == '-'){
                        firstHalf = input.substring(0,i+1);
                        secondHalf = input.substring(i+2, input.length());
                        input = firstHalf+secondHalf;
                    }
                    break;

            }
        }
        return input;
    }

    //This method initiates
    public String doTrans(String input) {
        //The ~ character is appended to output in order to help separate the numbers that are more than one digit.
        for (int j = 0; j < input.length(); j++) {
            char ch = input.charAt(j);
            switch (ch) {
                //if the operator is a plus or minus then call with precidence 1 gotOper()
                case '+':
                case '-':
                    output += "~";
                    gotOper(ch+"", 1);
                    break;
                //if the operator is a asterick or foward slash then call with precidence 2 gotOper()
                case '*':
                case '/':
                    output += "~";
                    gotOper(""+ch, 2);
                    break;
                case '^':
                case 'r':
                    output += "~";
                    gotOper(""+ch, 3);
                    break;
                //if there is open paranthesis push directly on the stack.
                case '(':
                    theStack.push(""+ch);
                    break;
                //closed paranthesis call gotParen method
                case ')':
                    output += "~";
                    gotParen(ch);
                    break;
                default:
                    output += ch;
                    break;
            }
        }
        //start here looking for logical error
        while (!theStack.isEmpty()) {
            output = output + theStack.pop();
        }
        System.out.println(output);
        return output;
    }

    public void gotOper(String opThis, int prec1) {
        //loop while stack is not empty
        while (!theStack.isEmpty()) {
            String opTop = theStack.pop();
            //if open parenthesis push onto stack then exit the loop
            if (opTop.equals("(")) {
                theStack.push(opTop);
                break;
            } else {
                //precedence vvariable is here inorder to compare operator precedence
                int prec2;
                if (opTop.equals("+") || opTop.equals("-"))
                    prec2 = 1;
                else if(opTop.equals("*") || opTop.equals("/"))
                    prec2 = 2;
                else
                    prec2 = 3;
                if (prec2 < prec1) {
                    //If the next opperator precedence is greater than the prior operator precedence
                    theStack.push(opTop);
                    break;
                }
                //If the next opperator precedence is less than the prior operator precedence
                else{

                    output = output + opTop;

                }
            }
        }
        //place the operator on the stack after exiting the while loop
        theStack.push(opThis);
    }

    //to handle closing parenthesis
    public void gotParen(char ch) {

        //pops all operators off the stack  and append it to output string until openning parenthesis is reached
        while (!theStack.isEmpty()) {
            //append all operator onto output operator until opening parenthesis is reached
            String chx = theStack.pop();
            //break loop if open parenthesis is reached
            if (chx.equals("("))
                break;
            else{
                //System.out.println(output+" append "+chx);
                output = output + chx;
            }
        }
    }
    //edit final input inorder for the calculate algorithm  to work
    public String editExpression(String expression){
        int i = expression.length()-1;
        char numNow;
        String firstHalf, lastHalf, result;
        while(i > 0){
            numNow = expression.charAt(i);
            //if any number  ~ behind last number
            if(numNow == '0' || numNow == '1' || numNow == '2' || numNow == '3' || numNow == '4' || numNow == '5' || numNow == '6' || numNow == '7' || numNow == '8' || numNow == '9'){
                //cut from the begginning of the string to the last number
                firstHalf = expression.substring(0,i+1);
                //from last number to the end of the string
                lastHalf = expression.substring(i+1);
                //add in the ~ behind the last number
                result = firstHalf+'~'+lastHalf;

                i = 0;
                return result;
            }
            i--;
        }
        return expression;
    }

    //calculate expression postfix fix expression
    public String calculate(String expression, String input){
        step = input;
        al.add("Step"+count+": "+step);
        System.out.println("Step"+count+": "+step);

        System.out.println();

        int len = expression.length(), stopCut;
        char ch;
        calculateIndex = 0;
        //loop for length of expression
        while(calculateIndex < expression.length()){
            if(calculateIndex == expression.length()-1){lastOperationDone = true;}

            ch = expression.charAt(calculateIndex);
            //operator passed to method toward the opperand
            switch (ch) {
                case '+':
                    numberStack.push(add());
                    removeParen();
                    removeNegativeParen();
                    break;
                case '-':
                    numberStack.push(sub());
                    removeParen();
                    removeNegativeParen();
                    break;
                case '*':
                    numberStack.push(mul());
                    removeParen();
                    removeNegativeParen();
                    break;

                case '/':
                    numberStack.push(div());
                    removeParen();
                    removeNegativeParen();
                    break;
                case '~':
                    //just break.
                    break;
                case '^':
                    numberStack.push(power());
                    removeParen();
                    removeNegativeParen();
                    break;
                case 'r':
                    numberStack.push(root());
                    removeParen();
                    removeNegativeParen();
                    break;
                default:
                    //index ending used to cut from step string
                    stopCut = calculateIndex + extractNum(expression, calculateIndex);
                    //push number onto the stack from index i to index stopCut
                    String s = sign.remove();

                    if(s.equals("-")){
                        numberStack.push("-"+expression.substring(calculateIndex,stopCut));
                    }else{ numberStack.push(expression.substring(calculateIndex,stopCut));}

                    calculateIndex = stopCut;
                    break;
            }

            calculateIndex++;
        }
        //pops out the final result
        return "" + numberStack.pop();

        //System.out.println(numberStack.pop());
    }
    //returns last index of a speccific numer
    public  int extractNum(String expression, int index){
        numCount++;
        int indexStop = 0;

        for(int i = index; i < expression.length(); i++){
            //add to index now as long it's a number otherwise break the loop and break the loop return indexStop
            char numNow = expression.charAt(i);
            if(numNow == '0' || numNow == '1' || numNow == '2' || numNow == '3' || numNow == '4' || numNow == '5' || numNow == '6' || numNow == '7' || numNow == '8' || numNow == '9'){
                indexStop++;

            }else{
                break;
            }
        }

        return indexStop;
    }
    //add method
    public String add(){
        String check;
        double numOne, numTwo, sum;

        numOne = (double)  Double.parseDouble(numberStack.pop());
        numTwo = (double)  Double.parseDouble(numberStack.pop());
        check = ""+numTwo;
        sum = numOne + numTwo;

        int n1 = (int)numTwo;
        int n2 = (int)numOne;
        int n3 = (int)sum;

        stepByStep(n1+"\\Q+\\E"+n2, n3+"");

        return ""+sum;
    }
    //still working on it
    public String sub(){

        double numOne, numTwo, difference;

        String test = numberStack.peek();
        numOne = (double)  Double.parseDouble(numberStack.pop());
        numTwo = (double)  Double.parseDouble(numberStack.pop());
        if(numOne < 0){
            numOne *= -1;
        }
        if(parenNegative){
            numOne *= -1;
            parenNegative = false;
        }
        difference = numTwo - numOne;

        int n1 = (int)numTwo;
        int n2 = (int)numOne;
        int n3 = (int)difference;
        System.out.println();
        stepByStep(n1+"-"+n2, n3+"");

        return ""+difference;
    }

    //multiply
    public String mul(){
        double numOne, numTwo, product;

        numOne = (double)  Double.parseDouble(numberStack.pop());
        numTwo = (double)  Double.parseDouble(numberStack.pop());

        if(parenNegative){
            numOne *= -1;
            doubleNegative();
            parenNegative = false;
        }

        product = numOne * numTwo;

        int n1 = (int)numTwo;
        int n2 = (int)numOne;
        int n3 = (int)product;

        stepByStep("\\b"+n1+"\\*"+n2+"\\b", n3+"");

        return ""+product;
    }
    //division method
    public String div(){
        double numOne, numTwo, quotient;

        numOne = (double)  Double.parseDouble(numberStack.pop());
        numTwo = (double)  Double.parseDouble(numberStack.pop());

        if(parenNegative){
            numOne *= -1;
            doubleNegative();
            parenNegative = false;
        }

        quotient = numTwo / numOne;


        int n1 = (int)numTwo;
        int n2 = (int)numOne;
        int n3 = (int)quotient;
        if(n1 < 0){n1 *= -1;}
        if(n2 < 0){n2 *= -1;}

        stepByStep("-?"+n1+"\\/-?"+n2, n3+"");

        return ""+quotient;
    }
    //handles power method
    public String power(){
        double numOne, numTwo, result;

        numOne = (double)  Double.parseDouble(numberStack.pop());
        numTwo = (double)  Double.parseDouble(numberStack.pop());

        if(parenNegative){
            numOne *= -1;
            doubleNegative();
            parenNegative = false;
        }

        result = Math.pow(numTwo, numOne);

        int n1 = (int)numTwo;
        int n2 = (int)numOne;
        int n3 = (int)result;

        stepByStep("\\b"+n1+"\\^"+n2+"\\b", n3+"");
        return Double.toString(result);
    }
    //handles root
    public String root(){
        double numOne, numTwo, root;

        numOne = (double)  Double.parseDouble(numberStack.pop());
        numTwo = (double)  Double.parseDouble(numberStack.pop());

        if(parenNegative){
            numOne *= -1;
            doubleNegative();
            parenNegative = false;
        }

        root = Math.pow(numTwo, 1/numOne);

        int n1 = (int)numTwo;
        int n2 = (int)numOne;
        int n3 = (int)root;

        stepByStep(n1+"r"+n2, n3+"");

        return Double.toString(root);
    }

    //alters the original expression in infix form to show next step
    public void stepByStep(String REGEX, String next){

        count++;
        String first, second;

        //regular expression tools
        Pattern p = Pattern.compile(REGEX);
        Matcher m = p.matcher(step);
        //while pattern is found
        if(m.find()){

            first = step.substring(0,m.start());

            second = step.substring(m.end(),step.length());
            step = first+next+second;
            //paste remove paren back here
            al.add("Step"+count+": "+step);

            System.out.println("Step"+count+": "+step);
            System.out.println();
        }

    }
    //removes parenthesis
    public void removeParen(){
        count++;
        String temp, temp2, temp3, s;
        //finds a single number in parenthesis

        Pattern pat = Pattern.compile("\\([0-9]*\\)");
        Matcher mat = pat.matcher(step);
        //if found remove the parenthesis
        if(mat.find()){
            temp2 = step.substring(mat.start()+1, mat.end()-1);
            temp = step.substring(0,mat.start());
            temp3 = step.substring(mat.end(), step.length());
            step = temp+temp2+temp3;
            System.out.println("Step"+count+": "+step);
            s = parenQ.remove();

            if(s.equals("-")){
                parenNegative = true;
                if(lastOperationDone){
                    double then = (double)  Double.parseDouble(numberStack.pop());
                    double now = then * -1;
                    numberStack.push(now+"");
                }
            }
        }
    }

    public void removeNegativeParen(){
        count++;
        String temp, temp2, temp3, s;
        //finds a single number in parenthesis

        Pattern patNegative = Pattern.compile("\\(-[0-9]*\\)");
        Matcher matNegative = patNegative.matcher(step);
        //if found remove the parenthesis
        if(matNegative.find()){
            temp2 = step.substring(matNegative.start()+1, matNegative.end()-1);
            temp = step.substring(0,matNegative.start());
            temp3 = step.substring(matNegative.end(), step.length());
            step = temp+temp2+temp3;
            System.out.println("Step"+count+": "+step);
            s = parenQ.remove();

            if(s.equals("-")){
                parenNegative = true;
                if(lastOperationDone){
                    double then = (double)  Double.parseDouble(numberStack.pop());
                    double now = then * -1;
                    numberStack.push(now+"");
                }
            }
        }

    }

    public void doubleNegative(){
        count++;
        char ch;
        String temp, oTemp;
        boolean onNegative;
        int consecutiveNeg = 0;

        for(int i = 0; i < step.length(); i++){
            ch = step.charAt(i);

            if(ch == '-'){
                onNegative = true;
                consecutiveNeg++;
            }else{
                onNegative = false;
                consecutiveNeg = 0;
            }

            if(onNegative == true && consecutiveNeg == 2){
                temp = step.substring(0,i-1);
                oTemp = step.substring(i+1, step.length());
                step = temp+oTemp;
            }
        }

        System.out.println("Step"+count+": "+step);
        System.out.println();

    }


    class Stack {
        private int maxSize;
        private String[] stackArray;
        private int top;

        public Stack(int max) {
            maxSize = max;
            stackArray = new String[maxSize];
            top = -1;
        }
        public void push(String j) {
            stackArray[++top] = j;
        }
        public String pop() {
            return stackArray[top--];
        }
        public String peek() {
            return stackArray[top];
        }
        public boolean isEmpty() {
            return (top == -1);
        }
    }
}

