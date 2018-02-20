package evo.mathApp.model;

import java.io.IOException;
import java.util.ArrayList;
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
   private String input;
   //output
   private String output = "";
   //variables to help seperate numbers 
   private static int count = 0, numCount = 0;
   //to show the steps that change over time
   private static String step;
   //arrayList to hold the steps: Save steps as Strings and add to array list.
   private static ArrayList<String> stepsToSolve = new ArrayList<String>();
   
 
   //constructor class
   public InToPost(String in) {
      input = in;
      int stackSize = input.length();
      theStack = new Stack(stackSize); 
      numberStack = new Stack(stackSize);
   }
   //this method initiates 
   public String doTrans() {
       //The , character is appended to output in order to help separate the numbers that are more than one digit.
      for (int j = 0; j < input.length(); j++) {
         char ch = input.charAt(j);
         switch (ch) {
            //if the operator is a plus or minus then call with precidence 1 gotOper()  
            case '+': 
            case '-':
               output += ",";
               gotOper(ch+"", 1); 
               break; 
            //if the operator is a asterick or foward slash then call with precidence 2 gotOper()
            case '*': 
            case '/':
               output += ","; 
               gotOper(""+ch, 2); 
               break; 
            case '^':
            case 'r':
                output += ",";
                gotOper(""+ch, 3);
                  break;
            //if there is open paranthesis push directly on the stack. 
            case '(': 
               theStack.push(""+ch);
               break; 
               //closed paranthesis call gotParen method 
            case ')': 
               output += ","; 
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
      //System.out.println(output); //cutThis
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
            else
            prec2 = 2;
            
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
         //System.out.println(output+" append "+chx); //cutThis
             output = output + chx;
         } 
      }
   }
   //edit final input in order for the calculate algorithm  to work
   public String editExpression(String expression){
       int i = expression.length()-1;
       char numNow;
       String firstHalf, lastHalf, result;
       while(i > 0){
           numNow = expression.charAt(i);
           //if any number  , behind last number  
           if(numNow == '0' || numNow == '1' || numNow == '2' || numNow == '3' || numNow == '4' || numNow == '5' || numNow == '6' || numNow == '7' || numNow == '8' || numNow == '9'){
               //cut from the begginning of the string to the last number
               firstHalf = expression.substring(0,i+1);
            //from last number to the end of the string 
            lastHalf = expression.substring(i+1);
            //add in the , behind the last number
            result = firstHalf+','+lastHalf;

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
   //System.out.println("Step"+count+": "+step); //cutThis
   //System.out.println(); //cutThis
       int len = expression.length(), stopCut;
 
       char ch;
       //loop for length of expression 
       for(int i = 0; i < expression.length(); i++){
           ch = expression.charAt(i);
      //operator passed to method toward the opperand
           switch (ch) {
            case '+':
               numberStack.push(add()); 
               break;
            case '-':
               numberStack.push(sub()); 
               break;
            case '*':
                numberStack.push(mul());
                break;
                
            case '/':
                numberStack.push(div());
                break;
            case ',':
               //just break. 
                break;
            case '^':
                numberStack.push(power());
                break;
            case 'r':
                numberStack.push(root());
                break;
            default: 
               //index ending used to cut from step string
               stopCut = i + extractNum(expression, i);
               //push number onto the stack from index i to index stopCut
               numberStack.push(expression.substring(i,stopCut));
               i = stopCut;
               break;
         }
           
           
       }
       //pops out the final result
       //System.out.println(numberStack.pop());
	return "" + numberStack.pop();
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
      if(check.charAt(0) != '-'){
      stepByStep("\\b"+n1+"\\Q+\\E"+n2+"\\b", n3+"");
      }else{
      scanRemove(n1+"+"+n2, ""+n3);
      } 
       return ""+sum;
   }
   //still working on it
   public String sub(){

     double numOne, numTwo, difference;
     if(numCount > 1){
     String test = numberStack.peek();
       numOne = (double)  Double.parseDouble(numberStack.pop());
       numTwo = (double)  Double.parseDouble(numberStack.pop());
       
       difference = numTwo - numOne;
 
       int n1 = (int)numTwo;
       int n2 = (int)numOne;
       int n3 = (int)difference;
       scanRemove(n1+"-"+n2, ""+n3);

       return ""+difference;
   }else{
       numOne = (double)  Double.parseDouble(numberStack.pop());
       numOne *= -1;
     return ""+numOne;
     }
   }
   //multiply
   public String mul(){
       double numOne, numTwo, product;
       
       numOne = (double)  Double.parseDouble(numberStack.pop());
       numTwo = (double)  Double.parseDouble(numberStack.pop());
       
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
       
       quotient = numTwo / numOne;

       int n1 = (int)numTwo;
       int n2 = (int)numOne;
       int n3 = (int)quotient;
       
      stepByStep("\\b"+n1+"\\/"+n2+"\\b", n3+"");
       return ""+quotient;
    }
    //handles power method
    public String power(){
     double numOne, numTwo, result;
       
       numOne = (double)  Double.parseDouble(numberStack.pop());
       numTwo = (double)  Double.parseDouble(numberStack.pop());
       
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
       
       root = Math.pow(numTwo, 1/numOne);
      
       int n1 = (int)numTwo;
       int n2 = (int)numOne;
       int n3 = (int)root;
       
        stepByStep("\\("+n1+"r"+n2+"\\)", n3+"");
       
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
       while(m.find()){   
      first = step.substring(0,m.start());
     
      second = step.substring(m.end(),step.length());
    
      step = first+next+second;
      removeParen();
      //System.out.println("Step"+count+": "+step); //cutThis
      //System.out.println(); //cutThis
      }       

    }
   //removes parenthesis    
    public void removeParen(){
        
        String temp, temp2, temp3;
    //finds a single number in parenthesis 
    Pattern pat = Pattern.compile("\\([0-9]*\\)");
    Matcher mat = pat.matcher(step);
     //if found remove the parenthesis 
    while(mat.find()){
        temp2 = step.substring(mat.start()+1, mat.end()-1);
        temp = step.substring(0,mat.start());
        temp3 = step.substring(mat.end(), step.length());
        step = temp+temp2+temp3;
      } 
    }
    // still being worked on 
    public void scanRemove(String REGEX, String replacement){
        System.out.println(replacement);
        count++;
        boolean resetStartIndex; 
        int startIndex = 0, endIndex = 0, matchCounter = 0; 
        char r, s;
        String strip = step;
        
         for(int i = 0; i < strip.length(); i++){
             String first, second;
             r = REGEX.charAt(i);
             s = step.charAt(i);
             System.out.println("Current i "+i);
              System.out.println("length of step string is "+step);
             if(r == s){
             resetStartIndex = false;
             matchCounter++;
             endIndex = i;
             }else{
             resetStartIndex = true;
             }
             
             if(resetStartIndex == true){
              startIndex = 0;
              strip = strip.substring(matchCounter, strip.length());
              matchCounter = 0;
             }
             
             //to break out of the loop
             if(matchCounter == REGEX.length()){
                first = step.substring(0,startIndex);
                second = step.substring(endIndex+1,step.length());
    
                step = first+replacement+second;
                 i = step.length();
            }       
             removeParen();
    }
         
    System.out.println("Step"+count+": "+step);
    }
    
   public static void main(String[] args) throws IOException {
      System.out.print("Enter expression:");
      Scanner sc = new Scanner(System.in); 
      String input = sc.next();
      System.out.println();
      String output;
      InToPost theTrans = new InToPost(input);
      output = theTrans.doTrans(); 
      output = theTrans.editExpression(output);
      System.out.println("Before Postfix "+input+" After Postfix is " + output + '\n');
      theTrans.calculate(output,input);
 
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
