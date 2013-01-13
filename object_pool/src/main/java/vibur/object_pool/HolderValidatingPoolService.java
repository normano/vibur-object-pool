/**
 * Copyright 2013 Simeon Malchev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package vibur.object_pool;

import java.util.concurrent.TimeUnit;

/**
 * <p>Extends the functionality defined by {@link BasePoolService} with
 * take and restore methods which provide validation for whether the
 * restored (returned) object is one which has been taken before that from this object pool,
 * as well as whether the object is restored only once to the object pool.
 *
 * <p>The object returned by the {@code take} methods is enclosed in a thin wrapper class which
 * is created by the object pool and which is implementing the described below {@link Holder}
 * interface. The underlying object is accessible via the interface's {@code getTarget()}
 * method.
 *
 * @author Simeon Malchev
 * @param <T> the type of objects held in this object pool
 */
public interface HolderValidatingPoolService<T> extends BasePoolService {

    /**
     * Takes an object from the object pool if there is such available. This is a blocking call which
     * waits indefinetly until an object becomes available. If the calling thread is interrupted
     * while waiting this call will return {@code null} and the thread's interrupted status will
     * be set to {@code true}.
     *
     * @return an object taken from the object pool and enclosed into a thin wrapper class implementing
     * the {@link Holder} interface or {@code null} if was interrupted while waiting
     */
    Holder<T> take();

    /**
     * Takes an object from the object pool if there is such available. This is a blocking call which
     * waits indefinetly until an object becomes available.
     *
     * @return an object taken from the object pool and enclosed into a thin wrapper class implementing
     * the {@link Holder} interface
     */
    Holder<T> takeUninterruptibly();

    /**
     * Takes an object from the object pool if there is such available. This is a blocking call which
     * waits up to the specified {@code timeout} for an object to become available. If the calling
     * thread is interrupted while waiting this call will return {@code null} and the thread's
     * interrupted status will be set to {@code true}.
     *
     * @param timeout the maximum time to wait for an object to become available in the object pool
     * @param unit the time unit of the {@code timeout} argument
     * @return an object taken from the object pool and enclosed into a thin wrapper class implementing
     * the {@link Holder} interface or {@code null} if the specified timeout expires
     * or if was interrupted while waiting
     */
    Holder<T> tryTake(long timeout, TimeUnit unit);

    /**
     * Tries to take an object from the object pool if there is one which is immediately available. Returns
     * {@code null} if no object is available at the moment of the call.
     *
     * @return an object taken from the object pool and enclosed into a thin wrapper class implementing
     * the {@link Holder} interface or {@code null} if there is no object available in the object pool
     */
    Holder<T> tryTake();

    /**
     * Restores (returns) an object to the object pool. The object pool <strong>validates</strong>
     * whether the object restored has been taken before from this object pool and whether it is restored
     * only once. If the object restored was not taken from the object pool or if it is restored more than
     * once, this method will return {@code false} otherwise will return {@code true}.
     *
     * @param holder a thin wrapper enclosing the object that is to be restored to the object pool
     * @return {@code true} if the underlying object from the given {@code holder} was taken
     * before that from this object pool and if it is returned once only (i.e. for a first time),
     * {@code false} otherwise
     */
    boolean restore(Holder<T> holder);
}
