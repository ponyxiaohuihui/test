package com.winphysoft.test.map.dic;

import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntCollection;
import it.unimi.dsi.fastutil.ints.IntSet;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.IntFunction;
import java.util.function.IntUnaryOperator;

/**
 * @author pony
 * @version 1.1
 * Created by pony on 2020/3/5
 */
public class Int2IntMapNIO {
    protected final transient int minN;
    protected final float f;
    protected transient int[] key;
    protected transient int[] value;
    protected transient int mask;
    protected transient int n;
    protected transient int maxFill;
    protected int size;
    protected transient Int2IntMap.FastEntrySet entries;
    protected transient IntSet keys;
    protected transient IntCollection values;
    private int defRetValue = -1;

    public Int2IntMapNIO(int expected, float f) {
        if (f > 0.0F && f <= 1.0F) {
            if (expected < 0) {
                throw new IllegalArgumentException("The expected number of elements must be nonnegative");
            } else {
                this.f = f;
                this.minN = this.n = HashCommon.arraySize(expected, f);
                this.mask = this.n - 1;
                this.maxFill = HashCommon.maxFill(this.n, f);
                this.key = new int[this.n + 1];
                this.value = new int[this.n + 1];
            }
        } else {
            throw new IllegalArgumentException("Load factor must be greater than 0 and smaller than or equal to 1");
        }
    }

    public Int2IntMapNIO() {
        this(16, 0.75F);
    }


    private int realSize() {
        return this.size;
    }

    private void ensureCapacity(int capacity) {
        int needed = HashCommon.arraySize(capacity, this.f);
        if (needed > this.n) {
            this.rehash(needed);
        }

    }

    private void tryCapacity(long capacity) {
        int needed = (int) Math.min(1073741824L, Math.max(2L, HashCommon.nextPowerOfTwo((long) Math.ceil((float) capacity / this.f))));
        if (needed > this.n) {
            this.rehash(needed);
        }

    }

    private int find(int k) {
        int[] key = this.key;
        int curr;
        int pos;
        if ((curr = key[pos = HashCommon.mix(k) & this.mask]) == 0) {
            return -(pos + 1);
        } else if (k == curr) {
            return pos;
        } else {
            while ((curr = key[pos = pos + 1 & this.mask]) != 0) {
                if (k == curr) {
                    return pos;
                }
            }

            return -(pos + 1);
        }
    }

    private void insert(int pos, int k, int v) {
        this.key[pos] = k;
        this.value[pos] = v;
        if (this.size++ >= this.maxFill) {
            this.rehash(HashCommon.arraySize(this.size + 1, this.f));
        }

    }

    public int put(int k, int v) {
        int pos = this.find(k);
        if (pos < 0) {
            this.insert(-pos - 1, k, v);
            return this.defRetValue;
        } else {
            int oldValue = this.value[pos];
            this.value[pos] = v;
            return oldValue;
        }
    }

    private int addToValue(int pos, int incr) {
        int oldValue = this.value[pos];
        this.value[pos] = oldValue + incr;
        return oldValue;
    }

    public int addTo(int k, int incr) {
        int pos;
            int[] key = this.key;
            int curr;
            if ((curr = key[pos = HashCommon.mix(k) & this.mask]) != 0) {
                if (curr == k) {
                    return this.addToValue(pos, incr);
                }

                while ((curr = key[pos = pos + 1 & this.mask]) != 0) {
                    if (curr == k) {
                        return this.addToValue(pos, incr);
                    }
                }
            }

        this.key[pos] = k;
        this.value[pos] = this.defRetValue + incr;
        if (this.size++ >= this.maxFill) {
            this.rehash(HashCommon.arraySize(this.size + 1, this.f));
        }

        return this.defRetValue;
    }

    public int get(int k) {
        int[] key = this.key;
        int curr;
        int pos;
        if ((curr = key[pos = HashCommon.mix(k) & this.mask]) == 0) {
            return this.defRetValue;
        } else if (k == curr) {
            return this.value[pos];
        } else {
            while ((curr = key[pos = pos + 1 & this.mask]) != 0) {
                if (k == curr) {
                    return this.value[pos];
                }
            }

            return this.defRetValue;
        }
    }

    public boolean containsKey(int k) {
        int[] key = this.key;
        int curr;
        int pos;
        if ((curr = key[pos = HashCommon.mix(k) & this.mask]) == 0) {
            return false;
        } else if (k == curr) {
            return true;
        } else {
            while ((curr = key[pos = pos + 1 & this.mask]) != 0) {
                if (k == curr) {
                    return true;
                }
            }

            return false;
        }
    }

    public int getOrDefault(int k, int defaultValue) {
            int[] key = this.key;
            int curr;
            int pos;
            if ((curr = key[pos = HashCommon.mix(k) & this.mask]) == 0) {
                return defaultValue;
            } else if (k == curr) {
                return this.value[pos];
            } else {
                while ((curr = key[pos = pos + 1 & this.mask]) != 0) {
                    if (k == curr) {
                        return this.value[pos];
                    }
                }

                return defaultValue;
            }
    }

    public int putIfAbsent(int k, int v) {
        int pos = this.find(k);
        if (pos >= 0) {
            return this.value[pos];
        } else {
            this.insert(-pos - 1, k, v);
            return this.defRetValue;
        }
    }

    public boolean replace(int k, int oldValue, int v) {
        int pos = this.find(k);
        if (pos >= 0 && oldValue == this.value[pos]) {
            this.value[pos] = v;
            return true;
        } else {
            return false;
        }
    }

    public int replace(int k, int v) {
        int pos = this.find(k);
        if (pos < 0) {
            return this.defRetValue;
        } else {
            int oldValue = this.value[pos];
            this.value[pos] = v;
            return oldValue;
        }
    }

    public int computeIfAbsent(int k, IntUnaryOperator mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        int pos = this.find(k);
        if (pos >= 0) {
            return this.value[pos];
        } else {
            int newValue = mappingFunction.applyAsInt(k);
            this.insert(-pos - 1, k, newValue);
            return newValue;
        }
    }

    public int computeIfAbsentNullable(int k, IntFunction<? extends Integer> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        int pos = this.find(k);
        if (pos >= 0) {
            return this.value[pos];
        } else {
            Integer newValue = mappingFunction.apply(k);
            if (newValue == null) {
                return this.defRetValue;
            } else {
                int v = newValue;
                this.insert(-pos - 1, k, v);
                return v;
            }
        }
    }




    public void clear() {
        if (this.size != 0) {
            this.size = 0;
            Arrays.fill(this.key, 0);
        }
    }

    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    protected void rehash(int newN) {
        int[] key = this.key;
        int[] value = this.value;
        int mask = newN - 1;
        int[] newKey = new int[newN + 1];
        int[] newValue = new int[newN + 1];
        int i = this.n;

        int pos;
        for (int var9 = this.realSize(); var9-- != 0; newValue[pos] = value[i]) {
            do {
                --i;
            } while (key[i] == 0);

            if (newKey[pos = HashCommon.mix(key[i]) & mask] != 0) {
                while (newKey[pos = pos + 1 & mask] != 0) {
                }
            }

            newKey[pos] = key[i];
        }

        newValue[newN] = value[this.n];
        this.n = newN;
        this.mask = mask;
        this.maxFill = HashCommon.maxFill(this.n, this.f);
        this.key = newKey;
        this.value = newValue;
    }
}
