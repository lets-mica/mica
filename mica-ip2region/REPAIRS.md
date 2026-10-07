# IP lookup shutdown and configuration repairs

These two repairs start from Mica commit `b971d90b54b14c69bfc7e068fd63dac463078b60`. They change no public signatures or cache policies.

## Reproduce the failures

With a file-backed `Config`, create a one-searcher pool and borrow its searcher. Calling `pool.close(10)` on another thread should wait until that searcher returns. The original implementation instead throws `IllegalMonitorStateException`: it calls `Object.wait` on a `Condition` while holding the pool's `ReentrantLock`. The repair uses that condition's `await` methods. Positive intervals repeat until every loan returns; zero waits indefinitely. Interruption propagates and releases the lock. Negative intervals remain invalid when a wait is needed.

Build a configuration from an IPv4 XDB input stream with `Config.BufferCache`, then call `asV6()`; repeat with an IPv6 stream and `asV4()`. Both should throw `XdbException` with version diagnostics. The original implementation throws `NullPointerException` while trying to print the absent file path. The repair identifies this source as `<input stream>` and preserves the file-backed message.

## Tests and commands

The new JUnit tests construct local XDB fixtures and exercise the production configuration, searchers and pools. Shutdown tests use bounded concurrency checks, verify that all loans must return, check interruption and negative intervals, and verify real file-handle closure. Configuration tests cover both mismatch directions and matching file/stream configurations.

`Ip2RegionLifecycleTest` adds complete IPv4/IPv6 lookups through the public manager using databases containing actual region records. It covers every file cache mode and stream-backed configurations, followed by shutdown. These four regression cases also pass on the original source.

From the repository root with Java 17 and Maven:

```sh
mvn -pl mica-ip2region -am test \
  -Dtest=SearcherPoolTest,ConfigTest,Ip2RegionLifecycleTest,Ip2regionTest \
  -Dsurefire.failIfNoSpecifiedTests=false
```

Run the IP lookup module and its core dependency's complete test suites:

```sh
mvn -o -pl mica-ip2region -am test
```

The offline command requires dependencies to have been downloaded first. Verification with Maven 3.9.9 and Zulu Java 17.0.11 completed successfully: 134 core tests and 22 IP lookup tests, with no failures, errors or skipped tests. The 19 new regression cases also passed independently with JUnit Console 6.0.3. Against the original source, four shutdown cases and both stream mismatch cases failed for the reported bugs; the remaining cases passed.
