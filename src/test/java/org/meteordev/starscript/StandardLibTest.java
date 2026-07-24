package org.meteordev.starscript;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.meteordev.starscript.compiler.Compiler;
import org.meteordev.starscript.compiler.Parser;

import static org.junit.jupiter.api.Assertions.*;

public class StandardLibTest {

    private Starscript ss;

    @BeforeEach
    public void setUp() {
        ss = new Starscript();
        StandardLib.init(ss);
    }

    private String evaluate(String source) {
        Parser.Result result = Parser.parse(source);
        assertFalse(result.hasErrors(), "Parser should have no errors for: " + source);
        Script script = Compiler.compile(result);
        return ss.run(script).toString();
    }

    @Test
    public void testConstants() {
        assertEquals(String.valueOf(Math.PI), evaluate("{PI}"));
    }

    @Test
    public void testMathRound() {
        assertEquals("6", evaluate("{round(5.6)}"));
        assertEquals("5", evaluate("{round(5.4)}"));
        assertEquals("5.68", evaluate("{round(5.6789, 2)}"));
    }

    @Test
    public void testMathFloorCeilAbs() {
        assertEquals("5", evaluate("{floor(5.9)}"));
        assertEquals("6", evaluate("{ceil(5.1)}"));
        assertEquals("5.5", evaluate("{abs(-5.5)}"));
    }

    @Test
    public void testMathMinMaxClamp() {
        assertEquals("3", evaluate("{min(5, 3)}"));
        assertEquals("5", evaluate("{max(5, 3)}"));
        assertEquals("5", evaluate("{clamp(10, 1, 5)}"));
        assertEquals("3", evaluate("{clamp(2, 3, 7)}"));
    }

    @Test
    public void testMathPowSqrtSign() {
        assertEquals("8", evaluate("{pow(2, 3)}"));
        assertEquals("4", evaluate("{sqrt(16)}"));
        assertEquals("-1", evaluate("{sign(-5.5)}"));
        assertEquals("1", evaluate("{sign(5.5)}"));
        assertEquals("0", evaluate("{sign(0)}"));
    }

    @Test
    public void testStringFunctions() {
        assertEquals("TEST", evaluate("{toUpper(\"test\")}"));
        assertEquals("test", evaluate("{toLower(\"TEST\")}"));
        assertEquals("true", evaluate("{contains(\"hello world\", \"world\")}"));
        assertEquals("hello java", evaluate("{replace(\"hello world\", \"world\", \"java\")}"));
        assertEquals("  abc", evaluate("{pad(\"abc\", 5)}"));
    }

    @Test
    public void testExtendedStringFunctions() {
        assertEquals("3", evaluate("{length(\"abc\")}"));
        assertEquals("abc", evaluate("{trim(\"  abc  \")}"));
        assertEquals("ell", evaluate("{substring(\"hello\", 1, 4)}"));
        assertEquals("lo", evaluate("{substring(\"hello\", 3)}"));
    }

    @Test
    public void testFormatFunction() {
        assertEquals("Value: 42.000000", evaluate("{format(\"Value: %f\", 42)}"));
    }
}