package com.qsq.util;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.BeanUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author QSQ
 * @create 2019/4/12 13:46
 * No, again
 * 〈 集合常用操作类 〉
 */
public class CollectionUtils {
    public static <E> void forEach(Collection<E> collection, Consumer<? super E> action) {
        if (isEmpty(collection) || action == null) {
            return;
        }
        collection.forEach(action);
    }

    public static <F, T> List<T> map(Collection<F> source, Function<F, T> func) {
        if (isEmpty(source)) {
            return Collections.emptyList();
        }
        List<T> resultList = new ArrayList<>();
        for (F f : source) {
            T t = func.apply(f);
            resultList.add(t);
        }
        return resultList;
    }

    public static <F, T> List<T> mapNotSame(Collection<F> source, Function<F, T> func) {
        List<T> list = map(source, func);
        HashSet<T> hashSet = new HashSet<>(list);
        list.clear();
        list.addAll(hashSet);
        return list;
    }

    public static <F> String mapJoin(Collection<F> source, Function<F, String> func, String sep) {
        if (isEmpty(source)) {
            return "";
        }
        return source.stream().map(func).distinct().collect(Collectors.joining(sep));
    }

    public static <F, T> List<T> mapIndex(Collection<F> source, BiFunction<F, Integer, T> func) {
        if (isEmpty(source)) {
            return Collections.emptyList();
        }
        List<T> resultList = new ArrayList<>();
        AtomicInteger index = new AtomicInteger();
        for (F f : source) {
            resultList.add(func.apply(f, index.getAndIncrement()));
        }
        return resultList;
    }

    public static <E> List<E> filter(Collection<E> source, Predicate<E> predicate) {
        if (isEmpty(source) || predicate == null) {
            return Collections.emptyList();
        }
        List<E> result = new ArrayList<>();
        for (E element : source) {
            if (predicate.test(element)) {
                result.add(element);
            }
        }
        return result;
    }

    public static <F, T> List<T> mapNotNull(Collection<F> source, Function<F, T> func) {
        return filter(map(source, func), Objects::nonNull);
    }

    public static <T> boolean isEmpty(Collection<T> source) {
        return (source == null || source.size() <= 0);
    }

    public static <T> boolean isNotEmpty(Collection<T> source) {
        return !isEmpty(source);
    }

    public static <E> E findFirst(Collection<E> source) {
        return findFirst(source, e -> true);
    }

    public static <E> E findFirst(Collection<E> elements, Predicate<E> predicate) {
        if (CollectionUtils.isEmpty(elements)) {
            return null;
        }
        for (E element : elements) {
            if (predicate.test(element)) {
                return element;
            }
        }
        return null;
    }

    /**
     * @param left            左集合
     * @param right           右集合(如果后面构造的key相同，后面的元素覆盖前面的，与sql的leftJoin有些区别)
     * @param leftKeyBuilder  左集合元素构造key的方法
     * @param rightKeyBuilder 右集合元素构造key的方法
     * @param resultBuilder   返回集合元素构造的方式
     * @return 连接结果
     */
    public static <F1, F2, K, R> List<R> leftJoin(Collection<F1> left, Collection<F2> right, Function<F1, K> leftKeyBuilder, Function<F2, K> rightKeyBuilder, BiFunction<F1, F2, R> resultBuilder) {
        if (isEmpty(left)) {
            return Collections.emptyList();
        }
        Map<K, F2> rightMap = toMap(right, rightKeyBuilder);

        List<R> result = new ArrayList<>();
        for (F1 element : left) {
            F2 rightElement = rightMap.get(leftKeyBuilder.apply(element));
            result.add(resultBuilder.apply(element, rightElement));
        }
        return result;
    }

    /**
     * @param left            左集合
     * @param right           右集合(如果后面构造的key相同，后面的元素覆盖前面的，与sql的leftJoin有些区别)
     * @param leftKeyBuilder  左集合元素构造key的方法
     * @param rightKeyBuilder 右集合元素构造key的方法
     * @param resultBuilder   返回集合元素构造的方式
     * @return 连接结果
     */
    public static <F1, F2, K, R> List<R> innerJoin(Collection<F1> left, Collection<F2> right, Function<F1, K> leftKeyBuilder, Function<F2, K> rightKeyBuilder, BiFunction<F1, F2, R> resultBuilder) {
        if (isEmpty(left)) {
            return Collections.emptyList();
        }
        Map<K, F2> rightMap = toMap(right, rightKeyBuilder);

        List<R> result = new ArrayList<>();
        for (F1 element : left) {
            F2 rightElement = rightMap.get(leftKeyBuilder.apply(element));
            if (rightElement == null) {
                continue;
            }
            result.add(resultBuilder.apply(element, rightElement));
        }
        return result;
    }

    public static <K, E extends E1, E1> Map<K, E> toMap(Collection<E> elements, Function<E1, K> keyBuilder) {
        if (isEmpty(elements) || keyBuilder == null) {
            return Maps.newHashMap();
        }
        Map<K, E> result = Maps.newHashMap();
        for (E element : elements) {
            result.put(keyBuilder.apply(element), element);
        }
        return result;
    }

