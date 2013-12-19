package graphing.calculator.expressiontree;

/**
 * Expression node for a number
 * @author Noah
 */
public class NumNode extends ExpNode{

    
    private double number; //number of the node
    
    /**
     * Creates the node with only a number
     * @param val value to store for the node
     */
    public NumNode(double val)
    {
        this.number = val;
    }
    
    /**
     * Gets the value of the node
     * @return value of the node
     */
    double value() {
        return number;
    }
    
}
