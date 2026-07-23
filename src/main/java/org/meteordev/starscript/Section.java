package org.meteordev.starscript;

public class Section {
    private static final StringBuilder SB = new StringBuilder();

    public final int index;
    public final String text;

    public Section next;

    public Section(int index, String text) {
        this.index = index;
        this.text = text;
    }

    @Override
    public String toString() {
        SB.setLength(0);

        Section s = this;
        while (s != null) {
            SB.append(s.text);
            s = s.next;
        }

        return SB.toString();
    }
}
