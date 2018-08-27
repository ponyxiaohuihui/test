package sun.nio.ch;

import java.io.FileDescriptor;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by 小灰灰 on 2016/9/28.
 */
public class LargeChannel {
    public static MappedByteBuffer getBuffer(FileChannelImpl channel, long size) throws Exception{
        Method ensureOpen = channel.getClass().getDeclaredMethod("ensureOpen", null);
        ensureOpen.setAccessible(true);
        ensureOpen.invoke(channel, null);
        int imode = 0;
        long addr = -1;
        int ti = -1;
        Field f = channel.getClass().getDeclaredField("fd");
        f.setAccessible(true);
        FileDescriptor fd= (FileDescriptor) f.get(channel);
        Field tt = channel.getClass().getDeclaredField("threads");
        tt.setAccessible(true);
        Class thds = Class.forName("sun.nio.ch.NativeThreadSet");
        Constructor constructor = thds.getDeclaredConstructor(int.class);
        constructor.setAccessible(true);
        Object threads = constructor.newInstance(2);
        tt.set(channel, threads);
        try {
            Method begin = channel.getClass().getSuperclass().getSuperclass().getDeclaredMethod("begin", null);
            begin.setAccessible(true);
            begin.invoke(channel, null);
            if (!channel.isOpen())
                return null;
            Method add = threads.getClass().getDeclaredMethod("add");
            add.setAccessible(true);
            ti = (int) add.invoke(threads);

            long mapPosition = 0;
            long mapSize = size;
            Method map0 = channel.getClass().getDeclaredMethod("map0", int.class, long.class, long.class);
            map0.setAccessible(true);
            try {
                // If no exception was thrown from map0, the address is valid
                addr = (long) map0.invoke(channel, imode, mapPosition, mapSize);
            } catch (OutOfMemoryError x) {
                // An OutOfMemoryError may indicate that we've exhausted memory
                // so force gc and re-attempt map
                System.gc();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException y) {
                    Thread.currentThread().interrupt();
                }
                try {
                    addr = (long) map0.invoke(channel,imode, mapPosition, mapSize);
                } catch (OutOfMemoryError y) {
                    // After a second OOME, fail
                    throw new IOException("Map failed", y);
                }
            }

            int isize = (int)size;
            Unmapper um = new Unmapper(addr, size, channel );
            Method newMappedByteBufferR = Util.class.getDeclaredMethod("newMappedByteBufferR", int.class, long.class, FileDescriptor.class, Runnable.class);
            newMappedByteBufferR.setAccessible(true);
            return (MappedByteBuffer) newMappedByteBufferR.invoke(Util.class, isize, addr , fd,  um);
        } finally {
            Method remove = threads.getClass().getDeclaredMethod("remove", int.class);
            remove.setAccessible(true);
            remove.invoke(threads, ti);
            Method end = channel.getClass().getSuperclass().getSuperclass().getDeclaredMethod("end", boolean.class);
            end.setAccessible(true);
            end.invoke(channel, IOStatus.checkAll(addr));
        }
    }

    private static class Unmapper
            implements Runnable
    {

        private long address;
        private long size;
        private FileChannel channel;

        private Unmapper(long address, long size, FileChannel channel) {
            this.address = address;
            this.size = size;
            this.channel = channel;
        }

        public void run() {
            if (address == 0)
                return;
            try {
                Method unmap0 = channel.getClass().getDeclaredMethod("unmap0", long.class, long.class);
                unmap0.setAccessible(true);
                unmap0.invoke(channel, address, size);
            } catch (Exception e){

            }
            address = 0;
        }

    }
}
