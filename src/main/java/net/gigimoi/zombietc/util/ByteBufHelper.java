package net.gigimoi.zombietc.util;

import io.netty.buffer.ByteBuf;

/**
 * Created by gigimoi on 7/26/2014.
 */
public class ByteBufHelper {
    public static String readString(ByteBuf buf) {
        int length = buf.readInt();
        String out = "";
        for (int i = 0; i < length; i++) {
            out += buf.readChar();
        }
        return out;
    }

    public static void writeString(String s, ByteBuf buf) {
        buf.writeInt(s.length());
        for (int i = 0; i < s.length(); i++) {
            buf.writeChar(s.charAt(i));
        }
    }
}
