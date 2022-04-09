/*
 * Copyright (C) 2015 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License.  You may
 * obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.  See the License for the specific language governing
 * permissions and limitations under the License.
 */

package net.dreamlu.mica.core.utils;

import lombok.experimental.UtilityClass;

import java.util.*;
import java.util.Spliterators.AbstractSpliterator;
import java.util.function.*;
import java.util.stream.*;

import static java.lang.Math.min;
import static java.util.Objects.requireNonNull;

/**
 * Static utility methods related to {@code Stream} instances.
 *
 * @author Guava
 */
@UtilityClass
public class StreamUtil {

	/**
	 * Returns a sequential {@link Stream} of the contents of {@code iterable}, delegating to {@link
	 * Collection#stream} if possible.
	 */
	public static <T> Stream<T> stream(Iterable<T> iterable) {
		return (iterable instanceof Collection)
			? ((Collection<T>) iterable).stream()
			: StreamSupport.stream(iterable.spliterator(), false);
	}

	/**
	 * Returns a sequential {@link Stream} of the remaining contents of {@code iterator}. Do not use
	 * {@code iterator} directly after passing it to this method.
	 */
	public static <T> Stream<T> stream(Iterator<T> iterator) {
		return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, 0), false);
	}

	/**
	 * If a value is present in {@code optional}, returns a stream containing only that element,
	 * otherwise returns an empty stream.
	 */
	public static <T> Stream<T> stream(Optional<T> optional) {
		return optional.map(Stream::of).orElseGet(Stream::empty);
	}

	/**
	 * If a value is present in {@code optional}, returns a stream containing only that element,
	 * otherwise returns an empty stream.
	 *
	 * <p><b>Java 9 users:</b> use {@code optional.stream()} instead.
	 */
	public static IntStream stream(OptionalInt optional) {
		return optional.isPresent() ? IntStream.of(optional.getAsInt()) : IntStream.empty();
	}

	/**
	 * If a value is present in {@code optional}, returns a stream containing only that element,
	 * otherwise returns an empty stream.
	 *
	 * <p><b>Java 9 users:</b> use {@code optional.stream()} instead.
	 */
	public static LongStream stream(OptionalLong optional) {
		return optional.isPresent() ? LongStream.of(optional.getAsLong()) : LongStream.empty();
	}

	/**
	 * If a value is present in {@code optional}, returns a stream containing only that element,
	 * otherwise returns an empty stream.
	 *
	 * <p><b>Java 9 users:</b> use {@code optional.stream()} instead.
	 */
	public static DoubleStream stream(OptionalDouble optional) {
		return optional.isPresent() ? DoubleStream.of(optional.getAsDouble()) : DoubleStream.empty();
	}

	/**
	 * Returns a stream in which each element is the result of passing the corresponding element of
	 * each of {@code streamA} and {@code streamB} to {@code function}.
	 *
	 * <p>For example:
	 *
	 * <pre>{@code
	 * Streams.zip(
	 *   Stream.of("foo1", "foo2", "foo3"),
	 *   Stream.of("bar1", "bar2"),
	 *   (arg1, arg2) -> arg1 + ":" + arg2)
	 * }</pre>
	 *
	 * <p>will return {@code Stream.of("foo1:bar1", "foo2:bar2")}.
	 *
	 * <p>The resulting stream will only be as long as the shorter of the two input streams; if one
	 * stream is longer, its extra elements will be ignored.
	 *
	 * <p>Note that if you are calling {@link Stream#forEach} on the resulting stream, you might want
	 * to consider using {@link #forEachPair} instead of this method.
	 *
	 * <p><b>Performance note:</b> The resulting stream is not <a
	 * href="http://gee.cs.oswego.edu/dl/html/StreamParallelGuidance.html">efficiently splittable</a>.
	 * This may harm parallel performance.
	 */
	public static <A, B, R> Stream<R> zip(Stream<A> streamA, Stream<B> streamB, BiFunction<? super A, ? super B, R> function) {
		// same as Stream.concat
		boolean isParallel = streamA.isParallel() || streamB.isParallel();
		Spliterator<A> splitA = streamA.spliterator();
		Spliterator<B> splitB = streamB.spliterator();
		int characteristics =
			splitA.characteristics()
				& splitB.characteristics()
				& (Spliterator.SIZED | Spliterator.ORDERED);
		Iterator<A> itrA = Spliterators.iterator(splitA);
		Iterator<B> itrB = Spliterators.iterator(splitB);
		return StreamSupport.stream(
				new AbstractSpliterator<R>(
					min(splitA.estimateSize(), splitB.estimateSize()), characteristics) {
					@Override
					public boolean tryAdvance(Consumer<? super R> action) {
						if (itrA.hasNext() && itrB.hasNext()) {
							action.accept(function.apply(itrA.next(), itrB.next()));
							return true;
						}
						return false;
					}
				},
				isParallel)
			.onClose(streamA::close)
			.onClose(streamB::close);
	}

	/**
	 * Invokes {@code consumer} once for each pair of <i>corresponding</i> elements in {@code streamA}
	 * and {@code streamB}. If one stream is longer than the other, the extra elements are silently
	 * ignored. Elements passed to the consumer are guaranteed to come from the same position in their
	 * respective source streams. For example:
	 *
	 * <pre>{@code
	 * Streams.forEachPair(
	 *   Stream.of("foo1", "foo2", "foo3"),
	 *   Stream.of("bar1", "bar2"),
	 *   (arg1, arg2) -> System.out.println(arg1 + ":" + arg2)
	 * }</pre>
	 *
	 * <p>will print:
	 *
	 * <pre>{@code
	 * foo1:bar1
	 * foo2:bar2
	 * }</pre>
	 *
	 * <p><b>Warning:</b> If either supplied stream is a parallel stream, the same correspondence
	 * between elements will be made, but the order in which those pairs of elements are passed to the
	 * consumer is <i>not</i> defined.
	 *
	 * <p>Note that many usages of this method can be replaced with simpler calls to {@link #zip}.
	 * This method behaves equivalently to {@linkplain #zip zipping} the stream elements into
	 * temporary pair objects and then using {@link Stream#forEach} on that stream.
	 */
	public static <A, B> void forEachPair(Stream<A> streamA, Stream<B> streamB, BiConsumer<? super A, ? super B> consumer) {
		if (streamA.isParallel() || streamB.isParallel()) {
			zip(streamA, streamB, TemporaryPair::new).forEach(pair -> consumer.accept(pair.a, pair.b));
		} else {
			Iterator<A> iterA = streamA.iterator();
			Iterator<B> iterB = streamB.iterator();
			while (iterA.hasNext() && iterB.hasNext()) {
				consumer.accept(iterA.next(), iterB.next());
			}
		}
	}

	// Use this carefully - it doesn't implement value semantics
	private static class TemporaryPair<A, B> {
		final A a;
		final B b;

		TemporaryPair(A a, B b) {
			this.a = a;
			this.b = b;
		}
	}

	/**
	 * Returns a stream consisting of the results of applying the given function to the elements of
	 * {@code stream} and their indices in the stream. For example,
	 *
	 * <pre>{@code
	 * mapWithIndex(
	 *     Stream.of("a", "b", "c"),
	 *     (e, index) -> index + ":" + e)
	 * }</pre>
	 *
	 * <p>would return {@code Stream.of("0:a", "1:b", "2:c")}.
	 *
	 * <p>The resulting stream is <a
	 * href="http://gee.cs.oswego.edu/dl/html/StreamParallelGuidance.html">efficiently splittable</a>
	 * if and only if {@code stream} was efficiently splittable and its underlying spliterator
	 * reported {@link Spliterator#SUBSIZED}. This is generally the case if the underlying stream
	 * comes from a data structure supporting efficient indexed random access, typically an array or
	 * list.
	 *
	 * <p>The order of the resulting stream is defined if and only if the order of the original stream
	 * was defined.
	 */
	public static <T, R> Stream<R> mapWithIndex(Stream<T> stream, FunctionWithIndex<? super T, ? extends R> function) {
		boolean isParallel = stream.isParallel();
		Spliterator<T> fromSpliterator = stream.spliterator();

		if (!fromSpliterator.hasCharacteristics(Spliterator.SUBSIZED)) {
			Iterator<T> fromIterator = Spliterators.iterator(fromSpliterator);
			return StreamSupport.stream(
					new AbstractSpliterator<R>(
						fromSpliterator.estimateSize(),
						fromSpliterator.characteristics() & (Spliterator.ORDERED | Spliterator.SIZED)) {
						long index = 0;

						@Override
						public boolean tryAdvance(Consumer<? super R> action) {
							if (fromIterator.hasNext()) {
								action.accept(function.apply(fromIterator.next(), index++));
								return true;
							}
							return false;
						}
					},
					isParallel)
				.onClose(stream::close);
		}
		class Splitr extends MapWithIndexSpliterator<Spliterator<T>, R, Splitr> implements Consumer<T> {
			T holder;

			Splitr(Spliterator<T> splitr, long index) {
				super(splitr, index);
			}

			@Override
			public void accept(T t) {
				this.holder = t;
			}

			@Override
			public boolean tryAdvance(Consumer<? super R> action) {
				if (fromSpliterator.tryAdvance(this)) {
					try {
						// The cast is safe because tryAdvance puts a T into `holder`.
						action.accept(function.apply(uncheckedCastNullableTToT(holder), index++));
						return true;
					} finally {
						holder = null;
					}
				}
				return false;
			}

			@Override
			Splitr createSplit(Spliterator<T> from, long i) {
				return new Splitr(from, i);
			}
		}
		return StreamSupport.stream(new Splitr(fromSpliterator, 0), isParallel).onClose(stream::close);
	}

	/**
	 * Returns a stream consisting of the results of applying the given function to the elements of
	 * {@code stream} and their indexes in the stream. For example,
	 *
	 * <pre>{@code
	 * mapWithIndex(
	 *     IntStream.of(10, 11, 12),
	 *     (e, index) -> index + ":" + e)
	 * }</pre>
	 *
	 * <p>...would return {@code Stream.of("0:10", "1:11", "2:12")}.
	 *
	 * <p>The resulting stream is <a
	 * href="http://gee.cs.oswego.edu/dl/html/StreamParallelGuidance.html">efficiently splittable</a>
	 * if and only if {@code stream} was efficiently splittable and its underlying spliterator
	 * reported {@link Spliterator#SUBSIZED}. This is generally the case if the underlying stream
	 * comes from a data structure supporting efficient indexed random access, typically an array or
	 * list.
	 *
	 * <p>The order of the resulting stream is defined if and only if the order of the original stream
	 * was defined.
	 */
	public static <R> Stream<R> mapWithIndex(IntStream stream, IntFunctionWithIndex<R> function) {
		boolean isParallel = stream.isParallel();
		Spliterator.OfInt fromSpliterator = stream.spliterator();
		if (!fromSpliterator.hasCharacteristics(Spliterator.SUBSIZED)) {
			PrimitiveIterator.OfInt fromIterator = Spliterators.iterator(fromSpliterator);
			return StreamSupport.stream(
					new AbstractSpliterator<R>(
						fromSpliterator.estimateSize(),
						fromSpliterator.characteristics() & (Spliterator.ORDERED | Spliterator.SIZED)) {
						long index = 0;

						@Override
						public boolean tryAdvance(Consumer<? super R> action) {
							if (fromIterator.hasNext()) {
								action.accept(function.apply(fromIterator.nextInt(), index++));
								return true;
							}
							return false;
						}
					},
					isParallel)
				.onClose(stream::close);
		}
		class Splitr extends MapWithIndexSpliterator<Spliterator.OfInt, R, Splitr>
			implements IntConsumer, Spliterator<R> {
			int holder;

			Splitr(OfInt splitr, long index) {
				super(splitr, index);
			}

			@Override
			public void accept(int t) {
				this.holder = t;
			}

			@Override
			public boolean tryAdvance(Consumer<? super R> action) {
				if (fromSpliterator.tryAdvance(this)) {
					action.accept(function.apply(holder, index++));
					return true;
				}
				return false;
			}

			@Override
			Splitr createSplit(OfInt from, long i) {
				return new Splitr(from, i);
			}
		}
		return StreamSupport.stream(new Splitr(fromSpliterator, 0), isParallel).onClose(stream::close);
	}

	/**
	 * Returns a stream consisting of the results of applying the given function to the elements of
	 * {@code stream} and their indexes in the stream. For example,
	 *
	 * <pre>{@code
	 * mapWithIndex(
	 *     LongStream.of(10, 11, 12),
	 *     (e, index) -> index + ":" + e)
	 * }</pre>
	 *
	 * <p>...would return {@code Stream.of("0:10", "1:11", "2:12")}.
	 *
	 * <p>The resulting stream is <a
	 * href="http://gee.cs.oswego.edu/dl/html/StreamParallelGuidance.html">efficiently splittable</a>
	 * if and only if {@code stream} was efficiently splittable and its underlying spliterator
	 * reported {@link Spliterator#SUBSIZED}. This is generally the case if the underlying stream
	 * comes from a data structure supporting efficient indexed random access, typically an array or
	 * list.
	 *
	 * <p>The order of the resulting stream is defined if and only if the order of the original stream
	 * was defined.
	 */
	public static <R> Stream<R> mapWithIndex(LongStream stream, LongFunctionWithIndex<R> function) {
		boolean isParallel = stream.isParallel();
		Spliterator.OfLong fromSpliterator = stream.spliterator();

		if (!fromSpliterator.hasCharacteristics(Spliterator.SUBSIZED)) {
			PrimitiveIterator.OfLong fromIterator = Spliterators.iterator(fromSpliterator);
			return StreamSupport.stream(
					new AbstractSpliterator<R>(
						fromSpliterator.estimateSize(),
						fromSpliterator.characteristics() & (Spliterator.ORDERED | Spliterator.SIZED)) {
						long index = 0;

						@Override
						public boolean tryAdvance(Consumer<? super R> action) {
							if (fromIterator.hasNext()) {
								action.accept(function.apply(fromIterator.nextLong(), index++));
								return true;
							}
							return false;
						}
					},
					isParallel)
				.onClose(stream::close);
		}
		class Splitr extends MapWithIndexSpliterator<Spliterator.OfLong, R, Splitr>
			implements LongConsumer, Spliterator<R> {
			long holder;

			Splitr(OfLong splitr, long index) {
				super(splitr, index);
			}

			@Override
			public void accept(long t) {
				this.holder = t;
			}

			@Override
			public boolean tryAdvance(Consumer<? super R> action) {
				if (fromSpliterator.tryAdvance(this)) {
					action.accept(function.apply(holder, index++));
					return true;
				}
				return false;
			}

			@Override
			Splitr createSplit(OfLong from, long i) {
				return new Splitr(from, i);
			}
		}
		return StreamSupport.stream(new Splitr(fromSpliterator, 0), isParallel).onClose(stream::close);
	}

	/**
	 * Returns a stream consisting of the results of applying the given function to the elements of
	 * {@code stream} and their indexes in the stream. For example,
	 *
	 * <pre>{@code
	 * mapWithIndex(
	 *     DoubleStream.of(0.0, 1.0, 2.0)
	 *     (e, index) -> index + ":" + e)
	 * }</pre>
	 *
	 * <p>...would return {@code Stream.of("0:0.0", "1:1.0", "2:2.0")}.
	 *
	 * <p>The resulting stream is <a
	 * href="http://gee.cs.oswego.edu/dl/html/StreamParallelGuidance.html">efficiently splittable</a>
	 * if and only if {@code stream} was efficiently splittable and its underlying spliterator
	 * reported {@link Spliterator#SUBSIZED}. This is generally the case if the underlying stream
	 * comes from a data structure supporting efficient indexed random access, typically an array or
	 * list.
	 *
	 * <p>The order of the resulting stream is defined if and only if the order of the original stream
	 * was defined.
	 */
	public static <R> Stream<R> mapWithIndex(DoubleStream stream, DoubleFunctionWithIndex<R> function) {
		boolean isParallel = stream.isParallel();
		Spliterator.OfDouble fromSpliterator = stream.spliterator();

		if (!fromSpliterator.hasCharacteristics(Spliterator.SUBSIZED)) {
			PrimitiveIterator.OfDouble fromIterator = Spliterators.iterator(fromSpliterator);
			return StreamSupport.stream(
					new AbstractSpliterator<R>(
						fromSpliterator.estimateSize(),
						fromSpliterator.characteristics() & (Spliterator.ORDERED | Spliterator.SIZED)) {
						long index = 0;

						@Override
						public boolean tryAdvance(Consumer<? super R> action) {
							if (fromIterator.hasNext()) {
								action.accept(function.apply(fromIterator.nextDouble(), index++));
								return true;
							}
							return false;
						}
					},
					isParallel)
				.onClose(stream::close);
		}
		class Splitr extends MapWithIndexSpliterator<Spliterator.OfDouble, R, Splitr>
			implements DoubleConsumer, Spliterator<R> {
			double holder;

			Splitr(OfDouble splitr, long index) {
				super(splitr, index);
			}

			@Override
			public void accept(double t) {
				this.holder = t;
			}

			@Override
			public boolean tryAdvance(Consumer<? super R> action) {
				if (fromSpliterator.tryAdvance(this)) {
					action.accept(function.apply(holder, index++));
					return true;
				}
				return false;
			}

			@Override
			Splitr createSplit(OfDouble from, long i) {
				return new Splitr(from, i);
			}
		}
		return StreamSupport.stream(new Splitr(fromSpliterator, 0), isParallel).onClose(stream::close);
	}

	/**
	 * An analogue of {@link Function} also accepting an index.
	 *
	 * <p>This interface is only intended for use by callers of {@link #mapWithIndex(Stream,
	 * FunctionWithIndex)}.
	 *
	 * @since 21.0
	 */
	public interface FunctionWithIndex<T, R> {
		/**
		 * Applies this function to the given argument and its index within a stream.
		 */

		R apply(T from, long index);
	}

	private abstract static class MapWithIndexSpliterator<
		F extends Spliterator<?>,
		R,
		S extends MapWithIndexSpliterator<F, R, S>>
		implements Spliterator<R> {
		final F fromSpliterator;
		long index;

		MapWithIndexSpliterator(F fromSpliterator, long index) {
			this.fromSpliterator = fromSpliterator;
			this.index = index;
		}

		abstract S createSplit(F from, long i);

		@Override
		public S trySplit() {
			Spliterator<?> splitOrNull = fromSpliterator.trySplit();
			if (splitOrNull == null) {
				return null;
			}
			@SuppressWarnings("unchecked")
			F split = (F) splitOrNull;
			S result = createSplit(split, index);
			this.index += split.getExactSizeIfKnown();
			return result;
		}

		@Override
		public long estimateSize() {
			return fromSpliterator.estimateSize();
		}

		@Override
		public int characteristics() {
			return fromSpliterator.characteristics()
				& (Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED);
		}
	}

	/**
	 * An analogue of {@link IntFunction} also accepting an index.
	 *
	 * <p>This interface is only intended for use by callers of {@link #mapWithIndex(IntStream,
	 * IntFunctionWithIndex)}.
	 *
	 * @since 21.0
	 */
	public interface IntFunctionWithIndex<R> {
		/**
		 * Applies this function to the given argument and its index within a stream.
		 */

		R apply(int from, long index);
	}

	/**
	 * An analogue of {@link LongFunction} also accepting an index.
	 *
	 * <p>This interface is only intended for use by callers of {@link #mapWithIndex(LongStream,
	 * LongFunctionWithIndex)}.
	 *
	 * @since 21.0
	 */
	public interface LongFunctionWithIndex<R> {
		/**
		 * Applies this function to the given argument and its index within a stream.
		 */

		R apply(long from, long index);
	}

	/**
	 * An analogue of {@link DoubleFunction} also accepting an index.
	 *
	 * <p>This interface is only intended for use by callers of {@link #mapWithIndex(DoubleStream,
	 * DoubleFunctionWithIndex)}.
	 *
	 * @since 21.0
	 */
	public interface DoubleFunctionWithIndex<R> {
		/**
		 * Applies this function to the given argument and its index within a stream.
		 */

		R apply(double from, long index);
	}

	/**
	 * Returns the last element of the specified stream, or {@link Optional#empty} if the
	 * stream is empty.
	 *
	 * <p>Equivalent to {@code stream.reduce((a, b) -> b)}, but may perform significantly better. This
	 * method's runtime will be between O(log n) and O(n), performing better on <a
	 * href="http://gee.cs.oswego.edu/dl/html/StreamParallelGuidance.html">efficiently splittable</a>
	 * streams.
	 *
	 * <p>If the stream has nondeterministic order, this has equivalent semantics to {@link
	 * Stream#findAny} (which you might as well use).
	 *
	 * @throws NullPointerException if the last element of the stream is null
	 * @see Stream#findFirst()
	 */
	/*
	 * By declaring <T> instead of <T extends  Object>, we declare this method as requiring a
	 * stream whose elements are non-null. However, the method goes out of its way to still handle
	 * nulls in the stream. This means that the method can safely be used with a stream that contains
	 * nulls as long as the *last* element is *not* null.
	 *
	 * (To "go out of its way," the method tracks a `set` bit so that it can distinguish "the final
	 * split has a last element of null, so throw NPE" from "the final split was empty, so look for an
	 * element in the prior one.")
	 */
	public static <T> Optional<T> findLast(Stream<T> stream) {
		class OptionalState {
			boolean set = false;
			T value = null;

			void set(T value) {
				this.set = true;
				this.value = value;
			}

			T get() {
				/*
				 * requireNonNull is safe because we call get() only if we've previously called set().
				 *
				 * (For further discussion of nullness, see the comment above the method.)
				 */
				return requireNonNull(value);
			}
		}
		OptionalState state = new OptionalState();

		Deque<Spliterator<T>> splits = new ArrayDeque<>();
		splits.addLast(stream.spliterator());

		while (!splits.isEmpty()) {
			Spliterator<T> spliterator = splits.removeLast();

			if (spliterator.getExactSizeIfKnown() == 0) {
				continue; // drop this split
			}

			// Many spliterators will have trySplits that are SUBSIZED even if they are not themselves
			// SUBSIZED.
			if (spliterator.hasCharacteristics(Spliterator.SUBSIZED)) {
				// we can drill down to exactly the smallest nonempty spliterator
				while (true) {
					Spliterator<T> prefix = spliterator.trySplit();
					if (prefix == null || prefix.getExactSizeIfKnown() == 0) {
						break;
					} else if (spliterator.getExactSizeIfKnown() == 0) {
						spliterator = prefix;
						break;
					}
				}

				// spliterator is known to be nonempty now
				spliterator.forEachRemaining(state::set);
				return Optional.of(state.get());
			}

			Spliterator<T> prefix = spliterator.trySplit();
			if (prefix == null || prefix.getExactSizeIfKnown() == 0) {
				// we can't split this any further
				spliterator.forEachRemaining(state::set);
				if (state.set) {
					return Optional.of(state.get());
				}
				// fall back to the last split
				continue;
			}
			splits.addLast(prefix);
			splits.addLast(spliterator);
		}
		return Optional.empty();
	}

	/**
	 * Returns the last element of the specified stream, or {@link OptionalInt#empty} if the stream is
	 * empty.
	 *
	 * <p>Equivalent to {@code stream.reduce((a, b) -> b)}, but may perform significantly better. This
	 * method's runtime will be between O(log n) and O(n), performing better on <a
	 * href="http://gee.cs.oswego.edu/dl/html/StreamParallelGuidance.html">efficiently splittable</a>
	 * streams.
	 *
	 * @throws NullPointerException if the last element of the stream is null
	 * @see IntStream#findFirst()
	 */
	public static OptionalInt findLast(IntStream stream) {
		// findLast(Stream) does some allocation, so we might as well box some more
		Optional<Integer> boxedLast = findLast(stream.boxed());
		return boxedLast.map(OptionalInt::of).orElseGet(OptionalInt::empty);
	}

	/**
	 * Returns the last element of the specified stream, or {@link OptionalLong#empty} if the stream
	 * is empty.
	 *
	 * <p>Equivalent to {@code stream.reduce((a, b) -> b)}, but may perform significantly better. This
	 * method's runtime will be between O(log n) and O(n), performing better on <a
	 * href="http://gee.cs.oswego.edu/dl/html/StreamParallelGuidance.html">efficiently splittable</a>
	 * streams.
	 *
	 * @throws NullPointerException if the last element of the stream is null
	 * @see LongStream#findFirst()
	 */
	public static OptionalLong findLast(LongStream stream) {
		// findLast(Stream) does some allocation, so we might as well box some more
		Optional<Long> boxedLast = findLast(stream.boxed());
		return boxedLast.map(OptionalLong::of).orElseGet(OptionalLong::empty);
	}

	/**
	 * Returns the last element of the specified stream, or {@link OptionalDouble#empty} if the stream
	 * is empty.
	 *
	 * <p>Equivalent to {@code stream.reduce((a, b) -> b)}, but may perform significantly better. This
	 * method's runtime will be between O(log n) and O(n), performing better on <a
	 * href="http://gee.cs.oswego.edu/dl/html/StreamParallelGuidance.html">efficiently splittable</a>
	 * streams.
	 *
	 * @throws NullPointerException if the last element of the stream is null
	 * @see DoubleStream#findFirst()
	 */
	public static OptionalDouble findLast(DoubleStream stream) {
		// findLast(Stream) does some allocation, so we might as well box some more
		Optional<Double> boxedLast = findLast(stream.boxed());
		return boxedLast.map(OptionalDouble::of).orElseGet(OptionalDouble::empty);
	}

	public static <T> T uncheckedCastNullableTToT(T t) {
		return t;
	}

}
