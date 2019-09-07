package shcm.shsupercm.data.utils;

import shcm.shsupercm.data.framework.DataSerializer;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Handles compression and decompression along with serialization and deserialization.
 */
public abstract class CompressionUtils {
    /**
     * Serialization and deserialization is done without any compression.
     */
    public static final CompressionUtils NONE = new CompressionUtils() {
        @Override
        public byte[] serialize(Object object) throws Exception {
            byte[] bytesOut;
            try(ByteArrayOutputStream bos = new ByteArrayOutputStream();
                DataOutputStream dos = new DataOutputStream(bos)) {
                DataSerializer.write(dos, object);
                bytesOut = bos.toByteArray();
            }
            return bytesOut;
        }

        @Override
        public Object deserialize(byte[] bytesIn) throws Exception {
            Object readObject;
            try(ByteArrayInputStream bis = new ByteArrayInputStream(bytesIn);
                DataInputStream dis = new DataInputStream(bis)) {
                readObject = DataSerializer.read(dis);
            }
            return readObject;
        }
    };

    /**
     * Compression is done using java's GZip stream algorithms.
     */
    public static final CompressionUtils GZIP = new CompressionUtils() {
        /**
         * Written by Vladislav Kysliy.
         */
        private byte[] gzipCompress(byte[] uncompressedData) {
            byte[] result = new byte[]{};
            try (ByteArrayOutputStream bos = new ByteArrayOutputStream(uncompressedData.length);
                 GZIPOutputStream gzipOS = new GZIPOutputStream(bos)) {
                gzipOS.write(uncompressedData);
                gzipOS.close();
                result = bos.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        /**
         * Written by Vladislav Kysliy.
         */
        private byte[] gzipUncompress(byte[] compressedData) {
            byte[] result = new byte[]{};
            try (ByteArrayInputStream bis = new ByteArrayInputStream(compressedData);
                 ByteArrayOutputStream bos = new ByteArrayOutputStream();
                 GZIPInputStream gzipIS = new GZIPInputStream(bis)) {
                byte[] buffer = new byte[1024];
                int len;
                while ((len = gzipIS.read(buffer)) != -1) {
                    bos.write(buffer, 0, len);
                }
                result = bos.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        public byte[] serialize(Object object) throws Exception {
            byte[] bytesOut;
            try(ByteArrayOutputStream bos = new ByteArrayOutputStream();
                DataOutputStream dos = new DataOutputStream(bos)) {
                DataSerializer.write(dos, object);
                bytesOut = bos.toByteArray();
            }
            return gzipCompress(bytesOut);
        }

        @Override
        public Object deserialize(byte[] bytesIn) throws Exception {
            Object readObject;
            try(ByteArrayInputStream bis = new ByteArrayInputStream(gzipUncompress(bytesIn));
                DataInputStream dis = new DataInputStream(bis)) {
                readObject = DataSerializer.read(dis);
            }
            return readObject;
        }
    };

    /**
     * Serializes the given object into a byte array using the compression algorithm.
     *
     * @param object the object to serialize.
     * @return the bytes of the object.
     */
    public abstract byte[] serialize(Object object) throws Exception;

    /**
     * Deserializes the given byte array into an object using the decompression algorithm.
     *
     * @param bytesIn the bytes of the object
     * @return the deserialized object.
     */
    public abstract Object deserialize(byte[] bytesIn) throws Exception;
}
