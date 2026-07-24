package org.meteordev.starscript;

import org.meteordev.starscript.value.Value;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.IllegalFormatException;
import java.util.Random;
import java.util.TimeZone;

/**
 * Standard library with some default functions and variables.
 */
public class StandardLib {
    private static final Random rand = new Random();

    public static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd. MM. yyyy");

    /**
     * Adds the functions and variables to the provided {@link Starscript} instance.
     */
    public static void init(Starscript ss) {
        // Variables
        ss.set("PI", Math.PI);
        ss.set("time", () -> Value.string(timeFormat.format(new Date())));
        ss.set("date", () -> Value.string(dateFormat.format(new Date())));

        // Numbers
        ss.set("round", StandardLib::round);
        ss.set("roundToString", StandardLib::roundToString);
        ss.set("floor", StandardLib::floor);
        ss.set("ceil", StandardLib::ceil);
        ss.set("abs", StandardLib::abs);
        ss.set("min", StandardLib::min);
        ss.set("max", StandardLib::max);
        ss.set("clamp", StandardLib::clamp);
        ss.set("random", StandardLib::random);
        ss.set("pow", StandardLib::pow);
        ss.set("sqrt", StandardLib::sqrt);
        ss.set("sign", StandardLib::sign);

        // Strings
        ss.set("string", StandardLib::string);
        ss.set("toUpper", StandardLib::toUpper);
        ss.set("toLower", StandardLib::toLower);
        ss.set("contains", StandardLib::contains);
        ss.set("replace", StandardLib::replace);
        ss.set("pad", StandardLib::pad);
        ss.set("formatDateTime", StandardLib::formatDateTime);
        ss.set("format", StandardLib::format);
        ss.set("length", StandardLib::length);
        ss.set("trim", StandardLib::trim);
        ss.set("substring", StandardLib::substring);
    }

    // Numbers

    public static Value round(Starscript ss, int argCount) {
        if (argCount == 1) {
            double a = ss.popNumber("Argument to round() needs to be a number.");
            return Value.number(Math.round(a));
        } else if (argCount == 2) {
            double b = ss.popNumber("Second argument to round() needs to be a number.");
            double a = ss.popNumber("First argument to round() needs to be a number.");

            double x = Math.pow(10, (int) b);
            return Value.number(Math.round(a * x) / x);
        } else {
            ss.error("round() requires 1 or 2 arguments, got %d.", argCount);
            return null;
        }
    }

    public static Value roundToString(Starscript ss, int argCount) {
        if (argCount == 1) {
            double a = ss.popNumber("Argument to round() needs to be a number.");
            return Value.string(Double.toString(Math.round(a)));
        } else if (argCount == 2) {
            double b = ss.popNumber("Second argument to round() needs to be a number.");
            double a = ss.popNumber("First argument to round() needs to be a number.");

            double x = Math.pow(10, (int) b);
            return Value.string(Double.toString(Math.round(a * x) / x));
        } else {
            ss.error("round() requires 1 or 2 arguments, got %d.", argCount);
            return null;
        }
    }

    public static Value floor(Starscript ss, int argCount) {
        if (argCount != 1) ss.error("floor() requires 1 argument, got %d.", argCount);
        double a = ss.popNumber("Argument to floor() needs to be a number.");
        return Value.number(Math.floor(a));
    }

    public static Value ceil(Starscript ss, int argCount) {
        if (argCount != 1) ss.error("ceil() requires 1 argument, got %d.", argCount);
        double a = ss.popNumber("Argument to ceil() needs to be a number.");
        return Value.number(Math.ceil(a));
    }

    public static Value abs(Starscript ss, int argCount) {
        if (argCount != 1) ss.error("abs() requires 1 argument, got %d.", argCount);
        double a = ss.popNumber("Argument to abs() needs to be a number.");
        return Value.number(Math.abs(a));
    }

