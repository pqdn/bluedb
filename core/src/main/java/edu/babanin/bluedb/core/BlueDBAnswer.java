package edu.babanin.bluedb.core;

public final class BlueDBAnswer {
    private final boolean isSuccsecc;
    private final Object answer;
    private final Exception exception;

    public BlueDBAnswer(boolean isSuccsecc, Object answer, Exception e) {
        this.isSuccsecc = isSuccsecc;
        this.answer = answer;
        this.exception = e;
    }

    public BlueDBAnswer(boolean isSuccsecc, Object answer) {
        this(isSuccsecc, answer, null);
    }

    public BlueDBAnswer(Exception e) {
        this(false, null, e);
    }

    public boolean isSuccsecc() {
        return isSuccsecc;
    }

    public Object getAnswer() {
        return answer;
    }

    public Exception getException() {
        return exception;
    }
}
