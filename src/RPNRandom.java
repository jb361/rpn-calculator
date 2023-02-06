/**
 * RPNRandom
 *
 * Implement the GLIBC random number generator to generate the same infinite sequence of random 
 * numbers as the legacy calculator. The PRNG in java.util.Random uses a 48-bit seed and different 
 * multiplier so it can't generate the same number sequence. Also, storing random numbers from the 
 * legacy calculator in an array isn't a proper solution as eventually it will run out of numbers  
 * and diverge from the original. I started by porting the C code on the below page into Java then 
 * modified and extended it to permit an infinite sequence of random numbers to be generated.
 *
 * https://www.mathstat.dal.ca/~selinger/random/
 */

public class RPNRandom
{
  // An array of random numbers that can be used to generate future random numbers.
  private long[] rand = new long[344];

  // The current index into the array. This gets wrapped around at the end of the array.
  private int index = 0;

  // This is required to repeat the first 22 random numbers once like the legacy calculator does.
  private boolean repeated = false;

  /**
   * Construct the random number generator with a specified seed.
   */
  public RPNRandom(int seed)
  {
    setSeed(seed);
  }

  /**
   * Seed the random number generator. This can be invoked multiple times to generate random 
   * number sequences with different seeds. We discard the first 344 random numbers because their 
   * entropy is low.
   */
  public void setSeed(int seed)
  {
    rand[0] = seed;

    for (int i = 1; i < 344; ++i)
    {
      if (i < 31) {
        rand[i] = ((16807L * rand[i-1]) % Integer.MAX_VALUE) & (-1L >>> 32);
      }
      else {
        rand[i] = rand[i-31] + ((i >= 34) ? rand[i-3] : 0);
      }
    }
    index = 0;
  }

  /**
   * Return the next positive random integer in the sequence, minus the "least random" bit. Also 
   * increment the index or reset it to 0 to repeat the first 22 numbers once like the legacy 
   * calculator does.
   */
  public int nextInt()
  {
    rand[index] = rand[(index + 313) % 344] + rand[(index + 341) % 344];
    long unsignedRand = rand[index] & (-1L >>> 32);

    index = (index + 1) % 344;
    if (!repeated && index == 22)
    {
      repeated = true;
      index = 0;
    }
    return (int) (unsignedRand >> 1);
  }
}
