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
    private double solvedFunction;
    
    // Tests if a given string is a double.
    public static boolean isDouble(String c)
    {
        try {
            Double.parseDouble(c);
            return true;
        } catch(NumberFormatException e) {
            return false;
        }
    }
    
    // Solves a function in Reverse Polish Notation by creating a binary
    // expression tree object.
    public static double solve(String parsedFunc)
    {
        Stack<ExpNode> nodeStack = new Stack<>();
        ExpNode leftNode, rightNode;
        double numVal = 0.0;
        // go through each part of the function
        for(String c : parsedFunc.split(" "))
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
                    return Double.NaN;
                }
            }
        }
        
        // This end node which contains the entire expression tree.
        // When the method is called to solve it, it will recursively calculate
        // the value of each other node in it, giving the correct answer.
        return nodeStack.peek().value();
    }
    
    public static double solveWithX(String parsedFunc, double x)
    {
        //first, see if the x has a coefficient. if so, replace with a multiplier.
        for(String s : parsedFunc.split(" "))
        {
            // only use terms containing x and some other number.
            if(!s.contains("x") || s.length() == 1)
                continue;
            
            // TODO
            // fix this because you would be modifying "s" not parsedFunc if you
            // try to replace anything here
        }
        
        return Expression.solve(parsedFunc.replaceAll("x", Double.toString(x)));
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
            //If the next char is a double, automatically add it to the output stream
            if(Expression.isDouble(c))
            {
                parsedFunc += Double.parseDouble(c) + " ";
            } else if(c.contains("x")) // Dealing with variables
            {
                // If it is just x, add it to our parsed function normally.
                if(c.length() == 1)
                {
                    parsedFunc += c + " ";
                } else
                // Otherwise, separate into two expressions
                // e.g. -- with c of 2x, separate into 2 * x
                {
                    // Get the number leading up to x
                    // i.e. the 12 from 12x
                    String numStr = c.substring(0, c.indexOf("x"));
                    parsedFunc += numStr + " x * ";
                }
            } else if(OpNode.isValidOperator(c)) //If the next char is an operator...
            {
                // If there is an operator on the stack and the new operator's precedence
                // is less than or equal to the one on the stack, then pop the stack
                // until we can add the higher-precedence operator.
                // This way, higher-precedence operations will always be performed on
                // the correct numbers before other operands
                
                // e.g. -- when doing 2 + 5 * 10, we multiply 5*10 then add 2 to
                //         the result of that.
                char op = c.toCharArray()[0];
                while(!operatorStack.isEmpty() && 
                       operatorStack.peek() != '(' &&
                       operatorStack.peek() != ')' &&
                       OpNode.getOperatorPrecedence(op) <= OpNode.getOperatorPrecedence(operatorStack.peek()))
                {
                    parsedFunc += operatorStack.pop() + " ";
                }
                operatorStack.push(op);
            } else if(c.equals("("))
            {
                operatorStack.push(c.toCharArray()[0]);
            } else if(c.equals(")"))
            {
                while(operatorStack.peek() != '(')
                {
                    parsedFunc += operatorStack.pop() + " ";
                }
                
                // pop the left parenthesis off the stack, but don't add it
                // to the output.
                operatorStack.pop();
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
        if(!this.parsedFunction.contains("x"))
            this.solvedFunction = Expression.solve(parsedFunction);
    }
    
    public double solve()
    {
        return this.solvedFunction;
    }
    
    public double solveWithX(double x)
    {
        return Expression.solve(parsedFunction.replaceAll("x", Double.toString(x)));
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
