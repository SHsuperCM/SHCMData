import shcm.shsupercm.data.data.DataAnnotationRegistry;
import shcm.shsupercm.data.data.DataRegistry;
import shcm.shsupercm.data.data.IData;
import shcm.shsupercm.data.framework.DataSerializer;
import shcm.shsupercm.data.framework.DataBlock;
import shcm.shsupercm.data.framework.DataKeyedBlock;
import shcm.shsupercm.testing.WorldPos;

import java.io.*;
import java.util.Arrays;

public class Test {
    public static void main(String[] args) throws IOException, DataSerializer.UnknownDataTypeException, DataSerializer.UnexpectedByteException {
        //DataAnnotationRegistry.init(false);

        DataBlock original = new DataBlock()
            .set("where", new DataKeyedBlock<>(Character.class)
                .set('x', -45)
                .set('y', 53)
                .set('z', 23)
            )
            .set("what", "Just a thing, nothing really important tbh...")
            .set("byte_shit", new byte[]{0,48,-18,34,127,0,0,88,34,34,34,34,15,1})
            .set("storage", new DataBlock()
                .set("example_blocks", new DataBlock[] {
                    new DataBlock()
                        .set("test1", "lolzXD")
                        .set("test2", (short) 1286)
                        .set("test3", true)
                        .set("test4", 1255.132f)
                        .set("test5", 349086734096823L),
                    new DataBlock()
                        .set("test_with_byte_array", new byte[]{0,15,126,-15,-15,-15,-15,-15,84,-100,-5,57,31,44,20,1,0,0})
                        .set("test_with_string_array", new String[]{"line1 text usually goes here", "next line is probably here"})
                })
                .set("idata_array_testing", new IData[] {
                    new WorldPos(1, 15, 38),
                    new WorldPos(Integer.MAX_VALUE, 183, -5000),
                    new WorldPos(1355542, 26111110, 2754896),
                    new WorldPos(0, 14, -1)
                })
            )
            .set("worldposthing", new WorldPos(645, 63, -1346776));

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataSerializer.write(new DataOutputStream(byteArrayOutputStream), original);
        byte[] bytes = byteArrayOutputStream.toByteArray();

        DataBlock deserialized = (DataBlock) DataSerializer.read(new DataInputStream(new ByteArrayInputStream(bytes)));

        ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
        DataSerializer.write(new DataOutputStream(byteArrayOutputStream2), deserialized);
        byte[] bytes2 = byteArrayOutputStream2.toByteArray();

        assert Arrays.equals(bytes, bytes2);
        assert original.equals(deserialized);

        int x = (int) ((DataKeyedBlock<Character>)deserialized.get("where")).get('x');
        WorldPos pos2 = (WorldPos) deserialized.get("worldposthing");
        WorldPos pos3 = (WorldPos) ((IData[])deserialized.getBlock("storage").get("idata_array_testing"))[1];
        assert false;//ALL SHOULD BE GOOD HERE!
    }
}
