package emun;

import com.fr.base.FRContext;
import com.fr.bi.stable.gvi.*;

import java.io.IOException;

/**
 * Created by 小灰灰 on 2017/4/18.
 */
public enum  Type {
        ROARING_INDEX((byte) 4) {
            @Override
            public GroupValueIndex create() {
                return new RoaringGroupValueIndex();
            }
        },
        ROARING_INDEX_ID((byte) 3) {
            @Override
            public GroupValueIndex create() {
                return new RoaringGroupValueIndex();
            }
        },
        ROARING_INDEX_All_SHOW((byte) 5) {
            @Override
            public GroupValueIndex create() {
                return new AllShowRoaringGroupValueIndex();
            }
        };

        private byte type;

    Type(byte type) {
            this.type = type;
        }

        protected static GroupValueIndex createGroupValueIndex(BIByteDataInput stream) {
            if (stream.size() == 0) {
                return RoaringGroupValueIndex.createGroupValueIndex(new int[]{});
            }
            try {
                byte start = stream.readByte();
                GroupValueIndex result = createGroupValueIndexByType(start);
                result.readFields(stream);
                return result;
            } catch (IOException e) {
                FRContext.getLogger().error(e.getMessage(), e);
            }
            return null;
        }

        private static GroupValueIndex createGroupValueIndexByType(byte type) {
            for (Type creator : values()) {
                if (type == creator.getType()) {
                    return creator.create();
                }
            }
            return null;
        }

        public byte getType() {
            return type;
        }

        public abstract GroupValueIndex create();

}
