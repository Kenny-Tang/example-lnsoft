package com.redjujubetree.example.common;

import java.net.InetAddress;

public class IdentifierGenerator {

    private final IdGeneratorSnowflake generatorSnowflake;
    private IdentifierGenerator() {
        this.generatorSnowflake = new IdGeneratorSnowflake(null);
    }

    public IdentifierGenerator(InetAddress inetAddress) {
        this.generatorSnowflake = new IdGeneratorSnowflake(inetAddress);
    }

    public IdentifierGenerator(long workerId, long dataCenterId) {
        this.generatorSnowflake = new IdGeneratorSnowflake(workerId, dataCenterId);
    }

    public Long nextId() {
        return generatorSnowflake.nextId();
    }

    public static IdentifierGenerator getDefaultGenerator() {
        return DefaultInstance.INSTANCE;
    }

    private static class DefaultInstance {

        public static final IdentifierGenerator INSTANCE = new IdentifierGenerator();

    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            System.out.println(DefaultInstance.INSTANCE.nextId());
        }
    }

}