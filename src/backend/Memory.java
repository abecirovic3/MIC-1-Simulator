package backend;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Arrays;

public class Memory {
//    private short[] memory;
    private ObservableList<MemoryLine> memory;
    private short address;
    private byte readCounter, writeCounter;

    public Memory() {
//        memory = new short[4096];
//        Arrays.fill(memory, 0, 2048, (short)0x7000); // LOCO 0, act like NOP instr in CODE segment
//        Arrays.fill(memory, 2048, 4096, (short)0);
        memory = FXCollections.observableArrayList();
        for (short i = 0; i < 2048; i++)
            memory.add(new MemoryLine(i,(short)0x7000));    // LOCO, acts like NOP inst in CODE segment
        for (short i = 2048; i < 4096; i++)
            memory.add(new MemoryLine(i, (short)0));

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
//        return memory[address];
        return (short) memory.get(address).getValue();
    }

    public boolean isWriteReady() {
        return writeCounter == 2;
    }

    public void incrementWriteCounter() {
        writeCounter++;
    }

    public void write(short value) {
        writeCounter = 0;
//        memory[address] = value;
        memory.get(address).setValue(value);
    }

    public void setAddress(short address) {
        this.address = address;
    }

    public void write(short address, short value) {
//        memory[address] = value;
        memory.get(address).setValue(value);
    }

    public short read(short address) {
//        return memory[address];
        return (short) memory.get(address).getValue();
    }
}
