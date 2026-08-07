# Task Limiter

Limit the number of tasks that can be submitted in a given period (rate limiter).

For example, an email API can be sent max 5 emails per second.

- [See use case: 5 emails / second](#5-emails--second)

Notes: 
- This implementation favors simplicity.
- This is an in-memory solution for simple use cases such as "max 10 tasks per second", "max 50 tasks per minute".
- It does not survive power loss (it's not persistent). 
- It is not suitable for a large amount of tasks or sub-millisecond precision. 
- However it works fine if you don't care about millisecond precision, task amount is reasonable (0-1000) and in general non-mission-critical operations.

## Usage


You can subclass `com.giuseppetavella.core.TaskLimiter` to fit your use cases.

The limits are applied to the com.giuseppetavella.core.TaskLimiter object, so tasks that need to be rate-limited must go through the same instance, because the instance contains the history of task submissions.

### Simple usage

```java
import com.giuseppetavella.core.TaskLimiter;

// STEP 1: Instantiate
TaskLimiter taskLimiter = new TaskLimiter(5, 1000); // Max 5 tasks per 1000 milliseconds (1 second)

        // STEP 2: Have a task ready  
        Callable<String> task = () -> {
          return "future result";
        };

        // STEP 3: Submit a task through the task limiter
// We can submit 5 tasks because that is the limit
        Future<String> result1 = taskLimiter.submitOrThrow(task); // Try submitting a task. If rate overflow, custom exception is thrown 
        Future<String> result2 = taskLimiter.submitOrThrow(task);
        Future<String> result3 = taskLimiter.submitOrThrow(task);
        Future<String> result4 = taskLimiter.submitOrThrow(task);
        Future<String> result5 = taskLimiter.submitOrThrow(task);
        Future<String> result6 = taskLimiter.submitOrThrow(task); // Exception is thrown here because rate overflow (6th task in same second)

```

### Advanced usage

```java
import com.giuseppetavella.core.TaskLimiter;

// Upon instantation, you can optionally pass a custom thread pool. 
// If you do not, one will be created for you.
ExecutionService executor = Executors.newVirtualThreadPerTaskExecutor();

        TaskLimiter taskLimiter = new TaskLimiter(5, 1000, executor); // Max 5 tasks per 1000 milliseconds (1 second)

```


## Use cases

### 5 emails / second

```java

import com.giuseppetavella.EmailLimiter;

com.giuseppetavella.core.TaskLimiter emailLimiter = new EmailLimiter(5, 1); // Max 5 emails per second

// // STEP 2: Have a task ready  
// Callable<String> emailTask = () -> {
//     Thread.sleep(Duration.ofMillis(1000)); // 
//     return "future result";
// };
//
// // STEP 3: Submit a task through the task limiter
// // We can submit 5 tasks because that is the limit
// Future<String> result1 = taskLimiter.submitOrThrow(task); // Try submitting a task. If rate overflow, custom exception is thrown 
```


## Reasoning & Challenges

### Defining the real problem

At first I thought that the solution consisted in splitting the time into periods, and have something continuously update the "current start of period".

If I know the current start of period and the time now, then I could keep track of how many tasks have been submitted in this time delta.

So something had to keep the the "current start of period" updated. This something was a thread that would wake up at a custom interval and would keep the "current start of period" in sync.

So let's say you start the task limiter now to rate limit 5 tasks per second, then a thread wakes up every second and keeps updating the "current start of period"

which is simply `startOfPeriod += period`.

This solution was not viable for at least these reasons:
- Waste of resources. Waking up a thread just to keep the "current start of period" in sync. CPU cycles wasted and a OS context switch just to keep 
  the current start of period updated. If the tasks to be rate limited are infrequent, resources are wasted anyways. 
- Not scalable, not secure and not performant. A "time updater thread per task limiter" model as well as a thread waking up at a custom interval (such each second) suffers from trust issues, 
  in the sense that the user directly controls how frequently the thread wakes up.
- Reliance on OS hoping that context switch would occur timely and without much delay. Uncertainty about how to deal with task submission if task submission 
  occurred in the the time it takes between the actual end of the current period and when the thread wakes up to update the current period. 

**But most importantly, that wasn't even what the problem was all about!**

I thought the problem was splitting the time into chunks, then I asked myself, why am I structuring time? It feels like I'm inventing time. 

I'm wasting resources just to keep track of what is the current start of period, but don't I already know what second is now and what is the previous second?

So I thought, maybe I can ground the original start time to be a floored number, for example instead of taking 12h:32m:34s, let's just start from 12h:32m:00s.


This approach entails that in an 1 second "artificial" period, I can have 5 tasks submitted. Then this current artificial start of period gets updated, 

and immediately after that a task is submitted. The time difference between this newly submitted task and the last submitted task is only 5 milliseconds away.

And altogether, the 6 tasks have been submitted in less than a second. So I realized, this cannot happen, I need to reformulate the problem as follows:

```
INITIAL PROBLEM: allow max N tasks in T period

REFORMULATED PROBLEM: the sum of the time deltas of the most recent N tasks is <= T period,
    where time delta = time of new task submission - time of last task submission
```

But this approach forced me to think about time deltas, keeping track of them etc.

So I said myself, okay this makes more sense, but what do I actually need for the problem? The number of tasks or the deltas? 

And eventually realized that the tasks count in the T period was what I actually needed.

This means that the problem was not about structuring time or even knowing a current start of period. 

The problem revolved around how many tasks have been submitted in the T period, regardless of whether that period is part of a "time structure" like 

the difference between this second and the previous second, etc.

What matters is knowing whether the last N tasks have been submitted in the T period, which is another way of stating the most up-to-date reformulation of the problem:

`the number of tasks submitted between now and now - T period must be <= N max tasks`

### Waiting without waiting

In life, either you change yourself or you change the environment (or maybe there's nothing to change).

The problem is that waiting for tests to finish is not ideal.

A task limiter should limit the number of tasks in a given time window.

However that shouldn't mean waiting real time just to test that it works.

So back to our metaphor; Either you make the thread wait (and you wait for it), or you simulate the waiting.

Either you wait real time just so time can move forward, or you make that time move forward yourself.

It's on this intuition that a solution was created to simulate waiting without actually waiting.

Each history queue has its own concept of time and can be easily modified.