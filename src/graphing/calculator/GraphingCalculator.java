package graphing.calculator;
import graphing.calculator.expressiontree.*;

/**
 * Main Class of the calculator.
 * @author Noah
 */
public class GraphingCalculator {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String function = "2 + 2x + 4";
        Expression e = new Expression(function);
        
        System.out.println(e.getBaseFunction());
        System.out.println(e.getParsedFunction());
        
        System.out.println(e.solveWithX(2.0));
        
    }
}
