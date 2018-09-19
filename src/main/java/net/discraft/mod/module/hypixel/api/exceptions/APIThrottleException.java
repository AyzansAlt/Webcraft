package net.discraft.mod.module.hypixel.api.exceptions;

public class APIThrottleException extends HypixelAPIException {
    public APIThrottleException() {
        super("You have passed the API throttle limit!");
    }
}