    public static Value min(Starscript ss, int argCount) {
        if (argCount != 2) ss.error("min() requires 2 arguments, got %d.", argCount);
        double b = ss.popNumber("Second argument to min() needs to be a number.");
        double a = ss.popNumber("First argument to min() needs to be a number.");
        return Value.number(Math.min(a, b));
    }

    public static Value max(Starscript ss, int argCount) {
        if (argCount != 2) ss.error("max() requires 2 arguments, got %d.", argCount);
        double b = ss.popNumber("Second argument to max() needs to be a number.");
        double a = ss.popNumber("First argument to max() needs to be a number.");
        return Value.number(Math.max(a, b));
    }

    public static Value clamp(Starscript ss, int argCount) {
        if (argCount != 3) ss.error("clamp() requires 3 arguments, got %d.", argCount);
        double c = ss.popNumber("Third argument to clamp() needs to be a number.");
        double b = ss.popNumber("Second argument to clamp() needs to be a number.");
        double a = ss.popNumber("First argument to clamp() needs to be a number.");
        return Value.number(Math.max(b, Math.min(c, a)));
    }

    public static Value random(Starscript ss, int argCount) {
        if (argCount == 0) return Value.number(rand.nextDouble());
        else if (argCount == 2) {
            double max = ss.popNumber("Second argument to random() needs to be a number.");
            double min = ss.popNumber("First argument to random() needs to be a number.");

            return Value.number(min + (max - min) * rand.nextDouble());
        }

        ss.error("random() requires 0 or 2 arguments, got %d.", argCount);
        return Value.null_();
    }

    public static Value pow(Starscript ss, int argCount) {
        if (argCount != 2) ss.error("pow() requires 2 arguments, got %d.", argCount);
        double exp = ss.popNumber("Second argument to pow() needs to be a number.");
        double base = ss.popNumber("First argument to pow() needs to be a number.");
        return Value.number(Math.pow(base, exp));
    }

    public static Value sqrt(Starscript ss, int argCount) {
        if (argCount != 1) ss.error("sqrt() requires 1 argument, got %d.", argCount);
        double a = ss.popNumber("Argument to sqrt() needs to be a number.");
        return Value.number(Math.sqrt(a));
    }

    public static Value sign(Starscript ss, int argCount) {
        if (argCount != 1) ss.error("sign() requires 1 argument, got %d.", argCount);
        double a = ss.popNumber("Argument to sign() needs to be a number.");
        return Value.number(Math.signum(a));
    }

    // Strings

    private static Value string(Starscript ss, int argCount) {
        if (argCount != 1) ss.error("string() requires 1 argument, got %d.", argCount);
        return Value.string(ss.pop().toString());
    }

    public static Value toUpper(Starscript ss, int argCount) {
        if (argCount != 1) ss.error("toUpper() requires 1 argument, got %d.", argCount);
        String a = ss.popString("Argument to toUpper() needs to be a string.");
        return Value.string(a.toUpperCase());
    }

    public static Value toLower(Starscript ss, int argCount) {
        if (argCount != 1) ss.error("toLower() requires 1 argument, got %d.", argCount);
        String a = ss.popString("Argument to toLower() needs to be a string.");
        return Value.string(a.toLowerCase());
    }

    public static Value contains(Starscript ss, int argCount) {
        if (argCount != 2) ss.error("replace() requires 2 arguments, got %d.", argCount);

        String search = ss.popString("Second argument to contains() needs to be a string.");
        String string = ss.popString("First argument to contains() needs to be a string.");

        return Value.bool(string.contains(search));
    }

    public static Value replace(Starscript ss, int argCount) {
        if (argCount != 3) ss.error("replace() requires 3 arguments, got %d.", argCount);

        String to = ss.popString("Third argument to replace() needs to be a string.");
        String from = ss.popString("Second argument to replace() needs to be a string.");
        String string = ss.popString("First argument to replace() needs to be a string.");

        return Value.string(string.replace(from, to));
    }

