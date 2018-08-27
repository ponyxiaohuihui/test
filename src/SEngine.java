import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * Created by 小灰灰 on 2017/8/28.
 */
public class SEngine {
    public static void main(String[] args) throws ScriptException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("javascript");
        engine.eval("var a = 3;");
        Object ob = engine.get("a");
        System.out.println(ob);
    }
}
