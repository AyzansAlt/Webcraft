package net.discraft.mod.module.hypixel.api.util;

import net.discraft.mod.module.hypixel.api.reply.AbstractReply;

@FunctionalInterface
public interface Callback<T extends AbstractReply> {

    void callback(Throwable failCause, T result);
}
