package com.aws.peach.infrastructure.configuration.support;

import com.aws.peach.domain.support.Message;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.header.internals.RecordHeader;

import java.util.UUID;
import java.util.stream.StreamSupport;

public class MessageIdUtils {

    private static final String HEADER_KEY_MESSAGE_ID = "custom-message_id"; // 주의: 모든 마이크로서비스가 같은 값을 사용해야 함

    public static Message.Id parse(Headers headers) {
        return StreamSupport.stream(headers.spliterator(), false)
                .filter(h -> HEADER_KEY_MESSAGE_ID.equals(h.key()))
                .map(h -> new Message.Id(new String(h.value())))
                .findAny()
                .orElse(null);
    }

    public static Header createMessageIdHeader() {
        byte[] value = createMessageId().getBytes();
        return new RecordHeader(HEADER_KEY_MESSAGE_ID, value);
    }

    private static String createMessageId() {
        return UUID.randomUUID().toString();
    }
}
