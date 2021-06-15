package org.feiquan.webapigateway.impl.expression;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;

import java.util.HashMap;
import java.util.Map;

/**
 * 表达式运行工具
 * @author junwei.jjw
 * @date 2021/6/15
 */
public class GroovyScriptRunner {
    private static GroovyScriptRunner instance;
    private final Map<String, Script> scripts = new HashMap<>();
    private GroovyShell groovy = new GroovyShell();

    public static GroovyScriptRunner getInstance() {
        if (instance == null) {
            synchronized (GroovyScriptRunner.class) {
                if (instance == null) {
                    instance = new GroovyScriptRunner();
                }
            }
        }
        return instance;
    }

    /**
     * 运行指定的脚本
     * @param script
     * @param binding
     * @return
     */
    @SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
    public Object run(String script, Binding binding) {
        Script compiledScript = getOrCompile(script);
        synchronized (script) {
            compiledScript.setBinding(binding);
            return compiledScript.run();
        }
    }

    /**
     * 判断脚本是否已存在，如不存在则进行编译后返回
     * @param script
     * @return
     */
    public Script getOrCompile(String script) {
        Script compiledScript = scripts.get(script);
        if (compiledScript == null) {
            synchronized (scripts) {
                compiledScript = scripts.get(script);
                if (compiledScript == null) {
                    compiledScript = groovy.parse(script);
                    scripts.put(script, compiledScript);
                }
            }
        }
        return compiledScript;
    }
}
