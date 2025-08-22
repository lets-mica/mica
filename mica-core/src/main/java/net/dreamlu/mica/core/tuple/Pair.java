/*
 * Copyright (c) 2019-2029, Dreamlu 卢春梦 (596392912@qq.com & www.dreamlu.net).
 * <p>
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE 3.0;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.dreamlu.mica.core.tuple;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jspecify.annotations.Nullable;

/**
 * tuple Pair
 *
 * @param <L> 泛型
 * @param <R> 泛型
 * @author L.cm
 **/
public record Pair<L, R>(@Nullable L left, @Nullable R right) {
	private static final Pair<Object, Object> EMPTY = new Pair<>(null, null);

	/**
	 * Returns an empty pair.
	 *
	 * @return Pair
	 */
	@SuppressWarnings("unchecked")
	public static <L, R> Pair<L, R> empty() {
		return (Pair<L, R>) EMPTY;
	}

	/**
	 * Constructs a pair with its left value being {@code left}, or returns an empty pair if
	 * {@code left} is null.
	 *
	 * @param left left
	 * @param <L>  泛型
	 * @param <R>  泛型
	 * @return the constructed pair or an empty pair if {@code left} is null.
	 */
	public static <L, R> Pair<L, R> createLeft(@Nullable L left) {
		if (left == null) {
			return empty();
		} else {
			return new Pair<>(left, null);
		}
	}

	/**
	 * Constructs a pair with its right value being {@code right}, or returns an empty pair if
	 * {@code right} is null.
	 *
	 * @param right right
	 * @param <L>   泛型
	 * @param <R>   泛型
	 * @return the constructed pair or an empty pair if {@code right} is null.
	 */
	public static <L, R> Pair<L, R> createRight(@Nullable R right) {
		if (right == null) {
			return empty();
		} else {
			return new Pair<>(null, right);
		}
	}

	@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
	public static <L, R> Pair<L, R> create(@Nullable @JsonProperty("left") L left, @Nullable @JsonProperty("right") R right) {
		if (right == null && left == null) {
			return empty();
		} else {
			return new Pair<>(left, right);
		}
	}

}
