package sample;

import java.util.Stack;

public class GraphingTree {

    public double variableValue;
    public Node head;

    public static enum Operator {
        PLUS, MINUS, MULTIPLY, DIVIDE, EXPONENT, ROOT, NEGATIVE, POSITIVE, NONE
    }

    public abstract class Node {

        public Node parent;

        @Override
        public abstract String toString();

        public abstract double evaluate();

    }

    public class IntermediateNode extends Node {

        public Node leftChild, rightChild;
        public Operator operator;

        public IntermediateNode(Operator operator) {
            this.operator = operator;
        }

        public IntermediateNode(Operator operator,
                                Node leftChild, Node rightChild) {
            this.operator = operator;
            this.leftChild = leftChild;
            this.rightChild = rightChild;
            leftChild.parent = this;
            rightChild.parent = this;
        }

        @Override
        public String toString() {
            String result = "(" + leftChild.toString();
            switch (operator) {
                case PLUS:
                    result += " + ";
                    break;
                case MINUS:
                    result += " - ";
                    break;
                case MULTIPLY:
                    result += " * ";
                    break;
                case DIVIDE:
                    result += " / ";
                    break;
                case EXPONENT:
                    result += "^";
                    break;
                case ROOT:
                    result += "r";
                    break;
            }
            result += rightChild.toString() + ")";
            return result;
        }

        @Override
        public double evaluate() {
            switch (operator) {
                case PLUS:
                    return leftChild.evaluate() + rightChild.evaluate();
                case MINUS:
                    return leftChild.evaluate() - rightChild.evaluate();
                case MULTIPLY:
                    return leftChild.evaluate() * rightChild.evaluate();
                case DIVIDE:
                    return leftChild.evaluate() / rightChild.evaluate();
                case EXPONENT:
                    return Math.pow(leftChild.evaluate(), rightChild.evaluate());
                case ROOT:
                    return Math.pow(leftChild.evaluate(), 1.0 / rightChild.evaluate());
                default:
                    return Double.NaN;
            }
        }
    }

    public class UnaryNode extends Node {

        public Node child;
        public Operator operator;

        public UnaryNode(Operator operator) {
            this.operator = operator;
        }

        public UnaryNode(Operator operator, Node child) {
            this.operator = operator;
            this.child = child;
            this.child.parent = this;
        }

        @Override
        public String toString() {
            String result = "(";
            switch (operator) {
                case POSITIVE:
                    result += " + ";
                    break;
                case NEGATIVE:
                    result += " - ";
                    break;
            }
            result += child.toString() + ")";
            return result;
        }

        @Override
        public double evaluate() {
            switch (operator) {
                case POSITIVE:
                    return child.evaluate();
                case NEGATIVE:
                    return -child.evaluate();
                default:
                    return Double.NaN;
            }
        }

    }

    public abstract class LeafNode extends Node {
    }

    public class NumberNode extends LeafNode {

        private double value;

        public NumberNode(double value) {
            this.value = value;
        }

        public void setValue(double value) {
            this.value = value;
        }

        public double getValue() {
            return value;
        }

        @Override
        public String toString() {
            return value + "";
        }

        @Override
        public double evaluate() {
            return value;
        }
    }

    public class VariableNode extends LeafNode {

        @Override
        public String toString() {
            return variableValue + "";
        }

        @Override
        public double evaluate() {
            return variableValue;
        }
    }

    public void parse(String expression) {
        String token = "";
        Stack<Node> operandStack = new Stack<>();
        Stack<Character> operatorStack = new Stack<>();
        char prevChar = '\0';

        for (int i = 0; i < expression.length(); i++) {
            char currentChar = expression.charAt(i);
            //System.out.println(currentChar);
            if (isDigit(currentChar)) {
                token += currentChar;
            } else {
                if (!token.isEmpty()) {
                    try {
                        double value = Double.parseDouble(token);
                        NumberNode newNode = new NumberNode(Double.parseDouble(token));
                        operandStack.push(newNode);
                    } catch (NumberFormatException ex) {
                        VariableNode newNode = new VariableNode();
                        operandStack.push(newNode);
                    }
                    token = "";
                } else {
                    if (prevChar != ')'
                            && (currentChar == '-' || currentChar == '+')) {
                        char currentOperator = (currentChar == '+') ? 'p' : 'n';
                        while (!operatorStack.isEmpty()
                                && getPrecedence(currentOperator)
                                < getPrecedence(operatorStack.peek())) {
                            popStacks(operandStack, operatorStack);
                        }
                        operatorStack.push(currentOperator);
                        continue;
                    }
                }
                if (currentChar != '(' && currentChar != ')') {
                    while (!operatorStack.isEmpty()
                            && getPrecedence(currentChar)
                            <= getPrecedence(operatorStack.peek())) {
                        popStacks(operandStack, operatorStack);
                    }
                    operatorStack.push(currentChar);
                } else if (currentChar == '(') {
                    operatorStack.push(currentChar);
                } else if (currentChar == ')') {
                    while (operatorStack.peek() != '(') {
                        popStacks(operandStack, operatorStack);
                    }
                    operatorStack.pop();
                }
            }
            prevChar = currentChar;
        }

        if (!token.isEmpty()) {
            try {
                double value = Double.parseDouble(token);
                NumberNode newNode = new NumberNode(Double.parseDouble(token));
                operandStack.push(newNode);
            } catch (NumberFormatException ex) {
                VariableNode newNode = new VariableNode();
                operandStack.push(newNode);
            }
        }

        while (!operatorStack.isEmpty()) {
            popStacks(operandStack, operatorStack);
        }
        head = operandStack.pop();
    }

    private void popStacks(Stack<Node> operandStack,
                           Stack<Character> operatorStack) {
        Operator operator = charToOperator(operatorStack.pop());
        if (operator == Operator.POSITIVE || operator == Operator.NEGATIVE) {
            Node operand = operandStack.pop();
            UnaryNode newNode = new UnaryNode(operator, operand);
            operandStack.push(newNode);
        } else {
            Node operand2 = operandStack.pop();
            Node operand1 = operandStack.pop();
            IntermediateNode newNode = new IntermediateNode(operator,
                    operand1, operand2);
            operandStack.push(newNode);
        }
    }

    private boolean isDigit(char c) {
        return Character.isDigit(c) || c == '.' || Character.isAlphabetic(c)
                && c != 'r';
    }

    private int getPrecedence(Operator o) {
        if (o == Operator.PLUS || o == Operator.MINUS) {
            return 0;
        }
        if (o == Operator.MULTIPLY || o == Operator.DIVIDE) {
            return 1;
        }
        if (o == Operator.EXPONENT || o == Operator.ROOT) {
            return 2;
        }
        if (o == Operator.POSITIVE || o == Operator.NEGATIVE) {
            return 3;
        }
        return -1;
    }

    private int getPrecedence(char c) {
        return getPrecedence(charToOperator(c));
    }

    private Operator charToOperator(char c) {
        switch (c) {
            case '+':
                return Operator.PLUS;
            case '-':
                return Operator.MINUS;
            case '*':
                return Operator.MULTIPLY;
            case '/':
                return Operator.DIVIDE;
            case '^':
                return Operator.EXPONENT;
            case 'r':
                return Operator.ROOT;
            case 'p':
                return Operator.POSITIVE;
            case 'n':
                return Operator.NEGATIVE;
            default:
                return Operator.NONE;
        }
    }

    @Override
    public String toString() {
        return head.toString();
    }

    public double evaluate() {
        return head.evaluate();
    }
}