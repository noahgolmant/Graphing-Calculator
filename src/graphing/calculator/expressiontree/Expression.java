package graphing.calculator.expressiontree;

import java.util.EmptyStackException;
import java.util.Stack;

/**
 * Creates an expression with a collection of nodes
 * @author Noah
 */
public class Expression {
    
    private ExpNode endNode;
    private String baseFunction, parsedFunction;
    
    public static boolean isDouble(String s)
    {
        try {
            double i = Double.parseDouble(s);
            return true;
        } catch(NumberFormatException e) {
            return false;
        }
    }
    
    //Shunting yard parser implementation
    // returns function in reverse polish notation to be used by the binary expression tree
    public String parseFunction(String input)
    {
        //Create output and operator stack
        String parsedFunc = "";
        Stack<Character> operatorStack = new Stack<Character>();
        
        for(String c : input.split(" "))
        {
            //If the next char is an double, automatically add it to the output stream
            if(Expression.isDouble(c))
            {
                parsedFunc += Double.parseDouble(c) + " ";
            } else if(OpNode.isValidOperator(c)) //If the next char is an operator...
            {
                // If there is an operator on the stack and the new operator's precedence
                // is less than or equal to the one on the stack, then pop the stack
                // until we can add the higher-precedence operator.
                // This way, higher-precedence operations will always be performed on
                // the correct number pairs before other ones
                
                // e.g. -- when doing 2 + 5 * 10, we multiply 5*10 then add 2 to
                //         the result of that.
                char op = c.toCharArray()[0];
                while(!operatorStack.isEmpty() && OpNode.getOperatorPrecedence(op) <= OpNode.getOperatorPrecedence(operatorStack.peek()))
                {
                    parsedFunc += operatorStack.pop() + " ";
                }
                operatorStack.push(op);
            }
        }
        
        // Add any remaining operators to be applied to the function to the end.
        while(!operatorStack.isEmpty())
        {
            parsedFunc += operatorStack.pop() + " ";
        }
        
        return parsedFunc;
    }
    
    public Expression(String function)
    {
        this.baseFunction = function;
        this.parsedFunction = parseFunction(function);
        
        Stack<ExpNode> nodeStack = new Stack<>();
        ExpNode leftNode, rightNode;
        double numVal;
        // go through each part of the function
        for(String c : parsedFunction.split(" "))
        {
            // If the node is a number, immediately add it to the stack.
            if(Expression.isDouble(c))
            {
                numVal = Double.parseDouble(c);
                nodeStack.push(new NumNode(numVal));
            } else if(OpNode.isValidOperator(c))
            {
                // If the node is an operator, immediately take the two previous
                // nodes in the stack and branch them off of the new operator.
                try {
                    rightNode = nodeStack.pop();
                    leftNode  = nodeStack.pop();
                    
                    nodeStack.push(new OpNode(c.toCharArray()[0], leftNode, rightNode));
                } catch(EmptyStackException e) {
                    return;
                }
            }
        }
        
        // This end node which contains the entire expression tree.
        // When the method is called to solve it, it will recursively calculate
        // the value of each other node in it, giving the correct answer.
        this.endNode = nodeStack.peek();
    }
    
    public double value()
    {
        return this.endNode.value();
    }
    
    public String getBaseFunction()
    {
        return this.baseFunction;
    }
    
    public String getParsedFunction()
    {
        return this.parsedFunction;
    }
    
}