    public static Value pad(Starscript ss, int argCount) {
        if (argCount != 2) ss.error("pad() requires 2 arguments, got %d.", argCount);

        int width = (int) ss.popNumber("Second argument to pad() needs to be a number.");
        String text = ss.pop().toString();

        if (text.length() >= Math.abs(width)) return Value.string(text);

        char[] padded = new char[Math.max(text.length(), Math.abs(width))];

        if (width >= 0) {
            int padLength = width - text.length();
            for (int i = 0; i < padLength; i++) padded[i] = ' ';
            for (int i = 0; i < text.length(); i++) padded[padLength + i] = text.charAt(i);
        } else {
            for (int i = 0; i < text.length(); i++) padded[i] = text.charAt(i);
            for (int i = 0; i < Math.abs(width) - text.length(); i++) padded[text.length() + i] = ' ';
        }

        return Value.string(new String(padded));
    }

    public static Value length(Starscript ss, int argCount) {
        if (argCount != 1) ss.error("length() requires 1 argument, got %d.", argCount);
        String a = ss.popString("Argument to length() needs to be a string.");
        return Value.number(a.length());
    }

    public static Value trim(Starscript ss, int argCount) {
        if (argCount != 1) ss.error("trim() requires 1 argument, got %d.", argCount);
        String a = ss.popString("Argument to trim() needs to be a string.");
        return Value.string(a.trim());
    }

    public static Value substring(Starscript ss, int argCount) {
        if (argCount == 2) {
            double start = ss.popNumber("Second argument to substring() needs to be a number.");
            String str = ss.popString("First argument to substring() needs to be a string.");
            int beginIndex = Math.max(0, (int) start);
            if (beginIndex >= str.length()) return Value.string("");
            return Value.string(str.substring(beginIndex));
        } else if (argCount == 3) {
            double end = ss.popNumber("Third argument to substring() needs to be a number.");
            double start = ss.popNumber("Second argument to substring() needs to be a number.");
            String str = ss.popString("First argument to substring() needs to be a string.");

            int beginIndex = Math.max(0, (int) start);
            int endIndex = Math.min(str.length(), (int) end);
            if (beginIndex > endIndex) return Value.string("");
            return Value.string(str.substring(beginIndex, endIndex));
        } else {
            ss.error("substring() requires 2 or 3 arguments, got %d.", argCount);
            return null;
        }
    }

    // Formatters

    public static Value formatDateTime(Starscript ss, int argCount) {
        if (argCount < 1 || argCount > 2)
            ss.error("formatTime(fmt, timezone) requires 1 to 2 arguments, got %d.", argCount);
        try {
            String timeZone = null;
            if (argCount == 2) timeZone = ss.popString("Argument to formatTime(fmt, timezone) needs to be a string.");
            String fmt = ss.popString("Argument to formatTime(fmt, timezone) needs to be a string.");
            SimpleDateFormat formatter = new SimpleDateFormat(fmt);
            if (timeZone != null && !timeZone.isEmpty()) formatter.setTimeZone(TimeZone.getTimeZone(timeZone));
            return Value.string(formatter.format(new Date()));
        } catch (IllegalArgumentException e) {
            ss.error(e.toString());
        }
        return Value.null_();
    }

    public static Value format(Starscript ss, int argCount) {
        if (argCount < 1) ss.error("format(fmt, ...args) requires at least 1 argument, got %d.", argCount);
        Object[] args = new Object[argCount - 1];
        for (int i = argCount - 2; i >= 0; i--) {
            Value v = ss.pop();
            Object o;
            switch (v.type) {
                case Boolean: o = v.getBool(); break;
                case Number: o = v.getNumber(); break;
                case String: o = v.getString(); break;
                case Function: o = v.getFunction(); break;
                case Map: o = v.getMap(); break;
                case Null:
                default: o = null; break;
            }
            args[i] = o;
        }
        String fmt = ss.popString("Argument `fmt` to format() needs to be a string.");
        try {
            return Value.string(String.format(fmt, args));
        }
        catch (IllegalFormatException e) {
            ss.error(e.toString());
        }
        return Value.null_();
    }

}
