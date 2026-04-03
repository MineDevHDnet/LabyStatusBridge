package net.minedevhd.labybridge.api;

@SuppressWarnings("serial")
public final class BridgeUnavailableException extends RuntimeException {

    public BridgeUnavailableException() {
        super("LabymodBridge API is not available.");
    }

    public BridgeUnavailableException(String message) {
        super(message);
    }
}