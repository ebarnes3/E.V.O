package sample;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Stack;

public class ExpressionTree {

    public Node head, collapsedHead;
    ArrayList<Token> tokens = new ArrayList<>();

    public static enum Operator {
        PLUS, MINUS, MULTIPLY, DIVIDE, EXPONENT, ROOT, NEGATIVE, POSITIVE, NONE
    }

    public class Token {

        String tokenString = "";
        Node node, collapsedNode;

        public Token() {
        }

        public Token(String tokenString) {
            this.tokenString = tokenString;
        }

        public Token(String tokenString, Node node) {
            this.tokenString = tokenString;
            this.node = node;
        }

        @Override
        public String toString() {
            return tokenString;
        }
    }

    public abstract class Node {

        public Node parent;
        public Token token;

        @Override
        public abstract String toString();

        public abstract double evaluate();

        public abstract Node cloneToCollapsed();

        public abstract Node collapse();

        public abstract String getTokenString();

        public Node getNonUnaryParent() {
            if (parent == null) {
                return null;
            }
            Node current = parent;
            while (current.parent != null
                    && (current instanceof UnaryNode
                    || current instanceof KnaryNode
                    && ((KnaryNode) current).precedence == 3)) {
                current = current.parent;
            }

            return current;
        }

        // Return a list of node from the current node to the
        // first unary node of the unary string.
        public ArrayList<Node> traverseToUnaryRoot() {
            ArrayList<Node> result = new ArrayList<>();
            result.add(this);
            if (parent == null) {
                return result;
            }
            Node current = parent;
            while (current.parent != null
                    && (current instanceof UnaryNode
                    || current instanceof KnaryNode
                    && ((KnaryNode) current).precedence == 3)) {
                result.add(current);
                current = current.parent;
            }
            return result;
        }
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

        @Override
        public IntermediateNode cloneToCollapsed() {
            IntermediateNode newNode = new IntermediateNode(operator,
                    leftChild.cloneToCollapsed(), rightChild.cloneToCollapsed());
            newNode.token = token;
            token.collapsedNode = newNode;
            return newNode;
        }

        @Override
        public Node collapse() {
            KnaryNode newNode = new KnaryNode();
            newNode.precedence = getPrecedence(operator);
            if (leftChild instanceof LeafNode) {
                newNode.addChild(leftChild);
            } else {
                KnaryNode leftKnaryNode = (KnaryNode) leftChild.collapse();
                if (leftKnaryNode.precedence == newNode.precedence) {
                    for (Node childNode : leftKnaryNode.children) {
                        newNode.addChild(childNode);
                    }
                } else {
                    newNode.addChild(leftKnaryNode);
                }
            }
            if (rightChild instanceof LeafNode) {
                newNode.addChild(rightChild);
            } else {
                KnaryNode rightKnaryNode = (KnaryNode) rightChild.collapse();
                if (rightKnaryNode.precedence == newNode.precedence) {
                    for (Node childNode : rightKnaryNode.children) {
                        newNode.addChild(childNode);
                    }
                } else {
                    newNode.addChild(rightKnaryNode);
                }
            }
            return newNode;
        }

        @Override
        public String getTokenString() {
            switch (operator) {
                case PLUS:
                    return "+";
                case MINUS:
                    return "-";
                case MULTIPLY:
                    return "*";
                case DIVIDE:
                    return "/";
                case EXPONENT:
                    return "^";
                case ROOT:
                    return "r";
                default:
                    return "";
            }
        }

        public Node otherChild(Node child) {
            if (child == leftChild) {
                return rightChild;
            }
            if (child == rightChild) {
                return leftChild;
            }
            return null;
        }
    }

    public class KnaryNode extends Node {

        public ArrayList<Node> children = new ArrayList<>();
        public int precedence = -1;

        @Override
        public String toString() {
            String result = precedence + "(";
            for (Node node : children) {
                result += node + " ";
            }
            result = result.substring(0, result.length() - 1) + ")";
            return result;
        }

