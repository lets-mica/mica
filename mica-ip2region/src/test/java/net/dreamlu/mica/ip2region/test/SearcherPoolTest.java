package net.dreamlu.mica.ip2region.test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.LockSupport;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.io.TempDir;

import net.dreamlu.mica.ip2region.config.Config;
import net.dreamlu.mica.ip2region.core.SearcherPool;
import net.dreamlu.mica.ip2region.xdb.Searcher;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

@Timeout(10)
class SearcherPoolTest {

	@TempDir
	Path directory;

	@Test
	void positiveIntervalWaitsUntilSearcherReturns() throws Exception {
		assertWaitsUntilReturned(10);
	}

	@Test
	void zeroIntervalWaitsUntilSearcherReturns() throws Exception {
		assertWaitsUntilReturned(0);
	}

	@Test
	void shutdownWaitsForEveryBorrowedSearcher() throws Exception {
		SearcherPool pool = newPool(2);
		Searcher first = pool.borrowSearcher();
		Searcher last = pool.borrowSearcher();
		FutureTask<Void> close = new FutureTask<>(() -> {
			pool.close(0);
			return null;
		});
		Thread closer = startCloser(close);
		boolean firstReturned = false;
		boolean lastReturned = false;
		try {
			awaitWaiting(closer, close);
			firstReturned = true;
			returnSearcherBounded(pool, first);
			assertThrows(TimeoutException.class, () -> close.get(100, TimeUnit.MILLISECONDS));
			assertEquals("", first.search(new byte[4]));
			assertEquals("", last.search(new byte[4]));
			lastReturned = true;
			returnSearcherBounded(pool, last);
			close.get(2, TimeUnit.SECONDS);
			assertThrows(IOException.class, () -> first.search(new byte[4]));
			assertThrows(IOException.class, () -> last.search(new byte[4]));
		} finally {
			stopCloser(closer);
			if (!firstReturned) {
				returnSearcherBounded(pool, first);
			}
			if (!lastReturned) {
				returnSearcherBounded(pool, last);
			}
			pool.close(0);
		}
	}

	@Test
	void interruptedClosePropagatesInterruptionAndReleasesLock() throws Exception {
		SearcherPool pool = newPool();
		Searcher searcher = pool.borrowSearcher();
		FutureTask<Void> close = new FutureTask<>(() -> {
			pool.close(0);
			return null;
		});
		Thread closer = startCloser(close);
		try {
			awaitWaiting(closer, close);
			closer.interrupt();
			Exception failure = assertThrows(ExecutionException.class,
				() -> close.get(2, TimeUnit.SECONDS));
			assertInstanceOf(InterruptedException.class, failure.getCause());
			assertEquals("", searcher.search(new byte[4]));
		} finally {
			stopCloser(closer);
			returnAndClose(pool, searcher);
		}
		assertThrows(IOException.class, () -> searcher.search(new byte[4]));
	}

	@Test
	void negativeIntervalIsRejectedWhileSearcherIsBorrowed() throws Exception {
		SearcherPool pool = newPool();
		Searcher searcher = pool.borrowSearcher();
		try {
			assertThrows(IllegalArgumentException.class, () -> pool.close(-1));
			assertEquals("", searcher.search(new byte[4]));
		} finally {
			returnAndClose(pool, searcher);
		}
		assertThrows(IOException.class, () -> searcher.search(new byte[4]));
	}

	@Test
	void negativeIntervalWithoutLoansStillClosesResources() throws Exception {
		SearcherPool pool = newPool();
		Searcher searcher = pool.borrowSearcher();
		pool.returnSearcher(searcher);
		pool.close(-1);
		assertThrows(IOException.class, () -> searcher.search(new byte[4]));
	}

	@Test
	void closeWithoutLoansClosesFileBackedResources() throws Exception {
		SearcherPool pool = newPool();
		Searcher searcher = pool.borrowSearcher();
		assertEquals("", searcher.search(new byte[4]));
		pool.returnSearcher(searcher);
		pool.close();
		assertEquals(0, pool.getLoanCount());
		assertThrows(IOException.class, () -> searcher.search(new byte[4]));
	}

	private SearcherPool newPool() throws Exception {
		return newPool(1);
	}

	private SearcherPool newPool(int searchers) throws Exception {
		Config config = Config.custom()
			.setXdbFile(XdbTestFixtures.writeDatabase(directory, 4).toFile())
			.setCachePolicy(Config.NoCache)
			.setSearchers(searchers)
			.asV4();
		return SearcherPool.create(config);
	}

	private void assertWaitsUntilReturned(long interval) throws Exception {
		SearcherPool pool = newPool();
		Searcher searcher = pool.borrowSearcher();
		FutureTask<Void> close = new FutureTask<>(() -> {
			pool.close(interval);
			return null;
		});
		Thread closer = startCloser(close);
		boolean returned = false;
		try {
			awaitWaiting(closer, close);
			assertThrows(TimeoutException.class, () -> close.get(100, TimeUnit.MILLISECONDS));
			assertEquals("", searcher.search(new byte[4]));
			returned = true;
			returnSearcherBounded(pool, searcher);
			close.get(2, TimeUnit.SECONDS);
			assertEquals(0, pool.getLoanCount());
			assertThrows(IOException.class, () -> searcher.search(new byte[4]));
		} finally {
			stopCloser(closer);
			if (!returned) {
				returnAndClose(pool, searcher);
			}
		}
	}

	private static Thread startCloser(FutureTask<Void> close) {
		Thread closer = new Thread(close, "searcher-pool-close-test");
		closer.setDaemon(true);
		closer.start();
		return closer;
	}

	private static void awaitWaiting(Thread closer, FutureTask<Void> close) throws Exception {
		long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(2);
		while (System.nanoTime() < deadline) {
			if (close.isDone()) {
				close.get(2, TimeUnit.SECONDS);
			}
			Thread.State state = closer.getState();
			if (state == Thread.State.WAITING || state == Thread.State.TIMED_WAITING) {
				return;
			}
			LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(1));
		}
		fail("Shutdown did not start waiting within two seconds");
	}

	private static void stopCloser(Thread closer) throws InterruptedException {
		closer.interrupt();
		closer.join(2000);
		assertFalse(closer.isAlive(), "Shutdown thread did not finish");
	}

	private static void returnAndClose(SearcherPool pool, Searcher searcher) throws Exception {
		returnSearcherBounded(pool, searcher);
		pool.close(0);
	}

	private static void returnSearcherBounded(SearcherPool pool, Searcher searcher) throws Exception {
		FutureTask<Void> cleanup = new FutureTask<>(() -> {
			pool.returnSearcher(searcher);
			return null;
		});
		Thread worker = new Thread(cleanup, "searcher-pool-cleanup-test");
		worker.setDaemon(true);
		worker.start();
		cleanup.get(2, TimeUnit.SECONDS);
	}
}
