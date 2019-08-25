package shcm.shsupercm.data.framework;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * The main serializer responsible for translating between raw data and understandable data.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class DataSerializer {
    public static byte getByteForType(Object object) throws UnknownDataTypeException {
        if(object == null)
            throw new NullPointerException();

        if(object instanceof DataKeyedBlock)
            return -1;
        else if(object instanceof String)
            return 1;
        else if(object instanceof Boolean)
            return 2;
        else if(object instanceof Byte)
            return 3;
        else if(object instanceof Short)
            return 4;
        else if(object instanceof Character)
            return 5;
        else if(object instanceof Integer)
            return 6;
        else if(object instanceof Float)
            return 7;
        else if(object instanceof Long)
            return 8;
        else if(object instanceof Double)
            return 9;
        else if(object instanceof DataKeyedBlock[])
            return -2;
        else if(object instanceof String[])
            return 11;
        else if(object instanceof boolean[])
            return 12;
        else if(object instanceof byte[])
            return 13;
        else if(object instanceof short[])
            return 14;
        else if(object instanceof char[])
            return 15;
        else if(object instanceof int[])
            return 16;
        else if(object instanceof float[])
            return 17;
        else if(object instanceof long[])
            return 18;
        else if(object instanceof double[])
            return 19;

        throw new UnknownDataTypeException();
    }

    public static void write(DataOutput dataOut, DataKeyedBlock value) throws IOException, UnknownDataTypeException {
        dataOut.writeByte(-1);
        value.write(dataOut);
    }
    public static void write(DataOutput dataOut, String value) throws IOException {
        dataOut.writeByte(1);
        dataOut.writeUTF(value);
    }
    public static void write(DataOutput dataOut, Boolean value) throws IOException {
        dataOut.writeByte(2);
        dataOut.writeBoolean(value);
    }
    public static void write(DataOutput dataOut, Byte value) throws IOException {
        dataOut.writeByte(3);
        dataOut.writeByte(value);
    }
    public static void write(DataOutput dataOut, Short value) throws IOException {
        dataOut.writeByte(4);
        dataOut.writeShort(value);
    }
    public static void write(DataOutput dataOut, Character value) throws IOException {
        dataOut.writeByte(5);
        dataOut.writeChar(value);
    }
    public static void write(DataOutput dataOut, Integer value) throws IOException {
        dataOut.writeByte(6);
        dataOut.writeInt(value);
    }
    public static void write(DataOutput dataOut, Float value) throws IOException {
        dataOut.writeByte(7);
        dataOut.writeFloat(value);
    }
    public static void write(DataOutput dataOut, Long value) throws IOException {
        dataOut.writeByte(8);
        dataOut.writeLong(value);
    }
    public static void write(DataOutput dataOut, Double value) throws IOException {
        dataOut.writeByte(9);
        dataOut.writeDouble(value);
    }

    public static void write(DataOutput dataOut, DataKeyedBlock[] values) throws IOException, UnknownDataTypeException {
        dataOut.writeByte(-2);
        dataOut.writeInt(values.length);
        for(DataKeyedBlock value : values)
            value.write(dataOut);
    }
    public static void write(DataOutput dataOut, String[] values) throws IOException {
        dataOut.writeByte(11);
        dataOut.writeInt(values.length);
        for(String value : values)
            dataOut.writeUTF(value);
    }
    public static void write(DataOutput dataOut, boolean[] values) throws IOException {
        dataOut.writeByte(12);
        dataOut.writeInt(values.length);
        for(boolean value : values)
            dataOut.writeBoolean(value);
    }
    public static void write(DataOutput dataOut, byte[] values) throws IOException {
        dataOut.writeByte(13);
        dataOut.writeInt(values.length);
        for(byte value : values)
            dataOut.writeByte(value);
    }
    public static void write(DataOutput dataOut, short[] values) throws IOException {
        dataOut.writeByte(14);
        dataOut.writeInt(values.length);
        for(short value : values)
            dataOut.writeShort(value);
    }
    public static void write(DataOutput dataOut, char[] values) throws IOException {
        dataOut.writeByte(15);
        dataOut.writeInt(values.length);
        for(char value : values)
            dataOut.writeChar(value);
    }
    public static void write(DataOutput dataOut, int[] values) throws IOException {
        dataOut.writeByte(16);
        dataOut.writeInt(values.length);
        for(int value : values)
            dataOut.writeInt(value);
    }
    public static void write(DataOutput dataOut, float[] values) throws IOException {
        dataOut.writeByte(17);
        dataOut.writeInt(values.length);
        for(float value : values)
            dataOut.writeFloat(value);
    }
    public static void write(DataOutput dataOut, long[] values) throws IOException {
        dataOut.writeByte(18);
        dataOut.writeInt(values.length);
        for(long value : values)
            dataOut.writeLong(value);
    }
    public static void write(DataOutput dataOut, double[] values) throws IOException {
        dataOut.writeByte(19);
        dataOut.writeInt(values.length);
        for(double value : values)
            dataOut.writeDouble(value);
    }

    public static void write(DataOutput dataOut, Object value) throws UnknownDataTypeException, IOException {
        if(value == null)
            throw new NullPointerException();

        if(value instanceof DataKeyedBlock)
            write(dataOut, (DataKeyedBlock) value);
        else if(value instanceof String)
            write(dataOut, (String) value);
        else if(value instanceof Boolean)
            write(dataOut, (Boolean) value);
        else if(value instanceof Byte)
            write(dataOut, (Byte) value);
        else if(value instanceof Short)
            write(dataOut, (Short) value);
        else if(value instanceof Character)
            write(dataOut, (Character) value);
        else if(value instanceof Integer)
            write(dataOut, (Integer) value);
        else if(value instanceof Float)
            write(dataOut, (Float) value);
        else if(value instanceof Long)
            write(dataOut, (Long) value);
        else if(value instanceof Double)
            write(dataOut, (Double) value);
        else if(value instanceof DataKeyedBlock[])
            write(dataOut, (DataKeyedBlock[]) value);
        else if(value instanceof String[])
            write(dataOut, (String[]) value);
        else if(value instanceof boolean[])
            write(dataOut, (boolean[]) value);
        else if(value instanceof byte[])
            write(dataOut, (byte[]) value);
        else if(value instanceof short[])
            write(dataOut, (short[]) value);
        else if(value instanceof char[])
            write(dataOut, (char[]) value);
        else if(value instanceof int[])
            write(dataOut, (int[]) value);
        else if(value instanceof float[])
            write(dataOut, (float[]) value);
        else if(value instanceof long[])
            write(dataOut, (long[]) value);
        else if(value instanceof double[])
            write(dataOut, (double[]) value);
        else
            throw new UnknownDataTypeException();
    }

    public static Object read(DataInput dataIn) throws IOException, UnexpectedByteException {
        return read(dataIn.readByte(), dataIn);
    }

    public static Object read(byte type, DataInput dataIn) throws IOException, UnexpectedByteException {
        switch (type) {
            case -1:
                return DataKeyedBlock.read(dataIn);
            case 1:
                return dataIn.readUTF();
            case 2:
                return dataIn.readBoolean();
            case 3:
                return dataIn.readByte();
            case 4:
                return dataIn.readShort();
            case 5:
                return dataIn.readChar();
            case 6:
                return dataIn.readInt();
            case 7:
                return dataIn.readFloat();
            case 8:
                return dataIn.readLong();
            case 9:
                return dataIn.readDouble();

            case -2: {
                DataKeyedBlock[] values = new DataKeyedBlock[dataIn.readInt()];
                for (int i = 0; i < values.length; i++)
                    values[i] = (DataKeyedBlock)read((byte)-1, dataIn);
                return values;
            }
            case 11: {
                String[] values = new String[dataIn.readInt()];
                for (int i = 0; i < values.length; i++)
                    values[i] = (String)read((byte)1, dataIn);
                return values;
            }
            case 12: {
                boolean[] values = new boolean[dataIn.readInt()];
                for (int i = 0; i < values.length; i++)
                    values[i] = (boolean)read((byte)2, dataIn);
                return values;
            }
            case 13: {
                byte[] values = new byte[dataIn.readInt()];
                for (int i = 0; i < values.length; i++)
                    values[i] = (byte)read((byte)3, dataIn);
                return values;
            }
            case 14: {
                short[] values = new short[dataIn.readInt()];
                for (int i = 0; i < values.length; i++)
                    values[i] = (short)read((byte)4, dataIn);
                return values;
            }
            case 15: {
                char[] values = new char[dataIn.readInt()];
                for (int i = 0; i < values.length; i++)
                    values[i] = (char)read((byte)5, dataIn);
                return values;
            }
            case 16: {
                int[] values = new int[dataIn.readInt()];
                for (int i = 0; i < values.length; i++)
                    values[i] = (int)read((byte)6, dataIn);
                return values;
            }
            case 17: {
                float[] values = new float[dataIn.readInt()];
                for (int i = 0; i < values.length; i++)
                    values[i] = (float)read((byte)7, dataIn);
                return values;
            }
            case 18: {
                long[] values = new long[dataIn.readInt()];
                for (int i = 0; i < values.length; i++)
                    values[i] = (long)read((byte)8, dataIn);
                return values;
            }
            case 19: {
                double[] values = new double[dataIn.readInt()];
                for (int i = 0; i < values.length; i++)
                    values[i] = (double)read((byte)9, dataIn);
                return values;
            }
        }

        throw new UnexpectedByteException();
    }

    public static class UnexpectedByteException extends Exception {}
    public static class UnknownDataTypeException extends Exception {}
}
