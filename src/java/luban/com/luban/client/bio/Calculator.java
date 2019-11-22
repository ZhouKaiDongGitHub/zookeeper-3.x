package com.luban.client.bio;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class Calculator {
    private final static ScriptEngine jse = new ScriptEngineManager().getEngineByName("js");
    public static Object cal(String expresion) throws ScriptException {
        return jse.eval(expresion);
    }
}