    public static <K, E, V> Map<K, V> toMap(Collection<E> elements, Function<E, K> keyBuilder, Function<E, V> valueBuilder) {
        if (isEmpty(elements) || keyBuilder == null) {
            return Maps.newHashMap();
        }
        Map<K, V> result = Maps.newHashMap();
        for (E element : elements) {
            result.put(keyBuilder.apply(element), valueBuilder.apply(element));
        }
        return result;
    }


    public static <K, E, V> Map<K, V> toReduceMap(Collection<E> elements, Function<E, K> keyBuilder, V initValue, BiFunction<V, E, V> reduceFunc) {
        if (isEmpty(elements) || keyBuilder == null) {
            return Maps.newHashMap();
        }
        Map<K, V> result = Maps.newHashMap();
        Map<K, List<E>> kListMap = toMapList(elements, keyBuilder);
        kListMap.forEach((k, es) -> result.put(k, reduce(es, initValue, reduceFunc)));
        return result;
    }

    public static <K, E> Map<K, E> flatMap(Collection<E> elements, Function<E, List<K>> spiltFunc) {
        if (isEmpty(elements) || spiltFunc == null) {
            return Maps.newHashMap();
        }
        Map<K, E> result = Maps.newHashMap();
        for (E element : elements) {
            List<K> keys = spiltFunc.apply(element);
            if (isEmpty(keys)) {
                continue;
            }
            for (K key : keys) {
                result.put(key, element);
            }
        }
        return result;
    }

    public static <E, K> Map<K, List<E>> toMapList(Collection<E> elements, Function<E, K> keyFunc) {
        return toMapList(elements, keyFunc, Function.identity());
    }

    public static <E, K, V> Map<K, List<V>> toMapList(Collection<E> elements, Function<E, K> keyFunc, Function<E, V> valueFunc) {
        if (isEmpty(elements) || keyFunc == null || valueFunc == null) {
            return new HashMap<>(16);
        }
        Map<K, List<V>> result = Maps.newHashMap();
        for (E element : elements) {
            K key = keyFunc.apply(element);
            List<V> values = result.computeIfAbsent(key, k -> new ArrayList<>());
            values.add(valueFunc.apply(element));
        }
        return result;
    }

    public static <E, K, R> Map<K, R> groupBy(Collection<E> elements, Function<E, K> keyFunc, BiFunction<K, List<E>, R> resultBuilder) {
        if (isEmpty(elements) || keyFunc == null || resultBuilder == null) {
            return Collections.emptyMap();
        }
        Map<K, List<E>> map = toMapList(elements, keyFunc, Function.identity());

        Map<K, R> resultMap = Maps.newHashMap();
        for (Map.Entry<K, List<E>> entry : map.entrySet()) {
            resultMap.put(entry.getKey(), resultBuilder.apply(entry.getKey(), entry.getValue()));
        }
        return resultMap;
    }

    public static <E, R> R reduce(Collection<E> elements, R initValue, BiFunction<R, E, R> reduceFunc) {
        if (isEmpty(elements)) {
            return initValue;
        }
        R result = initValue;
        for (E element : elements) {
            if (null != element) {
                result = reduceFunc.apply(result, element);
            }
        }
        return result;
    }

    public static <E> List<E> combine(Collection<Collection<E>> es) {
        if (isEmpty(es)) {
            return Collections.emptyList();
        }
        List<E> result = new ArrayList<>();
        for (Collection<E> e : es) {
            if (isEmpty(e)) {
                continue;
            }
            result.addAll(e);
        }
        return result;
    }

    public static <T extends Object & Comparable<? super T>> T max(List<T> elements) {
        if (isEmpty(elements)) {
            return null;
        }
        return Collections.max(elements);
    }

    /**
     * list值拷贝与转换
     *
     * @param source
     * @param clazz
     * @param <S>
     * @param <R>
     * @return
     * @author kesc
     */
    public static <S, R> List<R> convert(List<S> source, Class<R> clazz) {
        if (DataUtil.isEmpty(source)) {
            return Collections.emptyList();
        }
        List<R> result = Lists.newArrayList();
        source.forEach(x -> {
            try {
                R instance = clazz.newInstance();
                BeanUtils.copyProperties(x, instance);
                result.add(instance);
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e.getMessage());
            }
        });
        return result;
    }

    /**
     * 过滤重复出现的元素
     *
     * @param elements
     * @param keyFunc
     * @param <E>
     * @param <K>
     * @return
     */
    public static <E, K> List<E> filterSame(Collection<E> elements, Function<E, K> keyFunc) {
        Map<K, E> keMap = toMap(elements, keyFunc, Function.identity());
        return Lists.newArrayList(keMap.values());
    }

    public static <E> int size(List<E> elements) {
        if (isEmpty(elements)) {
            return 0;
        }
        return elements.size();
    }

    public static <E, K> boolean hasDistinct(Collection<E> elements, Function<E, K> keyFunc) {
        if (CollectionUtils.isEmpty(elements)) {
            return true;
        }
        long count = elements.stream().map(keyFunc).distinct().count();
        return count != elements.size();
    }
}