        @Override
        public double evaluate() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Node cloneToCollapsed() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Node collapse() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            /*ArrayList<Node> newChildren = new ArrayList<>();
            for (Node node : newChildren) {
                if (node instanceof KnaryNode) {
                    for (Node childNode : ((KnaryNode) node.collapse()).children) {
                        newChildren.add(childNode);
                    }
                } else {
                    newChildren.add(node);
                }
            }
            children = newChildren;
            return this;*/
        }

        public void addChild(Node child) {
            children.add(child);
            child.parent = this;
        }

        @Override
        public String getTokenString() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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

        @Override
        public UnaryNode cloneToCollapsed() {
            UnaryNode newNode = new UnaryNode(operator, child.cloneToCollapsed());
            newNode.token = token;
            token.collapsedNode = newNode;
            return newNode;
        }

        @Override
        public Node collapse() {
            KnaryNode newNode = new KnaryNode();
            newNode.addChild(child.collapse());
            newNode.token = token;
            newNode.precedence = getPrecedence(operator);
            return newNode;
        }

        @Override
        public String getTokenString() {
            switch (operator) {
                case POSITIVE:
                    return "+";
                case NEGATIVE:
                    return "-";
                default:
                    return "";
            }
        }
    }

    public abstract class LeafNode extends Node {

        @Override
        public Node collapse() {
            return this;
        }
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

        @Override
        public NumberNode cloneToCollapsed() {
            NumberNode newNode = new NumberNode(value);
            newNode.token = token;
            token.collapsedNode = newNode;
            return newNode;
        }

        @Override
        public String getTokenString() {
            NumberFormat formatter = new DecimalFormat("##.#######");
            return formatter.format(value);
        }
    }

    public class VariableNode extends LeafNode {

        @Override
        public String toString() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public double evaluate() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Node cloneToCollapsed() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public String getTokenString() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }

    public void parse(String expression) {
        String token = "";
        Stack<Node> operandStack = new Stack<>();
        Stack<Character> operatorStack = new Stack<>();
        Stack<Token> tokenStack = new Stack<>();
        char prevChar = '\0';

        for (int i = 0; i < expression.length(); i++) {
            char currentChar = expression.charAt(i);
            //System.out.println(currentChar);
            if (isDigit(currentChar)) {
                token += currentChar;
            } else {
                if (!token.isEmpty()) {
                    NumberNode newNode = new NumberNode(Double.parseDouble(token));
                    Token newToken = new Token(token, newNode);
                    tokens.add(newToken);
                    token = "";
                    operandStack.push(newNode);
                } else {
                    if (prevChar != ')'
                            && (currentChar == '-' || currentChar == '+')) {
                        Token newToken = new Token(currentChar + "");
                        tokens.add(newToken);
                        char currentOperator = (currentChar == '+') ? 'p' : 'n';
                        while (!operatorStack.isEmpty()
                                && getPrecedence(currentOperator)
                                < getPrecedence(operatorStack.peek())) {
                            popStacks(operandStack, operatorStack, tokenStack);
                        }
                        operatorStack.push(currentOperator);
                        tokenStack.push(newToken);
                        continue;
                    }
                }
                if (currentChar != '(' && currentChar != ')') {
                    Token newToken = new Token(currentChar + "");
                    tokens.add(newToken);
                    while (!operatorStack.isEmpty()
                            && getPrecedence(currentChar)
                            <= getPrecedence(operatorStack.peek())) {
                        popStacks(operandStack, operatorStack, tokenStack);
                    }
                    operatorStack.push(currentChar);
                    tokenStack.push(newToken);
                } else if (currentChar == '(') {
                    operatorStack.push(currentChar);
                    tokens.add(new Token(currentChar + ""));
                } else if (currentChar == ')') {
                    tokens.add(new Token(currentChar + ""));
                    while (operatorStack.peek() != '(') {
                        popStacks(operandStack, operatorStack, tokenStack);
                    }
                    operatorStack.pop();
                }
            }
            prevChar = currentChar;
        }

        if (!token.isEmpty()) {
            NumberNode newNode = new NumberNode(Double.parseDouble(token));
            Token newToken = new Token(token, newNode);
            tokens.add(newToken);
            operandStack.push(newNode);
        }

        while (!operatorStack.isEmpty()) {
            popStacks(operandStack, operatorStack, tokenStack);
        }
        head = operandStack.pop();

        // Add reference of the tokens to the nodes.
        for (Token currentToken : tokens) {
            if (currentToken.node != null) {
                currentToken.node.token = currentToken;
            }
        }

        collapsedHead = head.cloneToCollapsed();
        collapsedHead = collapsedHead.collapse();
    }

    private void popStacks(Stack<Node> operandStack,
                           Stack<Character> operatorStack, Stack<Token> tokenStack) {
        Operator operator = charToOperator(operatorStack.pop());
        Token token = tokenStack.pop();
        if (operator == Operator.POSITIVE || operator == Operator.NEGATIVE) {
            Node operand = operandStack.pop();
            UnaryNode newNode = new UnaryNode(operator, operand);
            token.node = newNode;
            operandStack.push(newNode);
        } else {
            Node operand2 = operandStack.pop();
            Node operand1 = operandStack.pop();
            IntermediateNode newNode = new IntermediateNode(operator,
                    operand1, operand2);
            token.node = newNode;
            operandStack.push(newNode);
        }
    }

    private boolean isDigit(char c) {
        return Character.isDigit(c) || c == '.';
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

    public boolean isValidMove(Token token1, Token token2) {
        if (token1.collapsedNode == null
                || token2.collapsedNode == null) {
            return false;
        }
        boolean sameParent = token1.collapsedNode.getNonUnaryParent()
                == token2.collapsedNode.getNonUnaryParent();
        // If they have the same parent, test to see if there's a close 
        // parenthesis in the middle.
        if (sameParent) {
            int index1 = tokens.indexOf(token1);
            int index2 = tokens.indexOf(token2);
            // If index 2 comes before 1 then swap.
            if (index2 < index1) {
                int temp = index1;
                index1 = index2;
                index2 = temp;
            }
            int parentheses = 0;
            for (int i = index1; i < index2; i++) {
                if (tokens.get(i).tokenString.equals("(")) {
                    parentheses++;
                } else if (tokens.get(i).tokenString.equals(")")) {
                    parentheses--;
                    if (parentheses < 0) {
                        return false;
                    }
                }
            }
            if (parentheses != 0) {
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    public double evaluate() {
        return head.evaluate();
    }

    // Merge token1 into token2. Token1 will be destroyed.
    // This function only remove the unary operations before merging.
    // It'll subsequently call the merge non-unary function.
    public void merge(Token token1, Token token2) {
        // Assuming the valid move check is already handled.
        ArrayList<Node> unaryString1 = token1.node.traverseToUnaryRoot();
        ArrayList<Node> unaryString2 = token2.node.traverseToUnaryRoot();
        int precedence = ((KnaryNode) token1.collapsedNode.getNonUnaryParent()).precedence;
        NumberNode node1 = stripUnary(unaryString1);
        NumberNode node2 = stripUnary(unaryString2);
        mergeNonUnary(node1, node2, precedence);
    }

    // Remove all unary tokens from the list and replace the last element
    // (the root of the unary string) with the bottom node (the number node).
    // Return the newly replace node (the number node).
    private NumberNode stripUnary(ArrayList<Node> unaryString) {
        if (unaryString.size() == 1) {
            return (NumberNode) unaryString.get(0);
        }
        for (int i = 0; i < unaryString.size() - 1; i++) {
            tokens.remove(unaryString.get(i).token);
        }
        Node unaryRoot = unaryString.get(unaryString.size() - 1);
        NumberNode numberNode = (NumberNode) unaryString.get(0);
        numberNode.value = unaryRoot.evaluate();
        replaceNode(numberNode, unaryRoot, true);
        return numberNode;
    }

    // Merge node1 into node2.
    // Node1 and node2 are number nodes with non-unary parents.
    private void mergeNonUnary(NumberNode node1, NumberNode node2, int precedence) {
        // Assuming the valid move check is already handled.
        for (Token token : tokens) {
            token.collapsedNode = null;
        }
        IntermediateNode parent1 = (IntermediateNode) node1.parent;
        IntermediateNode parent2 = (IntermediateNode) node2.parent;

        // If they have the same parents, just replace the parent with a number
        // node with the evaluation.
        if (parent1 == parent2) {
            NumberNode newNode = new NumberNode(parent1.evaluate());
            replaceNode(newNode, parent1, true);
            tokens.remove(node1.token);
            tokens.remove(node2.token);
        } else {
            // Handle for plus and minuses.
            if (precedence == 0) {
                tokens.remove(node1.token);
                if (parent1.operator == Operator.PLUS
                        || parent1.operator == Operator.MINUS
                        && node1 == parent1.rightChild) {
                    tokens.remove(parent1.token);
                    replaceNode(parent1.otherChild(node1), parent1, false);
                } else {
                    // If the node 1 is the left child of a minus node.
                    // The binary minus node becomes a unary negative node.
                    UnaryNode newNode = new UnaryNode(Operator.NEGATIVE,
                            parent1.otherChild(node1));
                    replaceNode(newNode, parent1, true);
                }

                double result = node1.evaluate();
                if (parent1.operator == Operator.MINUS && node1 == parent1.rightChild) {
                    result *= -1;
                }
                double value2 = node2.evaluate();
                if (parent2.operator == Operator.MINUS && node2 == parent2.rightChild) {
                    result = value2 - result;
                } else {
                    result += value2;
                }
                node2.value = result;
                node2.token.tokenString = node2.getTokenString();
            } else if (precedence == 1) {
                double result = node1.evaluate();
                if (parent1.operator == Operator.MULTIPLY
                        || parent1.operator == Operator.DIVIDE
                        && node1 == parent1.rightChild) {
                    tokens.remove(parent1.token);
                    tokens.remove(node1.token);
                    replaceNode(parent1.otherChild(node1), parent1, false);
                } else {
                    // If the node 1 is the left child of a divide node.
                    // Replace node 1 with 1.
                    node1.value = 1;
                    node1.token.tokenString = node1.getTokenString();
                }

                if (parent1.operator == Operator.DIVIDE && node1 == parent1.rightChild) {
                    result = 1.0 / result;
                }
                double value2 = node2.evaluate();
                if (parent2.operator == Operator.DIVIDE && node2 == parent2.rightChild) {
                    result = value2 / result;
                } else {
                    result *= value2;
                }
                node2.value = result;
                node2.token.tokenString = node2.getTokenString();
            }
        }

        // Remove useless parentheses (the ones that encloses only 1 number).
        boolean changed = true;
        while (changed) {
            changed = false;
            int middleCount = 0;
            Token openParenthesis = null, closeParenthesis = null;
            for (Token token : tokens) {
                if (openParenthesis == null) {
                    if (token.tokenString.equals("(")) {
                        openParenthesis = token;
                    }
                } else {
                    if (middleCount > 2) {
                        openParenthesis = null;
                        middleCount = -1;
                    }
                    if (middleCount == 1 && token.tokenString.equals(")")) {
                        closeParenthesis = token;
                        changed = true;
                        break;
                    }
                    middleCount++;
                }
            }
            if (changed) {
                tokens.remove(openParenthesis);
                tokens.remove(closeParenthesis);
            }
        }

        // Recollapse the tree because I'm lazy ;).
        collapsedHead = head.cloneToCollapsed();
        collapsedHead = collapsedHead.collapse();
        /*System.out.println(head);
        System.out.println(collapsedHead);*/
    }

    // Replace the dst node by the src node.
    private void replaceNode(Node src, Node dst, boolean replaceToken) {
        Node dstParent = dst.parent;
        if (dst.parent instanceof IntermediateNode) {
            IntermediateNode parent = (IntermediateNode) dstParent;
            if (dst == parent.leftChild) {
                parent.leftChild = src;
            }
            if (dst == parent.rightChild) {
                parent.rightChild = src;
            }
        } else if (dst.parent instanceof UnaryNode) {
            ((UnaryNode) dstParent).child = src;
        }
        src.parent = dstParent;
        if (replaceToken) {
            src.token = dst.token;
            //System.out.println("Replace idx" + ": " + tokens.indexOf(src.token));
            src.token.node = src;
            src.token.tokenString = src.getTokenString();
        }
    }
}