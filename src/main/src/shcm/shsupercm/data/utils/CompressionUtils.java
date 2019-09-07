package shcm.shsupercm.data.utils;

import shcm.shsupercm.data.framework.DataSerializer;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Handles compression and decompression along with serialization and deserialization.
 */
public abstract class CompressionUtils {
    public static final CompressionUtils NONE = new CompressionUtils() {
        public byte[] serialize(Object object) throws Exception {
            byte[] bytesOut;
            try(ByteArrayOutputStream bos = new ByteArrayOutputStream();
                DataOutputStream dos = new DataOutputStream(bos)) {
                DataSerializer.write(dos, object);
                bytesOut = bos.toByteArray();
            } catch (IOException | DataSerializer.UnknownDataTypeException e) {
                throw e;
            }
            return bytesOut;
        }

        public Object deserialize(byte[] bytesIn) throws Exception {
            Object readObject;
            try(ByteArrayInputStream bis = new ByteArrayInputStream(bytesIn);
                DataInputStream dis = new DataInputStream(bis)) {
                readObject = DataSerializer.read(dis);
            } catch (IOException | DataSerializer.UnexpectedByteException e) {
                throw e;
            }
            return readObject;
        }
    };

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

        public byte[] serialize(Object object) throws Exception {
            byte[] bytesOut;
            try(ByteArrayOutputStream bos = new ByteArrayOutputStream();
                DataOutputStream dos = new DataOutputStream(bos)) {
                DataSerializer.write(dos, object);
                bytesOut = bos.toByteArray();
            } catch (IOException | DataSerializer.UnknownDataTypeException e) {
                throw e;
            }
            return gzipCompress(bytesOut);
        }

        public Object deserialize(byte[] bytesIn) throws Exception {
            Object readObject;
            try(ByteArrayInputStream bis = new ByteArrayInputStream(gzipUncompress(bytesIn));
                DataInputStream dis = new DataInputStream(bis)) {
                readObject = DataSerializer.read(dis);
            } catch (IOException | DataSerializer.UnexpectedByteException e) {
                throw e;
            }
            return readObject;
        }
    };

    public abstract byte[] serialize(Object object) throws Exception;

    public abstract Object deserialize(byte[] bytesIn) throws Exception;
}
