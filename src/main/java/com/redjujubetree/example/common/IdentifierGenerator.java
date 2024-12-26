package com.redjujubetree.example.common;

import java.net.InetAddress;

public class IdentifierGenerator {

    private final IdGeneratorSnowflake sequence;
    private IdentifierGenerator() {
        this.sequence = new IdGeneratorSnowflake(null);
    }

    public IdentifierGenerator(InetAddress inetAddress) {
        this.sequence = new IdGeneratorSnowflake(inetAddress);
    }

    public IdentifierGenerator(long workerId, long dataCenterId) {
        this.sequence = new IdGeneratorSnowflake(workerId, dataCenterId);
    }

    public Long nextId() {
        return sequence.nextId();
    }

    public static IdentifierGenerator getDefaultGenerator() {
        return DefaultInstance.INSTANCE;
    }

    private static class DefaultInstance {

        public static final IdentifierGenerator INSTANCE = new IdentifierGenerator();

    }

}