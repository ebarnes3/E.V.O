package sample;

//This is a java program to construct Expression Tree using Infix Expression
import java.io.*;
import java.util.*;

class Node
{
    public char data;
    public Node leftChild;
    public Node rightChild;

    public Node(char data)
    {
        this.data = data;
    }

    public void displayNode()
    {
        System.out.print(data);
    }

    public String getNode(){
        return this.data+"";
    }
}

class Stack1
{
    private Node[] a;
    private int    top, m;

    public Stack1(int max)
    {
        m = max;
        a = new Node[m];
        top = -1;
    }

    public void push(Node key)
    {
        a[++top] = key;
    }

    public Node pop()
    {
        return (a[top--]);
    }

    public boolean isEmpty()
    {
        return (top == -1);
    }
}

class Stack2
{
    private String[] a;
    private int    top, m;

    public Stack2(int max)
    {
        m = max;
        a = new String[m];
        top = -1;
    }

    public void push(String key)
    {
        a[++top] = key;
    }

    public String pop()
    {
        return (a[top--]);
    }

    public boolean isEmpty()
    {
        return (top == -1);
    }
}

class Conversion
{
    private Stack2 s;
    private String input;
    private String output = "";

    public Conversion(String str)
    {
        input = str;
        s = new Stack2(str.length());
    }

    public String inToPost()
    {
        for (int i = 0; i < input.length(); i++)
        {
            char ch = input.charAt(i);
            switch (ch)
            {
                case '+':
                case '-':
                    gotOperator(ch, 1);
                    break;
                case '*':
                case '/':
                    gotOperator(ch, 2);
                    break;
                case '(':
                    s.push(ch+"");
                    break;
                case ')':
                    gotParenthesis();
                    break;
                default:
                    output = output + ch;
            }
        }
        while (!s.isEmpty())
            output = output + s.pop();
        return output;
    }

    private void gotOperator(char opThis, int prec1)
    {
        while (!s.isEmpty())
        {
            String opTop = s.pop();
            if (opTop.equals("("))
            {
                s.push(opTop);
                break;
            } else
            {
                int prec2;
                if (opTop.equals("+") || opTop.equals("-"))
                    prec2 = 1;
                else
                    prec2 = 2;
                if (prec2 < prec1)
                {
                    s.push(opTop);
                    break;
                } else
                    output = output + opTop;
            }
        }
        s.push(opThis+"");
    }

    private void gotParenthesis()
    {
        while (!s.isEmpty())
        {
            String ch = s.pop();
            if (ch.equals("("))
                break;
            else
                output = output + ch;
        }
    }
}

class Tree
{
    public Node root;
    public ArrayList <String> al = new ArrayList <String>();
    static boolean firstTime = true;
    Map<String, Integer> freqNum = new HashMap<>();

    public Tree()
    {
        root = null;
    }

    public void insert(String s)
    {
        Conversion c = new Conversion(s);
        s = c.inToPost();
        Stack1 stk = new Stack1(s.length());
        s = s + "#";
        int i = 0;
        char symbol = s.charAt(i);
        Node newNode;
        while (symbol != '#')
        {
            if (symbol >= '0' && symbol <= '9' || symbol >= 'A'
                    && symbol <= 'Z' || symbol >= 'a' && symbol <= 'z')
            {
                newNode = new Node(symbol);
                stk.push(newNode);
            } else if (symbol == '+' || symbol == '-' || symbol == '/'
                    || symbol == '*')
            {
                Node ptr1 = stk.pop();
                Node ptr2 = stk.pop();
                newNode = new Node(symbol);
                newNode.leftChild = ptr2;
                newNode.rightChild = ptr1;
                stk.push(newNode);
            }
            symbol = s.charAt(++i);
        }
        root = stk.pop();
    }

    public void traverse(int type)
    {
        switch (type)
        {
            case 1:
                System.out.print("Preorder Traversal:-    ");
                preOrder(root);
                break;
            case 2:
                System.out.print("Inorder Traversal:-     ");
                inOrder(root);
                break;
            case 3:
                System.out.print("Postorder Traversal:-   ");
                postOrder(root);
                break;
            default:
                System.out.println("Invalid Choice");
        }
    }

    public void preOrder(Node localRoot)
    {
        if (localRoot != null)
        {
            localRoot.displayNode();
            String yo = localRoot.getNode();
            System.out.println();
            System.out.println("pre order Next is : "+yo);
            al.add(yo);
            preOrder(localRoot.leftChild);
            preOrder(localRoot.rightChild);
        }
    }

    private void inOrder(Node localRoot)
    {
        if (localRoot != null)
        {
            inOrder(localRoot.leftChild);
            localRoot.displayNode();
            String yo = localRoot.getNode();
            System.out.println();
            System.out.println("Next is : "+yo);
            // al.add(yo);
            inOrder(localRoot.rightChild);
        }
    }

    private void postOrder(Node localRoot)
    {
        if (localRoot != null)
        {
            String yo = localRoot.getNode();
            postOrder(localRoot.leftChild);
            postOrder(localRoot.rightChild);
            localRoot.displayNode();
            System.out.println();
            System.out.println("postOrder Next is : "+yo);
            //al.add(yo);
        }
    }

