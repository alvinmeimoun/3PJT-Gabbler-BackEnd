package com.supinfo.gabbler.server.exception;

/**
 * @author Alvin Meimoun
 */
public abstract class AbstractApiException extends Exception {

    protected String ihmMessage;
    protected String level = "danger";

    public AbstractApiException() {
        super();
    }

    public AbstractApiException(String ihmMessage) {
        this.ihmMessage = ihmMessage;
    }

    public String getIhmMessage() {
        return ihmMessage;
    }

    public AbstractApiException setIhmMessage(String message) {
        this.ihmMessage = message;
        return this;
    }

    public String getLevel() {
        return level;
    }

    public AbstractApiException setLevel(String level) {
        this.level = level;
        return this;
    }
}
