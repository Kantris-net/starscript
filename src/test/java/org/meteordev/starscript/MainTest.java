package org.meteordev.starscript;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.meteordev.starscript.compiler.Compiler;
import org.meteordev.starscript.compiler.Parser;
import org.meteordev.starscript.utils.StarscriptError;
import org.meteordev.starscript.value.Value;
import org.meteordev.starscript.value.ValueMap;

import static org.junit.jupiter.api.Assertions.*;

public class MainTest {

    private Starscript ss;

    @BeforeEach
    public void setUp() {
        ss = new Starscript();
        StandardLib.init(ss);
    }

    @Test
    public void testScriptEvaluationDotNotation() {
        String source = "Name: {player.name}     Age: {player.age()}";

        Parser.Result result = Parser.parse(source);
        assertFalse(result.hasErrors(), "Parser should have no errors");

        Script script = Compiler.compile(result);
        assertNotNull(script, "Script should compile successfully");

        ss.set("player.name", "MineGame159");
        ss.set("player.age", (ss1, argCount) -> Value.number(5));

        String output = ss.run(script).toString();
        assertEquals("Name: MineGame159     Age: 5", output);

        ss.remove("player.name");
        String outputAfterRemoval = ss.run(script).toString();
        assertEquals("Name: null     Age: 5", outputAfterRemoval);
    }

    @Test
    public void testScriptEvaluationMapNotation() {
        String source = "Name: {player.name}     Age: {player.age()}";

        Parser.Result result = Parser.parse(source);
        assertFalse(result.hasErrors());

        Script script = Compiler.compile(result);

        ss.set("player", new ValueMap()
                .set("name", "MineGame159")
                .set("age", (ss1, argCount) -> Value.number(5))
        );

        String output = ss.run(script).toString();
        assertEquals("Name: MineGame159     Age: 5", output);
    }

    @Test
    public void testKeywordAssignmentThrowsError() {
        assertThrows(StarscriptError.class, () -> {
            ss.set("true", Value.null_());
        }, "Setting a variable name as a keyword should throw a StarscriptError");
    }
}
