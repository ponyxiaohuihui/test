import com.fr.script.Calculator;
import com.fr.stable.UtilEvalError;

/**
 * Created by pony on 2018/3/28.
 */
public class TestFormula {
    public static void main(String[] args) throws UtilEvalError {
        Calculator c = Calculator.createCalculator();
        c.set("0", "0d");
        System.out.println(c.eval("$0 = '00'"));
    }
}
