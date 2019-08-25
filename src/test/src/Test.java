import shcm.shsupercm.data.framework.DataSerializer;
import shcm.shsupercm.data.framework.DataBlock;
import shcm.shsupercm.data.framework.DataKeyedBlock;

import java.io.*;

public class Test {
    public static void main(String[] args) throws IOException, DataSerializer.UnknownDataTypeException, DataSerializer.UnexpectedByteException {
        DataBlock original = new DataBlock()
                .set("where", new DataKeyedBlock<>(' ')
                    .set('x', -45)
                    .set('y', 53)
                    .set('z', 23))
                .set("what", "Just a thing, nothing really important tbh...")
                .set("storage", new byte[]{0,48,-18,34,127,0,0,88,34,34,34,34,15,1});

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataSerializer.write(new DataOutputStream(byteArrayOutputStream), original);
        byte[] bytes = byteArrayOutputStream.toByteArray();

        Object deserialized = DataSerializer.read(new DataInputStream(new ByteArrayInputStream(bytes)));
        System.out.println(original.equals(deserialized));
        System.out.println();
    }
}