    public ArrayList<String> getList(){
        return this.al;
    }

    public void formList(ArrayList<String> al) throws IOException{
        Stack2 s = new Stack2(al.size());
        Queue<String> q = new LinkedList<String>();
        char ch, lastTime = ' ';
        String get;
        boolean sameOperator = false;
        int index = al.size()-1;

        while(index >= 0){
            System.out.println();
            get = al.get(index);
            ch = get.charAt(0);


            switch(ch){
                case'0':
                case'1':
                case'2':
                case'3':
                case'4':
                case'5':
                case'6':
                case'7':
                case'8':
                case'9':
                    s.push(ch+"");
                    break;
                default:


                    if(ch == lastTime){

                        sameOperator = true;
                    }else{

                        sameOperator = false;}

                    if(sameOperator || firstTime){

                        q.add(ch+"");
                        firstTime = false;
                    }else{

                        formOptions(s,q);
                        q.add(ch+"");
                        sameOperator = false;
                        firstTime = true;
                    }
                    lastTime = ch;
                    break;
            }
            index = index - 1;

            System.out.println();
        }

        s = formOptions(s,q);
    }

    public Stack2 formOptions(Stack2 s, Queue<String> q) throws IOException{

        System.out.println("formOptions method breached");
        int loopFor = q.size();
        boolean moreOptions = false;

        if(loopFor > 1){moreOptions = true;}

        ArrayList <String> options = new ArrayList <String>();
        String rightOperand,leftOperand,extraOperand;
        System.out.println("queue size is "+q.size());

        //holds the current operator and remove it from the queue
        String operator = q.remove();

        //get left and right operands
        leftOperand = s.pop();
        rightOperand = s.pop();

        //call to add to hash map and manage the quanity of each number
        numberManager(Integer.parseInt(leftOperand),"add");
        numberManager(Integer.parseInt(rightOperand),"add");

        //add an option to the list
        options.add(leftOperand+""+operator+""+rightOperand);

        if(moreOptions){
            //forms additional opotions for example if there is a 7+6+5 7+6 is added to the options list now it will add 7+5 & 7+6
            for(int i = 1; i < loopFor; i++){
                extraOperand = s.pop();
                options.add(leftOperand+""+operator+""+extraOperand);
                options.add(rightOperand+""+operator+""+extraOperand);
                if(i == 1){numberManager(Integer.parseInt(extraOperand),"add");}
            }
        }
        System.out.println("Options are "+options);

        s = decisions(options,s);
        if(q.size() > 0){formOptions(s,q);}
        return s;
    }

    public void numberManager(int op, String addOrSub){
        System.out.println("numberManager method breached");
        System.out.println(op+" is op boolean is in freqNum "+freqNum.containsKey(op+""));
        //if empty add first two operands else see if it's already in the stack
        if(freqNum.isEmpty() == true || freqNum.containsKey(op+"") == false){
            freqNum.put(op+"",1);
            System.out.println("First time op "+op+" qty shoul be: "+freqNum.get(op+""));
        }else{
            //if caller passes add then increment the value of the key other wise reduce it
            if(addOrSub.equals("add") == true){
                System.out.println("add to qty is "+freqNum.get(op+""));
                freqNum.computeIfPresent(op+"", (k, v) -> v+1);
                System.out.println("add to qty is now "+freqNum.get(op+""));
            }else{
                System.out.println("sub to qty is "+freqNum.get(op+""));
                freqNum.computeIfPresent(op+"", (k, v) -> v-1);}
            System.out.println("sub to qty is now "+freqNum.get(op+""));
        }
        System.out.println("op is "+op+" and qty is "+freqNum.get(op+""));
        System.out.println();
    }

