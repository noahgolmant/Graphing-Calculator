/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphing.calculator.expressiontree;

import java.util.HashMap;

/**
 * Node with any operator
 * @author Noah
 */
public class OpNode extends ExpNode{
    
    private char op;                // operator symbol
    private ExpNode left, right;    // nodes attached to the tree
    
    //operators with associated precedenc according to order of operations
    private static HashMap<Character, Integer> operators = new HashMap<Character, Integer>()
    {{
        put('^', 3);
        put('*', 2);
        put('/', 2);
        put('+', 1);
        put('-', 1);
    }};

   
    /**
     * Creates an operator node with two other nodes attached to it
     * @param op    operator code to use
     * @param left  left node on the tree
     * @param right right node on the tree
     */
    public OpNode(char op, ExpNode left, ExpNode right)
    {
        this.op = op;
        this.left = left;
        this.right = right;
    }
    
    /**
     * Gets the total value of all nodes in this branch by recursively checking
     * all other attached nodes.
     * @return total value of the nodes using the object operator
     */
    double value()
    {
        double leftVal = left.value();
        double rightVal = right.value();
        switch(op)
        {
            case '+': return leftVal + rightVal;
            case '-': return leftVal - rightVal;
            case '*': return leftVal * rightVal;
            case '/': return leftVal / rightVal;
            case '^': return Math.pow(leftVal, rightVal);
            default:  return Double.NaN;
        }
    }
    
    public int getOperatorPrecedence()
    {
        return OpNode.getOperatorPrecedence(this.op);
    }
    
    //-------------------
    // STATIC FUNCTIONS
    //-------------------

    static boolean isValidOperator(String c)
    {
        return OpNode.operators.containsKey(c.toCharArray()[0]);
    }
    
    static int getOperatorPrecedence(char c)
    {
        // this way the precedence will always be lower and we
        // don't have to deal with parens
        return OpNode.operators.get(c);
    }
    
    private class Ex extends Exception
    {
        Ex(String message)
        {
            super(message);
        }
    }
}
