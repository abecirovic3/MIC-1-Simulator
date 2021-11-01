package backend;

import java.util.Arrays;

public class Memory {
    private short[] memory;
    private short address;
    private byte readCounter, writeCounter;

    public Memory() {
        memory = new short[4096];
        Arrays.fill(memory, (short)0);
        address = 0;
        readCounter = 0;
        writeCounter = 0;
    }

    public boolean isReadReady() {
        return readCounter == 2;
    }

    public void incrementReadCounter() {
        readCounter++;
    }

    public short read() {
        readCounter = 0;
        return memory[address];
    }

    public boolean isWriteReady() {
        return writeCounter == 2;
    }

    public void incrementWriteCounter() {
        writeCounter++;
    }

    public void write(short value) {
        writeCounter = 0;
        memory[address] = value;
    }

    public void setAddress(short address) {
        this.address = address;
    }

    public void write(short address, short value) {
        memory[address] = value;
    }

    public short read(short address) {
        return memory[address];
    }
}
