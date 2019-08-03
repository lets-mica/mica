package net.dreamlu.mica.test.script;

import org.junit.Assert;
import org.junit.Test;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.Map;

/**
 * 解析 jsonp
 */
public class JsonpTest {

	@Test
	public void test() throws ScriptException {
		String jsonp = "/**/callback( {\"client_id\":\"123\",\"openid\":\"123\",\"unionid\":\"123\"} )";

		String jsFun = "function callback(json) { return json };";

		ScriptEngineManager engineManager = new ScriptEngineManager();
		ScriptEngine engine = engineManager.getEngineByMimeType("text/javascript");

		engine.eval(jsFun);

		Map json = (Map) engine.eval(jsonp);
		Assert.assertEquals("123", json.get("client_id"));
		Assert.assertEquals("123", json.get("openid"));
		Assert.assertEquals("123", json.get("unionid"));
	}

}
