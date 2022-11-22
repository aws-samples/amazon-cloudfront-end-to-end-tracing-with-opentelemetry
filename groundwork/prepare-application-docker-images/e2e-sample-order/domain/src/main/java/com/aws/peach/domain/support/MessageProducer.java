package com.aws.peach.domain.support;

@FunctionalInterface
public interface MessageProducer<K,V> {
    String send(K key, V value);
}