    public Stack2 decisions(ArrayList <String> choices, Stack2 s) throws IOException{
        //used as index
        int index;

        //for user input
        DataInputStream inp = new DataInputStream(System.in);

        //choice will hold the string chosen from options, chosen will hold actual Sring vallue of option
        //the rest are for operands
        String choice, chosen, op1 = "", op2 = "", op3 = "", op4 = "", temp2;
        char made, operator, temp;

        System.out.println("make choice");

        //displays options
        for(int i = 0; i < choices.size(); i++){
            System.out.println("option "+i+" :"+choices.get(i));
        }

        //get response from user
        choice = inp.readLine();
        //turns String input to an integer
        index = Integer.parseInt(choice);
        //hold String value of the choice
        chosen = choices.get(index);

        //gets character from choice String value in order to make propper calculation
        made = choice.charAt(0);

        //integer value of the choice made
        int pick = Character.getNumericValue(made);
        System.out.println("chosen in decision "+chosen);

        operator = '0';
        //split string and prepare it for calculation
        for(int i = 0; i < chosen.length(); i++){
            System.out.println();
            //holds temporary character from String value
            temp = chosen.charAt(i);

            System.out.println("temp is "+temp);

            //get the operator
            if(temp == '+' ||temp == '-' || temp == '*' || temp == '/'){
                operator = temp;
            }
        }
        //holds boolean value to know what to conchatonate to first or second operand
        boolean swap = false;
        char is;

        //split String into to get operands example: "3+5" would result in op1 = "3" & op2 = "op2"
        for(int g = 0; g < chosen.length(); g++){
            is = chosen.charAt(g);
            if(is == '+' || is == '-' || is == '*' || is == '/'){
                op1 = chosen.substring(0,g);
                op2 = chosen.substring(g+1,chosen.length());
                numberManager(Integer.parseInt(op1),"sub");
                numberManager(Integer.parseInt(op2),"sub");
            }
        }
        //loop in order to get all operands from all the previouse options
        for(int j = 0; j < choices.size(); j++){
            temp2 = choices.get(j);
            System.out.println("line 403 "+temp2);
            for(int i = 0; i < temp2.length(); i++){
                is = temp2.charAt(i);
                if(is == '+' || is == '-' || is == '*' || is == '/'){
                    op3 = chosen.substring(0,i);
                    op4 = chosen.substring(i+1,chosen.length());
                }
            }

            //Put numbers from options back in the stack
            int quanity;
            System.out.println("-------------------------------------------------------------------------------------------------------");
            quanity = freqNum.get(op3+"");
            System.out.println("op3 is "+op3+" and qty is "+quanity);
            if(quanity > 0){
                for(int t = 0; t < quanity; t++){
                    s.push(op3+"");
                    System.out.println("op3 is "+op3+" and qty is "+quanity);
                }
            }

            quanity = freqNum.get(op4+"");
            if(quanity > 0){
                for(int t = 0; t < quanity; t++){
                    s.push(op4+"");
                    System.out.println("op4 is "+op4+" and qty is "+quanity);
                }
            }
        }

        switch(operator){
            case '+':
                s.push(add(chosen)+"");
                break;
            case '-':
                s.push(sub(chosen)+"");
                break;
            case '*':
                s.push(mul(chosen)+"");
                break;
            case '/':
                s.push(div(chosen)+"");
                break;
        }

        return s;
    }

    public int add(String expression){
        System.out.println("add method breached");
        String op1 = "",op2 = "";
        boolean swap = false;
        char is;
        int a,b,c,sum;

        for(int i = 0; i < expression.length(); i++){
            is = expression.charAt(i);

            if(is == '+'){swap = true;}

            if(swap == false){op1 += is;}else{
                if(is != '+'){op2 += is;}
            }
        }

        a =  Integer.parseInt(op1);
        b = Integer.parseInt(op2);
        c = a+b;
        System.out.println(a+" + "+b+" = "+c);
        sum = c;

        return sum;
    }

    public int sub(String expression){
        String op1 = "",op2 = "";
        boolean swap = false;
        char is;
        int a,b,c,difference;

        for(int i = 0; i < expression.length(); i++){
            is = expression.charAt(i);

            if(is == '-'){
                swap = true;

            }

            if(swap == false){op1 += is;}else{op2 += is;}
        }



        a =  Integer.parseInt(op1);
        b = Integer.parseInt(op2);
        c = a-b;

        difference = c;

        return difference;
    }

    public int mul(String expression){
        String op1 = "",op2 = "";
        boolean swap = false;
        char is;
        int a,b,c,product;

        for(int i = 0; i < expression.length(); i++){
            is = expression.charAt(i);

            if(is == '*'){
                swap = true;
            }

            if(swap == false){op1 += is;}else{
                if(is != '*'){op2 += is;}
            }
        }
        System.out.println("op2 is "+op2);
        a =  Integer.parseInt(op1);
        b = Integer.parseInt(op2);
        c = a*b;

        product = c;

        return product;
    }

    public int div(String expression){
        String op1 = "",op2 = "";
        boolean swap = false;
        char is;
        int a,b,c,quotient;

        for(int i = 0; i < expression.length(); i++){
            is = expression.charAt(i);

            if(is == '/'){
                swap = true;
            }

            if(swap == false){op1 += is;}else{op2 += is;}
        }

        a =  Integer.parseInt(op1);
        b = Integer.parseInt(op2);
        c = a/b;

        quotient = c;

        return quotient;
    }
    public void restack(ArrayList<Integer> list, int op1, int op2){


    }
}

public class Infix_Expression_Tree
{
    public static void main(String args[]) throws IOException
    {
        String ch = "y";
        DataInputStream inp = new DataInputStream(System.in);
        while(ch.equals("y"))
        {
            Tree t1 = new Tree();
            System.out.println("-------------------------testing--------------------------------");
            Map<String, Integer> words = new HashMap<>();
            words.put("hello", 3);
            words.put("world", 4);
            words.computeIfPresent("hello", (k, v) -> v+1);
            System.out.println(words.get("hello"));
            System.out.println();

            System.out.println("Enter the Expression");
            String a = inp.readLine();
            t1.insert(a);
            t1.traverse(1);
            System.out.println("");
            t1.traverse(2);
            System.out.println("");
            t1.traverse(3);
            System.out.println("");
            System.out.print("Enter y to continue ");
            ch = inp.readLine();
            System.out.println("print array List "+t1.getList());

            t1.formList(t1.getList());

        }
    }
}

