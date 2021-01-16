/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.rx_cache3.core.internal.cache.memory.apache;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * The "read" subset of the {@link Map} interface.
 *
 * @since 4.0
 * @version $Id: Get.java 1543265 2013-11-19 00:48:44Z ggregory $
 *
 * @see Put
 */
public interface Get<K, V> {

    /**
     * @see Map#containsKey(Object)
     */
    boolean containsKey(Object key);

    /**
     * @see Map#containsValue(Object)
     */
    boolean containsValue(Object value);

    /**
     * @see Map#entrySet()
     */
    Set<Map.Entry<K, V>> entrySet();

    /**
     * @see Map#get(Object)
     */
    V get(Object key);

    /**
     * @see Map#remove(Object)
     */
    V remove(Object key);

    /**
     * @see Map#isEmpty()
     */
    boolean isEmpty();

    /**
     * @see Map#keySet()
     */
    Set<K> keySet();

    /**
     * @see Map#size()
     */
    int size();

    /**
     * @see Map#values()
     */
    Collection<V> values();

}
