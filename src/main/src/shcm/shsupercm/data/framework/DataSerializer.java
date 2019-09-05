package shcm.shsupercm.data.framework;

import shcm.shsupercm.data.data.DataRegistry;
import shcm.shsupercm.data.data.IData;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * The main serializer responsible for translating between raw bytes and SHCMData.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class DataSerializer {

    //Write IDatas in here instead of DataKeyedBlock
    //Also run DataAnnotationRegistry if it hasn't ran already
    /**
     * Retrieves the id for the class type.
     * @param type the class to work with.
     * @return the id for the class type.
     * @throws UnknownDataTypeException when there is no registered id for the specified class type.
     */
    public static byte getByteForType(Class<?> type) throws UnknownDataTypeException {
        if(type == null)
            throw new NullPointerException();

        if(!type.isArray()) {
            if (type.equals(DataKeyedBlock.class))
                return -1;
            else if (type.equals(String.class))
                return 1;
            else if (type.equals(Boolean.class))
                return 2;
            else if (type.equals(Byte.class))
                return 3;
            else if (type.equals(Short.class))
                return 4;
            else if (type.equals(Character.class))
                return 5;
            else if (type.equals(Integer.class))
                return 6;
            else if (type.equals(Float.class))
                return 7;
            else if (type.equals(Long.class))
                return 8;
            else if (type.equals(Double.class))
                return 9;
            else if (IData.class.isAssignableFrom(type))
                return -1;
        } else {
            if (type.equals(DataBlock[].class))
                return -3;
            else if (type.equals(DataKeyedBlock[].class))
                return -2;
            else if (type.equals(String[].class))
                return 11;
            else if (type.equals(boolean[].class))
                return 12;
            else if (type.equals(byte[].class))
                return 13;
            else if (type.equals(short[].class))
                return 14;
            else if (type.equals(char[].class))
                return 15;
            else if (type.equals(int[].class))
                return 16;
            else if (type.equals(float[].class))
                return 17;
            else if (type.equals(long[].class))
                return 18;
            else if (type.equals(double[].class))
                return 19;
            else if (IData.class.isAssignableFrom(type.getComponentType()))
                return -3;
        }

        throw new UnknownDataTypeException();
    }

    /**
     * Writes {@code value} to {@code dataOut} along with its id.
     */
    public static void write(DataOutput dataOut, DataKeyedBlock value) throws IOException, UnknownDataTypeException {
        dataOut.writeByte(-1);
        value.write(dataOut);
    }
    /**
     * Writes {@code value} to {@code dataOut} along with its id.
     */
    public static void write(DataOutput dataOut, IData value) throws IOException, UnknownDataTypeException {
        write(dataOut, DataRegistry.write(new DataBlock(), value));
    }
    /**
     * Writes {@code value} to {@code dataOut} along with its id.
     */
    public static void write(DataOutput dataOut, String value) throws IOException {
        dataOut.writeByte(1);
        dataOut.writeUTF(value);
    }
    /**
     * Writes {@code value} to {@code dataOut} along with its id.
     */
    public static void write(DataOutput dataOut, Boolean value) throws IOException {
        dataOut.writeByte(2);
        dataOut.writeBoolean(value);
    }
    /**
     * Writes {@code value} to {@code dataOut} along with its id.
     */
    public static void write(DataOutput dataOut, Byte value) throws IOException {
        dataOut.writeByte(3);
        dataOut.writeByte(value);
    }
    /**
     * Writes {@code value} to {@code dataOut} along with its id.
     */
    public static void write(DataOutput dataOut, Short value) throws IOException {
        dataOut.writeByte(4);
        dataOut.writeShort(value);
    }
    /**
     * Writes {@code value} to {@code dataOut} along with its id.
     */
    public static void write(DataOutput dataOut, Character value) throws IOException {
        dataOut.writeByte(5);
        dataOut.writeChar(value);
    }
    /**
     * Writes {@code value} to {@code dataOut} along with its id.
     */
    public static void write(DataOutput dataOut, Integer value) throws IOException {
        dataOut.writeByte(6);
        dataOut.writeInt(value);
    }
    /**
     * Writes {@code value} to {@code dataOut} along with its id.
     */
    public static void write(DataOutput dataOut, Float value) throws IOException {
        dataOut.writeByte(7);
        dataOut.writeFloat(value);
    }
    /**
     * Writes {@code value} to {@code dataOut} along with its id.
     */
    public static void write(DataOutput dataOut, Long value) throws IOException {
        dataOut.writeByte(8);
        dataOut.writeLong(value);
    }
    /**
     * Writes {@code value} to {@code dataOut} along with its id.
     */
    public static void write(DataOutput dataOut, Double value) throws IOException {
        dataOut.writeByte(9);
        dataOut.writeDouble(value);
    }

    /**
     * Writes {@code value} to {@code dataOut} along with its id.
     */
    public static void write(DataOutput dataOut, DataBlock[] values) throws IOException, UnknownDataTypeException {
        dataOut.writeByte(-3);
        dataOut.writeInt(values.length);
        for(DataBlock value : values)
            value.write(dataOut);
    }
    /**
     * Writes {@code value} to {@code dataOut} along with its id.
     */
    public static void write(DataOutput dataOut, IData[] values) throws IOException, UnknownDataTypeException {
        dataOut.writeByte(-3);
        dataOut.writeInt(values.length);
        for(IData value : values)
            value.write(DataRegistry.write(new DataBlock(), value));
    }
    /**
     * Writes {@code value} to {@code dataOut} along with its id.
     */
    public static void write(DataOutput dataOut, DataKeyedBlock[] values) throws IOException, UnknownDataTypeException {
        dataOut.writeByte(-2);
        dataOut.writeInt(values.length);
        for(DataKeyedBlock value : values)
            value.write(dataOut);
    }
    /**
     * Writes {@code value} to {@code dataOut} along with its id.
     */
    public static void write(DataOutput dataOut, String[] values) throws IOException {
        dataOut.writeByte(11);
        dataOut.writeInt(values.length);
        for(String value : values)
            dataOut.writeUTF(value);
    }
    /**
     * Writes {@code value} to {@code dataOut} along with its id.
     */
    public static void write(DataOutput dataOut, boolean[] values) throws IOException {
        dataOut.writeByte(12);
        dataOut.writeInt(values.length);
        for(boolean value : values)
            dataOut.writeBoolean(value);
    }
    /**
     * Writes {@code value} to {@code dataOut} along with its id.
     */
    public static void write(DataOutput dataOut, byte[] values) throws IOException {
        dataOut.writeByte(13);
        dataOut.writeInt(values.length);
        for(byte value : values)
            dataOut.writeByte(value);
    }
    /**
     * Writes {@code value} to {@code dataOut} along with its id.
     */
    public static void write(DataOutput dataOut, short[] values) throws IOException {
        dataOut.writeByte(14);
        dataOut.writeInt(values.length);
        for(short value : values)
            dataOut.writeShort(value);
    }
    /**
     * Writes {@code value} to {@code dataOut} along with its id.
     */
    public static void write(DataOutput dataOut, char[] values) throws IOException {
        dataOut.writeByte(15);
        dataOut.writeInt(values.length);
        for(char value : values)
            dataOut.writeChar(value);
    }
    /**
     * Writes {@code value} to {@code dataOut} along with its id.
     */
    public static void write(DataOutput dataOut, int[] values) throws IOException {
        dataOut.writeByte(16);
        dataOut.writeInt(values.length);
        for(int value : values)
            dataOut.writeInt(value);
    }
    /**
     * Writes {@code value} to {@code dataOut} along with its id.
     */
    public static void write(DataOutput dataOut, float[] values) throws IOException {
        dataOut.writeByte(17);
        dataOut.writeInt(values.length);
        for(float value : values)
            dataOut.writeFloat(value);
    }
    /**
     * Writes {@code value} to {@code dataOut} along with its id.
     */
    public static void write(DataOutput dataOut, long[] values) throws IOException {
        dataOut.writeByte(18);
        dataOut.writeInt(values.length);
        for(long value : values)
            dataOut.writeLong(value);
    }
    /**
     * Writes {@code value} to {@code dataOut} along with its id.
     */
    public static void write(DataOutput dataOut, double[] values) throws IOException {
        dataOut.writeByte(19);
        dataOut.writeInt(values.length);
        for(double value : values)
            dataOut.writeDouble(value);
    }

    /**
     * Writes {@code value} to {@code dataOut} along with its id.
     */
    public static void write(DataOutput dataOut, Object value) throws UnknownDataTypeException, IOException {
        if(value == null)
            return;

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
        else if(value instanceof DataBlock[])
            write(dataOut, (DataBlock[]) value);
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
        else if(value instanceof IData)
            write(dataOut, (IData) value);
        else if(value instanceof IData[])
            write(dataOut, (IData[]) value);//todo test if array components work with instanceof
        else
            throw new UnknownDataTypeException();
    }

    /**
     * Identifies a data type(reading the first byte) and reads the object from {@code dataIn}.
     */
    public static Object read(DataInput dataIn) throws IOException, UnexpectedByteException {
        return read(dataIn.readByte(), dataIn);
    }

    /**
     * Reads the object from {@code dataIn} based on a given data type.
     */
    public static Object read(byte type, DataInput dataIn) throws IOException, UnexpectedByteException {
        switch (type) {
            case -1:
                DataKeyedBlock object = DataKeyedBlock.read(dataIn);

                //test for IData
                if(object instanceof DataBlock && ((DataBlock)object).get(DataRegistry.DATA_ID_IDENTIFIER) != null)
                    return DataRegistry.read((DataBlock) object);

                return object;
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

            case -3: {
                DataBlock[] values = new DataBlock[dataIn.readInt()];
                for (int i = 0; i < values.length; i++)
                    values[i] = (DataBlock) read((byte)-1, dataIn);
                return values;
            }
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

    /**
     * Thrown when an unexpected byte is encountered mid reading.
     */
    public static class UnexpectedByteException extends Exception {}

    /**
     * Thrown when an unknown data type is trying to pass through serialization/deserialization.
     */
    public static class UnknownDataTypeException extends Exception {}
}
