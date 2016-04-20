package com.dempe.ocean.rpc.utils;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Type;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * 参数解码类
 * User: Dempe
 * Date: 2016/4/20
 * Time: 19:55
 * To change this template use File | Settings | File Templates.
 */
public class ArgsCodec {

    public static byte[] encodeArgs(Object[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(512);
        for (Object arg : args) {
            if (arg instanceof String) {
                String value = (String) arg;
                byte[] bytes = value.getBytes();
                short len = (short) bytes.length;
                buffer = ensureCapacity(buffer, len + 2);
                buffer.putShort(len);
                buffer.put(bytes);
            } else if (arg instanceof Integer) {
                Integer value = (Integer) arg;
                buffer = ensureCapacity(buffer, 4);
                buffer.putInt(value);
            } else if (arg instanceof Short) {
                Short value = (Short) arg;
                buffer = ensureCapacity(buffer, 2);
                buffer.putShort(value);
            } else if (arg instanceof Long) {
                Long value = (Long) arg;
                buffer = ensureCapacity(buffer, 8);
                buffer.putLong(value);
            } else if (arg instanceof Double) {
                Double value = (Double) arg;
                buffer = ensureCapacity(buffer, 8);
                buffer.putDouble(value);
            } else if (arg instanceof Float) {
                Float value = (Float) arg;
                buffer = ensureCapacity(buffer, 4);
                buffer.putFloat(value);
            }
        }
        // encode args
        int position = buffer.position();
        byte data[] = new byte[position];
        buffer.get(data);
        return data;

    }

    public static ByteBuffer ensureCapacity(ByteBuffer buffer, int increament)
            throws BufferOverflowException {
        if (buffer.remaining() >= increament) {
            return buffer;
        }
        int requiredCapacity = buffer.capacity() + increament
                - buffer.remaining();
        if (requiredCapacity > 2 * 1024 * 1024) {
            throw new BufferOverflowException();
        }
        int tmp = Math.max(requiredCapacity, buffer.capacity() * 2);
        int newCapacity = Math.min(tmp, 2 * 1024 * 1024);

        ByteBuffer newBuffer = ByteBuffer.allocate(newCapacity);
        newBuffer.order(buffer.order());
        buffer.flip();
        newBuffer.put(buffer);
        return newBuffer;
    }

    /**
     * 解码方法参数
     *
     * @param data
     * @param type
     * @return
     */
    public static Object[] decodeArgs(byte[] data, Type[] type) {
        ByteBuffer buffer = ByteBuffer.wrap(data);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        Object[] args = new Object[type.length];
        for (int i = 0; i < type.length; i++) {
            if (Integer.class == type[i] || StringUtils.equals(type[i].toString(), "int")) {
                args[i] = buffer.getInt();
            } else if (String.class == type[i]) {
                short size = buffer.getShort();
                byte[] arr = new byte[size];
                buffer.get(arr);
                args[i] = new String(arr);
            } else if (Boolean.class == type[i] || StringUtils.equals(type[i].toString(), "boolean")) {
                args[i] = buffer.get() > 0;
            } else if (Long.class == type[i] || StringUtils.equals(type[i].toString(), "long")) {
                args[i] = buffer.getLong();
            } else if (Short.class == type[i] || StringUtils.equals(type[i].toString(), "short")) {
                args[i] = buffer.getShort();
            } else if (Double.class == type[i] || StringUtils.equals(type[i].toString(), "double")) {
                args[i] = buffer.getDouble();
            } else if (Float.class == type[i] || StringUtils.equals(type[i].toString(), "float")) {
                args[i] = buffer.getFloat();
            }
        }
        return args;
    }


}
