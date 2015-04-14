package com.citrix.shared;

public class RetryStrategy
{
	
 public static final int DEFAULT_NUMBER_OF_RETRIES = 5;
 public static final long DEFAULT_WAIT_TIME = 1000; //1 millisconds
 
 private int numberOfRetries; //total number of tries
 private int numberOfTriesLeft; //number left
 private long timeToWaitInMs; //wait interval
 
 public RetryStrategy()
 {
  this(DEFAULT_NUMBER_OF_RETRIES, DEFAULT_WAIT_TIME);
 }
 
 public RetryStrategy(int numberOfRetries, long timeToWait)
 {
  this.numberOfRetries = numberOfRetries;
  numberOfTriesLeft = numberOfRetries;
  this.timeToWaitInMs = timeToWait;
 }
 
 /**
  * @return true if there are tries left
  */
 public boolean shouldRetry()
 {
  return numberOfTriesLeft > 0;
 }
 
 /**
  * This method should be called if a try fails.
  *
  * @throws RetryException if there are no more tries left
  */
 public void errorOccured() throws RetryException
 {
  numberOfTriesLeft --;
  if (!shouldRetry())
  {
   throw new RetryException(numberOfRetries +
     " attempts to retry failed at " + getTimeToWait() +
     "ms interval");
  }
  waitUntilNextTry();
 }
 
 /**
  * @return time period between retries
  */
 public long getTimeToWait()
 {
  return timeToWaitInMs ;
 }
 
 /**
  * Sleeps for the duration of the defined interval
  */
 private void waitUntilNextTry()
 {
  try
  {
   Thread.sleep(getTimeToWait());
  }
  catch (InterruptedException ignored) {}
 }
}
