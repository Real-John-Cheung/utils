package pinkNoise;

import java.util.*;
/**
 * pink Noise based on https://gist.github.com/tom-merchant/5ced03a0638b06138ee7d11c0c209aa4 
 */
class PinkNoise{

    /**
     * https://commons.apache.org/proper/commons-rng/commons-rng-core/jacoco/org.apache.commons.rng.core.source32/MiddleSquareWeylSequence.java.html 
     */
    static class MiddleSquareRand {
        private static final int SEED_SIZE = 3;
        private static final long[] DEFAULT_SEED = {0x012de1babb3c4104L, 0xc8161b4202294965L, 0xb5ad4eceda1ce2a9L};
        private long s, w, x;

        /**
         * 
         * @param seed unsigned long values
         */
        MiddleSquareRand(long[] seed){
            if (seed.length < SEED_SIZE) {
                final long[] tmp = Arrays.copyOf(seed, SEED_SIZE);
                System.arraycopy(DEFAULT_SEED, seed.length, tmp, seed.length, SEED_SIZE - seed.length);
                setSeedInternal(tmp);
            } else {
                setSeedInternal(seed);
            }
        }

        /**
         * 
         */
        MiddleSquareRand(){
            this(new long[]{});
        }

        /**
         * 
         * @param seed seeds
         */
        private void setSeedInternal(long[] seed){
            this.x = seed[0];
            this.w = seed[1];
            this.s = seed[2] | 1L;
        }

        public int nextInt(){
            this.x *= this.x;
            this.x += (this.w += this.s);
            this.x = (this.x >>> 32) | ( this.x << 32);
            return (int) x;
        }

        public long nextLong(){
            this.x *= this.x;
            this.x += (this.w += this.s);
            final long i1 = this.x & 0xffffffff00000000L;
            this.x = (x >>> 32) | (x << 32);
            this.x *= this.x;
            this.x += (this.w += this.s);
            final long i2 = this.x >>> 32;
            this.x = i2| this.x << 32;
            return i1|i2;
        }

    }

    private MiddleSquareRand rand;
    private static final int[] trailingZero = new int[]  {8,0,1,0,2,0,1,0,3,0,1,0,2,0,1,0,4,0,1,0,2,0,1,0,3,0,1,0,2,0,1,0,5,0,1,0,2,0,1,0,3,0,1,0,2,0,1,0,4,0,1,0,2,0,1,0,3,0,1,0,2,0,1,0,6,0,1,0,2,0,1,0,3,0,1,0,2,0,1,0,4,0,1,0,2,0,1,0,3,0,1,0,2,0,1,0,5,0,1,0,2,0,1,0,3,0,1,0,2,0,1,0,4,0,1,0,2,0,1,0,3,0,1,0,2,0,1,0,7,0,1,0,2,0,1,0,3,0,1,0,2,0,1,0,4,0,1,0,2,0,1,0,3,0,1,0,2,0,1,0,5,0,1,0,2,0,1,0,3,0,1,0,2,0,1,0,4,0,1,0,2,0,1,0,3,0,1,0,2,0,1,0,6,0,1,0,2,0,1,0,3,0,1,0,2,0,1,0,4,0,1,0,2,0,1,0,3,0,1,0,2,0,1,0,5,0,1,0,2,0,1,0,3,0,1,0,2,0,1,0,4,0,1,0,2,0,1,0,3,0,1,0,2,0,1,0,};

    private int counter;
    private double out;
    private double[] octaveVals;
    /**
     * 
     * @param seed
     */
    PinkNoise(long[] seed){
        this.rand = new MiddleSquareRand(seed);
        this.counter = 0;
        this.octaveVals = new double[9];
        this.out = 0;
    }

    /**
     * 
     */
    PinkNoise(){
        this.rand = new MiddleSquareRand();
        this.counter = 0;
        this.octaveVals = new double[9];
        this.out = 0;
    }

    /**
     * 
     * @return the next noise value;
     */
    double next(){
        int octave = trailingZero[this.counter];
        this.out -= this.octaveVals[octave];
        this.octaveVals[octave] = (double)this.rand.nextInt() / (double)2147483647;
        this.octaveVals[octave] /= 10 - octave;
        this.out += this.octaveVals[octave];
        this.counter ++;
        this.counter = this.counter % trailingZero.length;

        return this.out;
    }
}