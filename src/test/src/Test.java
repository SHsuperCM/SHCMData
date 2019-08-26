import shcm.shsupercm.data.framework.DataSerializer;
import shcm.shsupercm.data.framework.DataBlock;
import shcm.shsupercm.data.framework.DataKeyedBlock;

import java.io.*;
import java.util.Arrays;

public class Test {
    public static void main(String[] args) throws IOException, DataSerializer.UnknownDataTypeException, DataSerializer.UnexpectedByteException {
        DataBlock original = new DataBlock()
                .set("where", new DataKeyedBlock<>('\u0000')
                    .set('x', -45)
                    .set('y', 53)
                    .set('z', 23))
                .set("what", "Just a thing, nothing really important tbh...")
                .set("byte_shit", new byte[]{0,48,-18,34,127,0,0,88,34,34,34,34,15,1})
                .set("storage", new DataBlock()
                    .set("example_blocks", new DataBlock[] {
                        new DataBlock()
                            .set("test1", "lolzXD")
                            .set("test2", (short) 1286)
                            .set("test3", true)
                            .set("test4", 1255.132f)
                            .set("test5", 349086734096823L)
                        })
                );

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataSerializer.write(new DataOutputStream(byteArrayOutputStream), original);
        byte[] bytes = byteArrayOutputStream.toByteArray();

        DataBlock deserialized = (DataBlock) DataSerializer.read(new DataInputStream(new ByteArrayInputStream(bytes)));
        System.out.println(original.equals(deserialized));
        System.out.println();

        ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
        DataSerializer.write(new DataOutputStream(byteArrayOutputStream2), deserialized);
        byte[] bytes2 = byteArrayOutputStream2.toByteArray();
        System.out.println(Arrays.equals(bytes,bytes2));
        int x = (int) ((DataKeyedBlock<Character>)deserialized.get("where")).get('x');
        System.out.println();
    }
}
